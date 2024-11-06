package com.vincent.coretest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ro.ColumnDefVo;

public class ParseOutputTest {
	protected final Logger logger = LoggerFactory.getLogger(ParseOutputTest.class);

	String domainFilename = ".\\src\\test\\input\\domainRef.csv";

	String responseFilename = ".\\src\\test\\input\\response.txt";

	@Test
	public void genResponseObjectAndRef() throws IOException {
		File file = new File(responseFilename);
		logger.info("file " + file.getAbsolutePath() + " existed: " + file.exists());

		Map<String, List<ColumnDefVo>> objectMap = TextUtil.parseExcelInputOutputObject(file);
		Set<String> keySet = objectMap.keySet();

		for (String key : keySet) {
			List<ColumnDefVo> list = objectMap.get(key);
			if(list.size() > 0) {
				logger.info("object key " + key);
				for (ColumnDefVo value : list) {
					logger.info("value " + value);
				}
			}
		}
		
		logger.info("array");
		logger.info("======================================");
		
		Map<String, List<ColumnDefVo>> objectArrayMap = TextUtil.parseExcelInputOutputObjectArray(file);
		Set<String> keySet2 = objectArrayMap.keySet();

		for (String key : keySet2) {
			List<ColumnDefVo> list = objectArrayMap.get(key);
			if(list.size() > 0) {
				logger.info("array key " + key);
				for (ColumnDefVo value : list) {
					logger.info("value " + value);
				}
			}
		}

	}
}
