package com.vincent.coretest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.vincent.coretest.vo.ro.ColumnDefVo;

import sun.awt.SunHints.Value;

public class TextUtil {
	public static List<String> splitBrackets(String input) {

		List<String> tokens = new ArrayList<String>();

		Pattern pattern = Pattern.compile("\\{(.*?)\\}"); // 使用非贪婪模式匹配花括号内部内容
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			tokens.add(matcher.group(1));
		}

		return tokens;
	}

	public static Map<String, String> readFileToDomainMap(File domainFile) throws IOException {
		FileReader in = new FileReader(domainFile);
		BufferedReader br = new BufferedReader(in);

		Map<String, String> map = new HashMap<String, String>();

		String line;
		while ((line = br.readLine()) != null) {
			String[] tokens = null;
			if (line != null && line.length() > 0) {
				// System.out.print(line + " =>");
				tokens = line.split(",");

				int length = tokens != null ? tokens.length : 0;
				// System.out.println(length);

				if (length == 3) {
					String key = StringUtils.trim(tokens[0]);
					String value1 = StringUtils.trim(tokens[1]);
					String value2 = StringUtils.trim(tokens[2]);
					if (!map.containsKey(key)) {
						map.put(key, value1 + ":" + value2);
					} else {
						String value = map.get(key);
						map.put(key, value + "," + value1 + ":" + value1);
					}
				}
			}
		}
		in.close();

		return map;
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

	/**
	 * A### B###
	 * 
	 * @param filename
	 * @return
	 */
	public static Map<String, List<ColumnDefVo>> parseExcelInputOutputObject(File file) {
		return parseExcelInputOutputObject(file, "A", "B");
	}

	/**
	 * A### C###
	 * 
	 * @param filename
	 * @return
	 */
	public static Map<String, List<ColumnDefVo>> parseExcelInputOutputObjectArray(File file) {
		return parseExcelInputOutputObject(file, "A", "C");
	}

	public static Map<String, List<ColumnDefVo>> parseExcelInputOutputObject(File file, String mark0, String mark1) {
		if (file == null || !file.exists()) {
			return null;
		}

		Map<String, List<ColumnDefVo>> map = new HashMap<String, List<ColumnDefVo>>();

		FileReader in = null;
		try {
			in = new FileReader(file);
			BufferedReader br = new BufferedReader(in);

			String currentToken = null;
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("###")) {
					String[] resultArr = line.split("###");
					if (resultArr != null && resultArr.length == 2) {
						String head = resultArr[0].trim();
						if (mark0.equals(head)) { //
							currentToken = resultArr[1].trim();

							if (currentToken.endsWith(":")) {
								currentToken = currentToken.substring(0, currentToken.length() - 1);
							}

							if (!map.containsKey(currentToken)) {
								map.put(currentToken, new ArrayList<ColumnDefVo>());
							}
						} else if (mark1.equals(head)) {

							ColumnDefVo vo = ColumnDefVo.convert(resultArr[1].trim());

							map.get(currentToken).add(vo);
						}
					}
				}
			}
		} catch (IOException e) {

		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return map;
	}

}
