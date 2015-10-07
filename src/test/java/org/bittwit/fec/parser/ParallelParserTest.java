package org.bittwit.fec.parser;

import java.util.Arrays;
import java.util.List;

import org.bittwit.fec.parser.ParserResult;
import org.bittwit.fec.parser.SiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.domain.SiteEntry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParallelParserTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(ParallelParserTest.class);

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ParallelParserTest.class );
    }

    public void testParallel () {
    	LOG.info("Starting parallel test.");
    	SiteParser sp = new SiteParser();
    	
    	SiteEntry se = new SiteEntry("test1", "http://www.google.com");
    	SiteEntry se2 = new SiteEntry("test2", "http://www.valvesoftware.com/contact/");
    	
    	List<SiteEntry> entries = Arrays.asList(se, se2);
    	List<ParserResult> results = sp.parseSitesInParallel(entries);
    	
    	sp.close();
    	
    	assertTrue(results.size() == 2);
    	assertTrue(results.get(0).isError() == false);
    	assertTrue(results.get(1).isError() == false);
    	
//    	get google results
    	ParserResult googleResult = null;
    	for (ParserResult p : results) {
    		if (p.getUrl().equals("http://www.google.com")) {
    			googleResult = p;
    		}
    	}
    	assertNotNull(googleResult.getResults());
    	assertTrue(googleResult.getResults().size() == 0);

//    	get google results
    	ParserResult valveResult = null;
    	for (ParserResult p : results) {
    		if (p.getUrl().equals("http://www.valvesoftware.com/contact/")) {
    			valveResult = p;
    		}
    	}
    	assertNotNull(valveResult.getResults());
    	assertTrue(valveResult.getResults().size() > 0);
    	
    }
}
