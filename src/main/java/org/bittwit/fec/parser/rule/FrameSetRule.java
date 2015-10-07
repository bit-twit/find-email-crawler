package org.bittwit.fec.parser.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrameSetRule implements Rule {
	private static final String URL_GROUP_NAME = "url";
	protected static String URL_RULE = "[a-zA-Z0-9-_/?%():=\\.]+";
	private static final String ATTRIB_CHAR = "[a-zA-Z0-9-_/?\"%():=\\.]|\\s";
//	<frameset rows="95px, *,100px" framespacing="0" frameborder="no" border="0">
//	   <frame src="top.php" name="top" frameborder="0" scrolling="auto" noresize id="top" />

//	  <frameset cols="*,38%,22%">
//	  <frame name="Logopagina (boven)" scrolling="no" noresize target="Logopagina (boven)" src="Logopagina%20(boven).htm">
//	    <frame name="Logopagina (boven)1" src="Tekstpagina%20(boven).htm" target="Tekstpagina (rechts)" scrolling="no" noresize>
	protected static String FRAMESET_LINK_RULE = "<(iframe|frame|FRAME|IFRAME)(" + ATTRIB_CHAR + ")*(src|SRC)=\"?(?<" + URL_GROUP_NAME + ">" + URL_RULE + ")\"?(" + ATTRIB_CHAR + ")*/?>";
    private static java.util.regex.Pattern pattern = Pattern.compile(FRAMESET_LINK_RULE);

	@Override
	public Matcher match(String s) {
		return pattern.matcher(s);
	}

	@Override
	public String getGroupName() {
		return URL_GROUP_NAME;
	}

}
