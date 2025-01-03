package com.vincent.coretest.yaml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	Map<String, Object> basedRefMap = new HashMap<String, Object>();
	Map<String, Object> scanedRefMap = new HashMap<String, Object>();

	@SuppressWarnings({ "rawtypes" })
	public void parseComponents(final HashMap componentsMap) {
		Map map = new HashMap();
		Object obj = null;

		LinkedHashMap contentMap = new LinkedHashMap();
		if (componentsMap != null && componentsMap.containsKey("schemas")) {
			obj = componentsMap.get("schemas");
			if (obj instanceof LinkedHashMap) {
				contentMap = (LinkedHashMap) obj;

				handleSchemas(contentMap);
			}
		}
	}

	private void handleSchemas(LinkedHashMap contentMap) {
		contentMap.forEach((key, value) -> {
			logger.debug("key {} value {}", key, value);
			baseRefSet.add(key.toString());
			basedRefMap.put(key.toString(), value);
			if (value instanceof LinkedHashMap) {
				LinkedHashMap dataMap = (LinkedHashMap) value;
				findAllRef(1, scanedRefSet, dataMap);
			}
		});
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
					scanedRefMap.put(value.toString(), value);
					logger.debug("===>find ref " + value + " " + scanedRefSet.size());
				}
			});
		}
	}

	public void showData() {
		logger.info("**********");
//		logger.info("based ********** " + baseRefSet.size());
//		baseRefSet.stream().forEach(ref -> {
//			logger.info(" based =>" + ref + "<=");
//		});
//		logger.info("scaned ********** " + scanedRefSet.size());
//		scanedRefSet.stream().forEach(ref -> {
//			logger.info(" scaned =>" + ref + "<=");
//		});

		logger.info("basedRefMap ********** " + scanedRefSet.size());
		basedRefMap.forEach((key, value) -> {
			logger.info("    key {} value {}", key, value);
		});

		logger.info("scanedRefMap ********** " + scanedRefSet.size());
		scanedRefMap.forEach((key, value) -> {
			logger.info("    key {} value {}", key, value);
		});
	}

	@SuppressWarnings("rawtypes")
	public LinkedHashMap getComponentNodeByComponentName(String componentName) {
		if (basedRefMap.containsKey(componentName)) {
			Object object = basedRefMap.get(componentName);
			if (object != null && object instanceof LinkedHashMap) {
				return (LinkedHashMap) object;
			}
			logger.info("obj {} {}", object.getClass(), object);
		}
		return null;
	}
}
