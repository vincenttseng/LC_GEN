package com.vincent.coretest;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.DomainMapUtil;

public class DomainMapUtilTest {
	protected final Logger logger = LoggerFactory.getLogger(DomainMapUtilTest.class);
	
	@Test
	public void testTypeToDomainMap() throws IOException {
		Map<String, String> map = DomainMapUtil.readFileTypeToDomainMap();
		for (String key : map.keySet()) {
			logger.info(key + "=>" + map.get(key));
		}
	}
}
