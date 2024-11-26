package com.vincent.coretest.reader;

import java.util.List;

import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ColumnDefVo;

public class PathUtil {
	
	public static String getPathParamString(String fullPath) {
		int index = fullPath.indexOf("?");
		String path = fullPath;
		if(index >= 0) {
			path = fullPath.substring(0, index);
		}
		List<String> tokens = TextUtil.splitBrackets(path);
		StringBuilder sb = new StringBuilder();
		for (String token : tokens) {
			sb.append("        - name: ").append(token).append("\n");
			sb.append("          in: path").append("\n");
			sb.append("          required: true").append("\n");
			sb.append("          schema:").append("\n");
			sb.append("            type: string").append("\n");
			String desc = TextUtil.phaseWordToDesc(token);
			sb.append("          description: ").append(desc);
			sb.append("\n");
		}
		
		if(index >= 0) {
			path = fullPath.substring(index + 1);
			path = path.replaceAll("-", "_");
	
			String[] queryArray = path.split("&");
			for (String query : queryArray) {
				String[] values = query.split("=");
				String token = values[0];
				String type = values[1];
				type = type.replace("<", "");
				type = type.replace(">", "");
				String convertedType = ColumnDefVo.convertType(type);
	
				sb.append("        - name: ").append(token).append("\n");
				sb.append("          in: query").append("\n");
				sb.append("          required: false").append("\n");
				sb.append("          schema:").append("\n");
				sb.append("            type: ").append(convertedType).append("\n");
				String desc = TextUtil.phaseWordToDesc(token);
				sb.append("          description: ").append(desc);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
