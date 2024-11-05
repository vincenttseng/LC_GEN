/**
 * 
 */
package com.vincent.coretest.vo;

import com.vincent.coretest.vo.ro.ResultStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;

@JsonInclude(Include.NON_EMPTY)	
@EqualsAndHashCode(callSuper=false)
public class FailedResult extends ResultStatus {

	public static final String STATUS = "failed";

	private FailedDetail err;

	public FailedResult(Integer code, String message) {
		super(STATUS);
		this.code = code;
		err = new FailedDetail(code, message);
	}

	public FailedDetail getErr() {
		return err;
	}

	public void setErr(FailedDetail err) {
		this.err = err;
	}

	@Override
	public String toString() {
		return "FailedResult [err=" + err + "]";
	}

	@JsonInclude(Include.NON_EMPTY)	
	public static class FailedDetail {

		private Integer code;

		private String msg;

		FailedDetail(Integer code, String message) {
			this.code = code;
			this.msg = message;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		@Override
		public String toString() {
			return "FailedDetail [code=" + code + ", msg=" + msg + "]";
		}

	}

}
