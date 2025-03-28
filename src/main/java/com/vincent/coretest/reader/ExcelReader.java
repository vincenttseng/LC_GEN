package com.vincent.coretest.reader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReader {
	protected static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

	private static String DEFAULT_TAB_PREFIX = "b4";

	public static List<Map<Integer, Object>> getActiveRow(String xlsxFile, boolean includeHeader) {
		return getActiveRow(DEFAULT_TAB_PREFIX, xlsxFile, includeHeader);
	}

	public static List<Map<Integer, Object>> getActiveRow(String tabPrefix, String xlsxFile, boolean includeHeader) {
		List<Map<Integer, Object>> rowMapList = new ArrayList<Map<Integer, Object>>();

		InputStream excelFileToRead = null;
		XSSFWorkbook wb = null;
		try {
			excelFileToRead = new FileInputStream(xlsxFile);
			wb = new XSSFWorkbook(excelFileToRead);

			XSSFSheet sheet = getTargetSheet(tabPrefix, xlsxFile, wb);

			if(sheet == null) {
				sheet = getTargetSheet(DEFAULT_TAB_PREFIX, xlsxFile, wb);
			}
			logger.info("reading row for sheet " + sheet.getSheetName());
			
			if (sheet == null) {
				logger.info("no sheet found {} ", xlsxFile);
				return rowMapList;
			}

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
						rowDataMap.put(columnIndex, cell.getStringCellValue());
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

	public static Map<String, Integer> getHeaderIndex(String xlsxFile) {
		return getHeaderIndex(DEFAULT_TAB_PREFIX, xlsxFile);
	}

	public static Map<String, Integer> getHeaderIndex(String tabPrefix, String xlsxFile) {
		Map<String, Integer> result = new HashMap<String, Integer>();

		InputStream excelFileToRead = null;
		XSSFWorkbook wb = null;
		try {
			excelFileToRead = new FileInputStream(xlsxFile);
			wb = new XSSFWorkbook(excelFileToRead);

			XSSFSheet sheet = getTargetSheet(tabPrefix, xlsxFile, wb);
			if(sheet == null) {
				sheet = getTargetSheet(DEFAULT_TAB_PREFIX, xlsxFile, wb);
			}
			logger.info("reading getHeaderIndex for sheet " + sheet.getSheetName());

			if (sheet == null) {
				logger.info("no sheet found {}", xlsxFile);
				return result;
			}

			int totalRows = sheet.getPhysicalNumberOfRows();
			logger.debug("row count {}", totalRows);

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
			e.printStackTrace();
		} finally {
			try {
				excelFileToRead.close();
			} catch (Exception e) {
			}
		}

		return result;
	}

	private static XSSFSheet getTargetSheet(String tabPrefix, String fileName, XSSFWorkbook wb) {
		String prefix = tabPrefix != null ? tabPrefix.toLowerCase() : DEFAULT_TAB_PREFIX;
		XSSFSheet sheet = null;
		try {
			int sheetCnt = wb.getNumberOfSheets();
			String sheetName = "";
			for (int i = 0; i < sheetCnt; i++) {
				XSSFSheet tmp = wb.getSheetAt(i);
				String name = tmp.getSheetName();
				String comparee = StringUtils.trim(StringUtils.lowerCase(name));
				if (comparee != null && comparee.startsWith(prefix)) {
					sheetName = name;
				}
			}
			sheet = wb.getSheet(sheetName);
			if (sheet == null) {
				throw new Exception("no sheet in name " + sheetName);
			}
			return sheet;
		} catch (Exception e) {
			logger.info("no sheet for " + tabPrefix + " " + fileName);

		}
		return null;
	}
}
