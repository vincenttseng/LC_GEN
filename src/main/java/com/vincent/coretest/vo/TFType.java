package com.vincent.coretest.vo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFType {
	public String name;
	public String type;
	public String value;

	public static TFType toTFType(String value) {
		if (value == null) {
			return null;
		}

		String[] tokens = value.split(" ");

		List<String> availTokenList = new ArrayList<String>();
		for (String token : tokens) {
			token = token.trim();

			if (token != null && token.length() > 0) {
				availTokenList.add(token);
			}
		}

		if (availTokenList.size() == 3) {
			return new TFType(availTokenList.get(0), availTokenList.get(1), availTokenList.get(2));
		} else {
			return null;
		}
	}

	public TFType(String name, String type, String value) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String toYamlTypeString() {
		StringBuilder sb = new StringBuilder();
		if (name != null) {
			if(name.startsWith("d_Alpha")) {
				sb.append("maxLength: ").append(value);
			} else if("d_Address".equals(name)) {
				sb.append("maxLength: ").append(value);
			} 
		} 
		return sb.toString();
	}

	@Override
	public String toString() {
		return "DTType [name=" + name + ", type=" + type + ", value=" + value + "]";
	}

	private static final String tfDatFilename = ".\\src\\test\\input\\TF.dat";

	public static final Map<String, TFType> readDTMap() throws IOException {
		File tfDataFile = new File(tfDatFilename);

		if (tfDataFile == null || !tfDataFile.exists()) {
			return new HashMap<String, TFType>();
		}

		Map<String, TFType> map = new HashMap<String, TFType>();

		FileReader in = null;
		try {
			in = new FileReader(tfDataFile);
			BufferedReader br = new BufferedReader(in);

			String line;
			while ((line = br.readLine()) != null) {
				TFType type = TFType.toTFType(line);
				if (type != null) {
					map.put(type.name, type);
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
