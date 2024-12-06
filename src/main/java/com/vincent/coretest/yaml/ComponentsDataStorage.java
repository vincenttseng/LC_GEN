package com.vincent.coretest.yaml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.FuncGenEnum;
import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.vo.MVPScopeVO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class ComponentsDataStorage {
	protected static final Logger logger = LoggerFactory.getLogger(ComponentsDataStorage.class);

	Set<String> baseRefSet = new ListOrderedSet<String>();
	Set<String> scanedRefSet = new ListOrderedSet<String>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void parseComponents(final HashMap componentsMap) {
		Map map = new HashMap();
		Object obj = null;

		LinkedHashMap contentMap = new LinkedHashMap();
		if (componentsMap != null && componentsMap.containsKey("schemas")) {
			obj = componentsMap.get("schemas");
			if (obj instanceof LinkedHashMap) {
				contentMap = (LinkedHashMap) obj;
				contentMap.forEach((key, value) -> {
					logger.debug("key {} {} value {} {}", key.getClass(), key, value.getClass(), value);
					baseRefSet.add(key.toString());
					if (value instanceof LinkedHashMap) {
						LinkedHashMap dataMap = (LinkedHashMap) value;
						findAllRef(1, scanedRefSet, dataMap);
					}
				});
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void findAllRef(int deep, Set<String> scanedRefSet, LinkedHashMap map) {
		if (map != null) {
			map.forEach((key, value) -> {
				logger.debug("    key {} value {} {}", key, value.getClass(), value);
				if (value instanceof LinkedHashMap) {
					findAllRef(deep + 1, scanedRefSet, (LinkedHashMap) value);
				}

				if ("$ref".equals(key)) {
					scanedRefSet.add(value.toString());
					logger.debug("===>find ref " + value + " " + scanedRefSet.size());
				}
			});
		}
	}

	public void showData() {
		logger.info("**********");
		logger.info("based ********** " + baseRefSet.size());
		baseRefSet.stream().forEach(ref -> {
			logger.info(" based =>" + ref + "<=");
		});
		logger.info("scaned ********** " + scanedRefSet.size());
		scanedRefSet.stream().forEach(ref -> {
			logger.info(" scaned =>" + ref + "<=");
		});
	}
}
