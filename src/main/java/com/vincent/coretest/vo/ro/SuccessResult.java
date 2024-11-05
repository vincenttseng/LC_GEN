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
@EqualsAndHashCode(callSuper = false)
public class SuccessResult extends ResultStatus {

	public static final String STATUS = "ok";

	public static final String STATUS_WARNING = "warning";

	public SuccessResult() {
		super(STATUS);
		this.code = 0;
	}
}