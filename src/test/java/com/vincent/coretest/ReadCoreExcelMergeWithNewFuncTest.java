package com.vincent.coretest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.util.DomainTypeUtil;
import com.vincent.coretest.vo.MVPScopeVO;
import com.vincent.coretest.yaml.vo.RESTfulKey;

public class ReadCoreExcelMergeWithNewFuncTest {
	protected final Logger logger = LoggerFactory.getLogger(ReadCoreExcelMergeWithNewFuncTest.class);

	private String coreCategoryXlsxFromPYFile = ".\\src\\test\\input\\ref\\api_catalog_LC.xlsx";

	Map<RESTfulKey, List<MVPScopeVO>> coreApiNameToApiDataMapFromExcel = null;

	@Test
	public void testBuildWholeYaml() throws FileNotFoundException, IOException {
		logger.info("testBuildWholeYaml");

		coreApiNameToApiDataMapFromExcel = readCoreApiMap();
		showMap(coreApiNameToApiDataMapFromExcel);

	}

	private Map<RESTfulKey, List<MVPScopeVO>> readCoreApiMap() {
		ExcelReader.setDataTabPrefix("api details"); // b4, api details
		HashMap<RESTfulKey, List<MVPScopeVO>> map = new HashMap<RESTfulKey, List<MVPScopeVO>>();

		Map<String, Integer> headerMap = ExcelReader.getHeaderIndex(coreCategoryXlsxFromPYFile);
		List<Map<Integer, Object>> rowMapList = ExcelReader.getActiveRow(coreCategoryXlsxFromPYFile, false);

		logger.info("rowCount {}", rowMapList.size());
		for (Map<Integer, Object> rowData : rowMapList) {
			MVPScopeVO vo = null;
			try {
				vo = new MVPScopeVO(headerMap, rowData);
			} catch (Exception e) {
				continue;
			}

			logger.debug("{}", vo);

			String domainDesc = DomainTypeUtil.getDescriptionByDomainValue(vo.getBusinessName(), vo.getDomainValue());
			if (StringUtils.isNotBlank(domainDesc)) {
				// logger.info("changing desc {}", desc);
				vo.setDescription(domainDesc);
			}
			if (StringUtils.isNotBlank(vo.getDescription())) {
				if (domainDesc != null) {
					int index = domainDesc.indexOf(DomainTypeUtil.ALLOWED_VALUES);
					if (index >= 0) {
						vo.setDataType("number");
					}
				}
			}

			HttpMethod method = HttpMethod.resolve(vo.getHttpMethod());
			if (method != null) {
				RESTfulKey key = new RESTfulKey(vo.getPath(), method);
				if (!map.containsKey(key)) {
					map.put(key, new ArrayList<MVPScopeVO>());
				}
				map.get(key).add(vo);
			}
		}
		return map;
	}

	private void showMap(Map<RESTfulKey, List<MVPScopeVO>> coreMap) {
		if (coreMap != null) {
			logger.info("size {}", coreMap.size());
			Set<RESTfulKey> keySet = coreMap.keySet();
			for (RESTfulKey key : keySet) {
				logger.info("showing key {}", key);
				List<MVPScopeVO> valueList = coreMap.get(key);
				for (MVPScopeVO vo : valueList) {
					logger.info("=====> {}", vo);
				}
			}
		}
	}

}
