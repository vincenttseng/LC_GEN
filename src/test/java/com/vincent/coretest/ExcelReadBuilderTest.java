package com.vincent.coretest;

import java.util.List;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.ExcelReader;

public class ExcelReadBuilderTest {
	protected final Logger logger = LoggerFactory.getLogger(ExcelReadBuilderTest.class);

	String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001.xlsx";

	@Test
	public void buildYamlFromExcelForNew() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");
		List<List<Object>> rows = ExcelReader.getActiveRow(xlsxFile, "B4-001", false);
		
		int index = 1;
		for (List<Object> rowData : rows) {
			StringBuilder sb = new StringBuilder();
			boolean found = false;
			for (Object obj : rowData) {
				if (found) {
					sb.append(",");
				}
				sb.append(obj.toString());
				found = true;
			}
			logger.info("{} => {}", index, sb.toString());
			index++;
		}

	}

}
