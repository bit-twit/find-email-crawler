package org.bittwit.fec.parser.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * To get only the URL part from matcher, should be used as m.group(ContactLinkRule.URL_GROUP_NAME).
 * 
 * @author bit_twit
 *
 */
public class ContactLinkRule implements Rule {
	public static final String URL_GROUP_NAME = "url";
	static final String VALID_TAG_CHARS = "[a-zA-Z0-9-_.'/;=\"]|\\s"; 
	protected static String URL_RULE = "[a-zA-Z0-9-_/:.?=]*contact[a-zA-Z0-9-._/.?=]*";
	protected static String CONTACT_LINK_RULE = "<(a|link)(" + VALID_TAG_CHARS + ")*href=\"(?<" + URL_GROUP_NAME + ">" + URL_RULE + ")\"(" + VALID_TAG_CHARS + ")*>";
    private static java.util.regex.Pattern pattern = Pattern.compile(CONTACT_LINK_RULE, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	@Override
	public Matcher match(String s) {
		return pattern.matcher(s);
	}

	@Override
	public String getGroupName() {
		return URL_GROUP_NAME;
	}
}
