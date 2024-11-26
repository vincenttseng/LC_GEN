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
import com.vincent.coretest.reader.HeaderUtil;
import com.vincent.coretest.vo.MVPScopeVO;

public class ExcelReadBuilderTest {
	protected final Logger logger = LoggerFactory.getLogger(ExcelReadBuilderTest.class);

	String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001.xlsx";

	@Test
	public void buildYamlFromExcelForNew() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");
		List<List<Object>> rows = ExcelReader.getActiveRow(xlsxFile, "B4-001", false);

		Map<String, List<MVPScopeVO>> apiNameToApiDataMap = new HashMap<String, List<MVPScopeVO>>();

		int index = 1;
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

		Set<String> keySet = apiNameToApiDataMap.keySet();
		for (String key : keySet) {
			logger.info("workinig on ===================" + key);
			List<MVPScopeVO> params = apiNameToApiDataMap.get(key);

			for (MVPScopeVO vo : params) {
				// logger.info(" =====> {}", vo);
			}
			logger.info("=================================================");
		}

		checkData(apiNameToApiDataMap);

		printStartOfOutput();
		HeaderUtil.printHeader();
		showDefOfApi(apiNameToApiDataMap);
		showDefOfReference(apiNameToApiDataMap);
	}

	public static void printStartOfOutput() {
		System.out.println(
				"============================================= start of YAML =============================================");
	}

	public static void showDefOfApi(Map<String, List<MVPScopeVO>> apiNameToApiDataMap) {

	}

	public static void showDefOfReference(Map<String, List<MVPScopeVO>> apiNameToApiDataMap) {

	}

	public void checkData(Map<String, List<MVPScopeVO>> apiNameToApiDataMap) {
		boolean error = false;

		// check url same for same group
		Set<String> keySet = apiNameToApiDataMap.keySet();
		for (String key : keySet) {
			List<MVPScopeVO> params = apiNameToApiDataMap.get(key);

			boolean found = false;
			String targetPath = null;
			// should have same url
			for (MVPScopeVO vo : params) {
				if (!found) {
					targetPath = vo.getPath();
					found = true;
				} else {
					String path = vo.getPath();
					if (!path.equals(targetPath)) {
						error = true;
						logger.error("path diff " + vo + targetPath + " vs " + path);
					}
				}
			}
		}
		if (error) {
			System.exit(0);
		}

		logger.info("checkData pass");
	}
}
