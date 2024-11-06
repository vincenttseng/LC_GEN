package com.vincent.coretest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vincent.coretest.vo.ro.ColumnDefVo;

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
