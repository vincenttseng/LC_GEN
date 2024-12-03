package com.vincent.coretest.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vincent.coretest.util.TextUtil;

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
	public int maxLength;
	public String format;

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
		if (name.endsWith("*")) {
			name = name.substring(0, name.length() - 1);
			vo.required = true;
		}
		vo.name = name;

		int i = 1;
		while (i < result.length) {
			if (result[i].trim().length() == 0) {
				i++;
			} else {
				if ("Date".equals(result[i])) {
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
		swaggerTypeMap.put("Number", "number");
		swaggerTypeMap.put("Date", "string");
	}

	public static String convertType(String type) {
		if (swaggerTypeMap.containsKey(type)) {
			return swaggerTypeMap.get(type);
		} else {
			return type;
		}
	}

	public static ColumnDefVo toColumnDefVo(MVPScopeVO cellVO) {
		ColumnDefVo vo = new ColumnDefVo();

		vo.setRequired(cellVO.isRequired());

		vo.setName(cellVO.getBusinessName());
		String type = cellVO.getDataType();

		if (type != null) {
			if (type.toLowerCase().equals("date")) {
				vo.setType("string"); // format: date
				vo.setDate(true);
				vo.setFormat("date");
			} else if (type.toLowerCase().startsWith("varchar") || type.toLowerCase().startsWith("string")) {
				List<Integer> data = TextUtil.getNumberFromParentheses(type);
				if (data != null && data.size() > 0) {
					vo.setMaxLength(data.get(0));
				}
				vo.setType("string");
				vo.setFormat("string");
			} else if (type.toLowerCase().startsWith("numeric") || type.toLowerCase().startsWith("number")) {
				List<Integer> data = TextUtil.getNumberFromParentheses(type);
				if (data == null) {
					vo.setType("integer"); // format: int32
					vo.setFormat("int32");
				} else {
					if (data.size() == 1) {
						vo.setType("integer"); // format: int32
						vo.setMaxLength(data.get(0));
						vo.setFormat("int32");
					} else if (data.size() >= 2) {
						vo.setType("number");
						vo.setFormat("float");
						vo.setMaxLength(data.get(0));
					} else {
						vo.setType("integer"); // format: int32
						vo.setFormat("int32");
					}
				}
			} else {
				vo.setType("string");
				vo.setFormat("string");
			}
		} else {
			vo.setType("string");
			vo.setFormat("string");
		}

		vo.setDesc(cellVO.getDescription());
		vo.setRequired(cellVO.isRequired());

		return vo;
	}

}
