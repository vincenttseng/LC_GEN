package com.vincent.coretest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.reader.HeaderUtil;
import com.vincent.coretest.reader.PathUtil;
import com.vincent.coretest.util.FileOutputUtil;
import com.vincent.coretest.util.ReqRespParamVOUtil;
import com.vincent.coretest.util.SchemaBodyUtil;
import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ColumnDefVo;
import com.vincent.coretest.vo.MVPScopeVO;
import com.vincent.coretest.vo.ReqRespParamVO;

public abstract class AbstractExcelReadBuilder {

	public abstract Logger getLogger();

	// group map for same path different httpMethod
	Map<String, List<String>> mapForSameURLPath = new HashMap<String, List<String>>();

	// group map for all attributes of same function
	Map<String, List<MVPScopeVO>> apiNameToApiDataMapFromExcel = new HashMap<String, List<MVPScopeVO>>();

	Map<String, ReqRespParamVO> reqRespParamVOMap = new HashMap<String, ReqRespParamVO>();

	protected String outputFileName = null;

	private void appendOutputToFile(String line) {
		FileOutputUtil.printOut(outputFileName, line);
	}

	protected void doJob() {
		prepareData();
		outputData();
	}

	protected void prepareData() {
		checkData();
		groupingSameReqPath();
		groupingReqRespObject();
	}

	protected void outputData() {
		getLogger().info("======================== start of YAML ========================");
		HeaderUtil.printHeader(outputFileName);
		printDefOfApi();
		printBasicOutputComponent();
		printDefOfReference();
		getLogger().info("=========== end YAML =====" + outputFileName + " ===========");
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
						getLogger().error("path diff " + targetPath + " vs " + path + " ref" + vo);
					}
				}
			}
		}
//		if (error) {
//			System.exit(0);
//		}

		getLogger().info("checkData pass");
	}

	public void groupingSameReqPath() {
		Set<String> keySet = apiNameToApiDataMapFromExcel.keySet();
		for (String key : keySet) {
			List<MVPScopeVO> params = apiNameToApiDataMapFromExcel.get(key);

			if (params.size() > 0) {
				MVPScopeVO vo = params.get(0);
				String reqPath = vo.getReqPath();
				String httpMethod = vo.getHttpMethod();
				getLogger().info(key + " " + httpMethod + " " + reqPath);

				if (!mapForSameURLPath.containsKey(reqPath)) {
					mapForSameURLPath.put(reqPath, new ArrayList<String>());
				}
				mapForSameURLPath.get(reqPath).add(key);
			}
		}

		getLogger().info("==============:showing different path");
		mapForSameURLPath.keySet().forEach(key -> {
			getLogger().info("path " + key);
			List<String> values = mapForSameURLPath.get(key);
			values.forEach(value -> {
				getLogger().info("pathmethod : " + key + " method:" + value);
			});
		});
		getLogger().info("==============:ending different path");
	}

	public void groupingReqRespObject() {
		getLogger().info("====================groupingReqRespObject==========================");
		Set<String> keySet = apiNameToApiDataMapFromExcel.keySet();
		keySet.stream().forEach(key -> {
			getLogger().info("groupingReqRespObject key : {}", key);
			List<MVPScopeVO> attributes = apiNameToApiDataMapFromExcel.get(key);
			ReqRespParamVO vo = ReqRespParamVOUtil.getReqRespParamVO(key, attributes);

			vo.showContent();
			reqRespParamVOMap.put(key, vo);
		});
	}

	// output block
	public void printDefOfApi() {
		appendOutputToFile("paths:");
		Set<String> keySet = mapForSameURLPath.keySet();
		keySet.stream().forEach(reqPath -> {
			showDefApiByKey(reqPath);
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

		Set<String> requiredSet = collectGroupRequired(mapOfObjList, mapOfObjArrList);

		appendOutputToFile("    " + obj + ":");
		if (requiredSet != null && requiredSet.size() > 0) {
			appendOutputToFile("      required:");
			for (String node : requiredSet) {
//				getLogger().info("required node {}", node);
				appendOutputToFile("        - " + node);
			}
		}
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
				List<ColumnDefVo> variables = mapOfObjArrList.get(subNode);
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

	private Set<String> collectGroupRequired(Map<String, List<ColumnDefVo>> mapOfObjList, Map<String, List<ColumnDefVo>> mapOfObjArrList) {
		Set<String> requiredGroup = new HashSet<String>();
		if (mapOfObjList != null && mapOfObjList.size() > 0) {
			for (String subNode : mapOfObjList.keySet()) {
				List<ColumnDefVo> list = mapOfObjList.get(subNode);
				if (list != null && list.size() > 0) {
					ColumnDefVo vo = list.get(0);
					if (vo.isGroupRequired()) {
						requiredGroup.add(subNode.toLowerCase());
					}
				}
			}
		}

		if (mapOfObjArrList != null && mapOfObjArrList.size() > 0) {
			for (String subNode : mapOfObjArrList.keySet()) {
				List<ColumnDefVo> list = mapOfObjArrList.get(subNode);
				if (list != null && list.size() > 0) {
					ColumnDefVo vo = list.get(0);
					if (vo.isGroupRequired()) {
						requiredGroup.add(subNode.toLowerCase());
					}
				}
			}
		}
		return requiredGroup;
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
