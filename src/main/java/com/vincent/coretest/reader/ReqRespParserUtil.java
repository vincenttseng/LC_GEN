package com.vincent.coretest.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ColumnDefVo;

public class ReqRespParserUtil {
	protected static final Logger logger = LoggerFactory.getLogger(TextUtil.class);

	/**
	 * A### B###
	 * 
	 * @param filename
	 * @return
	 */
	public static Map<String, List<ColumnDefVo>> parseExcelInputOutputObject(File file) {
		Map<String, List<ColumnDefVo>> objectMap = parseExcelInputOutputObject(file, "A", "B");
		Set<String> keySet = objectMap.keySet();

		for (String key : keySet) {
			List<ColumnDefVo> list = objectMap.get(key);
			if (list.size() > 0) {
				logger.info("object key " + key);
				for (ColumnDefVo value : list) {
					logger.info("value " + value);
				}
			}
		}
		return objectMap;
	}

	/**
	 * A### C###
	 * 
	 * @param filename
	 * @return
	 */
	public static Map<String, List<ColumnDefVo>> parseExcelInputOutputObjectArray(File file) {
		Map<String, List<ColumnDefVo>> objectMap = parseExcelInputOutputObject(file, "A", "C");
		Set<String> keySet = objectMap.keySet();

		for (String key : keySet) {
			List<ColumnDefVo> list = objectMap.get(key);
			if (list.size() > 0) {
				logger.info("object key " + key);
				for (ColumnDefVo value : list) {
					logger.info("value " + value);
				}
			}
		}
		return objectMap;
	}

	public static Map<String, List<ColumnDefVo>> parseExcelInputOutputObject(File file, String mark0, String mark1) {
		if (file == null || !file.exists()) {
			return null;
		}

		Map<String, List<ColumnDefVo>> map = new HashMap<String, List<ColumnDefVo>>();

		FileReader in = null;
		BufferedReader br = null;
		try {
			in = new FileReader(file);
			br = new BufferedReader(in);

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
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
