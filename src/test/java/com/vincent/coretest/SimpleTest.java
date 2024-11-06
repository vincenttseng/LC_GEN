package com.vincent.coretest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.util.TextUtil;

public class SimpleTest {
	protected final Logger logger = LoggerFactory.getLogger(SimpleTest.class);

	String domainFilename = ".\\src\\test\\input\\domainRef.csv";
	String typeToDomainFilename = ".\\src\\test\\input\\typeToDomain.txt";

	@Test
	public void testDomain() throws IOException {
		File domainFile = new File(domainFilename);
		logger.info("domainFile " + domainFile.getAbsolutePath() + " existed: " + domainFile.exists());

		Map<String, String> map = TextUtil.readFileToDomainMap(domainFile);

		String name = "XXXXX";

		for (String key : map.keySet()) {
			StringBuilder sb = new StringBuilder();
			sb.append("description: '" + key + " Allowed values:");
			sb.append(map.get(key));
			sb.append(";'");

			// System.out.println(sb.toString());
		}

		name = "LC-POS-TYPE";
		String type = "d_PosTyp";

		StringBuilder sb = new StringBuilder();
		sb.append("description: '" + name + " Allowed values:");
		sb.append(map.get(type));
		sb.append(";'");

		System.out.println(sb.toString());
	}

	/**
	 * 
	 * - name: lc-pos-type in: path required: true schema: type: string description:
	 * LC POS TYPE
	 */
	@Test
	public void getGetParamAndQuery() {
		String path = "/letter-of-credit/{lc-pos-type}/{lc-reference}/{event-leg-id}/draft-details";

		List<String> tokens = TextUtil.splitBrackets(path);
		for (String token : tokens) {
			StringBuilder sb = new StringBuilder();
			sb.append("        - name: ").append(token).append("\n");
			sb.append("          in: path").append("\n");
			sb.append("          required: true").append("\n");
			sb.append("          schema:").append("\n");
			sb.append("            type: string").append("\n");
			String desc = TextUtil.phaseWordToDesc(token);
			sb.append("          description: ").append(desc);
			System.out.println(sb.toString());
		}

	}
	
	@Test
	public void testTypeToDomainMap() throws IOException {
		File typeToDomainFile = new File(typeToDomainFilename);
		Map<String, String> map = TextUtil.readFileTypeToDomainMap(typeToDomainFile);
		for(String key:map.keySet()) {
			logger.info(key + "=>" + map.get(key));
		}
	}
}
