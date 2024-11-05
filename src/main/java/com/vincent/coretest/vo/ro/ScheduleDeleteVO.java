/**
 * 
 */
package com.vincent.coretest.vo.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * com.vincent.coretest.vo ScheduleDeleteVO.java
 *
 * @author vincenttseng 2019年7月25日
 */
@Data
@JsonInclude(Include.NON_EMPTY)	
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScheduleDeleteVO extends SuccessResult {
	private String user_id;

	private String uuid; 
	
	private String trigger_name;
	
	private boolean deleted;
}
