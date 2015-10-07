package org.bittwit.fec;

import java.net.URISyntaxException;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bittwit.fec.App;
import org.bittwit.fec.domain.SiteEntry;
import org.bittwit.fec.parser.EntriesParser;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	private static final Logger LOG = LoggerFactory.getLogger(AppTest.class);
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testApp() throws URISyntaxException
    {
    	LOG.info("Testing testApp().");

    	EntriesParser parser = new EntriesParser();
        
        List<SiteEntry> entries = parser.getEntries(App.getFile("entries.xlsx"));
        
        assertTrue(entries.size() > 0);
        
        for (SiteEntry e : entries) {
        	assertTrue(e.getSiteName() != null);
        	assertTrue(e.getUrl() != null);
        	LOG.debug("Found entry: [{}]", e.getUrl());
        }
    }
}
