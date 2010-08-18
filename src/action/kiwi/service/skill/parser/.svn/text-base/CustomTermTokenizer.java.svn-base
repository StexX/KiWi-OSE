package kiwi.service.skill.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Attribute;
import org.tartarus.snowball.ext.EnglishStemmer;



/**
 * Tokenizer and analyzer for a given input stream.
 * 
 * @author Fred Durao
 *
 */
public class CustomTermTokenizer {
	
	public static final String STOP_WORDS_FILE = "/kiwi/service/skill/parser/stopwords_en.xml";

	private Tokenizer tokenStream;
	private TokenStream filteredTokenStream;
	
	@SuppressWarnings("unchecked")
	public Set<String> getStopWordList() {
				if (CustomTermTokenizer.class.getResourceAsStream(STOP_WORDS_FILE)==null) {
					try {
						throw new IOException("STOP_WORDS_FILE path file could not be solved");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					return WordlistLoader.getWordSet(new InputStreamReader(CustomTermTokenizer.class.getResourceAsStream(STOP_WORDS_FILE)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return Collections.EMPTY_SET;
	}

	
	
	/**
	 * Applies several filters to the input and returns a list with the 
	 * frequency with which each token appears in the input.
	 * The filters are:
	 *  - lower case filter
	 *  - ASCII folding filter
	 *  - ASCII filter
	 *  - stop word filter
	 *  - Snowball stemmer (English)
	 *  
	 * @param reader text to parse
	 * @return list of terms with their frequency
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Float> getFrequencyMap(Reader reader, Set<String> fakeSkill) {
		try {
			if (tokenStream == null) {
				tokenStream = new LowerCaseTokenizer(reader);
				filteredTokenStream = tokenStream;
				filteredTokenStream = new ASCIIFoldingFilter(filteredTokenStream);
				//filteredTokenStream = new ASCIIFilter(filteredTokenStream);
				if (CustomTermTokenizer.class.getResourceAsStream(STOP_WORDS_FILE)==null) {
					throw new IOException("STOP_WORDS_FILE path file could not be solved");
				}
				HashSet<String> stopWords = new HashSet<String>(fakeSkill);
				filteredTokenStream = new StopFilter(false, filteredTokenStream, stopWords);
				filteredTokenStream = new SnowballFilter(filteredTokenStream, new EnglishStemmer());
			} else {
				tokenStream.reset(reader);
		}
		} catch (IOException e) {
			//logger.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
		
		HashMap<String, Float> map = new HashMap<String, Float>();
		Attribute termAtt = filteredTokenStream.addAttribute(TermAttribute.class);
		try {
			while (filteredTokenStream.incrementToken()) {
				String term = ((TermAttribute)termAtt).term();
				Float freq = map.get(term);
				if (freq == null) {
					map.put(term, 1f);
				} else {
					map.put(term, freq + 1f);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return map;
	}
	
	/**
	 * @param maps
	 * @return
	 */
	public Map<String, Float> getNormalizedValues(Map<String, Float> maps){
		double max = 0d;
		for (String key : maps.keySet()) {
			if (maps.get(key)>=max)
				max = maps.get(key);
		}
		for (String key : maps.keySet()) {
			//double newV = (maps.get(key)/max)/edgesSize;
			double newV = maps.get(key)/max;
			DecimalFormat twoDigits = new DecimalFormat("##.##");
			maps.put(key, Float.valueOf(twoDigits.format(newV)));
		}
		return maps;
}	
	
	/**
	 * @param fileName
	 * @param content
	 */
	public static void saveFile(String fileName, String content){
		try{
			File file = new File(fileName);
		    BufferedWriter out = new BufferedWriter(new FileWriter(file));
		    out.newLine();
		    out.write(content);
		    out.close();				
	    }catch (Exception e){
	      System.err.println("Error: " + e.getMessage());
	    }
	 }

	public static void main(String[] args) {
//		StringReader  sreader = new StringReader("kiwi is a software project management kiwis story");
//		CustomTermTokenizer tokenizer = new CustomTermTokenizer();
//		
//		CustomTermTokenizer.addStopWords("ffred");
//		
////	    Map<String,Float> frequencyMap = tokenizer.getNormalizedValues(tokenizer.getFrequencyMap(sreader));
////		//frequencyMap = sortHashMap(frequencyMap);
////		for (String string : frequencyMap.keySet()) {
////			System.err.println(string +" -- "+frequencyMap.get(string));	
////		}
	}
	
		

}
