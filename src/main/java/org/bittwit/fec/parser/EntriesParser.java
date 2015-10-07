package org.bittwit.fec.parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.domain.SiteEntry;

public class EntriesParser {
	private static final Logger LOG = LoggerFactory.getLogger(EntriesParser.class);

	public List<SiteEntry> getEntries (File f) {
		List<SiteEntry> entries = new ArrayList<SiteEntry> ();
		
		try {
            FileInputStream file = new FileInputStream(f);
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
//                if we have a site name and a url , so we can match in results file
                if (row != null && row.getCell(SiteEntry.NAME_COLUMN) != null && row.getCell(SiteEntry.URL_COLUMN) != null
//                		if we haven't already tried something like write results or errors in that row 
//                		from a previous attempt
                		&& ((row.getCell(SiteEntry.RESULTS_COLUMN) == null || row.getCell(SiteEntry.RESULTS_COLUMN).getStringCellValue().trim().length() == 0)
                				&& (row.getCell(SiteEntry.ERROR_COLUMN) == null || row.getCell(SiteEntry.ERROR_COLUMN).getStringCellValue().trim().length() == 0))) {
	                String name = row.getCell(SiteEntry.NAME_COLUMN).getStringCellValue();
	                String url = row.getCell(SiteEntry.URL_COLUMN).getStringCellValue();
	                SiteEntry entry = new SiteEntry(name, url);
	                entries.add(entry);
                }
            }
            workbook.close();
            file.close();
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
		LOG.info("Found {} entries to process.", entries.size());
		return entries;
	}
}
