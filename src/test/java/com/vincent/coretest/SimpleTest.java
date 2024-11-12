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
import com.vincent.coretest.vo.ro.ColumnDefVo;

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

		name = "aaaa";
		String type = "d_WorkflowStatusp";

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
		String fullPath = "/export-lc-post-shipment-crystallization-or-bill-recovery/{crystallization-reference}/{sequence-id}";

		int index = fullPath.indexOf("?");
		String path = fullPath;
		if(index >= 0) {
			path = fullPath.substring(0, index);
		}
		logger.info("path " + path);
		List<String> tokens = TextUtil.splitBrackets(path);
		logger.info("tokens " + tokens);
		StringBuilder sb = new StringBuilder();
		for (String token : tokens) {
			sb.append("        - name: ").append(token).append("\n");
			sb.append("          in: path").append("\n");
			sb.append("          required: true").append("\n");
			sb.append("          schema:").append("\n");
			sb.append("            type: string").append("\n");
			String desc = TextUtil.phaseWordToDesc(token);
			sb.append("          description: ").append(desc);
			sb.append("\n");
		}
		
		if(index >= 0) {
			path = fullPath.substring(index + 1);
			path = path.replaceAll("-", "_");
			logger.info("path " + path);
	
			String[] queryArray = path.split("&");
			for (String query : queryArray) {
				String[] values = query.split("=");
				String token = values[0];
				String type = values[1];
				type = type.replace("<", "");
				type = type.replace(">", "");
				String convertedType = ColumnDefVo.convertType(type);
	
				sb.append("        - name: ").append(token).append("\n");
				sb.append("          in: query").append("\n");
				sb.append("          required: false").append("\n");
				sb.append("          schema:").append("\n");
				sb.append("            type: ").append(convertedType).append("\n");
				String desc = TextUtil.phaseWordToDesc(token);
				sb.append("          description: ").append(desc);
				sb.append("\n");
			}
		}
		System.out.println(sb.toString());
	}

	@Test
	public void testTypeToDomainMap() throws IOException {
		Map<String, String> map = TextUtil.readFileTypeToDomainMap();
		for (String key : map.keySet()) {
			logger.info(key + "=>" + map.get(key));
		}
	}
	
	@Test
	public void testNameToLowerCaseAndDash() {
		String name = "Draft Details";

		logger.info("=>" + TextUtil.nameToLowerCaseAndDash(name));
	}
}
