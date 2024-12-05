package com.vincent.coretest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.ExcelReader;

public class ExcelReaderSampleTest {
	protected final Logger logger = LoggerFactory.getLogger(ExcelReadBuilderTest.class);

	@Test
	public void buildYamlFromFolder() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");

		String root = "";
		File rootDir = new File(root);
		logger.info("root1 {}", rootDir.getAbsolutePath());

		String targetPath = rootDir.getAbsolutePath() + "\\src\\test\\input\\group\\";
		File targetFolderFile = new File(targetPath);
		logger.info("targetFolderFile {} existed {} dir {}", targetFolderFile.getAbsolutePath(), targetFolderFile.exists(), targetFolderFile.isDirectory());
		if (targetFolderFile == null || !targetFolderFile.isDirectory()) {
			return;
		}

		File[] dirFiles = targetFolderFile.listFiles();

		for (File xlsxFile : dirFiles) {
			if (!xlsxFile.getAbsolutePath().toLowerCase().endsWith("xlsx")) { // not xlsx => ignore
				continue;
			}
			logger.info("handling {}", xlsxFile);
			Map<String, Integer> headerMap = ExcelReader.getHeaderIndex(xlsxFile.getAbsolutePath());
			logger.info("header {}", headerMap);

			List<Map<Integer, Object>> rowMapList = ExcelReader.getActiveRow(xlsxFile.getAbsolutePath(), false);
			logger.info("size {}", rowMapList.size());
		}
	}
}
