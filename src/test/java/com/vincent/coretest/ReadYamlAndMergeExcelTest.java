package com.vincent.coretest;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.yaml.PathDataStorage;
import com.vincent.coretest.yaml.YamlParserToPartsStorage;

public class ReadYamlAndMergeExcelTest {
	protected final Logger logger = LoggerFactory.getLogger(ReadYamlAndMergeExcelTest.class);

	@Test
	public void testReadYamlAndMergeExcelThenGenNew() throws FileNotFoundException {
		logger.info("testReadYamlAndMergeExcelThenGenNew");

		String root = "";
		File rootDir = new File(root);
		logger.info("root1 {}", rootDir.getAbsolutePath());

		String targetPath = rootDir.getAbsolutePath() + "\\src\\test\\input\\OpenAPI_LC_ALL.yaml";

		YamlParserToPartsStorage yamlData = new YamlParserToPartsStorage();
		yamlData.readYamlAndMergeExcelThenGenNew(targetPath);

//		yamlData.getRootEleList().stream().forEach(element -> {
//			logger.info(element);
//		});

//		logger.info("================>show RESTFul Data");
//		yamlData.showData();
//
//		logger.info("================>show components");
//		yamlData.getComponentsStorage().showData();

		PathDataStorage pathData = new PathDataStorage();
		pathData.split(targetPath, yamlData.getRootEleList(), yamlData.getTheHttpMethodDetailsVOList());
	}
}
