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
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.FuncGenEnum;
import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.reader.HeaderUtil;
import com.vincent.coretest.reader.PathUtil;
import com.vincent.coretest.util.DomainTypeUtil;
import com.vincent.coretest.util.FileOutputUtil;
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

	String outputFileName = null;

	@Test
	public void buildYamlFromFolder() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromFolder");

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

		outputFileName = "20241225_" + genEnum.name() + ".yaml";

		logger.info("working on {}", genEnum);

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
				MVPScopeVO vo = null;
				try {
					vo = new MVPScopeVO(genEnum, headerMap, rowData);
				} catch (Exception e) {
					logger.info("cnt wrong {} {} {}", e.toString(), headerMap, rowData);
					continue;
				}

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
		doJob();
	}

	@Ignore
	@Test
	public void buildYamlFromOneExcel() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");

		String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001 2.xlsx";
		Map<String, Integer> headerMap = ExcelReader.getHeaderIndex(xlsxFile);
		List<Map<Integer, Object>> rowMapList = ExcelReader.getActiveRow(xlsxFile, false);

		FuncGenEnum genEnum = FuncGenEnum.NEW; // NEW EXISTED
		outputFileName = "20241225_" + genEnum.getMessage() + ".yaml";

		String target = genEnum.getPrefix();
		String ignoreTarget = genEnum.getIgnorePrefix();

		for (Map<Integer, Object> rowData : rowMapList) {
			MVPScopeVO vo = null;
			try {
				vo = new MVPScopeVO(headerMap, rowData);
			} catch (Exception e) {
				continue;
			}

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
		doJob();
	}

	private void doJob() {
		prepareData();
		outputData();
	}

	private void prepareData() {
		checkData();
		groupingSameReqPath();
		groupingReqRespObject();
	}

	private void outputData() {
		logger.info("======================== start of YAML ========================");
		HeaderUtil.printHeader(outputFileName);
		printDefOfApi();
		printBasicOutputComponent();
		printDefOfReference();
	}

	public void printDefOfApi() {
		appendOutputToFile("paths:");
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
		appendOutputToFile("  " + reqPath + ":");

		List<String> keySet = mapForSameURLPath.get(reqPath);
		for (String key : keySet) {
			appendOutputToFile("######## " + key);
			List<MVPScopeVO> params = apiNameToApiDataMapFromExcel.get(key);
			if (params.size() > 0) {
				MVPScopeVO vo = params.get(0);
				String method = vo.getHttpMethod().toLowerCase();
				appendOutputToFile("    " + method + ":");
				appendOutputToFile("      tags:");
				appendOutputToFile("        - " + vo.getApiNode());
				appendOutputToFile("      summary: " + vo.getApiName());
				appendOutputToFile("      description: " + vo.getApiName().toUpperCase());
				appendOutputToFile("      operationId: " + vo.getApiName().toUpperCase());
				appendOutputToFile("      parameters:");
				appendOutputToFile(HeaderUtil.getMethodHeadersString());
				appendOutputToFile(PathUtil.getPathParamString(vo.getOriginalPathWithQuery()));
			}

			ReqRespParamVO queryVo = reqRespParamVOMap.get(key);
			appendOutputToFile(PathUtil.showQueryFromExcel(queryVo.getQueryObjectList()));

			List<MVPScopeVO> attributes = apiNameToApiDataMapFromExcel.get(key);
			ReqRespParamVO vo = ReqRespParamVOUtil.getReqRespParamVO(key, attributes);
			showRequestRefDeclaration(key, vo);
			showResponseRefDeclaration(key, vo);
		}
	}

	private void showRequestRefDeclaration(String key, ReqRespParamVO vo) {
		String refKey = TextUtil.nameToLowerCaseAndDash(key + " " + GenTypeEnum.REQUEST.getMessage());
		if (vo.getMapOfInputObjectArrayList().size() > 0 || vo.getMapOfInputObjectList().size() > 0) {
			appendOutputToFile(SchemaBodyUtil.genRequestSchemaText(refKey));
		}
	}

	private void showResponseRefDeclaration(String key, ReqRespParamVO vo) {
		String refKey = TextUtil.nameToLowerCaseAndDash(key + " " + GenTypeEnum.RESPONSE.getMessage());
		boolean withRespObj = false;
		if (vo.getMapOfRespObjectArrayList().size() > 0 || vo.getMapOfRespObjectList().size() > 0) {
			withRespObj = true;
		}
		appendOutputToFile(SchemaBodyUtil.genResponseSchemaText(refKey, withRespObj));
	}

	private void appendOutputToFile(String line) {
		FileOutputUtil.printOut(outputFileName, line);
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
		appendOutputToFile("######## components");
		appendOutputToFile("components:");
		appendOutputToFile("  schemas:");
		// api-message-error
		appendOutputToFile("    api-message-error:");
		appendOutputToFile("      type: object");
		appendOutputToFile("      properties:");
		appendOutputToFile("        severity-code:");
		appendOutputToFile("          type: integer");
		appendOutputToFile("          format: int32");
		appendOutputToFile("        description:");
		appendOutputToFile("          type: string");
		appendOutputToFile("        id:");
		appendOutputToFile("          type: integer");
		appendOutputToFile("          format: int64");
		// api-messages
		appendOutputToFile("    api-messages:");
		appendOutputToFile("      type: object");
		appendOutputToFile("      properties:");
		appendOutputToFile("        max-severity-code:");
		appendOutputToFile("          type: integer");
		appendOutputToFile("          format: int32");
		appendOutputToFile("        max-severity-desc:");
		appendOutputToFile("          type: string");
		appendOutputToFile("        message-list:");
		appendOutputToFile("          type: array");
		appendOutputToFile("          items:");
		appendOutputToFile("            $ref: '#/components/schemas/api-message-error'");
	}

	public void printDefOfReference() {
		Set<String> keySet = reqRespParamVOMap.keySet();
		for (String key : keySet) {
			appendOutputToFile("######## " + key);
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

		appendOutputToFile("    " + obj + ":");
		appendOutputToFile("      type: object");
		appendOutputToFile("      properties:");
		// Request Response Object Definition
		if (mapOfObjList != null && mapOfObjList.size() > 0) {
			for (String subNode : mapOfObjList.keySet()) {
				appendOutputToFile("        " + subNode + ":");
				appendOutputToFile("          $ref: '#/components/schemas/" + obj + "-" + subNode.toLowerCase() + "'");
			}

		}

		if (mapOfObjArrList != null && mapOfObjArrList.size() > 0) {
			for (String subNode : mapOfObjArrList.keySet()) {
				appendOutputToFile("        " + subNode + ":");
				appendOutputToFile("          type: array");
				appendOutputToFile("          items:");
				appendOutputToFile("            $ref: '#/components/schemas/" + obj + "-" + subNode.toLowerCase() + "'");
			}
		}

		// object inside Request/Response Definition
		if (mapOfObjList != null && mapOfObjList.size() > 0) {
			for (String subNode : mapOfObjList.keySet()) {
				List<ColumnDefVo> variables = mapOfObjList.get(subNode);
				Set<String> requiredNameList = new HashSet<String>();
				for (ColumnDefVo vo : variables) {
					if (vo.isRequired()) {
						requiredNameList.add(vo.getName());
					}
				}

				if (variables != null && variables.size() > 0) {
					appendOutputToFile("    " + obj + "-" + subNode.toLowerCase() + ":");
					if (requiredNameList.size() > 0) {
						appendOutputToFile("      required:");
						for (String name : requiredNameList) {
							appendOutputToFile("        - " + name);
						}
					}
					appendOutputToFile("      type: object");
					appendOutputToFile("      properties:");

					printObjectVariables(variables);
				}
			}
		}

		if (mapOfObjArrList != null && mapOfObjArrList.size() > 0) {
			for (String subNode : mapOfObjArrList.keySet()) {
				List<ColumnDefVo> variables = mapOfObjList.get(subNode);
				Set<String> requiredNameList = new HashSet<String>();
				for (ColumnDefVo vo : variables) {
					if (vo.isRequired()) {
						requiredNameList.add(vo.getName());
					}
				}

				if (variables != null && variables.size() > 0) {
					appendOutputToFile("    " + obj + "-" + subNode.toLowerCase() + ":");
					if (requiredNameList.size() > 0) {
						appendOutputToFile("      required:");
						for (String name : requiredNameList) {
							appendOutputToFile("        - " + name);
						}
					}
					appendOutputToFile("      type: object");
					appendOutputToFile("      properties:");
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
					appendOutputToFile("        " + defVO.getName() + ":");
					appendOutputToFile("          type: " + defVO.getType());
					if (defVO.getMaxLength() > 0) {
						appendOutputToFile("          maxLength: " + defVO.getMaxLength());
					}
					appendOutputToFile("          format: " + defVO.getFormat());
					appendOutputToFile("          description: \"" + defVO.getDesc() + "\"");
					useNameSet.add(defVO.getName());
				}
			}
		}
	}
}
