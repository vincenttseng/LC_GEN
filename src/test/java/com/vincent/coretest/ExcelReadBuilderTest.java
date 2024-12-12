package com.vincent.coretest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.FuncGenEnum;
import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.reader.HeaderUtil;
import com.vincent.coretest.reader.PathUtil;
import com.vincent.coretest.util.DomainTypeUtil;
import com.vincent.coretest.util.ReqRespParamVOUtil;
import com.vincent.coretest.util.SchemaBodyUtil;
import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ColumnDefVo;
import com.vincent.coretest.vo.MVPScopeVO;
import com.vincent.coretest.vo.ReqRespParamVO;

public class ExcelReadBuilderTest {
	protected final Logger logger = LoggerFactory.getLogger(ExcelReadBuilderTest.class);

	// group map for same path different httpMethod
	Map<String, List<String>> mapForSameURLPath = new HashMap<String, List<String>>();

	// group map for all attributes of same function
	Map<String, List<MVPScopeVO>> apiNameToApiDataMapFromExcel = new HashMap<String, List<MVPScopeVO>>();

	Map<String, ReqRespParamVO> reqRespParamVOMap = new HashMap<String, ReqRespParamVO>();

	@Test
	public void buildYamlFromFolder() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");

		String root = "";
		File rootDir = new File(root);
		logger.info("root1 {}", rootDir.getAbsolutePath());

		String targetPath = rootDir.getAbsolutePath() + "\\src\\test\\input\\group\\";
		File targetFolderFile = new File(targetPath);
		logger.info("targetFolderFile {} existed {} dir {}", targetFolderFile.getAbsolutePath(), targetFolderFile.exists(), targetFolderFile.isDirectory());
		if (targetFolderFile == null || !targetFolderFile.isDirectory()) {
			return;
		}

		FuncGenEnum genEnum = FuncGenEnum.NEW; // NEW EXISTED

		String target = genEnum.getPrefix();
		String ignoreTarget = genEnum.getIgnorePrefix();

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
				MVPScopeVO vo = new MVPScopeVO(genEnum, headerMap, rowData);

				if (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(target)) {
					logger.info("{}", vo);
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
		logger.info("START handleData");
		handleData();
	}

	@Test
	public void buildYamlFromOneExcel() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");

		String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001 2.xlsx";
		Map<String, Integer> headerMap = ExcelReader.getHeaderIndex(xlsxFile);
		List<Map<Integer, Object>> rowMapList = ExcelReader.getActiveRow(xlsxFile, false);

		FuncGenEnum genEnum = FuncGenEnum.NEW; // NEW EXISTED
		String target = genEnum.getPrefix();
		String ignoreTarget = genEnum.getIgnorePrefix();

		for (Map<Integer, Object> rowData : rowMapList) {
			MVPScopeVO vo = new MVPScopeVO(headerMap, rowData);

			if (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(target)) {
				logger.info("{}", vo);
				String apiName = vo.getApiName();
				if (!apiNameToApiDataMapFromExcel.containsKey(apiName)) {
					apiNameToApiDataMapFromExcel.put(apiName, new ArrayList<MVPScopeVO>());
				}
				apiNameToApiDataMapFromExcel.get(apiName).add(vo);
			} else if (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(ignoreTarget)) {
				logger.debug("ignored {}", vo);
			} else {
				logger.info("error " + vo);
			}

			String desc = DomainTypeUtil.getDescriptionByDomainValue(vo.getBusinessName(), vo.getDomainValue());
			if (StringUtils.isNotBlank(desc)) {
				// logger.info("changing desc {}", desc);
				vo.setDescription(desc);
			}
			if (StringUtils.isNotBlank(vo.getDescription())) {
				int index = desc.indexOf(DomainTypeUtil.ALLOWED_VALUES);
				if (index >= 0) {
					vo.setDataType("number");
				}
			}
		}
		handleData();
	}

	private void handleData() {
		checkData();
		groupingSameReqPath();
		groupingReqRespObject();

		// STARTING YAML OUTPUT
		printStartOfOutput();
		HeaderUtil.printHeader();
		printDefOfApi();
		printBasicOutputComponent();
		printDefOfReference();
	}

	public static void printStartOfOutput() {
		System.out.println("============================================= start of YAML =============================================");
	}

	public void printDefOfApi() {
		System.out.println("paths:");
		Set<String> keySet = mapForSameURLPath.keySet();
		keySet.stream().forEach(reqPath -> {
			showDefApiByKey(reqPath);
		});
	}

	public void checkData() {
		boolean error = false;

		// check url same for same group
		Set<String> keySet = apiNameToApiDataMapFromExcel.keySet();
		for (String key : keySet) {
			List<MVPScopeVO> params = apiNameToApiDataMapFromExcel.get(key);

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
						logger.error("path diff " + targetPath + " vs " + path + " ref" + vo);
					}
				}
			}
		}
//		if (error) {
//			System.exit(0);
//		}

		logger.info("checkData pass");
	}

	public void groupingSameReqPath() {
		Set<String> keySet = apiNameToApiDataMapFromExcel.keySet();
		for (String key : keySet) {
			List<MVPScopeVO> params = apiNameToApiDataMapFromExcel.get(key);

			if (params.size() > 0) {
				MVPScopeVO vo = params.get(0);
				String reqPath = vo.getReqPath();
				String httpMethod = vo.getHttpMethod();
				logger.info(key + " " + httpMethod + " " + reqPath);

				if (!mapForSameURLPath.containsKey(reqPath)) {
					mapForSameURLPath.put(reqPath, new ArrayList<String>());
				}
				mapForSameURLPath.get(reqPath).add(key);
			}
		}

		logger.info("==============:showing different path");
		mapForSameURLPath.keySet().forEach(key -> {
			logger.info("path " + key);
			List<String> values = mapForSameURLPath.get(key);
			values.forEach(value -> {
				logger.info("pathmethod : " + key + " method:" + value);
			});
		});
		logger.info("==============:ending different path");
	}

	public void groupingReqRespObject() {
		logger.info("====================groupingReqRespObject==========================");
		Set<String> keySet = apiNameToApiDataMapFromExcel.keySet();
		keySet.stream().forEach(key -> {
			logger.info("groupingReqRespObject key : {}", key);
			List<MVPScopeVO> attributes = apiNameToApiDataMapFromExcel.get(key);
			ReqRespParamVO vo = ReqRespParamVOUtil.getReqRespParamVO(key, attributes);

			vo.showContent();
			reqRespParamVOMap.put(key, vo);
		});
	}

	public void showDefApiByKey(String reqPath) {
		System.out.println("  " + reqPath + ":");

		List<String> keySet = mapForSameURLPath.get(reqPath);
		for (String key : keySet) {
			System.out.println("######## " + key);
			List<MVPScopeVO> params = apiNameToApiDataMapFromExcel.get(key);
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
				System.out.println(PathUtil.getPathParamString(vo.getOriginalPathWithQuery()));
			}

			List<MVPScopeVO> attributes = apiNameToApiDataMapFromExcel.get(key);
			ReqRespParamVO vo = ReqRespParamVOUtil.getReqRespParamVO(key, attributes);
			showRequestRefDeclaration(key, vo);
			showResponseRefDeclaration(key, vo);
		}
	}

	private void showRequestRefDeclaration(String key, ReqRespParamVO vo) {
		String refKey = TextUtil.nameToLowerCaseAndDash(key + " " + GenTypeEnum.REQUEST.getMessage());
		if (vo.getMapOfInputObjectArrayList().size() > 0 || vo.getMapOfInputObjectList().size() > 0) {
			System.out.println(SchemaBodyUtil.genRequestSchemaText(refKey));
		}
	}

	private void showResponseRefDeclaration(String key, ReqRespParamVO vo) {
		String refKey = TextUtil.nameToLowerCaseAndDash(key + " " + GenTypeEnum.RESPONSE.getMessage());
		boolean withRespObj = false;
		if (vo.getMapOfRespObjectArrayList().size() > 0 || vo.getMapOfRespObjectList().size() > 0) {
			withRespObj = true;
		}
		System.out.println(SchemaBodyUtil.genResponseSchemaText(refKey, withRespObj));
	}

	/**
	 * @formatter:off 
components:
  schemas:
    api-message-error:
      type: object
      properties:
        severity-code:
          type: integer
          format: int32
        description:
          type: string
        id:
          type: integer
          format: int64
    api-messages:
      type: object
      properties:
        max-severity-code:
          type: integer
          format: int32
        max-severity-desc:
          type: string
        message-list:
          type: array
          items:
            $ref: '#/components/schemas/api-message-error'
	 * @formatter:on
	 */
	private void printBasicOutputComponent() {
		System.out.println("######## components");
		System.out.println("components:");
		System.out.println("  schemas:");
		// api-message-error
		System.out.println("    api-message-error:");
		System.out.println("      type: object");
		System.out.println("      properties:");
		System.out.println("        severity-code:");
		System.out.println("          type: integer");
		System.out.println("          format: int32");
		System.out.println("        description:");
		System.out.println("          type: string");
		System.out.println("        id:");
		System.out.println("          type: integer");
		System.out.println("          format: int64");
		// api-messages
		System.out.println("    api-messages:");
		System.out.println("      type: object");
		System.out.println("      properties:");
		System.out.println("        max-severity-code:");
		System.out.println("          type: integer");
		System.out.println("          format: int32");
		System.out.println("        max-severity-desc:");
		System.out.println("          type: string");
		System.out.println("        message-list:");
		System.out.println("          type: array");
		System.out.println("          items:");
		System.out.println("            $ref: '#/components/schemas/api-message-error'");
	}

	public void printDefOfReference() {
		Set<String> keySet = reqRespParamVOMap.keySet();
		for (String key : keySet) {
			System.out.println("######## " + key);
			ReqRespParamVO reqRespParamVO = reqRespParamVOMap.get(key);
			String refKey = null;
			if (reqRespParamVO.getMapOfInputObjectArrayList().size() > 0 || reqRespParamVO.getMapOfInputObjectList().size() > 0) {
				refKey = TextUtil.nameToLowerCaseAndDash(key + " " + GenTypeEnum.REQUEST.getMessage());
				printRefObject(refKey, reqRespParamVO.getMapOfInputObjectList(), reqRespParamVO.getMapOfInputObjectArrayList());
			}
			if (reqRespParamVO.getMapOfRespObjectList().size() > 0 || reqRespParamVO.getMapOfRespObjectArrayList().size() > 0) {
				refKey = TextUtil.nameToLowerCaseAndDash(key + " " + GenTypeEnum.RESPONSE.getMessage());
				printRefObject(refKey, reqRespParamVO.getMapOfRespObjectList(), reqRespParamVO.getMapOfRespObjectArrayList());
			}
		}
	}

	private void printRefObject(String obj, Map<String, List<ColumnDefVo>> mapOfObjList, Map<String, List<ColumnDefVo>> mapOfObjArrList) {
		int total = 0;
		total += mapOfObjList != null ? mapOfObjList.size() : 0;
		total += mapOfObjArrList != null ? mapOfObjArrList.size() : 0;

		if (total == 0) {
			return; // nothing to show
		}

		System.out.println("    " + obj + ":");
		System.out.println("      type: object");
		System.out.println("      properties:");
		// Request Response Object Definition
		if (mapOfObjList != null && mapOfObjList.size() > 0) {
			for (String subNode : mapOfObjList.keySet()) {
				System.out.println("        " + subNode + ":");
				System.out.println("          $ref: '#/components/schemas/" + obj + "-" + subNode.toLowerCase() + "'");
			}

		}

		if (mapOfObjArrList != null && mapOfObjArrList.size() > 0) {
			for (String subNode : mapOfObjArrList.keySet()) {
				System.out.println("        " + subNode + ":");
				System.out.println("          type: array");
				System.out.println("          items:");
				System.out.println("            $ref: '#/components/schemas/" + obj + "-" + subNode.toLowerCase() + "'");
			}
		}

		// object inside Request/Response Definition
		if (mapOfObjList != null && mapOfObjList.size() > 0) {
			for (String subNode : mapOfObjList.keySet()) {
				List<ColumnDefVo> variables = mapOfObjList.get(subNode);
				List<String> requiredNameList = new ArrayList<String>();
				for (ColumnDefVo vo : variables) {
					if (vo.isRequired()) {
						requiredNameList.add(vo.getName());
					}
				}

				if (variables != null && variables.size() > 0) {
					System.out.println("    " + obj + "-" + subNode.toLowerCase() + ":");
					if (requiredNameList.size() > 0) {
						System.out.println("      required:");
						for (String name : requiredNameList) {
							System.out.println("        - " + name);
						}
					}
					System.out.println("      type: object");
					System.out.println("      properties:");

					printObjectVariables(variables);
				}
			}
		}

		if (mapOfObjArrList != null && mapOfObjArrList.size() > 0) {
			for (String subNode : mapOfObjArrList.keySet()) {
				List<ColumnDefVo> variables = mapOfObjList.get(subNode);
				List<String> requiredNameList = new ArrayList<String>();
				for (ColumnDefVo vo : variables) {
					if (vo.isRequired()) {
						requiredNameList.add(vo.getName());
					}
				}

				if (variables != null && variables.size() > 0) {
					System.out.println("    " + obj + "-" + subNode.toLowerCase() + ":");
					if (requiredNameList.size() > 0) {
						System.out.println("      required:");
						for (String name : requiredNameList) {
							System.out.println("        - " + name);
						}
					}
					System.out.println("      type: object");
					System.out.println("      properties:");
					printObjectVariables(variables);
				}
			}
		}
	}

	private void printObjectVariables(List<ColumnDefVo> variables) {
		Set<String> useNameSet = new HashSet<String>();
		if (variables != null && variables.size() > 0) {
			for (ColumnDefVo defVO : variables) {
				if (useNameSet.contains(defVO.getName()) == false) {
					System.out.println("        " + defVO.getName() + ":");
					System.out.println("          type: " + defVO.getType());
					if (defVO.getMaxLength() > 0) {
						System.out.println("          maxLength: " + defVO.getMaxLength());
					}
					System.out.println("          format: " + defVO.getFormat());
					System.out.println("          description: \"" + defVO.getDesc() + "\"");
					useNameSet.add(defVO.getName());
				}
			}
		}
	}
}
