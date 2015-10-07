package org.bittwit.fec.parser.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailToRule implements Rule {
	protected static String EMAIL_TO_PATTERN = "(mailto|MAILTO):" + EmailRule.EMAIL_RULE;
	private static Pattern pattern  = Pattern.compile(EMAIL_TO_PATTERN);

	@Override
	public Matcher match(String s) {
		return pattern.matcher(s);
	}

	@Override
	public String getGroupName() {
		return EmailRule.EMAIL_GROUP_NAME;
	}

}
