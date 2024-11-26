package com.vincent.coretest.reader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReader {
	protected static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

	@SuppressWarnings("removal")
	public static List<List<Object>> getActiveRow(String xlsxFile, String sheetName, boolean includeHeader) {
		List<List<Object>> rowList = new ArrayList<List<Object>>();

		InputStream excelFileToRead = null;
		XSSFWorkbook wb = null;
		try {
			excelFileToRead = new FileInputStream(xlsxFile);
			wb = new XSSFWorkbook(excelFileToRead);

			XSSFSheet sheet = wb.getSheet(sheetName);
			int totalRows = sheet.getPhysicalNumberOfRows();
			logger.info("row count {}", totalRows);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			int rowIndex = 1;
			while (rowIterator.hasNext()) {
				logger.debug("row {}", rowIndex);
				if (rowIndex == 1 && !includeHeader) {
					rowIndex++;
					continue;
				}
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				List<Object> rowData = new ArrayList<Object>();

				boolean withData = false;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						rowData.add(Double.valueOf(cell.getNumericCellValue()));
						withData = true;
						break;
					case Cell.CELL_TYPE_STRING:
						rowData.add(cell.getStringCellValue());
						withData = true;
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						withData = true;
						rowData.add(Boolean.valueOf(cell.getBooleanCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
					default:
						rowData.add("");
						break;
					}
				}
				if (withData) {
					rowList.add(rowData);
				}
				rowIndex++;
			}

		} catch (Exception e) {

		} finally {
			try {
				excelFileToRead.close();
			} catch (Exception e) {
			}
		}

		return rowList;

	}
}
