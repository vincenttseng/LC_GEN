package com.vincent.coretest;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.HeaderUtil;

public class HeaderUtilTest {
	protected final Logger logger = LoggerFactory.getLogger(HeaderUtilTest.class);

	@Test
	public void testMethodHeader() {
		String methodHeader = HeaderUtil.getMethodHeadersString();
		logger.info("{}", methodHeader);
	}


}
