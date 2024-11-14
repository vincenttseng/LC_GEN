package com.vincent.coretest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

}
