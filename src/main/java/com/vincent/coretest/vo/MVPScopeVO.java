package com.vincent.coretest.vo;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.FuncGenEnum;
import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.util.HttpUtils;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Getter
@NoArgsConstructor
@ToString
public class MVPScopeVO {
	protected static final Logger logger = LoggerFactory.getLogger(MVPScopeVO.class);

	FuncGenEnum genEnum = FuncGenEnum.EXISTED;

	int rowIndex = 0;
	String type = "";
	String apiType = "";
	String apiName = "";
	String apiNode = "";
	String path = "";
	GenTypeEnum direction = null;

	String groupName = "";

	boolean required = false;

	String businessName = null;
	String dataType = null;
	String description = null;

	String httpMethod = null;
	String reqPath = null;
	String originalPathWithQuery = null;

	String domainValue = null;

	Map<String, Integer> headerMap = null;
	Map<Integer, Object> rowData = null;

	boolean isArray = false;

	public MVPScopeVO(FuncGenEnum genEnum, Map<String, Integer> headerMap, Map<Integer, Object> rowData) throws Exception {
		this.genEnum = genEnum;
		this.headerMap = headerMap;
		this.rowData = rowData;

		if (!isValidDataCount(rowData, 5)) {
			throw new Exception(rowData.toString());
		}

		handleInitData();
	}

	public MVPScopeVO(Map<String, Integer> headerMap, Map<Integer, Object> rowData) throws Exception {
		this.genEnum = FuncGenEnum.All;
		this.headerMap = headerMap;
		this.rowData = rowData;

		if (!isValidDataCount(rowData, 5)) {
			throw new Exception(rowData.toString());
		}

		handleInitData();
	}

	private void handleInitData() {
		Object obj = rowData.get(-1);

		if (obj instanceof Integer) {
			rowIndex = ((Integer) obj).intValue();
		}

		Integer index = headerMap.get("LC");
		if (index != null) {
			type = getValueFromMap(rowData, index, "");
		}

		index = headerMap.get("API type(N/E)");
		if (index != null) {
			apiType = getValueFromMap(rowData, index, "");
		}

		index = headerMap.get("Impacted API Name");
		if (index != null) {
			apiName = getValueFromMap(rowData, index, "");
		}

		index = headerMap.get("API Node");
		if (index != null) {
			apiNode = getValueFromMap(rowData, index, "");
		}

		index = headerMap.get("Group Name");
		if (index != null) {
			groupName = getValueFromMap(rowData, index, "");
		}
		groupName = correctGroupName(groupName);

		index = headerMap.get("Field Name");
		if (index != null) {
			description = getValueFromMap(rowData, index, "");
		}

		index = headerMap.get("Data type");
		if (index != null) {
			dataType = getValueFromMap(rowData, index, "");
		}

		index = headerMap.get("M/O");
		if (index != null) {
			String mo = getValueFromMap(rowData, index, "");
			if (mo != null && mo.trim().toLowerCase().equalsIgnoreCase("m")) {
				required = true;
			}
		}

		index = headerMap.get("Business Names");
		if (index != null) {
			businessName = getValueFromMap(rowData, index, "");
		}

		index = headerMap.get("Domain values");
		if (index != null) {
			domainValue = getValueFromMap(rowData, index, "");
		}

		// method part
		Integer methodIndex = headerMap.get("method");
		if (methodIndex == null) {
			methodIndex = headerMap.get("Method");
		}

		if (methodIndex != null) {
			httpMethod = getValueFromMap(rowData, methodIndex, "");
		}

		index = headerMap.get("New API URL");
		if (index != null) {
			String tmp = getValueFromMap(rowData, index, "");
			if (methodIndex != null) {
				path = HttpUtils.getPathRemovingifHttpMethod(tmp);
			} else {
				path = tmp;
			}
		}

		int offset = path.indexOf("/");
		try {
			if (StringUtils.isBlank(httpMethod)) {
				httpMethod = path.substring(0, offset);
			}
			originalPathWithQuery = path.substring(offset);
			path = originalPathWithQuery;
		} catch (Exception e) {
			// logger.info("path {}", path);
		}

		reqPath = HttpUtils.showURIWithoutQuery(originalPathWithQuery);

		if (genEnum == FuncGenEnum.NEW && reqPath.toLowerCase().contains("v2")) {
			apiName = new StringBuilder().append("V2 ").append(apiName).toString();
		}

		index = headerMap.get("Request/Response");
		if (index != null) {
			direction = GenTypeEnum.of(getValueFromMap(rowData, index, ""));
		}

		index = headerMap.get("Is Array");
		if (index != null) {
			String tmp = getValueFromMap(rowData, index, "");
			isArray = Boolean.parseBoolean(tmp);
		}
	}

	public static final String getValueFromMap(Map<Integer, Object> map, Integer key, String defaultVal) {
		if (map.containsKey(key)) {
			String value = map.get(key) != null ? StringUtils.trim(map.get(key).toString()).trim() : defaultVal;
			value = value.replace("\u00A0", "");

			return value;

		} else {
			return defaultVal;
		}
	}

	public static final String correctGroupName(String value) {
		value = value.replace(" ", "-");
		if (value.endsWith("(new)") || value.endsWith("(new-node)")) {
			int index = value.indexOf("(");
			value = value.substring(0, index);
		}
		return value;
	}

	public static final boolean isValidDataCount(Map<Integer, Object> map, int min) {
		Collection values = map.values();
		int cnt = 0;
		for (Object o : values) {
			String tmp = o != null ? o.toString() : "";
			if (StringUtils.isNotBlank(tmp)) {
				cnt++;
			}
			if (cnt > min) {
//				logger.info("AAA{} {}", cnt, map);
				return true;
			}
		}
//		logger.info("BBB{} {}", cnt, map);
		return (cnt > min);
	}
}
