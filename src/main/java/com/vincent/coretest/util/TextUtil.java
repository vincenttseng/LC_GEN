package com.vincent.coretest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextUtil {
	protected final Logger logger = LoggerFactory.getLogger(TextUtil.class);

	public static List<String> splitBrackets(String input) {

		List<String> tokens = new ArrayList<String>();

		Pattern pattern = Pattern.compile("\\{(.*?)\\}"); // 使用非贪婪模式匹配花括号内部内容
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			tokens.add(matcher.group(1));
		}

		return tokens;
	}

	public static String nameToLowerCaseAndDash(String name) {
		if (name == null) {
			return "";
		}

		String[] tokens = name.split(" ");
		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (String token : tokens) {
			token = token.trim();
			if (token != null && token.length() > 0) {
				if (found) {
					sb.append("-");
				}
				sb.append(token.toLowerCase());
				found = true;
			}
		}
		return sb.toString();
	}

	/**
	 * a-b-c to a b c
	 * 
	 * @param phase
	 */
	public static String phaseWordToDesc(String phase) {
		String[] parts = phase.split("-"); // 使用 "-" 作為分隔符

		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (String part : parts) {
			if (found) {
				sb.append(" ");
			}
			sb.append(part);
			found = true;
		}
		return sb.toString();
	}

	public static List<Integer> getNumberFromParentheses(String type) {
		List<Integer> list = new ArrayList<Integer>();

		if (type != null) {
			int index0 = type.indexOf("(");
			int index1 = type.indexOf(")");
			if (index0 > 0 && index1 > 0 && index0 < index1) {
				String subString = type.substring(index0 + 1, index1);
				String[] tokens = subString.split(",");
				for (String token : tokens) {
					try {
						int value = Integer.parseInt(token.trim());
						list.add(value);
					} catch (Exception e) {
					}
				}
			}
		}

		return list;
	}

	public static int objectToInt(Object obj, int defaultVal) {
		try {
			return Integer.parseInt(obj != null ? obj.toString() : null);
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static String objectToString(Object obj, String defaultVal) {
		return (obj != null) ? obj.toString() : defaultVal;
	}

	public static final int countLeadSpace(String line) {
		if (StringUtils.isBlank(line)) {
			return 0;
		}
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) != ' ') {
				return i;
			}
		}
		return -1;
	}

	public static String removeEndingSemicolonAndDash(String line) {
		String tmp = line;
		String result = trimEndingSemicolonAndDash(tmp);
		while (result.length() != tmp.length()) {
			tmp = result;
			result = trimEndingSemicolonAndDash(tmp);
		}
		return result;
	}

	private static String trimEndingSemicolonAndDash(String line) {
		if (StringUtils.isNotBlank(line)) {
			if (line.endsWith(":") || line.endsWith("-")) {
				return StringUtils.trim(line.substring(0, line.length() - 1));
			} else {
				return StringUtils.trim(line);
			}
		} else {
			return line;
		}
	}

	public static String getHttpMethodFromYamlMethodComments(String line) {
		int indexSemi = line.indexOf(":");
		int indexComment = line.indexOf("#");
		if (indexComment > indexSemi) {
			return removeEndingSemicolonAndDash(StringUtils.trim(line.substring(0, indexSemi)));
		} else {
			return removeEndingSemicolonAndDash(line);
		}
	}

	public static String getCommentsFromYamlMethodComments(String line) {
		int indexSemi = line.indexOf(":");
		int indexComment = line.indexOf("#");
		if (indexComment > indexSemi) {
			return StringUtils.trim(line.substring(indexComment));
		} else {
			return "";
		}
	}

	public static String filterAPIName(String apiName) {
		return apiName.replaceAll("[^a-zA-Z0-9 ]", "");
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
}
