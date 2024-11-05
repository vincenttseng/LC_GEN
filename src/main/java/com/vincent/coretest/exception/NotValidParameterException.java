/**
 * 
 */
package com.vincent.coretest.exception;

/**
 * @author Vincent YW Tseng 2017年12月26日
 */
public class NotValidParameterException extends DqsException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8551220196485073413L;

	public NotValidParameterException(String message) {
		super(message);
	}

	@Override
	public DqsError getCode() {
		return DqsError.INVALID_PARAMETER;
	}
}
