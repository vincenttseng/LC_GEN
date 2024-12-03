package com.vincent.coretest.vo;

import java.util.List;
import java.util.Map;

import com.vincent.coretest.enumeration.GenTypeEnum;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Getter
@NoArgsConstructor
@ToString
public class MVPScopeVO {

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

	String domainValue = null;

	Map<Integer, Object> rowData = null;

	public MVPScopeVO(Map<String, Integer> headerMap, Map<Integer, Object> rowData) {
		this.rowData = rowData;

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
		
		index = headerMap.get("New API URL");
		if (index != null) {
			path = getValueFromMap(rowData, index, "");
		}
		int offset = path.indexOf("/");
		httpMethod = path.substring(0, offset);
		reqPath = path.substring(offset);

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
