package org.bittwit.fec.parser;

import java.net.URISyntaxException;
import java.util.regex.Matcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.parser.rule.FrameSetRule;

public class FrameSetRuleTest extends TestCase {
//	<frameset rows="95px, *,100px" framespacing="0" frameborder="no" border="0">
//	   <frame src="top.php" name="top" frameborder="0" scrolling="auto" noresize id="top" />
	private static final Logger LOG = LoggerFactory.getLogger(FrameSetRuleTest.class);
	private static final String TEST_HTML = 
			 "	<frameset rows=\"95px, *,100px\" framespacing=\"0\" frameborder=\"no\" border=\"0\">"
			+ "		<frame src=\"top.php\" name=\"top\" frameborder=\"0\" scrolling=\"auto\" noresize id=\"top\" />"
			+ "<frame src=\"sidebar.php?page=\" name=\"sidebar\" frameborder=\"0\" scrolling=\"no\" noresize=\"noresize\" marginwidth=\"0\" marginheight=\"0\" id=\"sidebar\">"
			+ "<frame name=\"Logopagina (boven)1\" src=\"Tekstpagina%20(boven).htm\" target=\"Tekstpagina (rechts)\" scrolling=\"no\" noresize>";
	
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FrameSetRuleTest.class );
    }
    
    public void testFrameSetRule() throws URISyntaxException
    {
    	LOG.info("Testing FrameSet rule.");

    	FrameSetRule rule = new FrameSetRule();
        Matcher m = rule.match(TEST_HTML);

        int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(rule.getGroupName()));
        }
        assertTrue(count == 3);
        LOG.info("Found {} results.", count);
    }
}
