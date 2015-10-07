package org.bittwit.fec.parser;

import java.net.URISyntaxException;
import java.util.regex.Matcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.parser.rule.MetaRefreshRule;

public class MetaRefreshRuleTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(MetaRefreshRuleTest.class);
	private static final String TEST_HTML = 
			 "	<ul class=\"email\">"
			+ "		<li><span>E-mail:</span> <a href=\"mailto:oginfo@govaert.nl\">oginfo@govaert.nl</a></li>"
			+ "	</ul><meta HTTP-EQUIV=\"REFRESH\" content=\"0; url=index2.php\">"
			+ "	<ul class=\"openingstijden\"><a href=\"mailto:ogi3nfo@govaert.nl\">"
			+ "		<li><span>Openingstijden:</span> Maandag t/m vrijdag van 09:00 uur tot 17:30 uur, zaterdagen en s avonds op afspraak.</li>"
			+ "	</ul><a href=\"mailto:og444info@govaert.nl\">";
//	<meta HTTP-EQUIV="REFRESH" content="0; url=index2.php">
	
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MetaRefreshRuleTest.class );
    }
    
    public void testMetaRefreshRule() throws URISyntaxException
    {
    	LOG.info("Testing meta refresh rule.");

    	MetaRefreshRule rule = new MetaRefreshRule();
        Matcher m = rule.match(TEST_HTML);

        int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(rule.getGroupName()));
        }
        assertTrue(count == 1);
        LOG.info("Found {} results.", count);
    }
}
