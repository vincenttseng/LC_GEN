package com.vincent.coretest.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefDefDetailUtilTest {
	protected final Logger logger = LoggerFactory.getLogger(RefDefDetailUtilTest.class);

	@Test
	public void testGetComponentNameFromReqRef0() {
		String reqRef = "#/components/schemas/BALMSControllerFABT0302ModBSPLSrvcAPIInObject";
		String componentName = RefDefDetailUtil.getComponentNameFromReqRef(reqRef);
		logger.info("{}=>{}<=", reqRef, componentName);
	}

	@Test
	public void testGetComponentNameFromReqRef1() {
		String reqRef = "#/components/schemas/BALMSControllerAMEBTCrDrUpdSrvcAPIInObject";
		String componentName = RefDefDetailUtil.getComponentNameFromReqRef(reqRef);
		logger.info("{}=>{}<=", reqRef, componentName);
	}
}
