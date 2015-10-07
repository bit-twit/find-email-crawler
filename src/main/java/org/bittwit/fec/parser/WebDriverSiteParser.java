package org.bittwit.fec.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.Props;
import org.bittwit.fec.parser.rule.FrameSetRule;
import org.bittwit.fec.parser.rule.Rule;

public class WebDriverSiteParser extends SiteParser {
	private static final Logger LOG = LoggerFactory.getLogger(WebDriverSiteParser.class);
	private static Rule frameSetRule = new FrameSetRule();

	private WebDriver driver;

	{
		String profileFolderPath = Props.get("firefox.profile.dir");
		LOG.info("Trying to use profile folder [{}]", profileFolderPath);
		driver = new FirefoxDriver(new FirefoxProfile(new File (profileFolderPath)));
		driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(300, TimeUnit.SECONDS);
	}

	@Override
	protected String getHtmlContent (String url) {
	    driver.get(url);
        String pageSource = driver.getPageSource();

//		try and find frame page url and follow that
		if (hasFrames(pageSource)) {
			pageSource = getMergedHtml(getHtmlFromFrames(driver, pageSource));
		}

	    return pageSource;
	}

	@Override
	public void close () {
	    driver.quit();
	}

    private String getMergedHtml (List<String> htmls) {
		StringBuilder sb = new StringBuilder();
		for (String s : htmls) {
			sb.append(s);
		}
		return sb.toString();
	}

	private boolean hasFrames(String result) {
		boolean frameSetUrlFound = false;
		Matcher m = frameSetRule.match(result);
		
		if (m.find()) {
			frameSetUrlFound = true;
		}
		LOG.info("Has frames {}.", frameSetUrlFound);		

		return frameSetUrlFound;
	}
	
	/**
	 * Not implemented in simple SiteParser.
	 * @see WebDriverSiteParser
	 * 
	 * @param driver
	 * @param result
	 * @return
	 */
	private static List<String> getHtmlFromFrames(WebDriver driver, String result) {
		LOG.info("Trying to get HTML from frames.");
		List<String> frameContent = new ArrayList<String>();

        java.util.List<WebElement> l = driver.findElements(By.tagName("iframe"));
        
        if (l.size() == 0) {
        	l = driver.findElements(By.tagName("frame"));
        }
        LOG.info("Found {} frame tags.", l.size());

        for( WebElement ifr : l) {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(ifr);

            try {
                String pageSource = driver.getPageSource();
                frameContent.add(pageSource);
            }
            catch (NoSuchElementException e) {
            	LOG.error(e.getMessage(), e);
            }
            driver.switchTo().defaultContent();
        }

		LOG.trace("{} frames with values : {}", frameContent.size(), frameContent);

		return frameContent;
	}
}
