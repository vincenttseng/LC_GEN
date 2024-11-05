package com.vincent.coretest.exception;

public enum DqsError {
	// @formatter:off
	OPERATION_SUCCESSFULLY_COMPLETED(0, "Operation Successfully Completed"),
	INTERNAL_SERVER_ERROR(7000, "Internal Server Error"),
	
	INVALID_PARAMETER(7100, "Invalid Parameter"),
	INVALID_OPERATION(7101, "Invalid Operation"),
	UNAUTHORIZED(7102, "Unauthorized"),
	UNVERIFIED(7103, "User Not Verified"),
	FORBIDDEN(7104, "Forbidden"),
	UNKNOWN_DEVICE_TYPE(7105, "Unknown Device Type"),
	DEVICE_NOT_FOUND(7106, "Device Not Found"),
	DEVICE_ALREADY_OCCUPIED(7107, "Device Already Occupied"),
	SCHEDULE_DUPLICATE_DEVICE_CONTROL(7108, "Schedule Duplicate for Device or control type"),
	
	DATABASE_UNEXPECT_ERROR(7200, "Database Unexpected Error"),
	INCOMPLETE_DATA(7202, "Incomplete Data"),
	INACCURATE_DATA(7203, "Inaccurate Data"),
//	VIOLATION_OF_UNIQUE_KEY_CONSTRAINT("7204", "Violation of Unique Key Constraint"),  //IF3
	
	TARGET_SERVER_ERROR(7300, "Target Server Error"),
	TARGET_SERVER_CONNECTION_FAILED(7301, "Target Server Connection Failed"),
	COMMAND_SYNTAX_ERROR(7302, "Command Syntax Error"),
	COMMAND_RESPONSE_TIMEOUT(7303, "Command Response Timeout"),
	COMMAND_EXECUTION_ERROR(7304, "Command Execution Error");
	// @formatter:on

	private final Integer code;
	private final String msg;

	private DqsError(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}