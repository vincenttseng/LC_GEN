package com.vincent.coretest.reader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ColumnDefVo;

public class PathUtil {

	public static String getPathParamString(String fullPath) {
		int index = fullPath.indexOf("?");
		String path = fullPath;
		if (index >= 0) {
			path = fullPath.substring(0, index);
		}

		Set<String> existed = new HashSet<String>();

		List<String> tokens = TextUtil.splitBrackets(path);
		StringBuilder sb = new StringBuilder();
		for (String token : tokens) {
			if (!existed.contains(token)) {
				existed.add(token);
				sb.append("        - name: ").append(token).append("\n");
				sb.append("          in: path").append("\n");
				sb.append("          required: true").append("\n");
				sb.append("          schema:").append("\n");
				sb.append("            type: string").append("\n");
				String desc = TextUtil.phaseWordToDesc(token);
				sb.append("          description: ").append(StringUtils.trim(desc));
				sb.append("\n");
			}
		}

		if (index >= 0) {
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

				if (!existed.contains(token)) {
					existed.add(token);
					sb.append("        - name: ").append(token).append("\n");
					sb.append("          in: query").append("\n");
					sb.append("          required: false").append("\n");
					sb.append("          schema:").append("\n");
					sb.append("            type: ").append(convertedType).append("\n");
					String desc = TextUtil.phaseWordToDesc(token);
					sb.append("          description: ").append(StringUtils.trim(desc));
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	public static String showQueryFromExcel(List<ColumnDefVo> list) {
		StringBuilder sb = new StringBuilder();

		if (list != null) {
			Set<String> existed = new HashSet<String>();
			for (ColumnDefVo vo : list) {
				String type = vo.getType();
				String token = vo.getName();
				if (StringUtils.isNotBlank(token) && !existed.contains(token)) {
					existed.add(token);
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
		}
		return sb.toString();
	}
}
