package org.bittwit.fec.parser;

import java.net.URISyntaxException;
import java.util.regex.Matcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.parser.rule.ContactLinkRule;
import org.bittwit.fec.parser.rule.PrefixContactLinkRule;
import org.bittwit.fec.parser.rule.SuffixContactLinkRule;

public class ContactLinkRuleTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(ContactLinkRuleTest.class);

	private static final String TEST_HTML = 
			 "	<ul class=\"email\">"
			+ "		<li><span>E-mail:</span> <a href=\"mailto:oginfo@govaert.nl\">oginfo@govaert.nl</a></li>"
			+ "	</ul><a href=\"/contact\">Contact</a>"
			+ "	<ul class=\"openingstijden\"><a href=\"mailto:ogi3nfo@govaert.nl\">"
			+ "<a href=\"http://www.woneninmiddenlimburg.nl/contact/\">Contact</a>"
			+ "		<li><span>Openingstijden:</span> Maandag t/m vrijdag van 09:00 uur tot 17:30 uur, zaterdagen en s avonds op afspraak.</li>"
			+ "	</ul><a href=\"mailto:og444info@govaert.nl\">"
			+ "<p>neem <a href=\"http://www.opwegnaareenhuis.nl/aankoopmakelaarzwolle/contact.html\" title=\"contact\">contact</a> op met Joyce</p>";

	private static final String TEST_LINK_HTML = "<link rel=\"canonical\" href=\"http://www.halmakelaardij.nl/overons/contact/\">";

	private static final String TEST_LINK_HTML_2 = "<a href=\"/contact\">"
			+ "<a class=\"sub  sf-with-ul\" title=\"Contact\" href=\"/overons/contact/\">" 
			+ "<span class=\"menu-item-left\"></span><span class=\"menu-item-mid\">Contact</span><span class=\"menu-item-right\"></span>"
						+ "</a>"
						+ "<a href=\"site7b72.html?pagina=contact\">Contact</a>";
		
	private static final String PREFIX_TEST_HTML = "<a target=\"_top\" onmouseover=\"raaf.src='./pics/mouse-contact.gif';\" "
			+ "onmouseout=\"raaf.src='./pics/mouse-raaf.gif';\" href=\"index2.php?page=5\">"
			+ "<img src=\"./pics/button-contact.gif\" border=\"0\"></a>";

	private static final String SUFFIX_TEST_HTML = "<a target=\"_top\" "
			+ "onmouseout=\"raaf.src='./pics/mouse-raaf.gif';\" href=\"index2.php?page=5\" onmouseover=\"raaf.src='./pics/mouse-contact.gif';\">"
			+ "<img src=\"./pics/button-contact.gif\"   border=\"0\"></a>";;

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ContactLinkRuleTest.class );
    }

    public void testContactARule() throws URISyntaxException
    {
    	LOG.info("Testing contact a rule.");

    	ContactLinkRule rule = new ContactLinkRule();
        Matcher m = rule.match(TEST_HTML);

        int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(ContactLinkRule.URL_GROUP_NAME));
        }
        assertTrue(count == 3);
        LOG.info("Found {} results.", count);
    }

    public void testContactLinkRule() throws URISyntaxException
    {
    	LOG.info("Testing contact link rule.");

    	ContactLinkRule rule = new ContactLinkRule();
        Matcher m = rule.match(TEST_LINK_HTML);

        int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(ContactLinkRule.URL_GROUP_NAME));
        }
        assertTrue(count == 1);
        LOG.info("Found {} results.", count);
    }

    public void testContactLink2Rule() throws URISyntaxException
    {
    	LOG.info("Testing contact link rule.");

    	ContactLinkRule rule = new ContactLinkRule();
        Matcher m = rule.match(TEST_LINK_HTML_2);

        int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(ContactLinkRule.URL_GROUP_NAME));
        }
        assertTrue(count == 3);
        LOG.info("Found {} results.", count);
    }
    
    public void testPrefixContactLinkRule () {
    	LOG.info("Testing prefix contact rule.");
    	
    	PrefixContactLinkRule rule = new PrefixContactLinkRule();
    	Matcher m = rule.match(PREFIX_TEST_HTML);
    	
    	int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(ContactLinkRule.URL_GROUP_NAME));
        }
        assertTrue(count == 1);
        LOG.info("Found {} results.", count);
    }
    
    public void testSuffixContactLinkRule () {
    	LOG.info("Testing suffix contact rule.");
    	
    	SuffixContactLinkRule rule = new SuffixContactLinkRule();
    	Matcher m = rule.match(SUFFIX_TEST_HTML);
    	
    	int count = 0;
        while (m.find()) {
        	count++;
        	LOG.info("Match {} value : [{}]", count, m.group(ContactLinkRule.URL_GROUP_NAME));
        }
        assertTrue(count == 1);
        LOG.info("Found {} results.", count);
    }
}
