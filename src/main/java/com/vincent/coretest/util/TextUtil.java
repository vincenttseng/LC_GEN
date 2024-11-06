package com.vincent.coretest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
	public static List<String> splitBrackets(String input) {

		List<String> tokens = new ArrayList<String>();

		Pattern pattern = Pattern.compile("\\{(.*?)\\}"); // 使用非贪婪模式匹配花括号内部内容
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
//			System.out.println(matcher.group(1)); // 提取花括号中的内容
			tokens.add(matcher.group(1));
		}

		return tokens;
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
