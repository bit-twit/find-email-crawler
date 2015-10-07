package org.bittwit.fec.parser.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailRule implements Rule {
	static final String EMAIL_GROUP_NAME = "email";
	protected static String EMAIL_RULE = "(?<" + EMAIL_GROUP_NAME + ">" + "[a-zA-Z0-9.!'*+^_-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,})))";
    private static java.util.regex.Pattern pattern = Pattern.compile(EMAIL_RULE);

	@Override
	public Matcher match(String s) {
		return pattern.matcher(s);
	}

	@Override
	public String getGroupName() {
		return EMAIL_GROUP_NAME;
	}

}
