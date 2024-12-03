package com.vincent.coretest.reader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReader {
	protected static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

	public static List<Map<Integer, Object>> getActiveRow(String xlsxFile, String sheetName, boolean includeHeader) {
		List<Map<Integer, Object>> rowMapList = new ArrayList<Map<Integer, Object>>();

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
				Row row = rowIterator.next();
				if (rowIndex == 1 && !includeHeader) {
					rowIndex++;
					continue;
				}

				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				Map<Integer, Object> rowDataMap = new HashMap<Integer, Object>();

				boolean withData = false;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (!rowDataMap.containsKey(-1)) {
						rowDataMap.put(-1, cell.getRowIndex());
					}
					int columnIndex = cell.getColumnIndex();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						rowDataMap.put(columnIndex, Double.valueOf(cell.getNumericCellValue()));
						withData = true;
						break;
					case Cell.CELL_TYPE_STRING:
						rowDataMap.put(columnIndex, cell.getStringCellValue());
						withData = true;
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						withData = true;
						rowDataMap.put(columnIndex, Boolean.valueOf(cell.getBooleanCellValue()));
						break;
					default:
						rowDataMap.put(columnIndex, "");
						break;
					}
				}
				if (withData) {
					rowMapList.add(rowDataMap);
				} else {
					logger.debug("rowIndex {} is void", rowIndex);
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

		return rowMapList;
	}

	public static Map<String, Integer> getHeaderIndex(String xlsxFile, String sheetName) {
		Map<String, Integer> result = new HashMap<String, Integer>();

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
			if (rowIterator.hasNext()) {
				logger.debug("row {}", rowIndex);
				Row row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						break;
					case Cell.CELL_TYPE_STRING:
						result.put(cell.getStringCellValue(), columnIndex);
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						break;
					default:
						break;
					}
				}

			}

		} catch (Exception e) {

		} finally {
			try {
				excelFileToRead.close();
			} catch (Exception e) {
			}
		}

		return result;
	}
}
