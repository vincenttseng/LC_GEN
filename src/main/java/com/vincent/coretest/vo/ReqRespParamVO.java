package com.vincent.coretest.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReqRespParamVO {
	protected static final Logger logger = LoggerFactory.getLogger(ReqRespParamVO.class);

	// input part
	// for each key as variable node, list constains fields of the node
	Map<String, List<ColumnDefVo>> mapOfInputObjectList = new HashMap<String, List<ColumnDefVo>>();
	Map<String, List<ColumnDefVo>> mapOfInputObjectArrayList = new HashMap<String, List<ColumnDefVo>>();

	// response part
	// for each key as variable node, list constains fields of the node
	Map<String, List<ColumnDefVo>> mapOfRespObjectList = new HashMap<String, List<ColumnDefVo>>();
	Map<String, List<ColumnDefVo>> mapOfRespObjectArrayList = new HashMap<String, List<ColumnDefVo>>();

	public void showContent() {
		logger.info("   ===== mapOfInputObjectList");
		showMap(mapOfInputObjectList);

		logger.info("   ===== mapOfInputObjectArrayList");
		showMap(mapOfInputObjectArrayList);

		logger.info("   ===== mapOfRespObjectList");
		showMap(mapOfRespObjectList);
		
		logger.info("   ===== mapOfRespObjectArrayList");
		showMap(mapOfRespObjectArrayList);
	}

	public static void showMap(Map<String, List<ColumnDefVo>> columnDefVoMap) {
		if (columnDefVoMap != null) {
			Set<String> keys = columnDefVoMap.keySet();
			for (String key : keys) {
				logger.info("        " + key);
				List<ColumnDefVo> list = columnDefVoMap.get(key);
				for (ColumnDefVo clmnDefVo : list) {
					logger.info("             " + clmnDefVo);
				}
			}
		}
	}
}
