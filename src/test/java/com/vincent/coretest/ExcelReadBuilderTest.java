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

import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.reader.HeaderUtil;
import com.vincent.coretest.reader.PathUtil;
import com.vincent.coretest.util.ReqRespParamVOUtil;
import com.vincent.coretest.util.SchemaBodyUtil;
import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.MVPScopeVO;
import com.vincent.coretest.vo.ReqRespParamVO;

public class ExcelReadBuilderTest {
	protected final Logger logger = LoggerFactory.getLogger(ExcelReadBuilderTest.class);

	String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001.xlsx";

	// group map for all attributes of same function
	Map<String, List<MVPScopeVO>> apiNameToApiDataMap = new HashMap<String, List<MVPScopeVO>>();

	// group map for same path different httpMethod
	Map<String, List<String>> mapForSamePath = new HashMap<String, List<String>>();

	Map<String, ReqRespParamVO> reqRespParamVOMap = new HashMap<String, ReqRespParamVO>();

	@Test
	public void buildYamlFromExcelForNew() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");
		List<List<Object>> rows = ExcelReader.getActiveRow(xlsxFile, "B4-001", false);

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

		checkData();
		groupingSameReqPath();
		groupingReqRespObject();

		// STARTING YAML OUTPUT
		printStartOfOutput();
		HeaderUtil.printHeader();
		showDefOfApi();
		showDefOfReference();
	}

	public static void printStartOfOutput() {
		System.out.println(
				"============================================= start of YAML =============================================");
	}

	public void showDefOfApi() {
		System.out.println("paths:");
		Set<String> keySet = mapForSamePath.keySet();
		keySet.stream().forEach(reqPath -> {
			showDefApiByKey(reqPath);
		});
	}

	public void showDefOfReference() {

	}

	public void checkData() {
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

	public void groupingSameReqPath() {
		Set<String> keySet = apiNameToApiDataMap.keySet();
		for (String key : keySet) {
			List<MVPScopeVO> params = apiNameToApiDataMap.get(key);

			if (params.size() > 0) {
				MVPScopeVO vo = params.get(0);
				String reqPath = vo.getReqPath();
				String httpMethod = vo.getHttpMethod();
				logger.info(key + " " + httpMethod + " " + reqPath);

				if (!mapForSamePath.containsKey(reqPath)) {
					mapForSamePath.put(reqPath, new ArrayList<String>());
				}
				mapForSamePath.get(reqPath).add(key);
			}
		}
		mapForSamePath.keySet().forEach(key -> {
			logger.info("for ===== " + key);
			List<String> values = mapForSamePath.get(key);
			values.forEach(value -> {
				logger.info(key + " " + value);
			});
		});
	}

	public void groupingReqRespObject() {
		logger.info("====================groupingReqRespObject==========================");
		Set<String> keySet = apiNameToApiDataMap.keySet();
		keySet.stream().forEach(key -> {
			logger.info("groupingReqRespObject key {}", key);
			List<MVPScopeVO> attributes = apiNameToApiDataMap.get(key);
			ReqRespParamVO vo = ReqRespParamVOUtil.getReqRespParamVO(key, attributes);

			logger.info("api " + key);
			vo.showContent();
		});
	}

	public void showDefApiByKey(String reqPath) {
		System.out.println("  '" + reqPath + "':");

		List<String> keySet = mapForSamePath.get(reqPath);
		for (String key : keySet) {
			System.out.println("######## " + key);
			List<MVPScopeVO> params = apiNameToApiDataMap.get(key);
			if (params.size() > 0) {
				MVPScopeVO vo = params.get(0);
				String method = vo.getHttpMethod().toLowerCase();
				System.out.println("    " + method + ":");
				System.out.println("      tags:");
				System.out.println("        - " + vo.getApiNode());
				System.out.println("      summary: " + vo.getApiName());
				System.out.println("      description: " + vo.getApiName().toUpperCase());
				System.out.println("      operationId: " + vo.getApiName().toUpperCase());
				System.out.println("      parameters:");
				System.out.print(HeaderUtil.getMethodHeadersString());
				System.out.println(PathUtil.getPathParamString(vo.getReqPath()));
			}

			String refKey = TextUtil.nameToLowerCaseAndDash(key + " " + GenTypeEnum.REQUEST.getMessage());
			List<MVPScopeVO> attributes = apiNameToApiDataMap.get(key);
			ReqRespParamVO vo = ReqRespParamVOUtil.getReqRespParamVO(key, attributes);
			showRequestRefDeclaration(refKey, vo);
			showResponseRefDeclaration(refKey, vo);
		}

	}

	private void showRequestRefDeclaration(String refKey, ReqRespParamVO vo) {
		if (vo.getMapOfInputObjectArrayList().size() > 0 || vo.getMapOfInputObjectList().size() > 0) {
			System.out.println(SchemaBodyUtil.genRequestSchemaText(refKey));
		}
	}

	private void showResponseRefDeclaration(String refKey, ReqRespParamVO vo) {
		System.out.println(SchemaBodyUtil.genResponseSchemaText(refKey));
	}
}
