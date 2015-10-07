package org.bittwit.fec.parser;

import java.net.URISyntaxException;
import java.util.regex.Matcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.parser.rule.EmailRule;
import org.bittwit.fec.parser.rule.MailToRule;

public class EmailRuleTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(EmailRuleTest.class);

	private static final String TEST_HTML = 
			 "	<ul class=\"email\">"
			+ "		<li><span>E-mail:</span> <a href=\"mailto:oginfo@govaert.nl\">oginfo@govaert.nl</a></li>"
			+ "	</ul>"
			+ "	<ul class=\"openingstijden\"><a href=\"mailto:ogi3nfo@govaert.nl\">"
			+ "		<li><span>Openingstijden:</span> Maandag t/m vrijdag van 09:00 uur tot 17:30 uur, zaterdagen en s avonds op afspraak.</li>"
			+ "	</ul><a href=\"mailto:og444info@govaert.nl\">"
			+ "<a id=\"generated_id163_9320779\" href=\"mailto:aalten@egginkmaalderink.nl\">";

//	Email rule shoudn't match all this as email	
	private static final String TEST_HTML_DUBIOS = "//woonzeker.com/Te-huur/huurwoning-te-huur/42036/ieplaan?action=replyandlog&email=judith.vdwetering@gmail.com";

	private static final String TEST_HTML_DUBIOS_2 = "<a href=\"mailto:bergenopzoom@hypodomus.nl\" "
			+ "id=\"generated_id131_9051005\"><span id=\"generated_id131_9051006\">bergenopzoom@hypodomus.nl</span></a>";
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( EmailRuleTest.class );
    }

    public void testEmailRule() throws URISyntaxException
    {
    	LOG.info("Testing email rule.");

    	EmailRule rule = new EmailRule();
        Matcher m = rule.match(TEST_HTML);

        int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(rule.getGroupName()));
        }
        assertTrue(count == 5);
        LOG.info("Found {} results.", count);
    }
    
    public void testEmailRuleDubios() throws URISyntaxException
    {
    	LOG.info("Testing email rule dubios.");

    	EmailRule rule = new EmailRule();
        Matcher m = rule.match(TEST_HTML_DUBIOS);

        int count = 0;
        String result = "";
        if (m.find()) {
        	count++;
        	result = m.group(rule.getGroupName());
        	LOG.info("Match {} value : [{}]", count, result);
        }
        assertTrue(count == 1);
        assertEquals("judith.vdwetering@gmail.com", result);
        LOG.info("Found {} results.", count);
    }

    public void testEmailRuleDubios2() throws URISyntaxException
    {
    	LOG.info("Testing email rule dubios 2, this should work.");

    	EmailRule rule = new EmailRule();
        Matcher m = rule.match(TEST_HTML_DUBIOS_2);

        int count = 0;
        String result = "";
        if (m.find()) {
        	count++;
        	result = m.group(rule.getGroupName());
        	LOG.info("Match {} value : [{}]", count, result);
        }
        assertTrue(count == 1);
        assertEquals("bergenopzoom@hypodomus.nl", result);
        LOG.info("Found {} results.", count);
    }

    public void testMailToRule() throws URISyntaxException
    {
    	LOG.info("Testing mailto rule.");

    	MailToRule rule = new MailToRule();
        Matcher m = rule.match(TEST_HTML);

        int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(rule.getGroupName()));
        }
        assertTrue(count == 4);
        LOG.info("Found {} results.", count);
    }
}
