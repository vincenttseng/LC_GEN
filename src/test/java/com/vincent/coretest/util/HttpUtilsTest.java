package com.vincent.coretest.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtilsTest {
	protected final Logger logger = LoggerFactory.getLogger(HttpUtilsTest.class);

	@Test
	public void testshowURIWithoutQuery() {
		String path = "/v2/letter-of-credit/{lc-pos-type}/{lc-reference}/{event-leg-id}/bank-details?bank-type=<Numeric>&page-num=<Numeric>&page-size=<Numeric>";
		String uri = HttpUtils.showURIWithoutQuery(path);
		logger.info("{}", uri);
	}

	@Test
	public void testgetPathRemovingifHttpMethody0() {
		String path = "GET /v2/afsdfasadf/adsf/adsf";
		path = HttpUtils.getPathRemovingifHttpMethod(path);
		logger.info("=>{}<=", path);
	}
	
	@Test
	public void testgetPathRemovingifHttpMethody1() {
		String path = " /v2/afsdfasadf/adsf/adsf";
		path = HttpUtils.getPathRemovingifHttpMethod(path);
		logger.info("=>{}<=", path);
	}
	
	@Test
	public void testgetPathRemovingifHttpMethody2() {
		String path = "POST/v2/afsdfasadf/adsf/adsf";
		path = HttpUtils.getPathRemovingifHttpMethod(path);
		logger.info("=>{}<=", path);
	}
}