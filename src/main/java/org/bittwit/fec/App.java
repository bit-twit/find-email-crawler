package org.bittwit.fec;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.domain.SiteEntry;
import org.bittwit.fec.parser.EntriesParser;
import org.bittwit.fec.parser.ParserResult;
import org.bittwit.fec.parser.SiteParser;
import org.bittwit.fec.parser.WebDriverSiteParser;
import org.bittwit.fec.writer.ExcelWriter;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        LOG.info( "Starting the party!" );
        EntriesParser parser = new EntriesParser();
        List<SiteEntry> entries = parser.getEntries(getFile(Props.get("file.input")));
        
        int maxEntries = entries.size();
        try {
        	maxEntries = Integer.parseInt(Props.get("excel.parser.max.rows"));
        }
        catch (Exception e) {
        	LOG.debug("Max rows to parse not defined properly, ignoring", e);
        }
        if (maxEntries > 0 && maxEntries < entries.size()) {
        	LOG.info("Parsing just {} rows.", maxEntries);
        	entries = entries.subList(0, maxEntries);
        }

        boolean useWebdriver = Boolean.parseBoolean(Props.get("parser.use.webdriver"));
        SiteParser sp = null;
        if (useWebdriver) {
        	sp = new WebDriverSiteParser();
        }
        else {
        	sp = new SiteParser();
        }
        
        boolean isParallel = Boolean.parseBoolean((String) Props.get("parser.parallel"));
        List<ParserResult> results = null;
        if (isParallel) {
        	results = sp.parseSitesInParallel(entries);
        }
        else {
        	results = sp.parseSites(entries);
        }

        sp.close();

        ExcelWriter ew = new ExcelWriter(getFileToWrite(Props.get("file.output")));
        ew.updateExcelBySiteUrl(results);
    }

	private static File getFileToWrite(String fileName) {
		String userDirPath = System.getProperty("user.dir");
		userDirPath += "/" + fileName;
		LOG.info("Trying file {}", userDirPath);
		File f = new File(userDirPath);
		return f;
	}

	protected static File getFile(String fileName) {
		File f = null;
		
//		try from current dir
		if (f == null || !f.exists()) {
			String userDirPath = System.getProperty("user.dir");
			userDirPath += "/" + fileName;
			LOG.info("Trying file from filesystem {}", userDirPath);
			f = new File(userDirPath);
			if (!f.exists()) {
				f = null;
				LOG.error("FileNotFound {}", userDirPath);
			}
		}

		if (f == null) {
//			try from classpath
			try {
				URL fileURL = ClassLoader.getSystemResource(fileName);
				LOG.info("Using file from classpath {}", fileURL);
				if (fileURL != null) {
					f = Paths.get(fileURL.toURI()).toFile();
				}
			}
			catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

		return f;
	}
}
