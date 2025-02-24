package com.vincent.coretest.vo;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.ExcelReader;

public class MVPScopeVOTest {
	protected final Logger logger = LoggerFactory.getLogger(MVPScopeVOTest.class);

	String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001.xlsx";

	@Test
	public void testListing() throws Exception {
		logger.info("testListing");
		Map<String, Integer> headerMap = ExcelReader.getHeaderIndex(xlsxFile);
		List<Map<Integer, Object>> rows = ExcelReader.getActiveRow(xlsxFile, false);

		for (Map<Integer, Object> rowData : rows) {
			MVPScopeVO vo = new MVPScopeVO(headerMap, rowData);
			logger.info("vo required {} source {} {} {} {}", vo.isRequired(), vo.getPath(), vo.getHttpMethod(), vo.getReqPath(), vo.getDirection());
			logger.info("{}", rowData);
		}
	}

	@Test
	public void testHeader() {
		logger.info("testHeader");
		Map<String, Integer> map = ExcelReader.getHeaderIndex(xlsxFile);

		map.keySet().stream().forEach(key -> {
			logger.info("key {} => {}", key, map.get(key));
		});
	}

	@Test
	public void testGroupName() {
		String value = "elc-bill-swift(new)";
		value = MVPScopeVO.correctGroupName(value);
		logger.info("value {}", value);
	}

	@Test
	public void testApiNode() {
		String apiNode = "forfeting-inititation";
		logger.info("{} value {}", apiNode, MVPScopeVO.formatVariableLowerAndDash(apiNode));

		apiNode = "Forfeting-Inititation";
		logger.info("{} value {}", apiNode, MVPScopeVO.formatVariableLowerAndDash(apiNode));

		apiNode = "Forfeting-Inititation-";
		logger.info("{} value {}", apiNode, MVPScopeVO.formatVariableLowerAndDash(apiNode));

		apiNode = "AbadA--titation";
		logger.info("{} value {}", apiNode, MVPScopeVO.formatVariableLowerAndDash(apiNode));
	}
}
