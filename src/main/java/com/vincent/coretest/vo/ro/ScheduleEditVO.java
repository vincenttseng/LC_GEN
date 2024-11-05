package com.vincent.coretest.vo.ro;

import com.vincent.coretest.vo.ro.DeviceScheduleVO.Schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonInclude(Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public class ScheduleEditVO extends SuccessResult {

	private String status = "ok";

	private Schedule schedule;
}