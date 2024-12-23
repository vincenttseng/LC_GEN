package com.vincent.coretest.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjCastUtil {
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertObjToArryaList(Object obj, Class<T> object) {
		if (obj != null || obj instanceof List) {
			return (List<T>) obj;
		}
		return new ArrayList<T>();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertObjToArryaList(Object obj) {
		if (obj != null || obj instanceof List) {
			return (List<T>) obj;
		}
		return new ArrayList<T>();
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> convertObjToSet(Object obj, Class<T> object) {
		Set<T> set = new HashSet<>();
		if (obj != null || obj instanceof Collection) {
			Collection<T> list = (Collection<T>) obj;
			set.addAll(list);
		}
		return new HashSet<T>();
	}

	@SuppressWarnings("unchecked")
	public static <T, V> Map<T, V> convertObjToMap(Object obj) {
		Map<T, V> map = new LinkedHashMap<T, V>();
		if (obj != null || obj instanceof Map) {
			map = (LinkedHashMap<T, V>) obj;
		}
		return map;
	}
}
