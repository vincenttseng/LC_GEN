package com.vincent.coretest.vo.ro;

import java.util.Date;
import java.util.List;

import com.vincent.coretest.vo.ScheduleEventVO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonInclude(Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public class DeviceScheduleVO extends SuccessResult {

	private String status = "ok";

	private int schedules_count;

	private List<Schedule> schedules;

	@Data
	@JsonInclude(Include.NON_EMPTY)
	public static class Schedule {
		@JsonIgnore
		private boolean createMode;

		private String uuid;

		private String user_id;

		private String trigger_name;

		private String job_detail_name;

		private String dev_id;

		private String action;

		private String sched_freq;

		private String sched_date;

		private String sched_week;

		private String sched_time;

		private String sched_desc;

		private String timezone;

		private String next_fire_time;

		private String next_fire_time_str;

		private String cron_expression;

		private Long last_updated;

		private Date last_updated_date;

		private String job_data;

		/** yes/no */
		private String enable;

		private List<ScheduleEventVO> events;

		public List<ScheduleEventVO> getEvents() {
			return events;
		}

		public void setEvents(List<ScheduleEventVO> events) {
			this.events = events;
		}
	}
}
