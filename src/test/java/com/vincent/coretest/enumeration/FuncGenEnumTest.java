package com.vincent.coretest.enumeration;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuncGenEnumTest {
	protected final Logger logger = LoggerFactory.getLogger(FuncGenEnumTest.class);

	@Test
	public void test0() throws IOException {
		FuncGenEnum genEnum = FuncGenEnum.NEW;
		logger.info("{} prefix {}", genEnum, genEnum.getPrefix());

		logger.info("{} ignore {}", genEnum, genEnum.getIgnorePrefix());
	}

	@Test
	public void test1() throws IOException {
		FuncGenEnum genEnum = FuncGenEnum.EXISTED;
		logger.info("{} prefix {}", genEnum, genEnum.getPrefix());

		logger.info("{} ignore {}", genEnum, genEnum.getIgnorePrefix());
	}
}
