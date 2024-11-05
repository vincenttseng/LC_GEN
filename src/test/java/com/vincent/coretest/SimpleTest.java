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

public class SimpleTest {
	protected final Logger logger = LoggerFactory.getLogger(SimpleTest.class);

	String domainFilename = ".\\src\\test\\resources\\domainRef.csv";

	@Test
	public void testDomain() throws IOException {
		logger.info("test");
		File file = new File(domainFilename);
		logger.info("file " + file.getAbsolutePath() + " existed: " + file.exists());

		FileReader in = new FileReader(file);
		BufferedReader br = new BufferedReader(in);

		Map<String, String> map = new HashMap<String, String>();

		String line;
		while ((line = br.readLine()) != null) {
			String[] tokens = null;
			if (line != null && line.length() > 0) {
				//System.out.print(line + " =>");
				tokens = line.split(",");
				
				int length = tokens != null ? tokens.length : 0;
				//System.out.println(length);
				
				if(length == 3) {
					String key = tokens[0];
					if(!map.containsKey(key)) {
						map.put(key, tokens[1] + ":" + tokens[2]);
					} else {
						String value = map.get(key);
						map.put(key, value + "," + tokens[1] + ":" + tokens[2]);
					}
				}
			}
		}
		in.close();
		
		
		String name = "XXXXX"; 
		
		for(String key:map.keySet()) {
			StringBuilder sb = new StringBuilder();
			sb.append("description: '" + key + " Allowed values:");
			sb.append(map.get(key));
			sb.append(";'");
			
			//System.out.println(sb.toString());
		}
		
		name = "LC-POS-TYPE";
		String type = "d_PosTyp";
		
		StringBuilder sb = new StringBuilder();
		sb.append("description: '" + name + " Allowed values:");
		sb.append(map.get(type));
		sb.append(";'");
		
		System.out.println(sb.toString());
	}
}
