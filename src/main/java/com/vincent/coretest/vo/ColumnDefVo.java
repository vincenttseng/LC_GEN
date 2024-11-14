package com.vincent.coretest.vo;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ColumnDefVo {
	public String name;
	public String type;
	public String desc;
	public boolean required = false;
	public boolean isDate = false;

	/**
	 * due-date-for-sight Date - Due Date for Sigh
	 * 
	 * @param input
	 */
	public static ColumnDefVo convert(String input) {
		if (input == null) {
			return null;
		}
		ColumnDefVo vo = new ColumnDefVo();

		String tmp = new String(input);
		String[] result = tmp.split(" ");

		String name = result[0];
		if(name.endsWith("*")) {
			name = name.substring(0, name.length() - 1);
			vo.required = true;
		}
		vo.name = name;
		
		int i = 1;
		while (i < result.length) {
			if (result[i].trim().length() == 0) {
				i++;
			} else {
				if("Date".equals(result[i])) {
					vo.isDate = true;
				}
				vo.type = convertType(result[i]);
				break;
			}
		}

		int index = tmp.indexOf(result[i]);
		tmp = tmp.substring(index + result[i].length());

		index = tmp.indexOf("-");
		if (index > 0) {
			vo.desc = tmp.substring(index + 1).trim();
		}

		return vo;
	}

	static final Map<String, String> swaggerTypeMap = new HashMap<String, String>();
	static {
		swaggerTypeMap.put("String", "string");
		swaggerTypeMap.put("Numeric", "number");
		swaggerTypeMap.put("Date", "string");
	}

	public static String convertType(String type) {
		if(swaggerTypeMap.containsKey(type)) {
			return swaggerTypeMap.get(type);
		} else {
			return type;
		}
	}

}
