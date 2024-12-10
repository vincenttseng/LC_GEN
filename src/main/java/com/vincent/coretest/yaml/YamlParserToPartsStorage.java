package com.vincent.coretest.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.vincent.coretest.yaml.vo.HttpMethodDetailsVO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class YamlParserToPartsStorage {
	protected static final Logger logger = LoggerFactory.getLogger(YamlParserToPartsStorage.class);

	List<HttpMethodDetailsVO> theHttpMethodDetailsVOList = new ArrayList<HttpMethodDetailsVO>();
	ComponentsDataStorage componentsStorage = null;
	List<String> rootEleList = new ArrayList<String>();

	public void readYamlAndMergeExcelThenGenNew(String targetPath) throws FileNotFoundException {
		logger.info("readYamlAndMergeExcelThenGenNew");

		Yaml yaml = new Yaml();
		InputStream inputStream = new FileInputStream(targetPath);
		logger.info("{} {}", targetPath, inputStream);

		HashMap yamlMap = yaml.load(inputStream);
		rootEleList.addAll(yamlMap.keySet());

		HashMap pathsMap = (HashMap) yamlMap.get("paths");

		@SuppressWarnings("unchecked")
		Set<String> pathSet = pathsMap.keySet();
		for (String path : pathSet) {
			LinkedHashMap methodDefMap = (LinkedHashMap) pathsMap.get(path);
			HttpMethodDetailsVO vo = HttpMethodDetailsVO.of(path, methodDefMap);
			if (vo != null) {
				theHttpMethodDetailsVOList.add(vo);
			}
		}

		componentsStorage = new ComponentsDataStorage();
		HashMap componentsMap = (HashMap) yamlMap.get("components");
		componentsStorage.parseComponents(componentsMap);
	}

	public void showData() {
		for (HttpMethodDetailsVO vo : theHttpMethodDetailsVOList) {
			logger.info("vo path:{} method:{}", vo.getPath(), vo.getHttpMethod());
			logger.info("req {}", vo.getReqRef());
			logger.info("resp {}", vo.getResponseList());
		}
	}
}
