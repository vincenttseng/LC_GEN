package com.vincent.coretest.vo;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.ExcelReader;

public class MVPScopeVOTest {
	protected final Logger logger = LoggerFactory.getLogger(MVPScopeVOTest.class);

	String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001.xlsx";

	@Test
	public void testListing() {
		logger.info("buildYamlFromExcelForNew");
		List<List<Object>> rows = ExcelReader.getActiveRow(xlsxFile, "B4-001", false);

		for (List<Object> rowData : rows) {
			MVPScopeVO vo = new MVPScopeVO(rowData);
			logger.info("vo required {} source {} {}", vo.isRequired(), rowData.get(8), rowData.get(9));
		}
	}

}
