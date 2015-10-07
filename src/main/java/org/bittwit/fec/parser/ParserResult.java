package org.bittwit.fec.parser;

import java.util.HashSet;
import java.util.Set;

public class ParserResult {

	private String url;
	private Set<String> results;
	private boolean error;
	private String errorMessage;

	@Override
	public String toString() {
		return "ParserResult [url=" + url + ", results=" + results + ", error="
				+ error + ", errorMessage=" + errorMessage + "]";
	}

	public ParserResult(String url, Set<String> results, boolean error,
			String errorMessage) {
		this(url);
		this.results = results;
		this.error = error;
		this.errorMessage = errorMessage;
	}

	public ParserResult(String url) {
		this.url = url;
		this.results  = new HashSet<>();
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Set<String> getResults() {
		return results;
	}
	public void setResults(Set<String> results) {
		this.results = results;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getResultsString() {
		StringBuilder sb = new StringBuilder();
		for (String s : results) {
			sb.append(s).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
