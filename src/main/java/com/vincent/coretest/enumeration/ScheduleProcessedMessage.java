package com.vincent.coretest.enumeration;

public enum ScheduleProcessedMessage {
	OPERATION_SUCCESSFULLY_COMPLETED(0, 1, "operation successfully completed"),	
	
	ADD_SUCCESS(1, 1, "successfully add schedule"),
	PAUSE_SUCCESS(2, 1, "successfully pause schedule"),
	UPDATE_SUCCESS(3, 1, "successfully update schedule"),
	DELETE_SUCCESS(4, 1, "successfully delete schedule"),
	RETRIEVE_SUCCESS(5, 1, "successfully retrieve schedule"),
	
	ADD_FAILURE(6, -1, "fail to add schedule"),	
	PAUSE_FAILURE(7, -1, "fail to pause scheduler"),
	UPDATE_FAILURE(8, -1, "fail to update scheduler"),
	DELETE_FAILURE(9, -1, "fail to delete scheduler"),
	RETRIEVE_FAILURE(10, 1, "fail to retrieve schedule"),
	
	DUPLICATED_SCHEDULE(11, -1, "schedule duplicated error"),
	FETCH_TRIGGERS_ERROR(12, -1, "fail to fetch triggers"),
	EXPIRED_TRIGGERS_ERROR(13, -1, "the trigger time is in the past and will never fire"),
	INVALID_DATE_FORMAT(14, -1, "invalid date format"),
	TRANSLATE_CRON_EXPRESSION_ERROR(15, -1, "translate cron expression error"),
	TARGET_TRIGGER_NOT_FOUND(16, -1, "target trigger not found"),
	PAGE_INDEX_OUT_OF_BOUND(17, -1, "page index out of bound"),
	LIMIT_VALUE_OUT_OF_BOUND(18, -1, "limit value out of bound"),
		
	UNKNOWN_ERROR(99, -1, "unknown error");
	
	private int code;
	
	private int type; //1: success; -1: failure
	
	private String message;
	
	private ScheduleProcessedMessage(int code, int type, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static ScheduleProcessedMessage of(int code) {
		for (ScheduleProcessedMessage entity : values()) {
			if (entity.getCode()==code) {
				return entity;
			}
		}
		return null;
	}

	public static boolean contains(int code) {
		return of(code) != null ? true : false;
	}

}
