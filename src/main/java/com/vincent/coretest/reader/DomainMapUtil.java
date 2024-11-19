package com.vincent.coretest.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class DomainMapUtil {
	private static final String TYPE_TO_DOMAIN_FILENAME = ".\\src\\test\\input\\typeToDomain.txt";
	private static final String DOMAINNAME_TO_DBTYPES_FILE = ".\\src\\test\\input\\domainRef.csv";

	public static Map<String, String> readFileTypeToDomainMap() throws IOException {
		return readFileTypeToDomainMap(TYPE_TO_DOMAIN_FILENAME);
	}

	public static Map<String, String> readFileTypeToDomainMap(String fileName) throws IOException {
		File typeToDomainFile = new File(fileName);

		if (typeToDomainFile == null || !typeToDomainFile.exists()) {
			return new HashMap<String, String>();
		}

		Map<String, String> map = new HashMap<String, String>();

		FileReader in = null;
		BufferedReader br = null;
		try {
			in = new FileReader(typeToDomainFile);
			br = new BufferedReader(in);

			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				List<String> list = new ArrayList<String>();
				for (String token : tokens) {
					if (token.trim().length() > 0) {
						list.add(token.trim());
					}
				}
				if (list.size() >= 2) {
					map.put(list.get(0).trim(), list.get(1).trim());
				}
			}
		} catch (IOException e) {

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return map;
	}

	public static Map<String, String> readDomainNameToTypesMap() throws IOException {
		File file = new File(DOMAINNAME_TO_DBTYPES_FILE);
		return readDomainNameToTypesMap(file);
	}
	
	public static Map<String, String> readDomainNameToTypesMap(File domainFile) throws IOException {
		FileReader in = new FileReader(domainFile);
		BufferedReader br = new BufferedReader(in);

		Map<String, String> map = new HashMap<String, String>();

		String line;
		while ((line = br.readLine()) != null) {
			String[] tokens = null;
			if (line != null && line.length() > 0) {
				// System.out.print(line + " =>");
				tokens = line.split(",");

				int length = tokens != null ? tokens.length : 0;
				// System.out.println(length);

				if (length == 3) {
					String key = StringUtils.trim(tokens[0]);
					String value1 = StringUtils.trim(tokens[1]);
					String value2 = StringUtils.trim(tokens[2]);
					if (!map.containsKey(key)) {
						map.put(key, value1 + ":" + value2);
					} else {
						String value = map.get(key);
						map.put(key, value + "," + value1 + ":" + value1);
					}
				}
			}
		}
		in.close();

		return map;
	}
}
