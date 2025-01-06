package com.vincent.coretest;

import java.io.File;
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

import com.vincent.coretest.enumeration.FuncGenEnum;
import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.util.DomainTypeUtil;
import com.vincent.coretest.vo.MVPScopeVO;
import com.vincent.coretest.yaml.vo.RESTfulKey;

public class ReadCoreExcelMergeWithNewFuncTest extends AbstractExcelReadBuilder {
	protected final Logger logger = LoggerFactory.getLogger(ReadCoreExcelMergeWithNewFuncTest.class);

	private String coreCategoryXlsxFromPYFile = ".\\src\\test\\input\\ref\\api_catalog_LC.xlsx";

	Map<RESTfulKey, List<MVPScopeVO>> coreApiNameToApiDataMapFromExcel = null;

	@Test
	public void testBuildWholeYaml() throws FileNotFoundException, IOException {
		logger.info("testBuildWholeYaml");

		coreApiNameToApiDataMapFromExcel = readCoreApiMap();
		showMap(coreApiNameToApiDataMapFromExcel);

		// old code for handling onlly v2
		ExcelReader.setDataTabPrefix("b4"); // b4, api details

		String root = "";
		File rootDir = new File(root);
		logger.info("root1 {}", rootDir.getAbsolutePath());

		String targetPath = rootDir.getAbsolutePath() + "\\src\\test\\input\\group\\";
		File targetFolderFile = new File(targetPath);
		logger.info("targetFolderFile {} existed {} dir {}", targetFolderFile.getAbsolutePath(), targetFolderFile.exists(), targetFolderFile.isDirectory());
		if (targetFolderFile == null || !targetFolderFile.isDirectory()) {
			return;
		}

		FuncGenEnum genEnum = FuncGenEnum.All; // NEW EXISTED

		outputFileName = "20250105_WHOLE_LC_" + genEnum.name() + ".yaml";

		logger.info("working on {}", genEnum);

		String target = (genEnum != FuncGenEnum.All) ? genEnum.getPrefix() : null;
		String ignoreTarget = (genEnum != FuncGenEnum.All) ? genEnum.getIgnorePrefix() : null;

		File[] dirFiles = targetFolderFile.listFiles();

		for (File xlsxFile : dirFiles) {
			if (!xlsxFile.getAbsolutePath().toLowerCase().endsWith("xlsx")) { // not xlsx => ignore
				continue;
			}

			logger.info("handling {}", xlsxFile);
			Map<String, Integer> headerMap = ExcelReader.getHeaderIndex(xlsxFile.getAbsolutePath());
			logger.info("header {}", headerMap);
			List<Map<Integer, Object>> rowMapList = ExcelReader.getActiveRow(xlsxFile.getAbsolutePath(), false);
			logger.info("size {}", rowMapList.size());

			int activeCnt = 0;
			for (Map<Integer, Object> rowData : rowMapList) {
				MVPScopeVO vo = null;
				try {
					vo = new MVPScopeVO(genEnum, headerMap, rowData);
				} catch (Exception e) {
					logger.info("cnt wrong {} {} {}", e.toString(), headerMap, rowData);
					continue;
				}

				if (target == null || (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(target))) {
					logger.debug("{}", vo);
					String apiName = vo.getApiName();
					if (!apiNameToApiDataMapFromExcel.containsKey(apiName)) {
						apiNameToApiDataMapFromExcel.put(apiName, new ArrayList<MVPScopeVO>());
					}
					apiNameToApiDataMapFromExcel.get(apiName).add(vo);
					activeCnt++;
				} else if (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(ignoreTarget)) {
					logger.debug("ignored {}", vo);
				} else {
					logger.debug("error " + vo);
				}

				String desc = DomainTypeUtil.getDescriptionByDomainValue(vo.getBusinessName(), vo.getDomainValue());
				if (StringUtils.isNotBlank(desc)) {
					// logger.info("changing desc {}", desc);
					vo.setDescription(desc);
				}
			}
			logger.info("=====>{} active line {}", xlsxFile, activeCnt);
		}

		apiNameToApiDataMapFromExcel.forEach((key, list) -> {
			if (list != null && list.size() > 0) {
				MVPScopeVO vo = list.get(0);
				String parentPath = v2GetParentPath(vo.getPath());
				logger.debug("key {} path {} root {} ", key, vo.getPath(), parentPath);
				HttpMethod method = HttpMethod.valueOf(vo.getHttpMethod());

				RESTfulKey aRESTfulKey = new RESTfulKey(parentPath, method);
				logger.debug("  {} RESTfulKey {} existed {}", method, aRESTfulKey, coreApiNameToApiDataMapFromExcel.containsKey(aRESTfulKey));

				if (coreApiNameToApiDataMapFromExcel.containsKey(aRESTfulKey)) {
					logger.info("found old ===> {} value {}", key, list);
					List<MVPScopeVO> v1List = coreApiNameToApiDataMapFromExcel.get(aRESTfulKey);
					for (MVPScopeVO v1MVPScopeVO : v1List) {
						try {
							MVPScopeVO clone = v1MVPScopeVO.clone();
							clone.setPath(vo.getPath()); // 給新的 V2 PATH
							clone.setOriginalPathWithQuery(vo.getOriginalPathWithQuery());
							clone.setReqPath(vo.getReqPath());
							list.add(0, clone);
							logger.info("found old ===> {} adding {}", key, clone);
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					logger.info("found old ===> {} result {}", key, v1List);
				} else { // check if it is new
					if (vo.getApiType().toLowerCase().startsWith("e")) { // 找不到V1 但是是EXISTED
						logger.warn("v1 not found BUT IT IS existed ==> {} ", vo);
					}

				}
			}
		});

		coreApiNameToApiDataMapFromExcel.forEach((key, list) -> {
			if (list != null && list.size() > 0) {
				MVPScopeVO vo = list.get(0);
				String apiName = "CORE  " + vo.getApiName();
				vo.setApiName(apiName);

				apiNameToApiDataMapFromExcel.put(apiName, list);
			}
		});

		logger.info("START handleData");
		doJob();
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
				logger.debug("showing key {}", key);
				List<MVPScopeVO> valueList = coreMap.get(key);
				for (MVPScopeVO vo : valueList) {
					logger.debug("=====> {}", vo);
				}
			}
		}
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

}
