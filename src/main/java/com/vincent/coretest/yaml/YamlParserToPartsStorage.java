package com.vincent.coretest.yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.vincent.coretest.util.RefDefDetailUtil;
import com.vincent.coretest.vo.CVSVO;
import com.vincent.coretest.yaml.vo.HttpMethodDetailsVO;
import com.vincent.coretest.yaml.vo.ParamVO;
import com.vincent.coretest.yaml.vo.SchemaVO;

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
			List<HttpMethodDetailsVO> list = HttpMethodDetailsVO.of(path, methodDefMap);

			if (list != null) {
				theHttpMethodDetailsVOList.addAll(list);
			}
		}

		componentsStorage = new ComponentsDataStorage();
		HashMap componentsMap = (HashMap) yamlMap.get("components");
		componentsStorage.parseComponents(componentsMap);
	}

	public void showData() {
		for (HttpMethodDetailsVO vo : theHttpMethodDetailsVOList) {

			logger.info("vo path:{} method:{}", vo.getPath(), vo.getHttpMethod());
			logger.info("param {}", vo.getParams());
			logger.info("req {}", vo.getReqRef());
			logger.info("resp {} {}", vo.getRespType(), vo.getResponseList());
		}
	}

	public void printCVS() {
		logger.info(CVSVO.headerLine());
		printParams();
		printRequest();
		printResponse();
	}

	private void printParams() {
		for (HttpMethodDetailsVO vo : theHttpMethodDetailsVOList) {
			CVSVO csv = new CVSVO();
			csv.setApiName(vo.getDescription());
			csv.setApiNode(vo.getApiNode());
			csv.setFullUrl(vo.getFullPathUrl());
			csv.setMethod(StringUtils.upperCase(vo.getHttpMethod()));

			List<ParamVO> paramList = vo.getParams();
			for (ParamVO paramVO : paramList) {
				if ("query".equalsIgnoreCase(paramVO.getIn())) {
					csv.setFieldName(paramVO.getName());
					if ("date".equalsIgnoreCase(paramVO.getSchema().getFormat())) {
						csv.setDataType("Date");
					} else {
						SchemaVO schemaVO = paramVO.getSchema();
						StringBuilder sb = new StringBuilder();
						sb.append(SchemaVO.convertTypeExceptDate(paramVO.getSchema().getType()));
						if (schemaVO.getMaxLength() > 0) {
							sb.append("(").append(schemaVO.getMaxLength()).append(")");
						}
						csv.setDataType(sb.toString());
					}
					csv.setFieldDesc(paramVO.getDescription());
					csv.setMo("M");
					csv.setReqResp("Path");
					logger.info("cvs {}", csv.toCsvLine());
				}
			}
		}
	}

	private void printRequest() {
		for (HttpMethodDetailsVO vo : theHttpMethodDetailsVOList) {
			CVSVO csv = new CVSVO();
			csv.setApiName(vo.getDescription());
			csv.setApiNode(vo.getApiNode());
			csv.setFullUrl(vo.getFullPathUrl());
			csv.setMethod(StringUtils.upperCase(vo.getHttpMethod()));

			String reqRef = vo.getReqRef();
			String componentName = RefDefDetailUtil.getComponentNameFromReqRef(reqRef);
			if (StringUtils.isNotBlank(componentName)) {
				logger.info("reqRef {} comp {}", reqRef, componentName);
			}

		}
	}

	private void printResponse() {

	}

}
