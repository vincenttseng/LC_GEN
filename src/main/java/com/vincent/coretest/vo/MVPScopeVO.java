package com.vincent.coretest.vo;

import static com.vincent.coretest.util.TextUtil.getValueFromMap;
import static com.vincent.coretest.util.TextUtil.removeEndingSemicolonAndDash;

import java.util.Collection;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.FuncGenEnum;
import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.util.HttpUtils;
import com.vincent.coretest.util.TextUtil;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Getter
@NoArgsConstructor
@ToString
public class MVPScopeVO implements Cloneable {
	protected static final Logger logger = LoggerFactory.getLogger(MVPScopeVO.class);

	FuncGenEnum genEnum = FuncGenEnum.EXISTED;

	int rowIndex = 0;
	String type = "";
	String apiType = "";
	String apiName = "";
	String apiNode = "";
	String apiDesc = "";
	String apiSummary = "";
	String path = "";
	GenTypeEnum direction = null;

	String groupName = "";
	boolean isGroupRequired = false;

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

	String fileName = null;

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
		if (index == null) {
			index = headerMap.get("API Name");
		}
		if (index != null) {
			apiName = getValueFromMap(rowData, index, "");
		}
		apiName = TextUtil.filterAPIName(apiName);
		apiName = TextUtil.removeEndingSemicolonAndDash(apiName);

		index = headerMap.get("API Node");
		if (index != null) {
			apiNode = getValueFromMap(rowData, index, "");
		}
		apiNode = formatVariableLowerAndDash(apiNode);

		index = headerMap.get("API Description");
		if (index != null) {
			apiDesc = getValueFromMap(rowData, index, "");
		}
		apiDesc = formatVariableLowerAndDash(apiDesc);

		index = headerMap.get("API Summary");
		if (index != null) {
			apiSummary = getValueFromMap(rowData, index, "");
		}
		apiSummary = formatVariableLowerAndDash(apiSummary);

		index = headerMap.get("Group Name");
		if (index == null) {
			index = headerMap.get("Object Reference");
		}
		if (index != null) {
			groupName = StringUtils.lowerCase(getValueFromMap(rowData, index, ""));
		}
		groupName = correctGroupName(groupName);

		index = headerMap.get("Field description");
		if (index != null) {
			String tmp = getValueFromMap(rowData, index, "");
			description = concateLineRemoveParenthesis(tmp);
		}

		index = headerMap.get("Field Name");
		if (index != null) {
			String tmp = getValueFromMap(rowData, index, "");
			if (StringUtils.isBlank(description)) {
				description = concateLineRemoveParenthesis(tmp);
			}
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
		businessName = formatVariableLowerAndDash(businessName);
		businessName = concateLineRemoveParenthesis(businessName);

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

		index = headerMap.get("Group Mandatory/Optional");
		if (index != null) {
			String tmp = getValueFromMap(rowData, index, "");
			if ("m".equalsIgnoreCase(tmp)) {
				isGroupRequired = true;
			}
		}

		int offset = path.indexOf("/");
		try {
			if (StringUtils.isBlank(httpMethod)) {
				httpMethod = StringUtils.trim(path.substring(0, offset));
			}
			originalPathWithQuery = path.substring(offset);
			path = originalPathWithQuery;
		} catch (Exception e) {
			// logger.info("path {}", path);
		}

		if (StringUtils.isNotBlank(httpMethod)) {
			httpMethod = httpMethod.toUpperCase();
		}

		reqPath = HttpUtils.showURIWithoutQuery(originalPathWithQuery);
		reqPath = reqPath.toLowerCase();

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

	// Override clone method
	@Override
	public MVPScopeVO clone() throws CloneNotSupportedException {
		return (MVPScopeVO) super.clone(); // Perform shallow copy
	}

	public static String formatVariableLowerAndDash(String origName) {
		String tmp = new String(origName);
		final String DASH = "-";
		tmp = tmp.replace("_", DASH);
		String[] tokens = tmp.split(DASH);
		StringBuilder sb = new StringBuilder();
		boolean found = false;
		if (tokens != null && tokens.length > 0) {
			for (String token : tokens) {
				if (StringUtils.length(token) > 0) {
					if (found) {
						sb.append(DASH);
					}
					sb.append(StringUtils.lowerCase(token));
					found = true;
				}
			}
		}
		return removeEndingSemicolonAndDash(sb.toString());
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
		Collection<?> values = map.values();
		int cnt = 0;
		for (Object o : values) {
			String tmp = o != null ? o.toString() : "";
			if (StringUtils.isNotBlank(tmp)) {
				cnt++;
			}
			if (cnt > min) {
				return true;
			}
		}
		return (cnt > min);
	}

	public static final String concateLineRemoveParenthesis(String input) {
		StringTokenizer st = new StringTokenizer(input, "\n");
		StringBuilder sb = new StringBuilder();
		boolean found = false;
		while (st.hasMoreElements()) {
			String token = st.nextToken();
			if (StringUtils.isNotBlank(token)) {
				if (found) {
					sb.append(" ");
				}
				sb.append(token.trim());
				found = true;
			}
		}
		return sb.toString();
	}
}
