package org.bittwit.fec.parser.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matches HTML content like "Forbidden", "404", "403", "Not found".
 *
 */
public class NoAccessRule implements Rule {
	public static final String NO_ACCESS_GROUP_NAME = "noaccess";
	protected static String NO_ACCESS_RULE = ".*(?<" + NO_ACCESS_GROUP_NAME + ">(Forbidden|Not found)).*";
    private static java.util.regex.Pattern pattern = Pattern.compile(NO_ACCESS_RULE);

	@Override
	public Matcher match(String s) {
		return pattern.matcher(s);
	}

	@Override
	public String getGroupName() {
		return NO_ACCESS_GROUP_NAME;
	}
}
