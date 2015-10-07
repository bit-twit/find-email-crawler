package org.bittwit.fec.parser;

public class SiteNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -2473177882731897772L;

	private String siteUrl;

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public SiteNotFoundException (String siteUrl, String message) {
		super(message);
		this.siteUrl = siteUrl;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + ", site: " + siteUrl;
		
	}
}
