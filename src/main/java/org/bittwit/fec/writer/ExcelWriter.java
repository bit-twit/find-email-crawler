package org.bittwit.fec.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bittwit.fec.domain.SiteEntry;
import org.bittwit.fec.parser.ParserResult;

public class ExcelWriter {
	private static final Logger LOG = LoggerFactory.getLogger(ExcelWriter.class);

	private File outputFile;

	public ExcelWriter(File fileToWrite) {
		this.outputFile = fileToWrite;
	}

	public void updateExcelBySiteUrl (List<ParserResult> results) {
		XSSFWorkbook workbook;
		FileInputStream fis;
		try {
			fis = new FileInputStream(this.outputFile);
			workbook = new XSSFWorkbook (fis);
		}
		catch (IOException e1) {
			LOG.error(e1.getMessage(), e1);
			return;
		}
		
		XSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.rowIterator();
		
		int totalRows = 0;
		int rowsWithNoResults = 0;
		int rowsWithEmails = 0;
		
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			
			Cell nameCell = row.getCell(SiteEntry.NAME_COLUMN);
//			check if we got through all the rows in the results
			if (nameCell == null || nameCell.getStringCellValue() == null || nameCell.getStringCellValue().trim().length() == 0) {
				break;
			}
			
			totalRows++;
			
			Cell siteCell = row.getCell(SiteEntry.URL_COLUMN);
			if (siteCell != null && siteCell.getStringCellValue() != null) {
				Cell resultsCell = row.getCell(SiteEntry.RESULTS_COLUMN);
				if (resultsCell == null) {
					resultsCell = row.createCell(SiteEntry.RESULTS_COLUMN);
				}

				for (ParserResult pr : results) {
					if (siteCell.getStringCellValue().equals(pr.getUrl())) {
						if (!pr.isError()) {
							if (pr.getResults().size() > 0) {
								resultsCell.setCellValue(pr.getResultsString());
							}
						}
						else {
							Cell errorsCell = row.getCell(SiteEntry.ERROR_COLUMN);
							if (errorsCell == null) {
								errorsCell = row.createCell(SiteEntry.ERROR_COLUMN);
							}
							errorsCell.setCellValue(pr.getErrorMessage());
						}
					}
				}

				if (resultsCell.getStringCellValue().trim().length() > 0) {
					rowsWithEmails++;
				}
				
//				check results and error cells
				Cell errorsCell = row.getCell(SiteEntry.ERROR_COLUMN);
				
				if ((resultsCell.getStringCellValue().trim().length() == 0)
						&& (errorsCell == null || errorsCell.getStringCellValue().trim().length() == 0)) {
					rowsWithNoResults++;
				}
			}			
		}

		LOG.info("From {} rows, {} emails found, {} rows with errors, {} rows with no results.", new Object[] {totalRows, rowsWithEmails, totalRows - rowsWithNoResults - rowsWithEmails, rowsWithNoResults});

		try {
			fis.close();
			FileOutputStream fos = new FileOutputStream(this.outputFile);
		    workbook.write(fos);
		    workbook.close();
		    fos.close();
		}
		catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
