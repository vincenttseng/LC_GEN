package com.vincent.coretest.vo;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@ToString
public class MVPScopeVO {
	int rowIndex = 0;
	String type = "";
	String apiType = "";
	List<Object> data = null;
	String apiName = "";

	public MVPScopeVO(List<Object> source) {
		data = source;
		if (source.size() >= 4) {
			Object obj = source.get(0);
			if (obj instanceof Integer) {
				rowIndex = ((Integer) obj).intValue();
			}
			type = source.get(1).toString();
			apiType = source.get(2).toString();
			apiName = source.get(3).toString();
		}
	}
}
