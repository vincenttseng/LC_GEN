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
	String apiName = "";
	String apiNode = "";
	String path = "";
	
	String httpMethod = null;
	String reqPath = null;
	
	List<Object> data = null;

	public MVPScopeVO(List<Object> source) {
		data = source;
		if (source.size() >= 14) {
			Object obj = source.get(0);
			if (obj instanceof Integer) {
				rowIndex = ((Integer) obj).intValue();
			}
			type = source.get(1).toString();
			apiType = source.get(2).toString();
			apiName = source.get(3).toString();
			apiNode = source.get(4).toString();
			
			path = source.get(13).toString();
			int offset = path.indexOf("/");
			httpMethod = path.substring(0, offset);
			reqPath = path.substring(offset);
		}
	}

	public String getDataDetail() {
		int index = 0;
		StringBuilder sb = new StringBuilder();

		boolean found = false;
		for (Object obj : data) {
			if (found) {
				sb.append(",");
			}
			sb.append(index++).append(obj.toString());
			found = true;
		}
		return sb.toString();
	}
}
