package com.vincent.coretest.enumeration;

public enum BinaryStatus {
	ENABLE(true, "1"),
	DISABLE(false, "0");

	private Boolean status;

	private String code;

	private BinaryStatus(boolean status, String code) {
		this.status = status;
		this.code = code;
	}

	public static BinaryStatus forCode(String code) {
		for (BinaryStatus binaryStatus : values()) {
			if (binaryStatus.getCode().equals(code)) {
				return binaryStatus;
			}
		}
		return DISABLE;
	}

	public static BinaryStatus forStatus(Boolean status) {
		for (BinaryStatus binaryStatus : values()) {
			if (binaryStatus.getStatus().equals(status)) {
				return binaryStatus;
			}
		}
		return DISABLE;
	}

	public Boolean getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public BinaryStatus invert() {
		return status == true ?
				BinaryStatus.DISABLE : BinaryStatus.ENABLE;
	}
}