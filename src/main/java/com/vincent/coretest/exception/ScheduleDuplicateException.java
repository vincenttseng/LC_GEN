/**
 * 
 */
package com.vincent.coretest.exception;

/**
 * com.vincent.coretest.exception ScheduleDuplicateException.java
 *
 * @author vincenttseng
 * 2019年8月2日 
 */
public class ScheduleDuplicateException extends DqsException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5926268167139678613L;

	public ScheduleDuplicateException(String message) {
		super(message);
	}

	@Override
	public DqsError getCode() {
		return DqsError.SCHEDULE_DUPLICATE_DEVICE_CONTROL;
	}
}