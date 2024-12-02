package com.vincent.coretest.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.util.ReqRespParamVOUtil;

public class ReqRespParamVOTest {
	protected final Logger logger = LoggerFactory.getLogger(ReqRespParamVOTest.class);

	String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001.xlsx";

	// group map for all attributes of same function
	Map<String, List<MVPScopeVO>> apiNameToApiDataMap = new HashMap<String, List<MVPScopeVO>>();

	@Test
	public void testListing() {
		logger.info("buildYamlFromExcelForNew");
		List<List<Object>> rows = ExcelReader.getActiveRow(xlsxFile, "B4-001", false);

		for (List<Object> rowData : rows) {
			MVPScopeVO vo = new MVPScopeVO(rowData);

			if ("new".equalsIgnoreCase(vo.getApiType())) {
				// logger.info("new =>{} => vo {}", vo.getApiName(), vo);
				String apiName = vo.getApiName();
				if (!apiNameToApiDataMap.containsKey(apiName)) {
					apiNameToApiDataMap.put(apiName, new ArrayList<MVPScopeVO>());
				}
				apiNameToApiDataMap.get(apiName).add(vo);
			} else if ("Existing".equalsIgnoreCase(vo.getApiType())) {

			} else {
				logger.info("error");
				for (Object obj : rowData) {
					// logger.info("ignore>{}", vo);
				}
			}
		}
		
		logger.info("====================groupingReqRespObject==========================");
		Set<String> keySet = apiNameToApiDataMap.keySet();
		logger.info("keys {}", keySet);
		keySet.stream().forEach(key -> {
			logger.info("groupingReqRespObject key: {}", key);
			List<MVPScopeVO> attributes = apiNameToApiDataMap.get(key);
			ReqRespParamVO vo = ReqRespParamVOUtil.getReqRespParamVO(key, attributes);
			
			logger.info("api " + key);
			vo.showContent();
		});
	}
}
