package com.vincent.coretest.util;

import java.util.List;
import java.util.Map;

import com.vincent.coretest.vo.ColumnDefVo;
import com.vincent.coretest.vo.TFType;

public class RefDefDetailUtil {

	public static void printRefDefDetail(String refKey, Map<String, List<ColumnDefVo>> objectMap,
			Map<String, String> domainToTypesMap, Map<String, String> typeToDomainMap, Map<String, TFType> tfTypeMap) {
		for (String key : objectMap.keySet()) {
			List<ColumnDefVo> list = objectMap.get(key);
			if (list.size() > 0) {
				String subkey = refKey + "-" + key;
				System.out.println("    " + subkey + ":");

				int cnt = 0;
				StringBuilder sba = new StringBuilder();
				for (ColumnDefVo vo : list) {
					if (vo.required) {
						cnt++;
						sba.append("        - " + vo.name + "\n");
					}
				}
				if (cnt > 0) {
					System.out.println("      required:");
					System.out.print(sba.toString());
				}

				System.out.println("      type: object");
				System.out.println("      properties:");

				for (ColumnDefVo vo : list) {
					String objKey = vo.name;
					System.out.println("        " + objKey + ":");
					System.out.println("          type: " + vo.type);

					String domainName = typeToDomainMap.get(vo.name);

					if (!domainToTypesMap.containsKey(domainName)) {
						System.out.println("          description: " + vo.desc);
					} else {
						String showName = vo.name.toUpperCase();
						StringBuilder sb = new StringBuilder();
						sb.append("description: '" + showName + " Allowed values:");
						sb.append(domainToTypesMap.get(domainName));
						sb.append(";'");

						System.out.println("          " + sb.toString());
					}

					TFType tfType = tfTypeMap.get(domainName);
					if (tfType != null) {
						String yamlExtraString = tfType.toYamlTypeString();
						if (yamlExtraString != null && yamlExtraString.length() > 0) {
							System.out.println("          " + yamlExtraString);
						}
					}
					if (vo.isDate) {
						System.out.println("          format: date");
					}
				}
			}
		}
	}
}
