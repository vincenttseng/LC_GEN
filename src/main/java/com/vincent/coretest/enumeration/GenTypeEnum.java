package com.vincent.coretest.enumeration;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum GenTypeEnum {

	//@formatter:off
	REQUEST("request"), 
	RESPONSE("response");
	//@formatter:on

	private String message;

	private GenTypeEnum(String message) {
		this.message = message;
	}
	

	public static GenTypeEnum of(String message) {
		for (GenTypeEnum entity : values()) {
			if (entity.getMessage().toLowerCase().equals(message != null?message.trim().toLowerCase():"")) {
				return entity;
			}
		}
		return null;
	}

}
