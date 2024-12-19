package com.vincent.coretest.yaml.vo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.yaml.YamlParserToPartsStorage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class HttpMethodDetailsVO {
	protected static final Logger logger = LoggerFactory.getLogger(HttpMethodDetailsVO.class);
	String path;
	String httpMethod;

	private List<String> tags;
	String summary;
	String description;
	String operationId;
	private List<ParamVO> params;
	LinkedHashMap requestBody;
	String reqRef;
	LinkedHashMap responses;
	String respType = "";
	List<String> responseList = new ArrayList<String>();

	public static List<HttpMethodDetailsVO> of(String path, LinkedHashMap methodDetails) {
		final List<HttpMethodDetailsVO> methodVOList = new ArrayList<HttpMethodDetailsVO>();
		if (methodDetails == null) {
			return methodVOList;
		}
		Set<String> keySet = methodDetails.keySet();
		if (keySet == null || keySet.size() == 0) {
			return methodVOList;
		}

		for (String key : keySet) {
			HttpMethodDetailsVO vo = new HttpMethodDetailsVO();
			vo.path = path;
			vo.httpMethod = key;
			LinkedHashMap linkedMap = (LinkedHashMap) methodDetails.get(key);
			setValue(vo, linkedMap);
			vo.prepareReqRef();
			vo.prepareResponseObj();
			vo.prepareValue();
			methodVOList.add(vo);
		}

		return methodVOList;
	}

	@SuppressWarnings("unchecked")
	public static void setValue(HttpMethodDetailsVO vo, LinkedHashMap linkedMap) {
		linkedMap.forEach((key, value) -> {
			if ("tags".equals(key)) {
				if (value instanceof ArrayList) {
					vo.tags = (ArrayList) value;
				}
			} else if ("summary".equals(key)) {
				vo.summary = TextUtil.objectToString(value, "");
			} else if ("description".equals(key)) {
				vo.description = TextUtil.objectToString(value, "");
			} else if ("operationId".equals(key)) {
				vo.operationId = TextUtil.objectToString(value, "");
			} else if ("parameters".equals(key)) {
				setParameters(vo, value);
			} else if ("requestBody".equals(key)) {
				setRequestBody(vo, value);
			} else if ("responses".equals(key)) {
				setResponses(vo, value);
			} else {

			}

		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setParameters(HttpMethodDetailsVO vo, Object value) {
		List<ParamVO> paramList = new ArrayList<ParamVO>();
		if (value instanceof ArrayList) {
			List<LinkedHashMap> list = (List<LinkedHashMap>) value;
			for (LinkedHashMap map : list) {
				ParamVO aParamVO = ParamVO.of(map);
				if (aParamVO != null) {
					paramList.add(aParamVO);
				}

			}
		}
		vo.setParams(paramList);
	}

	private String apiNode;

	public void prepareValue() {
		setApiNode();
		setApiUrl();
	}

	public void setApiNode() {
		String nodeName = null;
		Object obj = null;
		if (tags != null && tags.size() > 0) {
			nodeName = tags.get(0);
		}
		apiNode = nodeName;
	}

	private String fullPathUrl;

	public void setApiUrl() {
		StringBuilder sb = new StringBuilder();

		if (StringUtils.isNotBlank(httpMethod)) {
			sb.append(StringUtils.trim(StringUtils.upperCase(httpMethod)));
			sb.append(" ").append(StringUtils.trim(path));

		}
		boolean found = false;
		for (ParamVO paramVO : params) {
			if ("query".equals(paramVO.getIn())) {
				if(found == false) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				logger.info("query " + paramVO);
				
				sb.append(paramVO.getName()).append("=").append("<");
				
				SchemaVO schema = paramVO.getSchema();
				if("number".equalsIgnoreCase(schema.getType())||"integer".equalsIgnoreCase(schema.getType())) {
					sb.append("Numeric");
				} else {
					sb.append("String");
				}
				sb.append(">");
				
				found = true;
			}
		}
		fullPathUrl = sb.toString();
	}

	private static final String getNodeNameFromReq(LinkedHashMap map) {
		if (map == null) {
			return null;
		}
		LinkedHashMap tmpMap = new LinkedHashMap();
		tmpMap.putAll(map);
		Object tmpObj = null;
		if (tmpMap.containsKey("application/json")) {
			tmpObj = map.get("application/json");
			if (tmpObj instanceof LinkedHashMap) {
				tmpMap = (LinkedHashMap) tmpObj;
				if (tmpMap.containsKey("schema")) {
					tmpObj = map.get("schema");
					if (tmpObj instanceof LinkedHashMap) {
						tmpMap = (LinkedHashMap) tmpObj;
						if (tmpMap.containsKey("$ref")) {
							String value = map.get("$ref").toString();
							logger.info("value {}", value);
						}
					}
				}
			}
		}
		return "";
	}

	@SuppressWarnings("rawtypes")
	public static void setRequestBody(HttpMethodDetailsVO vo, Object value) {
		if (value instanceof LinkedHashMap) {
			LinkedHashMap map = (LinkedHashMap) value;
			vo.setRequestBody(map);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void setResponses(HttpMethodDetailsVO vo, Object value) {
		if (value instanceof LinkedHashMap) {
			LinkedHashMap map = (LinkedHashMap) value;
			vo.setResponses(map);
		}
//		logger.info("setResponseBody ==>{}", vo.getResponses());
	}

	@SuppressWarnings("rawtypes")
	public void prepareReqRef() {
		LinkedHashMap map = null;
		if (requestBody != null) {
			if (requestBody.containsKey("content")) {
				Object obj = requestBody.get("content");
				if (obj instanceof LinkedHashMap) {
					map = (LinkedHashMap) obj;
					if (map.containsKey("application/json")) {
						obj = map.get("application/json");
						if (obj instanceof LinkedHashMap) {
							map = (LinkedHashMap) obj;
							if (map.containsKey("schema")) {
								obj = map.get("schema");
								if (obj instanceof LinkedHashMap) {
									map = (LinkedHashMap) obj;
									if (map.containsKey("$ref")) {
										reqRef = TextUtil.objectToString(map.get("$ref"), null);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void prepareResponseObj() {
		LinkedHashMap map = null;
		if (responses != null) {
			if (responses.containsKey("200")) {
				Object obj = responses.get("200");
				if (obj instanceof LinkedHashMap) {
					map = (LinkedHashMap) obj;
					if (map.containsKey("content")) {
						obj = map.get("content");
						if (obj instanceof LinkedHashMap) {
							map = (LinkedHashMap) obj;
							if (map.containsKey("application/json")) {
								obj = map.get("application/json");
								if (obj instanceof LinkedHashMap) {
									map = (LinkedHashMap) obj;
									if (map.containsKey("schema")) {
										obj = map.get("schema");
										if (obj instanceof LinkedHashMap) {
											map = (LinkedHashMap) obj;
											if (map.containsKey("$ref")) {
												String path = TextUtil.objectToString(map.get("$ref"), null);
												if (path != null) {
													responseList.add(path);
												}
											} else if (map.containsKey("anyOf") || map.containsKey("oneOf")) {
												obj = map.get("anyOf");
												if (obj != null) {
													respType = "anyOf";
												} else {
													respType = "anyOf";
													obj = map.get("oneOf");
												}
												if (obj instanceof ArrayList) {
													ArrayList<?> list = (ArrayList) obj;
													list.stream().forEach(value -> {
														if (value instanceof LinkedHashMap) {
															Map tmpMap = (LinkedHashMap) value;
															if (tmpMap.containsKey("$ref")) {
																String path = TextUtil.objectToString(tmpMap.get("$ref"), null);
																if (path != null) {
																	responseList.add(path);
																}
															}
														}
													});
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
