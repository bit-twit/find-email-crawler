package org.bittwit.fec.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.Props;
import org.bittwit.fec.domain.SiteEntry;
import org.bittwit.fec.parser.rule.ContactLinkRule;
import org.bittwit.fec.parser.rule.EmailRule;
import org.bittwit.fec.parser.rule.MailToRule;
import org.bittwit.fec.parser.rule.MetaRefreshRule;
import org.bittwit.fec.parser.rule.NoAccessRule;
import org.bittwit.fec.parser.rule.PrefixContactLinkRule;
import org.bittwit.fec.parser.rule.Rule;
import org.bittwit.fec.parser.rule.SuffixContactLinkRule;

public class SiteParser {
	private static final Logger LOG = LoggerFactory.getLogger(SiteParser.class);
	private static final String HTTP_PREFIX = "http";
	private static final Random random = new Random();
	private static List<Rule> rules = Arrays.asList(new EmailRule(), new MailToRule());
	private static List<Rule> contactLinkRules = Arrays.asList(new ContactLinkRule(), new PrefixContactLinkRule(), new SuffixContactLinkRule());
	private static Rule metaRefreshRule = new MetaRefreshRule();
	private static Rule noAccessRule = new NoAccessRule();

	public SiteParser () {
	}

	/**
	 * @param url
	 * @return
	 * @throws SiteNotFoundException
	 */
	public ParserResult parseSite (String url) {
		return parseSite(url, url, 0, false);
	}

	/**
	 * If depth greater than 0, look for contact page URL and follow it.
	 * 
	 * @param url
	 * @param depth
	 * @return
	 */
	public ParserResult parseSite (String url, String rootUrl, int depth, boolean metaRedirect) {
		ParserResult parserResult = new ParserResult(rootUrl);
		Set<String> emails = new HashSet<>();

		LOG.info("Start parse for entry {}", url);
		try {
//			random sleep between 0-5 secs to simulate real user
			int sleepTime = random.nextInt(5);
			LOG.info("Sleeping for {} ", sleepTime);
			Thread.sleep(1000 * sleepTime);
			
			String result = getHtmlContent(url);
//			LOG.info(result);
			LOG.info("Got result for {}. Parsing rules.", url);
			if (result != null && result.length() > 0) {
//				check for noaccess response
				Matcher noAccessMatcher = noAccessRule.match(result);
				if (noAccessMatcher.find()) {
					throw new RuntimeException("Site closed.");
				}
				
				Matcher metaM = metaRefreshRule.match(result);
//				if we have a meta redirect follow that
				if (metaM.find()) {
					if (!metaRedirect) {
						String metaUrl = metaM.group(metaRefreshRule.getGroupName());
						String absoluteMetaUrl = getAbsoluteUrl(url,metaUrl);
						LOG.info("Following meta {} ", absoluteMetaUrl);
						return parseSite(absoluteMetaUrl, rootUrl, depth, true);
					}
					else {
						throw new RuntimeException("Meta redirect loop detected.");
					}
				}

		        int count = 0;

				for (Rule r : rules) {
					LOG.info("Rule: {}", r);
					Matcher m = r.match(result);
					
			        while (m.find()) {
			        	count++;
			        	LOG.debug("Match {} value : [{}]", count, m.group());
						emails.add(m.group(r.getGroupName()));
					}
			        if (count > 0) {
			        	break;
			        }
				}
				if (count > 0) {
					parserResult.setResults(emails);
				}
				
//				try and find contact page url and follow that
				if (count == 0 && depth > 0) {
					LOG.info("Trying contact page URL.");
					boolean contactUrlFound = false;
					for (Rule contactLinkRule : contactLinkRules) {
						Matcher m = contactLinkRule.match(result);
						if (m.find()) {
							contactUrlFound = true;
							String contactUrl = m.group(contactLinkRule.getGroupName());
							contactUrl = getAbsoluteUrl(url, contactUrl);
							LOG.debug("Match {} value : [{}]", count, m.group(contactLinkRule.getGroupName()));
							parserResult = parseSite(contactUrl, rootUrl, 0, false);
							if (parserResult.getResults().size() > 0) {
								count = parserResult.getResults().size();
								break;
							}
						}
					}
					if (!contactUrlFound) {
						LOG.info("Contact url not found.");
					}
				}

				LOG.info("Found {} matches for {}, [{}].", new Object[] { count, url, parserResult.getResults()});
			}
			else {
				throw new SiteNotFoundException(url, "Site not found");
			}
		}
		catch (Exception e) {
			parserResult.setError(true);
			parserResult.setErrorMessage(e.getMessage());
		}

		return parserResult;
	}

	/**
	 * Returns a new URL that takes the relative child URL and 
	 * transforms it into an absolute URL, based on the absolute parent URL.
	 * 
	 * @param parentUrl
	 * @param childUrl
	 * @return
	 */
	protected String getAbsoluteUrl(String parentUrl, String childUrl) {
		String finalChildUrl = childUrl;
		
		if (!childUrl.startsWith(HTTP_PREFIX)) {
//			/bla/bla - contact with domain
			if (childUrl.startsWith("/")) {
				int afterDomainSlashIndex = parentUrl.indexOf("/", parentUrl.indexOf("//") + 2);

//				parentUrl is like http://domain.com
//				or like http://domain.com?...
				if (afterDomainSlashIndex < 0) {
					if (parentUrl.indexOf("?") > 0) {
						finalChildUrl = parentUrl.substring(0, parentUrl.indexOf("?")) + childUrl;
					}
					else {
						finalChildUrl = parentUrl + childUrl;
					}
				}
//				parentUrl is like http://dopmain.com/bla/ss
				else {
					finalChildUrl = parentUrl.substring(0, afterDomainSlashIndex) + childUrl;
				}
			}
//			bla/bla - concat with relative path
			else {
//				check to see if we have any / after domain name
				int lastIndexOfSlash = parentUrl.indexOf("/", parentUrl.indexOf("//") + 2);
//				parentUrl is like http://domain.com/sds/sdsd/sdsds and we want the last slash
				if (lastIndexOfSlash > 0) {
					lastIndexOfSlash = parentUrl.lastIndexOf("/");
				}

				if (lastIndexOfSlash < 0) {
					if (parentUrl.indexOf("?") > 0) {
						finalChildUrl = parentUrl.substring(0, parentUrl.indexOf("?")) + "/" + childUrl;
					}
					else {
						finalChildUrl = parentUrl + "/" + childUrl;
					}
				}
				else {
					finalChildUrl = parentUrl.substring(0, lastIndexOfSlash + 1) + childUrl;
				}
			}
		}
		
		LOG.info("Built URL [{}] from parent: {} and child {}.", new Object[] {finalChildUrl, parentUrl, childUrl});
		
		return finalChildUrl;
	}

	/**
	 * TODO : get HTML using apache client.
	 * 
	 * @param url
	 * @return
	 */
	protected String getHtmlContent(String url) {
		HttpResponse response = callRestGet(url);
		if (response == null) {
			return "";
		}
		return response.content;
	}

	static class HttpResponse {
		String status;
		String content;
		HttpResponse(String status, String content) {
			this.status = status;
			this.content = content;
		}
		@Override
		public String toString() {
			return "HttpResponse [status=" + status + ", content=" + content
					+ "]";
		}
	}

	private HttpResponse callRestGet (String path) {
		HttpGet httpGet = new HttpGet(path);

        int timeout = Integer.parseInt(Props.get("http.request.timeout"));
        RequestConfig config = RequestConfig.custom()
          .setConnectTimeout(timeout * 1000)
          .setConnectionRequestTimeout(timeout * 1000)
          .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient httpClient = 
          HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        
//	      execute request
		CloseableHttpResponse response2 = null;
		HttpResponse responseContent = null;
		try {
			response2 = httpClient.execute(httpGet);

		    LOG.debug("Response status: " + response2.getStatusLine());
		    HttpEntity entity2 = response2.getEntity();
		    
		    responseContent = new HttpResponse(response2.getStatusLine().getStatusCode() + "", EntityUtils.toString(entity2));
		}
		catch (org.apache.http.conn.ConnectTimeoutException e) {
			LOG.error(e.getMessage(), e);
			throw new SiteNotFoundException(path, "Host did not respond.");
		}
		catch (java.net.SocketTimeoutException e) {
			LOG.error(e.getMessage(), e);
			throw new SiteNotFoundException(path, "Host did not respond.");
		}
		catch (ClientProtocolException e) {
			LOG.error(e.getMessage(), e);
			throw new SiteNotFoundException(path, "Host did not respond.");
		}
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SiteNotFoundException(path, e.getMessage());
		}
		finally {
			if (response2 != null) {
				try {
					response2.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}

		LOG.debug("Rest result : [" + responseContent + "]");
		return responseContent;
	}

	public List<ParserResult> parseSites(List<SiteEntry> entries) {
		LOG.info("ENTER sequential parser .");
		Date start = new Date();
		List<ParserResult> results = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
        	SiteEntry e = entries.get(i);
        	LOG.info("Parsing entry number : [{}]", i);
    		ParserResult pr = parseSite(e.getUrl(), e.getUrl(), 1, false);
    		results.add(pr);
    	}
        Date end = new Date();
        LOG.info("Parse sites duration {}", (end.getTime() - start.getTime())/1000);
        return results;
	}

	public List<ParserResult> parseSitesInParallel(List<SiteEntry> entries) {
		LOG.info("ENTER parallel parser.");
		
		class ParserTask implements Callable<ParserResult> {
			private SiteEntry input;
			private SiteParser parser;

			ParserTask (SiteEntry se, SiteParser sp) {
				this.input = se;
				this.parser = sp;
			}

			@Override
			public ParserResult call() throws Exception {
				ParserResult pr = parser.parseSite(input.getUrl(), input.getUrl(), 1, false);
				return pr;
			}
		}

		List<Callable<ParserResult>> tasks = new ArrayList<Callable<ParserResult>>();
		for (SiteEntry se:entries) {
			tasks.add(new ParserTask(se, this));
		}

		int nrThreads = Integer.parseInt(Props.get("parser.thread.number"));
		LOG.info("Starting executor with  {} threads.", nrThreads);
		ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);

		List<Future<ParserResult>> futures = new ArrayList<Future<ParserResult>>();

		for(Callable<ParserResult> future : tasks) {
		    Future<ParserResult> result = executorService.submit(future);
		    futures.add(result);
		}

		List<ParserResult> results = new ArrayList<>();
		try {
			for(Future<ParserResult> future : futures) {
				ParserResult pr = future.get(10, TimeUnit.MINUTES);
				System.out.println("future.get = " + pr);
			    if (pr == null) {
			    	continue;
			    }
			    results.add(pr);
			}
		}
		catch (InterruptedException|ExecutionException e) {
			LOG.error(e.getMessage(), e);
		}
		catch (TimeoutException e) {
			LOG.error("Computation didn't finish in 10 minutes !!", e);
		}
		
		executorService.shutdown();
        
		LOG.info("Gathered {} results.", results.size());

        return results;
	}

	public void close () {
	}
}
