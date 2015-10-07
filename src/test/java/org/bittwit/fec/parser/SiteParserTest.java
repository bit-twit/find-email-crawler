package org.bittwit.fec.parser;

import java.net.URISyntaxException;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.bittwit.fec.parser.ParserResult;
import org.bittwit.fec.parser.SiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteParserTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(SiteParserTest.class);

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SiteParserTest.class );
    }

    public void testGetSiteContent() throws URISyntaxException
    {
    	LOG.info("Testing site content.");

    	SiteParser parser = new SiteParser();
        
        String content = parser.getHtmlContent("http://www.google.com");
        parser.close();
        assertTrue(content.length() > 0);
        assertTrue(content.contains("<body"));
        assertTrue(content.contains("</body>"));
        assertTrue(content.contains("google"));
    }

    /**
     * This should go through all frames and merge the content from frame's view source :  TODO http://www.ilka-taxaties.nl/ 
     * 
     * @throws URISyntaxException
     */
    public void testGetSiteContentWithFrames() throws URISyntaxException {
    	LOG.info("Testing site content.");

    	SiteParser parser = new SiteParser();
        
        String content = parser.getHtmlContent("http://www.google.com");
        parser.close();
        assertTrue(content.length() > 0);
        assertTrue(content.contains("<body"));
        assertTrue(content.contains("</body>"));
        assertTrue(content.contains("google"));
    }

    public void testParseLink() throws URISyntaxException
    {
    	LOG.info("Testing parsing content from rules.");

    	SiteParser parser = new SiteParser();
        
//    	http://www.hoofddorp.com/
        ParserResult pr = parser.parseSite("http://www.valvesoftware.com/contact/");
        parser.close();
        
        assertFalse(pr.isError());
        
        Set<String> results = pr.getResults();
        assertTrue(results.size() > 0);
        LOG.info("Found results: {}", results);
    }
    
    /**
     * http://www.vgmakelaardij.nl/
     */
    public void testParseLinkFromContactUrl () {
    	LOG.info("Testing parsing of contact URL, if email not found on landing page.");
    	
    	SiteParser parser = new SiteParser();
    	Set<String> results = null;

//    	ParserResult pr = parser.parseSite("http://www.vgmakelaardij.nl/", "http://www.vgmakelaardij.nl/", 1);
    	ParserResult pr = parser.parseSite("http://www.westerhouse.nl", "http://www.westerhouse.nl", 1, false);
    	parser.close();
    	
    	assertFalse(pr.isError());
    	
    	results = pr.getResults();
    	assertTrue(results.size() > 0) ;
    	LOG.info("Found results : {}", results);

//    	mi-au dat cu deny
//    	LOG.info("Test for http://www.egginkmaalderinkaalten.nl/.");
//    	ParserResult pr2 = parser.parseSite("http://www.egginkmaalderinkaalten.nl/", 1);
//    	
//    	assertFalse(pr2.isError());
//    	
//    	results = pr2.getResults();
//    	assertTrue(results.size() > 0) ;
//    	LOG.info("Found results : {}", results);

//    	LOG.info("Second Testing parsing of contact URL, if email not found on landing page.");
//    	ParserResult pr2 = parser.parseSite("http://www.dewitteraafmakelaars.nl", 1);
//    	
//    	assertFalse(pr2.isError());
//    	
//    	results = pr2.getResults();
//    	assertTrue(results.size() > 0) ;
//    	LOG.info("Found results : {}", results);
    }

    public void testGetAbsoluteUrl () {
    	LOG.info("Testing get absolute url.");

    	SiteParser parser = new SiteParser();
    	String result = "";
    	
        result = parser.getAbsoluteUrl("http://www.blabl.com", "contact.html");
        assertEquals("http://www.blabl.com/contact.html", result);
        
        result = parser.getAbsoluteUrl("http://www.blabl.com/", "contact.html");
        assertEquals("http://www.blabl.com/contact.html", result);
        
        result = parser.getAbsoluteUrl("http://www.blabl.com/", "/contact.html");
        assertEquals("http://www.blabl.com/contact.html", result);

        result = parser.getAbsoluteUrl("http://www.blabl.com/ceva/bla?param1=sdsd", "contact.html");
        assertEquals("http://www.blabl.com/ceva/contact.html", result);

        result = parser.getAbsoluteUrl("http://www.blabl.com?param2=sdsd", "contact.html");
        assertEquals("http://www.blabl.com/contact.html", result);

        result = parser.getAbsoluteUrl("http://www.blabl.com/ceva/bla/aaa", "/contact.html");
        assertEquals("http://www.blabl.com/contact.html", result);
        
        parser.close();
    }
}
