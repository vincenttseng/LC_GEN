package com.vincent.coretest.yaml.vo;

import java.util.ArrayList;
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
public class ParamVO {
	String name;
	String in;
	String description;
	boolean required;
	SchemaVO schema;

	@SuppressWarnings("unchecked")
	public static ParamVO of(LinkedHashMap map) {
		if (map != null) {
			ParamVO vo = new ParamVO();
			map.forEach((key, value) -> {
				if ("name".equals(key)) {
					vo.name = TextUtil.objectToString(value, "");
				} else if ("in".equals(key)) {
					vo.in = TextUtil.objectToString(value, "");
				} else if ("description".equals(key)) {
					vo.description = TextUtil.objectToString(value, "");
				} else if ("required".equals(key)) {
					vo.required = Boolean.parseBoolean(value != null ? value.toString() : "false");
				} else if ("schema".equals(key)) {
					if (value instanceof LinkedHashMap) {
						LinkedHashMap schemaMap = (LinkedHashMap) value;
						vo.schema = SchemaVO.of(schemaMap);
					}
				}
			});
			return vo;
		}
		return null;
	}
}
