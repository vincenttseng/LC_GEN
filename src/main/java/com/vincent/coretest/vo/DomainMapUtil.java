package com.vincent.coretest.vo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainMapUtil {
	private static final String typeToDomainFilename = ".\\src\\test\\input\\typeToDomain.txt";
	public static Map<String, String> readFileTypeToDomainMap() throws IOException {
		File typeToDomainFile = new File(typeToDomainFilename);
		
		if (typeToDomainFile == null || !typeToDomainFile.exists()) {
			return new HashMap<String, String>();
		}

		Map<String, String> map = new HashMap<String, String>();

		FileReader in = null;
		try {
			in = new FileReader(typeToDomainFile);
			BufferedReader br = new BufferedReader(in);

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
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return map;
	}

}
