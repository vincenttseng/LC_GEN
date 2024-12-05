package com.vincent.coretest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.vincent.coretest.yamlvo.HttpMethodDetailsVO;

public class ReadYamlAndMergeExcel {
	protected final Logger logger = LoggerFactory.getLogger(ReadYamlAndMergeExcel.class);

	String path = "";

	String[] httpMethods = { "get", "post", "put", "patch" };

	@Test
	public void testReadYamlAndMergeExcelThenGenNew() throws FileNotFoundException {
		logger.info("testReadYamlAndMergeExcelThenGenNew");

		String root = "";
		File rootDir = new File(root);
		logger.info("root1 {}", rootDir.getAbsolutePath());

		String targetPath = rootDir.getAbsolutePath() + "\\src\\test\\input\\MP3_001_new.yaml";

		Yaml yaml = new Yaml();
		InputStream inputStream = new FileInputStream(targetPath);
		System.out.println(inputStream);

		HashMap yamlMap = yaml.load(inputStream);
		HashMap pathsMap = (HashMap) yamlMap.get("paths");

		List<HttpMethodDetailsVO> result = new ArrayList<HttpMethodDetailsVO>();

		@SuppressWarnings("unchecked")
		Set<String> pathSet = pathsMap.keySet();
		for (String path : pathSet) {
			logger.info("path {}", path);
			LinkedHashMap methodDefMap = (LinkedHashMap) pathsMap.get(path);
			HttpMethodDetailsVO vo = HttpMethodDetailsVO.of(path, methodDefMap);
			if (vo != null) {
				result.add(vo);
			}
		}
		logger.info("list result {}", result);
	}
}
