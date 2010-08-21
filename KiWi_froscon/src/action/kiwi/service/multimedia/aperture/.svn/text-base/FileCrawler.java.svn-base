/*
 * Copyright (c) 2005 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Academic Free License version 3.0.
 */

package kiwi.service.multimedia.aperture;

import java.io.File;
import java.util.List;

import org.ontoware.rdf2go.exception.ModelException;
import org.semanticdesktop.aperture.accessor.impl.DefaultDataAccessorRegistry;
import org.semanticdesktop.aperture.crawler.filesystem.FileSystemCrawler;
import org.semanticdesktop.aperture.datasource.filesystem.FileSystemDataSource;
import org.semanticdesktop.aperture.rdf.RDFContainer;
import org.semanticdesktop.aperture.rdf.impl.RDFContainerFactoryImpl;
import org.semanticdesktop.nepomuk.nrl.validator.ModelTester;
import org.semanticdesktop.nepomuk.nrl.validator.testers.DataObjectTreeModelTester;

/**
 * Example class that crawls a file system and stores all extracted metadata in a RDF file.
 * @author fluit, sauermann, klinkigt
 */
public class FileCrawler extends AbstractCrawler {

    private File rootFile;
    private Boolean suppressParentChildLinks = Boolean.FALSE;
    
    public static final String SUPPRESS_PARENT_CHILD_LINKS_OPTION = "--suppressParentChildLinks";
    
    public void crawl() throws ModelException {
        if (rootFile == null) {
            throw new IllegalArgumentException("root file cannot be null");
        }

        // create a data source configuration
        RDFContainerFactoryImpl factory = new RDFContainerFactoryImpl();
        RDFContainer configuration = factory.newInstance("source:testsource");

        // create the data source
        FileSystemDataSource source = new FileSystemDataSource();
        source.setConfiguration(configuration);

        source.setRootFolder(rootFile.getAbsolutePath());
        source.setSuppressParentChildLinks(suppressParentChildLinks);
        
        // setup a crawler that can handle this type of DataSource
        FileSystemCrawler crawler = new FileSystemCrawler();
        crawler.setDataSource(source);
        crawler.setDataAccessorRegistry(new DefaultDataAccessorRegistry());
        crawler.setCrawlerHandler(getHandler());
        crawler.setAccessData(getAccessData());
        
        // start crawling
        crawler.crawl();
    }

    public void setRootFile(File rootFile) {
        this.rootFile = rootFile;
    }
    
    public File getRootFile() {
        return rootFile;
    }
    
    
    /**
     * The FileSystem crawler satisfies a more strict constraint
     */
    @Override
    public ModelTester[] getAdditionalModelTesters() {
        return new ModelTester[] { new DataObjectTreeModelTester() };
    }

    /**
     * The main method
     * @param args command line arguments
     * @throws ModelException
     */
    public static void main(String[] args) throws Exception {
        // create a new ExampleFileCrawler instance
        FileCrawler crawler = new FileCrawler();

        // parse the command line options
        
        List<String> remaining = crawler.processCommonOptions(args);
        
        for (String arg : remaining) {
            if (arg.equals(SUPPRESS_PARENT_CHILD_LINKS_OPTION)) {
                crawler.setSuppressParentChildLinks(Boolean.TRUE);
                continue;
            } else if (arg.startsWith("-")) {
                System.err.println("Unknown option: " + arg);
                crawler.exitWithUsageMessage();
            } else if (crawler.getRootFile() == null) {
                crawler.setRootFile(new File(arg));
            }
            else {
                crawler.exitWithUsageMessage();
            }
        }
        
        if (crawler.getRootFile() == null) {
            crawler.exitWithUsageMessage();
        }

        // start crawling and exit afterwards
        crawler.crawl();
    }
    
    @Override
    protected String getSpecificExplanationPart() {
        return "   " + SUPPRESS_PARENT_CHILD_LINKS_OPTION + "   Supress the addition of parent->child nie:hasPart triples\n" + 
               "   <root-folder>  - the directory to start crawling";
    }

    @Override
    protected String getSpecificSyntaxPart() {
        return "[" + SUPPRESS_PARENT_CHILD_LINKS_OPTION + "] " + "<root-folder>";        
    }

    
    /**
     * @return Returns the suppressParentChildLinks.
     */
    public synchronized Boolean getSuppressParentChildLinks() {
        return suppressParentChildLinks;
    }

    
    /**
     * @param suppressParentChildLinks The suppressParentChildLinks to set.
     */
    public synchronized void setSuppressParentChildLinks(Boolean suppressParentChildLinks) {
        this.suppressParentChildLinks = suppressParentChildLinks;
    }
}
