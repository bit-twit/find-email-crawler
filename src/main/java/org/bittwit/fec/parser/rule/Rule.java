package org.bittwit.fec.parser.rule;

import java.util.regex.Matcher;

public interface Rule {

	public Matcher match (String s);
	
	public String getGroupName();
}
