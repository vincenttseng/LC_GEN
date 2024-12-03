package com.vincent.coretest.vo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.GenTypeEnum;

public class ColumnDefVoTest {
	protected final Logger logger = LoggerFactory.getLogger(ColumnDefVoTest.class);

	@Test
	public void testToColumnDefVo() {
		MVPScopeVO vo = new MVPScopeVO();
		vo.setApiName("ABC");
		vo.setBusinessName("twct-transaction-currency-");
		vo.setDataType("Varchar2(3)");

		ColumnDefVo aColumnDefVo = ColumnDefVo.toColumnDefVo(vo);
		logger.info("{}", aColumnDefVo);
	}

	@Test
	public void testToColumnDefVo1() {
		MVPScopeVO vo = new MVPScopeVO();
		vo.setApiName("NEW ILC Receptio");
		vo.setApiNode("ilc-reception");
		vo.setPath("POST/v2/trade-reception");
		vo.setDirection(GenTypeEnum.RESPONSE);
		vo.setGroupName("ilc-reception");
		vo.setBusinessName("twct-transaction-currency-");
		vo.setDataType("Numeric");
		
		ColumnDefVo aColumnDefVo = ColumnDefVo.toColumnDefVo(vo);
		logger.info("{}", aColumnDefVo);
	}

}
