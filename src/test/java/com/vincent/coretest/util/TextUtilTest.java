package com.vincent.coretest.util;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TextUtilTest {

	protected final Logger logger = LoggerFactory.getLogger(TextUtilTest.class);

	@Test
	public void testGetNumberFromParentheses() {
		List<Integer> result = TextUtil.getNumberFromParentheses("afd(1, 2 , 3)");
		logger.info("=> " + result);
	}
	
	@Test
	public void testGetNumberFromParentheses1() {
		List<Integer> result = TextUtil.getNumberFromParentheses("Number (21,7)");
		logger.info("=> " + result);
	}
	
	@Test
	public void testGetNumberFromParentheses2() {
		List<Integer> result = TextUtil.getNumberFromParentheses("Varchar2(3)");
		logger.info("=> " + result);
	}
	
	@Test
	public void testSemiComment() {
		String tmp = "    patch: #LC75 path";
		logger.info(TextUtil.getHttpMethodFromYamlMethodComments(tmp));
		logger.info(TextUtil.getCommentsFromYamlMethodComments(tmp));
		
	}
}
