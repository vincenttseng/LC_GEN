package com.vincent.coretest.vo;

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
	protected final Logger logger = LoggerFactory.getLogger(MVPScopeVO.class);

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

	public MVPScopeVO(FuncGenEnum genEnum, Map<String, Integer> headerMap, Map<Integer, Object> rowData) {
		this.genEnum = genEnum;
		this.headerMap = headerMap;
		this.rowData = rowData;

		handleInitData();
	}

	public MVPScopeVO(Map<String, Integer> headerMap, Map<Integer, Object> rowData) {
		this.genEnum = FuncGenEnum.EXISTED;
		this.headerMap = headerMap;
		this.rowData = rowData;

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
			httpMethod = getValueFromMap(rowData, methodIndex, "GET");
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
	}

	public static final String getValueFromMap(Map<Integer, Object> map, Integer key, String defaultVal) {
		if (map.containsKey(key)) {
			return map.get(key) != null ? map.get(key).toString().trim() : defaultVal;

		} else {
			return defaultVal;
		}
	}
}
