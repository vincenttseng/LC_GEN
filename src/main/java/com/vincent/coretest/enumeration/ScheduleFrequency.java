package com.vincent.coretest.enumeration;

import org.apache.commons.lang3.StringUtils;

public enum ScheduleFrequency {
	ONCE("once"),
	DAILY("daily"),
	WEEKLY("weekly");

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private ScheduleFrequency(String name) {
		this.name = name;
	}

	public static ScheduleFrequency of(String name) {
		if (StringUtils.isNotBlank(name)) {
			for (ScheduleFrequency entity : values()) {
				if (entity.getName().equals(name.toLowerCase())) {
					return entity;
				}
			}
		}
		return null;
	}

	public static boolean contains(String name) {
		return of(name) != null ? true : false;
	}

}
