/**
 * 
 */
package com.vincent.coretest.vo.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@JsonInclude(Include.NON_EMPTY)	
@EqualsAndHashCode
public abstract class ResultStatus {
	protected Integer code;
	private String status;

	public ResultStatus(String status) {
		this.status = status;
	}
}