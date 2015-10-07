package org.bittwit.fec.parser.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetaRefreshRule implements Rule {
	public static final String URL_GROUP_NAME = "url";
	static final String VALID_TAG_CHARS = "[a-zA-Z0-9-_.'/;=\"]|\\s"; 
	protected static String URL_RULE = "[a-zA-Z0-9-_/:.]+";
//	<meta HTTP-EQUIV="REFRESH" content="0; url=index2.php">
//	protected static String META_LINK_RULE = "<(meta)(" + VALID_TAG_CHARS + ")*(url|URL)=\"?(?<" + URL_GROUP_NAME + ">" + URL_RULE + ")\"?(" + VALID_TAG_CHARS + ")*>";
	protected static String META_LINK_RULE = "<(meta|META).*(url|URL)=\"?(?<" + URL_GROUP_NAME + ">" + URL_RULE + ")\"?.*>";
    private static java.util.regex.Pattern pattern = Pattern.compile(META_LINK_RULE);

	@Override
	public Matcher match(String s) {
		return pattern.matcher(s);
	}

	@Override
	public String getGroupName() {
		return URL_GROUP_NAME;
	}
}
