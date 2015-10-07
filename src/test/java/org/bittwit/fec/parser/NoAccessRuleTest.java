package org.bittwit.fec.parser;

import java.util.regex.Matcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.parser.rule.NoAccessRule;

public class NoAccessRuleTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(NoAccessRuleTest.class);

	private static final String TEST_HTML = 
			 "	<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">"
			 	+ "<html><head>"
			 	+ "<title>403 Forbidden</title>"
			 	+ "</head><body>"
			 	+ "<h1>Forbidden</h1>"
			 	+ "<p>You don't have permission to access /"
			 	+ "on this server.</p>"
			 	+ "</body></html>";
	
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( NoAccessRuleTest.class );
    }
    
    public void testNoAccessRule() {
    	LOG.info("Testing no access rule.");

    	NoAccessRule rule = new NoAccessRule();
        Matcher m = rule.match(TEST_HTML);

        int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(rule.getGroupName()));
        }
        assertTrue(count >= 1);
        LOG.info("Found {} results.", count);
    }
}
