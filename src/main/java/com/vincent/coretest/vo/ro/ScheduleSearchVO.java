package com.vincent.coretest.vo.ro;

import java.util.List;

import com.vincent.coretest.vo.ro.DeviceScheduleVO.Schedule;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * com.vincent.coretest.vo.ro ScheduleSearchVO.java
 *
 * @author vincenttseng 2019年7月26日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public class ScheduleSearchVO extends SuccessResult {
	private String uuid;
	private String user_id;
	private String action;
	private String dev_id;
	private String enable;

	private List<Schedule> schedules;

	public ScheduleSearchVO(String uuid, String user_id, String action, String dev_id) {
		this.uuid = uuid;
		this.user_id = user_id;
		this.action = action;
		this.dev_id = dev_id;
	}

	public ScheduleSearchVO(String uuid, String user_id, String action, String dev_id, String enable) {
		this.uuid = uuid;
		this.user_id = user_id;
		this.action = action;
		this.dev_id = dev_id;
		this.enable = enable;
	}

}
