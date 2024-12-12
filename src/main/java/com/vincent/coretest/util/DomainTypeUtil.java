package com.vincent.coretest.util;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.DomainMapUtil;

public class DomainTypeUtil {
	protected static final Logger logger = LoggerFactory.getLogger(DomainTypeUtil.class);
	static Map<String, String> domainToTypesMap = null;
	static {
		try {
			domainToTypesMap = DomainMapUtil.readDomainNameToTypesMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final String ALLOWED_VALUES = "Allowed values";
	
	public static String getDescriptionByDomainValue(String businessName, String domainValue) {
		String description = null;
		if (domainValue != null) {
			String domainValueArr[] = domainValue.split("[\\r\\n]+");

			if (domainValueArr.length > 1) {
				StringBuilder sb = new StringBuilder();
				sb.append(businessName);
				sb.append(" ").append(ALLOWED_VALUES).append(":");
				boolean existed = false;
				for (int i = 1; i < domainValueArr.length; i++) {
					if (existed) {
						sb.append(",");
					}
					sb.append(domainValueArr[i]).append(":").append(domainValueArr[i]);
					existed = true;
				}
				sb.append(";");
				description = sb.toString();
			} else if (domainValueArr.length == 1) {
				String key = domainValueArr[0].trim();
				if (domainToTypesMap.containsKey(key)) {
					StringBuilder sb = new StringBuilder();
					sb.append(businessName);
					sb.append(" ").append(ALLOWED_VALUES).append(":");
					sb.append(domainToTypesMap.get(key));
					description = sb.toString();
				}
			}
		}
		return description;
	}
}
