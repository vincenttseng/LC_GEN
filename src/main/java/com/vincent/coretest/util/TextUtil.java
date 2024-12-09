package com.vincent.coretest.util;

import java.util.ArrayList;
import java.util.List;
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
	 * a-b-c to A B C
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
			sb.append(part.toUpperCase());
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

	public static String removeEndingSemicolon(String line) {
		if (StringUtils.isBlank(line) || !line.endsWith(":")) {
			return StringUtils.trim(line);
		} else {
			return StringUtils.trim(line.substring(0, line.length() - 1));
		}
	}

	public static String getHttpMethodFromYamlMethodComments(String line) {
		int indexSemi = line.indexOf(":");
		int indexComment = line.indexOf("#");
		if (indexComment > indexSemi) {
			return removeEndingSemicolon(StringUtils.trim(line.substring(0, indexSemi)));
		} else {
			return removeEndingSemicolon(line);
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

}
