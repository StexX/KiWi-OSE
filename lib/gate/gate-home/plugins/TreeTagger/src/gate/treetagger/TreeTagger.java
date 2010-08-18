package gate.treetagger;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.net.URL;
import java.lang.*;

import gate.*;
import gate.creole.*;
import gate.creole.metadata.*;
import gate.util.*;

/**
 * This class is a wrapper for the language-independent POS tagger from the
 * University of Stuttgart, Germany.
 * It passes GATE a document and a Tree Tagger shell script.
 * Results are stored in the document's TreeTaggerToken annotations
 */

@CreoleResource(comment = "The TreeTagger is a language-independent "
    + "part-of-speech tagger, which currently supports English, French, "
    + "German, and Spanish.",
    helpURL = "http://gate.ac.uk/userguide/sec:misc-creole:treetagger")
public class TreeTagger
    extends AbstractLanguageAnalyser
    implements ProcessingResource {

  public static final String TREE_TAGGER_STRING_FEATURE_NAME
      = "treeTaggerString";

  /**
   * The document to be processed.
   */
  private gate.Document document;

  /**
   * The annotation set to be used for the generated annotations.
   */
  private String annotationSetName;

  /**
   * The TreeTagger shell script to use.
   */
  private URL treeTaggerBinary;

  /**
   * Character encoding to use when passing data to and from the tree tagger
   * binary.
   */
  private String encoding;

  /**
   * Charset corresponding to the specified encoding.
   */
  private Charset charset;

  private boolean failOnUnmappableChar;
  
  /**
   * Initialize this resource.  This method mostly consists of sanity checks on
   * the user's environment to ensure that the tree tagger binary will work.
   */
  public Resource init() throws ResourceInstantiationException {
    String tmpDir = System.getProperty("java.io.tmpdir");
    if (tmpDir == null || tmpDir.indexOf(' ') >= 0) {
      throw new ResourceInstantiationException(
          "TreeTagger requires your temporary directory to be set to a value "
          + "that does not contain spaces.  Please set java.io.tmpdir to a "
          + "suitable value.");
    }

    //the TreeTagger is only for Linux or Mac OS X
//    String osName = System.getProperty("os.name").toLowerCase();
//    if (osName.indexOf("linux") == -1 && !osName.startsWith("mac os x"))
//      throw new ResourceInstantiationException(
//          "The Tree Tagger can only be run on Linux or Mac OS X.");

    return this;
  }

  /**
   * Run the TreeTagger on the current document.  This writes the document text
   * to a temporary file, runs the tagger and processes its output to produce
   * TreeTaggerToken annotations on the document.
   */
  public void execute() throws ExecutionException {
    if (document == null)
      throw new ExecutionException("No document to process!");

    if (treeTaggerBinary == null)
      throw new ExecutionException(
          "Cannot proceed unless a TreeTagger script is specified.");

    if (charset == null) {
      throw new ExecutionException("No encoding specified");
    }

    //get current text from GATE for the TreeTagger
    File textfile = getCurrentText();
    
    // check that the file exists
    File scriptfile = Files.fileFromURL(treeTaggerBinary);
    if (scriptfile.exists()==false)
      throw new ExecutionException("Script "+scriptfile.getAbsolutePath()+" does not exist");
    
    // build the command line.
    // If the system property treetagger.sh.path is set, use this as the path
    // to the bourne shell interpreter and place it as the first item on the
    // command line.  If not, then just pass the script as the first item.  The
    // system property is useful on platforms that don't support shell scripts
    // with #! lines natively, e.g.  on Windows you can set the property to
    // c:\cygwin\bin\sh.exe (or whatever is appropriate on your system) to
    // invoke the script via Cygwin sh
    int index = 0;
    String[] treeTaggerCmd;
    String shPath = null;
    if((shPath = System.getProperty("treetagger.sh.path")) != null) {
      treeTaggerCmd = new String[3];
      treeTaggerCmd[0] = shPath;
      index = 1;
    }
    else {
      treeTaggerCmd = new String[2];
    }
    
    //generate TreeTagger command line
    treeTaggerCmd[index] = scriptfile.getAbsolutePath();
    treeTaggerCmd[index+1] = textfile.getAbsolutePath();

    //run TreeTagger
    runTreeTagger(treeTaggerCmd);

    //delete the temporary text file
    textfile.delete();
  }

  private File getCurrentText() throws ExecutionException {
    File gateTextFile = null;
    try {
      gateTextFile = File.createTempFile("treetagger", ".txt");
      // depending on the failOnUnmappableChar parameter, we either make the
      // output stream writer fail or replace the unmappable character with '?'
      CharsetEncoder charsetEncoder = charset.newEncoder()
          .onUnmappableCharacter(
              failOnUnmappableChar ? CodingErrorAction.REPORT
                                   : CodingErrorAction.REPLACE);
      FileOutputStream fos = new FileOutputStream(gateTextFile);
      OutputStreamWriter osw = new OutputStreamWriter(fos, charsetEncoder);
      BufferedWriter bw = new BufferedWriter(osw);
      AnnotationSet annotSet = (annotationSetName == null || annotationSetName.trim().length() == 0) ?
          document.getAnnotations() : document.getAnnotations(annotationSetName);
      annotSet = annotSet.get(ANNIEConstants.TOKEN_ANNOTATION_TYPE);
      if(annotSet == null || annotSet.size() == 0) {
        throw new GateRuntimeException("No Gate Tokens found in the document.. please run tokenizer first");
      }

      // sort tokens according to their offsets
      ArrayList tokens = new ArrayList(annotSet);
      Collections.sort(tokens, new OffsetComparator());
      // and now start writing them in a file
      for(int i=0;i<tokens.size();i++) {
        FeatureMap features = ((Annotation) tokens.get(i)).getFeatures();
        if(features == null || features.size() == 0 || !features.containsKey(gate.creole.ANNIEConstants.TOKEN_STRING_FEATURE_NAME)) {
          throw new GateRuntimeException("GATE Tokens must have string feature, which couldn't be found");
        }
        String string = (String) features.get(gate.creole.ANNIEConstants.TOKEN_STRING_FEATURE_NAME);
        bw.write(string);
        if(i+1 < tokens.size())
          bw.newLine();
      }
      bw.close();

      // now read the temp file back in and iterate over tokens pulling the
      // string that has been passed to the tree tagger into a feature of the
      // Token
      CharsetDecoder charsetDecoder = charset.newDecoder();
      FileInputStream fis = new FileInputStream(gateTextFile);
      InputStreamReader isr = new InputStreamReader(fis, charsetDecoder);
      BufferedReader br = new BufferedReader(isr);

      for(int i=0;i<tokens.size();i++) {
        FeatureMap features = ((Annotation) tokens.get(i)).getFeatures();
        features.put(TREE_TAGGER_STRING_FEATURE_NAME, br.readLine());
      }
      
    }
    catch (CharacterCodingException cce) {
      throw (ExecutionException)new ExecutionException(
          "Document contains a character that cannot be represented " +
          "in " + encoding)
          .initCause(cce);
    }
    catch (java.io.IOException except) {
      throw (ExecutionException)new ExecutionException(
          "Error creating temporary file for TreeTagger")
          .initCause(except);
    }
    return (gateTextFile);
  }

  private void runTreeTagger(String[] cmdline) throws ExecutionException {
    String line, word, tag, lemma;
    int indx = 0;
    AnnotationSet aSet;

    aSet = (annotationSetName == null || annotationSetName.trim().length() == 0) ?
        document.getAnnotations() : document.getAnnotations(annotationSetName);

    CharsetDecoder charsetDecoder = charset.newDecoder();
    // run TreeTagger and save output
    try {

      Process p = Runtime.getRuntime().exec(cmdline);

      //get its tree tagger output (gate input)
      BufferedReader input =
          new BufferedReader
          (new InputStreamReader(p.getInputStream(), charsetDecoder));

      // let us store all tagged data in lines
      // there must be at leat the original form a tab a POS
      // and possibly a lemma preceded by a tab
      ArrayList lines = new ArrayList();
      while( (line = input.readLine()) != null) {
        if(line.split("\t").length>1)
          lines.add(line);
      }

      // we need to check if the tokenization was done correctly
      // and we do so it by comparing the number of lines with the number of
      // tokens created by the GATE tokenizer
      AnnotationSet annotSet = aSet.get(ANNIEConstants.TOKEN_ANNOTATION_TYPE);
      if(annotSet == null || annotSet.size() == 0) {
        throw new GateRuntimeException("No Gate Tokens found in the document.. please run tokenizer first");
      }

      // sort tokens according to their offsets
      ArrayList tokens = new ArrayList(annotSet);
      Collections.sort(tokens, new OffsetComparator());


      if(tokens.size() != lines.size()) {
        System.out.println("Tokens : "+tokens.size()+" lines : "+lines.size());
        throw new GateRuntimeException("Document does not have the expected number of tokens created by the treeTaggerBinary file");
      }

      // take one line at a time
      // check its length and the string feature and go on addition features
      for(int i=0;i<lines.size();i++) {
        line = (String) lines.get(i);
        StringTokenizer st = new StringTokenizer(line);
        Annotation token = (Annotation) tokens.get(i);
        FeatureMap features = token.getFeatures();

        if(st.hasMoreTokens()) {
          tag = null;
          lemma = null;
          word = st.nextToken();

          // check if the word matches with the expected string (stored in the
          // treeTaggerString feature of the token).
          String expectedTokenString = (String) features.get(TREE_TAGGER_STRING_FEATURE_NAME);
          if(expectedTokenString == null) throw new GateException("Invalid Token in GATE document");
          if(!word.equals(expectedTokenString)) {
            throw new GateRuntimeException("Document does not have the expected number/sequence of tokens created by the treeTaggerBinary file");
          }

          if (st.hasMoreTokens())
            tag = st.nextToken();
          if (tag != null && st.hasMoreTokens())
            lemma = st.nextToken();

          // finally add features on the top of tokens
          if(lemma != null) features.put("lemma", lemma);
          if(tag != null) features.put("category", tag);

        } else {
          throw new GateRuntimeException("Document does not have the expected number of tokens created by the treeTaggerBinary file");
        }
      }

      // clean up the treeTaggerString features
      for(int i=0;i<tokens.size();i++) {
        FeatureMap features = ((Annotation) tokens.get(i)).getFeatures();
        features.remove(TREE_TAGGER_STRING_FEATURE_NAME);
      }
    }
    catch (Exception err) {
      throw (ExecutionException)new ExecutionException(
          "Error occurred running TreeTagger with command line "
                + Arrays.asList(cmdline))
          .initCause(err);
    }

  }

  // getter and setter methods

  /**
   * Set the document to process.
   */
  @RunTime
  @CreoleParameter(comment = "The document to process")
  public void setDocument(gate.Document document) {
    this.document = document;
  }

  /**
   * Return the document being processed.
   */
  public gate.Document getDocument() {
    return document;
  }

  /**
   * Set the name of the annotation set to place the generated TreeTaggerToken
   * annotations in.
   */
  @Optional
  @RunTime
  @CreoleParameter(
      comment = "The annotation set to be used for the generated annotations")
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }

  /**
   * Return the annotation set name used for the TreeTaggerTokens.
   */
  public String getAnnotationSetName() {
    return annotationSetName;
  }

  /**
   * Set the location of the TreeTagger script.
   */
  @RunTime
  @CreoleParameter(comment = "Name of the TreeTagger command file")
  public void setTreeTaggerBinary(URL treeTaggerBinary) {
    this.treeTaggerBinary = treeTaggerBinary;
  }

  /**
   * Return the location of the TreeTagger script.
   */
  public URL getTreeTaggerBinary() {
    return treeTaggerBinary;
  }

  /**
   * Set the character encoding to use for the temporary files.  This must be
   * the encoding that your tree tagger understands.
   *
   * @throws IllegalCharsetNameException if the specified string is not a valid
   * encoding name.
   */
  @RunTime
  @CreoleParameter(defaultValue = "ISO-8859-1",
      comment = "Character encoding for temporary files, must match "
        + "the encoding of your tree tagger data files")
  public void setEncoding(String newEncoding) {
    this.charset = Charset.forName(newEncoding);
    // if the line above didn't throw an exception, we are OK.
    this.encoding = newEncoding;
  }

  /**
   * Get the character encoding used for the temporary files.
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * Set the flag for whether we should fail if an unmappable character is
   * found.
   */
  @RunTime
  @CreoleParameter(defaultValue = "true",
        comment = "Should the tagger fail if it encounters a character which "
          + "is not mappable into the specified encoding?")
  public void setFailOnUnmappableChar(Boolean newValue) {
    failOnUnmappableChar = (newValue == null) ? true : newValue.booleanValue();
  }

  /**
   * Get the flag for whether we should fail if an unmappable character is
   * found.
   */
  public Boolean getFailOnUnmappableChar() {
    return Boolean.valueOf(failOnUnmappableChar);
  }
} // class TreeTagger
