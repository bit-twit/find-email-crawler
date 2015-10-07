package org.bittwit.fec.parser.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * To get only the URL part from matcher, should be used as m.group(ContactLinkRule.URL_GROUP_NAME).
 * 
 * @author bit_twit
 *
 */
public class SuffixContactLinkRule implements Rule {
	public static final String URL_GROUP_NAME = "url";
//	protected static String URL_RULE = "[a-zA-Z0-9-_/:.]*contact[a-zA-Z0-9-._/]*";
	protected static String CONTACT_LINK_RULE =
			"<(a|link)(" + ContactLinkRule.VALID_TAG_CHARS + ")*href=\"(?<" + URL_GROUP_NAME + ">" + "[a-zA-Z0-9-_/?=:.]+" + ")\"(" + ContactLinkRule.VALID_TAG_CHARS + ")*contact(" + ContactLinkRule.VALID_TAG_CHARS + ")*>";
    private static java.util.regex.Pattern pattern = Pattern.compile(CONTACT_LINK_RULE);

	@Override
	public Matcher match(String s) {
		return pattern.matcher(s);
	}

	@Override
	public String getGroupName() {
		return URL_GROUP_NAME;
	}
}
