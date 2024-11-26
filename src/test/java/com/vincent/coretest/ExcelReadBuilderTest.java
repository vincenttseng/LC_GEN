package com.vincent.coretest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.vo.MVPScopeVO;

public class ExcelReadBuilderTest {
	protected final Logger logger = LoggerFactory.getLogger(ExcelReadBuilderTest.class);

	String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001.xlsx";

	@Test
	public void buildYamlFromExcelForNew() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");
		List<List<Object>> rows = ExcelReader.getActiveRow(xlsxFile, "B4-001", false);

		Map<String, List<List<Object>>> apiNameToApiDataMap = new HashMap<String, List<List<Object>>>();

		int index = 1;
		for (List<Object> rowData : rows) {
			MVPScopeVO vo = new MVPScopeVO(rowData);
			// logger.info("vo {} ", vo);
			if ("new".equalsIgnoreCase(vo.getApiType())) {
				// logger.info("new =>{} => vo {}", vo.getApiName(), vo);
				String apiName = vo.getApiName();
				if (!apiNameToApiDataMap.containsKey(apiName)) {
					apiNameToApiDataMap.put(apiName, new ArrayList<List<Object>>());
				}
				apiNameToApiDataMap.get(apiName).add(rowData);
			} else if ("Existing".equalsIgnoreCase(vo.getApiType())) {

			} else {
				logger.info("error");
				for (Object obj : rowData) {
					logger.info("ignore>{}", vo);
				}
			}
		}

		Set<String> keySet = apiNameToApiDataMap.keySet();
		for (String key : keySet) {
			logger.info("workinig on ===================" + key);
			List<List<Object>> params = apiNameToApiDataMap.get(key);

			for (List<Object> param : params) {
				logger.info(" =====> {}", param);
			}
			logger.info("=================================================");
		}

		showDefOfApi(apiNameToApiDataMap);
		showDefOfReference(apiNameToApiDataMap);
	}
	
	public static void showDefOfApi(Map<String, List<List<Object>>> apiNameToApiDataMap) {
		
	}
	
	public static void showDefOfReference(Map<String, List<List<Object>>> apiNameToApiDataMap) {
		
	}

}
