package com.vincent.coretest.yaml.vo;

import java.util.LinkedHashMap;
import java.util.List;

import com.vincent.coretest.util.TextUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SchemaVO {
	int maxLength = 0;
	int minLength = 0;
	String type = "";
	String format = "";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static SchemaVO of(LinkedHashMap map) {
		if (map != null) {
			SchemaVO vo = new SchemaVO();
			map.forEach((key, value) -> {
				if ("maxLength".equals(key)) {
					vo.maxLength = TextUtil.objectToInt(value, 0);
				} else if ("minLength".equals(key)) {
					vo.minLength = TextUtil.objectToInt(value, 0);
				} else if ("type".equals(key)) {
					vo.type = TextUtil.objectToString(value, "string");
				} else if ("format".equals(key)) {
					vo.format = TextUtil.objectToString(value, null);
				} else {
					System.out.println("ERROR　unknow type " + key + " " + value);
				}
			});
			return vo;
		}
		return null;
	}
}
