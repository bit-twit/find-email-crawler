package org.bittwit.fec.domain;

import org.bittwit.fec.Props;

public class SiteEntry {
	public static final int NAME_COLUMN = Integer.parseInt(Props.get("excel.name.column"));
	public static final int URL_COLUMN = Integer.parseInt(Props.get("excel.url.column"));
	public static final int RESULTS_COLUMN = Integer.parseInt(Props.get("excel.results.column"));
	public static final int ERROR_COLUMN = Integer.parseInt(Props.get("excel.errors.column"));

	private String siteName;
	private String url;

	public SiteEntry(String siteName, String url) {
		super();
		this.siteName = siteName;
		this.url = url;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
