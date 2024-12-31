package com.vincent.coretest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.FuncGenEnum;
import com.vincent.coretest.reader.ExcelReader;
import com.vincent.coretest.util.DomainTypeUtil;
import com.vincent.coretest.vo.MVPScopeVO;

public class ExcelReadFileBuilderTest extends AbstractExcelReadBuilder {
	protected final Logger logger = LoggerFactory.getLogger(ExcelReadFileBuilderTest.class);

	@Test
	public void buildYamlFromOneExcel() throws FileNotFoundException, IOException {
		logger.info("buildYamlFromExcelForNew");

		String xlsxFile = ".\\src\\test\\input\\MVP3 scope for TF_LC_B4_001 2.xlsx";
		Map<String, Integer> headerMap = ExcelReader.getHeaderIndex(xlsxFile);
		List<Map<Integer, Object>> rowMapList = ExcelReader.getActiveRow(xlsxFile, false);

		FuncGenEnum genEnum = FuncGenEnum.NEW; // NEW EXISTED
		outputFileName = "20241225_" + genEnum.getMessage() + ".yaml";

		String target = genEnum.getPrefix();
		String ignoreTarget = genEnum.getIgnorePrefix();

		for (Map<Integer, Object> rowData : rowMapList) {
			MVPScopeVO vo = null;
			try {
				vo = new MVPScopeVO(headerMap, rowData);
			} catch (Exception e) {
				continue;
			}

			if (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(target)) {
				logger.info("{}", vo);
				String apiName = vo.getApiName();
				if (!apiNameToApiDataMapFromExcel.containsKey(apiName)) {
					apiNameToApiDataMapFromExcel.put(apiName, new ArrayList<MVPScopeVO>());
				}
				apiNameToApiDataMapFromExcel.get(apiName).add(vo);
			} else if (vo.getApiType() != null && vo.getApiType().toLowerCase().startsWith(ignoreTarget)) {
				logger.debug("ignored {}", vo);
			} else {
				logger.info("error " + vo);
			}

			String desc = DomainTypeUtil.getDescriptionByDomainValue(vo.getBusinessName(), vo.getDomainValue());
			if (StringUtils.isNotBlank(desc)) {
				// logger.info("changing desc {}", desc);
				vo.setDescription(desc);
			}
			if (StringUtils.isNotBlank(vo.getDescription())) {
				int index = desc.indexOf(DomainTypeUtil.ALLOWED_VALUES);
				if (index >= 0) {
					vo.setDataType("number");
				}
			}
		}
		doJob();
	}

	@Override
	public Logger getLogger() {
		return logger;
	}
}
