package com.vincent.coretest.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)	
public class ScheduleEventVO {

	private String start_time;

	private String end_time;

	/** on/off */
	private String action;

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "ScheduleEventVO [start_time=" + start_time + ", end_time="
				+ end_time + ", action=" + action + "]";
	}

}