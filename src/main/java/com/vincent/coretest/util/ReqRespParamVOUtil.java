package com.vincent.coretest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.vo.ColumnDefVo;
import com.vincent.coretest.vo.MVPScopeVO;
import com.vincent.coretest.vo.ReqRespParamVO;

public class ReqRespParamVOUtil {
	protected static final Logger logger = LoggerFactory.getLogger(ReqRespParamVOUtil.class);

	public static ReqRespParamVO getReqRespParamVO(String key, List<MVPScopeVO> attributes) {
		ReqRespParamVO vo = new ReqRespParamVO();

		Map<String, List<ColumnDefVo>> mapForInputObjectList = new HashMap<String, List<ColumnDefVo>>();
		Map<String, List<ColumnDefVo>> mapForInputObjectArrayList = new HashMap<String, List<ColumnDefVo>>();

		Map<String, List<ColumnDefVo>> mapForRespObjectList = new HashMap<String, List<ColumnDefVo>>();
		Map<String, List<ColumnDefVo>> mapForRespObjectArrayList = new HashMap<String, List<ColumnDefVo>>();

		Map<String, List<ColumnDefVo>> mapForQueryList = new HashMap<String, List<ColumnDefVo>>();

		Map<String, List<ColumnDefVo>> theMap = null;

		if (attributes != null) {
			for (MVPScopeVO cellVO : attributes) {
				boolean isArray = cellVO.isArray();
				// check isArray
				if (GenTypeEnum.REQUEST == cellVO.getDirection()) {
					if (isArray) {
						theMap = mapForInputObjectArrayList;
					} else {
						theMap = mapForInputObjectList;
					}
				} else if (GenTypeEnum.RESPONSE == cellVO.getDirection()) {
					if (isArray) {
						theMap = mapForRespObjectArrayList;
					} else {
						theMap = mapForRespObjectList;
					}
				} else if (GenTypeEnum.Query == cellVO.getDirection() || GenTypeEnum.Path == cellVO.getDirection()) {
					theMap = mapForQueryList;
				}

				String nodeName = null;
				try {
					nodeName = cellVO.getGroupName();
					if (theMap == null) {
						logger.info("error " + cellVO);
						continue;
					}
					if (!theMap.containsKey(nodeName)) {
						theMap.put(nodeName, new ArrayList<ColumnDefVo>());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				ColumnDefVo columnDefVo = ColumnDefVo.toColumnDefVo(cellVO);
				theMap.get(nodeName).add(columnDefVo);
			}
		}

		vo.setMapOfInputObjectList(mapForInputObjectList);
		vo.setMapOfInputObjectArrayList(mapForInputObjectArrayList);

		vo.setMapOfRespObjectList(mapForRespObjectList);
		vo.setMapOfRespObjectArrayList(mapForRespObjectArrayList);

		List<ColumnDefVo> queryList = new ArrayList<ColumnDefVo>();

		mapForQueryList.forEach((k, v) -> {
			queryList.addAll(v);

		});
		vo.setQueryObjectList(queryList);

		return vo;
	}
}
