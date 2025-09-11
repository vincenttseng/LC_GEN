package com.vincent.coretest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.FuncGenEnum;
import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.util.DomainTypeUtil;
import com.vincent.coretest.vo.MVPScopeVO;

public class ExcelReadFolderBuilderTest extends AbstractExcelReadBuilder {
	protected final Logger logger = LoggerFactory.getLogger(ExcelReadFolderBuilderTest.class);

	@Test
	public void buildYamlFromFolder() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromFolder");

		String root = "";
		File rootDir = new File(root);
		logger.info("root1 {}", rootDir.getAbsolutePath());

		String targetPath = rootDir.getAbsolutePath() + "\\src\\test\\input\\group\\";
		File targetFolderFile = new File(targetPath);
		logger.info("targetFolderFile {} existed {} dir {}", targetFolderFile.getAbsolutePath(), targetFolderFile.exists(), targetFolderFile.isDirectory());
		if (targetFolderFile == null || !targetFolderFile.isDirectory()) {
			return;
		}

		FuncGenEnum genEnum = FuncGenEnum.All; // NEW EXISTED

		outputFileName = "20250105_2_LC_" + genEnum.name() + ".yaml";

		logger.info("working on {}", genEnum);

		String target = (genEnum != FuncGenEnum.All) ? genEnum.getPrefix() : null;
		String ignoreTarget = (genEnum != FuncGenEnum.All) ? genEnum.getIgnorePrefix() : null;

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

			int activeCnt = 0;
			for (Map<Integer, Object> rowData : rowMapList) {
				MVPScopeVO vo = null;
				try {
					vo = new MVPScopeVO(genEnum, headerMap, rowData);
				} catch (Exception e) {
					logger.info("cnt wrong {} {} {}", e.toString(), headerMap, rowData);
					continue;
				}

				if (target == null || (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(target))) {
					logger.info("{}", vo);
					String apiName = vo.getApiName();
					if (!apiNameToApiDataMapFromExcel.containsKey(apiName)) {
						apiNameToApiDataMapFromExcel.put(apiName, new ArrayList<MVPScopeVO>());
					}
					apiNameToApiDataMapFromExcel.get(apiName).add(vo);
					activeCnt++;
				} else if (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(ignoreTarget)) {
					logger.debug("ignored {}", vo);
				} else {
					logger.debug("error " + vo);
				}

				String desc = DomainTypeUtil.getDescriptionByDomainValue(vo.getDescription(), vo.getDomainValue());
				if (StringUtils.isNotBlank(desc)) {
					// logger.info("changing desc {}", desc);
					vo.setDescription(desc);
				}
			}
			logger.info("=====>{} active line {}", xlsxFile, activeCnt);
		}
		logger.info("START handleData");
		doJob();
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

}
