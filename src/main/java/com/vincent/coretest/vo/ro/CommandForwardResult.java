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
 * com.vincent.coretest.vo.ro CommandForwardResult.java
 *
 * @author vincenttseng 2019年9月3日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public class CommandForwardResult extends SuccessResult {

	private String status = "ok";

	private String apsystem;
	private String user_id;
	private String dev_id;
	private String action;
	private String job_data;
	private String result;
}
