package org.bittwit.fec.parser;

import java.net.URISyntaxException;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.bittwit.fec.parser.ParserResult;
import org.bittwit.fec.parser.WebDriverSiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverSiteParserTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(WebDriverSiteParserTest.class);

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( WebDriverSiteParserTest.class );
    }

    public void testGetSiteContent() throws URISyntaxException {
    	LOG.info("Testing site content.");

    	WebDriverSiteParser parser = new WebDriverSiteParser();
        
        String content = parser.getHtmlContent("http://www.google.com");
        
        parser.close();

        assertTrue(content.length() > 0);
        assertTrue(content.contains("<body"));
        assertTrue(content.contains("</body>"));
        assertTrue(content.contains("google"));
    }
    
    public void testParseLink() throws URISyntaxException {
    	LOG.info("Testing parsing content from rules.");

    	WebDriverSiteParser parser = new WebDriverSiteParser();
        ParserResult pr = parser.parseSite("http://www.valvesoftware.com/contact/");
        parser.close();

        assertFalse(pr.isError());
        
        Set<String> results = pr.getResults();
        assertTrue(results.size() > 0);
        LOG.info("Found results: {}", results);
        
//        http://www.hypodomus-bergenopzoom.nl/
        WebDriverSiteParser parser2 = new WebDriverSiteParser();
        ParserResult pr2 = parser2.parseSite("http://www.hypodomus-bergenopzoom.nl/");
        parser2.close();
        
        assertFalse(pr2.isError());
  
        Set<String> results2 = pr2.getResults();
        assertTrue(results2.size() > 0);
        LOG.info("Found results: {}", results2);
      
    	WebDriverSiteParser parser3 = new WebDriverSiteParser();
        ParserResult pr3 = parser3.parseSite("http://www.hoofddorp.com/");
        parser3.close();
        
        assertFalse(pr3.isError());
        
        Set<String> results3 = pr3.getResults();
        assertTrue(results3.size() > 0);
        LOG.info("Found results: {}", results3);
    }
    
    /**
     * http://www.vgmakelaardij.nl/
     */
    public void testParseLinkFromContactUrl () {
    	LOG.info("Testing parsing of contact URL, if email not found on landing page.");
    	WebDriverSiteParser parser = new WebDriverSiteParser();
    	Set<String> results = null;

//    	ParserResult pr = parser.parseSite("http://www.vgmakelaardij.nl/", "http://www.vgmakelaardij.nl/", 1);
    	ParserResult pr = parser.parseSite("http://www.westerhouse.nl", "http://www.westerhouse.nl", 1, false);
    	
    	parser.close();
    	
    	assertFalse(pr.isError());
    	
    	results = pr.getResults();
    	assertTrue(results.size() > 0) ;
    	LOG.info("Found results : {}", results);
    	
//    	http://www.leonhuismanmakelaardij.nl/index.php/contact.html
    	WebDriverSiteParser parser2 = new WebDriverSiteParser();
    	ParserResult pr2 = parser2.parseSite("http://www.leonhuismanmakelaardij.nl", "http://www.leonhuismanmakelaardij.nl", 1, false);
    	parser2.close();
    	
    	assertFalse(pr2.isError());
    	
    	Set<String> results2 = pr2.getResults();
    	assertTrue(results2.size() > 0) ;
    	LOG.info("Found results : {}", results2);

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

    public void testParseLinkFromContactUrlWithFrames () {
    	LOG.info("Testing parsing of contact URL, if email not found on landing page.");
    	WebDriverSiteParser parser = new WebDriverSiteParser();
    	Set<String> results = null;

//    	ParserResult pr = parser.parseSite("http://www.vgmakelaardij.nl/", "http://www.vgmakelaardij.nl/", 1);
    	ParserResult pr = parser.parseSite("http://www.zijlvester.com/", "http://www.zijlvester.com/", 1, false);
    	
    	parser.close();
    	
    	assertFalse(pr.isError());
    	
    	results = pr.getResults();
    	assertTrue(results.size() > 0) ;
    	LOG.info("Found results : {}", results);
    }

    public void testGetAbsoluteUrl () {
    	LOG.info("Testing get absolute url.");

    	WebDriverSiteParser parser = new WebDriverSiteParser();
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
    
    /**
     * This should go through all frames and merge the content from frame's view source. 
     * Ex: http://www.ilka-taxaties.nl/
     * 
     * @throws URISyntaxException
     */
    public void testGetSiteContentWithFrames() throws URISyntaxException {
    	LOG.info("Testing site content.");

    	WebDriverSiteParser parser = new WebDriverSiteParser();
        
        String content = parser.getHtmlContent("http://www.ilka-taxaties.nl/");
        parser.close();
        assertTrue(content.length() > 0);
        assertTrue(content.contains("<body"));
        assertTrue(content.contains("</body>"));
        assertTrue(content.contains("ILKA Taxaties"));
        assertTrue(content.contains("href=\"contact.html\""));
    }
}
