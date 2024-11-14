package com.vincent.coretest.vo;

import java.util.ArrayList;
import java.util.List;

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
		if (type != null) {
			if ("VARCHAR2".equals(type)) {
				sb.append("maxLength: ").append(value);
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "DTType [name=" + name + ", type=" + type + ", value=" + value + "]";
	}

}
