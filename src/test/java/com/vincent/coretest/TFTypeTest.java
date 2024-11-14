package com.vincent.coretest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.TFTypeUtil;
import com.vincent.coretest.vo.TFType;

public class TFTypeTest {
	protected final Logger logger = LoggerFactory.getLogger(TFTypeTest.class);

	@Test
	public void testReadDTType() throws IOException {
		Map<String, TFType> map = TFTypeUtil.readDTMap();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			TFType type = map.get(key);
			
			if (type.toYamlTypeString() != null && type.toYamlTypeString().length() > 0) {
				logger.info("key " + key + " => " + type);
				logger.info("   yaml " + key + " => " + type.toYamlTypeString());
			}
		}
	}
}
