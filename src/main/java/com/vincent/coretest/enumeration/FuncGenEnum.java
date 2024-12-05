package com.vincent.coretest.enumeration;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum FuncGenEnum {
	//@formatter:off
	NEW("n"), 
	EXISTED("e");
	//@formatter:on

	private String message;

	private FuncGenEnum(String message) {
		this.message = message;
	}

	public static FuncGenEnum of(String message) {
		for (FuncGenEnum entity : values()) {
			if (entity.getMessage().toLowerCase().equals(message != null ? message.trim().toLowerCase() : "")) {
				return entity;
			}
		}
		return null;
	}

	public String getPrefix() {
		if (this == FuncGenEnum.NEW) {
			return this.getMessage();
		} else {
			return EXISTED.getMessage();
		}
	}

	public String getIgnorePrefix() {
		if (this == FuncGenEnum.NEW) {
			return EXISTED.getMessage();
		} else {
			return this.getMessage();
		}
	}
}
