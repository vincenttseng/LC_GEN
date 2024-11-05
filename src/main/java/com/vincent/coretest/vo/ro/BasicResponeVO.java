/**
 * 
 */
package com.vincent.coretest.vo.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * com.vincent.coretest.vo.ro BasicResponeVO.java
 *
 * @author vincenttseng 2019年8月29日
 */
@Data
@JsonInclude(Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public class BasicResponeVO extends SuccessResult {
	private String message;

	private String otherInfo;
	
	public BasicResponeVO(String message) {
		super();
		this.message = message;
	}

}
