package com.vincent.coretest.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.vincent.coretest.vo.ColumnDefVo;
import com.vincent.coretest.vo.TFType;

public class RefDefDetailUtil {

	public static void printRefDefDetail(String outputFileName, String refKey, Map<String, List<ColumnDefVo>> objectMap, Map<String, String> domainToTypesMap,
			Map<String, String> typeToDomainMap, Map<String, TFType> tfTypeMap) {
		for (String key : objectMap.keySet()) {
			List<ColumnDefVo> list = objectMap.get(key);
			if (list.size() > 0) {
				String subkey = refKey + "-" + key;
				System.out.println("    " + subkey + ":");

				int cnt = 0;
				StringBuilder sba = new StringBuilder();
				Set<String> requiredSet = new HashSet<String>();
				for (ColumnDefVo vo : list) {
					if (vo.required && requiredSet.contains(vo.name) == false) {
						cnt++;
						requiredSet.add(vo.name);
						sba.append("        - " + vo.name + "\n");
					}
				}
				if (cnt > 0) {
					FileOutputUtil.printOut(outputFileName, "      required:");
					FileOutputUtil.printOut(outputFileName, sba.toString());
				}

				FileOutputUtil.printOut(outputFileName, "      type: object");
				FileOutputUtil.printOut(outputFileName, "      properties:");

				for (ColumnDefVo vo : list) {
					String objKey = vo.name;
					FileOutputUtil.printOut(outputFileName, "        " + objKey + ":");
					FileOutputUtil.printOut(outputFileName, "          type: " + vo.type);

					String domainName = typeToDomainMap.get(vo.name);

					if (!domainToTypesMap.containsKey(domainName)) {
						System.out.println("          description: " + vo.desc);
					} else {
						String showName = vo.name.toUpperCase();
						StringBuilder sb = new StringBuilder();
						sb.append("description: '" + showName + " Allowed values:");
						sb.append(domainToTypesMap.get(domainName));
						sb.append(";'");

						FileOutputUtil.printOut(outputFileName, "          " + sb.toString());
					}

					TFType tfType = tfTypeMap.get(domainName);
					if (tfType != null) {
						String yamlExtraString = tfType.toYamlTypeString();
						if (yamlExtraString != null && yamlExtraString.length() > 0) {
							FileOutputUtil.printOut(outputFileName, "          " + yamlExtraString);
						}
					}
					if (vo.isDate) {
						FileOutputUtil.printOut(outputFileName, "          format: date");
					}
				}
			}
		}
	}

	/**
	 * #/components/schemas/BALMSControllerAMEBTCrDrUpdSrvcAPIInObjec
	 * 
	 * @param value
	 * @return ambt0303modinternalacntsrvcapi-v2-inobject-v10013406
	 */
	public static String getComponentNameFromCompSchema(String value) {
		if (StringUtils.isNotBlank(value) && StringUtils.startsWith(value, "#/components/schemas")) {
			int offset = value.lastIndexOf("/");
			if (offset > 0) {
				return value.substring(offset + 1);
			}
		}
		return null;
	}
}
