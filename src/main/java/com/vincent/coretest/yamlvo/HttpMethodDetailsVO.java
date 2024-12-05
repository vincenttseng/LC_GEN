package com.vincent.coretest.yamlvo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.util.TextUtil;

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
	LinkedHashMap responses;

	public static HttpMethodDetailsVO of(String path, LinkedHashMap methodDetails) {
		if (methodDetails == null) {
			return null;
		}
		Set<String> keySet = methodDetails.keySet();
		if (keySet == null || keySet.size() == 0) {
			return null;
		}

		HttpMethodDetailsVO vo = new HttpMethodDetailsVO();
		vo.path = path;
		for (String key : keySet) {
			logger.info(key);
			vo.httpMethod = key;
			LinkedHashMap linkedMap = (LinkedHashMap) methodDetails.get(key);
			setValue(vo, linkedMap);
		}

		return vo;
	}

	@SuppressWarnings("unchecked")
	public static void setValue(HttpMethodDetailsVO vo, LinkedHashMap linkedMap) {
		logger.info("map {}", linkedMap);
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
				logger.info("ignore {} {}", key, value);
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

	@SuppressWarnings("rawtypes")
	public static void setRequestBody(HttpMethodDetailsVO vo, Object value) {
		logger.info("setRequestBody {} {}", value.getClass(), value);
		if (value instanceof LinkedHashMap) {
			LinkedHashMap map = (LinkedHashMap) value;
			vo.setRequestBody(map);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void setResponses(HttpMethodDetailsVO vo, Object value) {
		logger.info("setResponseBody {} {}", value.getClass(), value);
		if (value instanceof LinkedHashMap) {
			LinkedHashMap map = (LinkedHashMap) value;
			vo.setResponses(map);
		}
	}
}
