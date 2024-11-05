/**
 * 
 */
package com.vincent.coretest.exception;

/**
 * com.vincent.coretest.exception KafkaSendingException.java
 *
 * @author vincenttseng
 * 2019年8月28日 
 */
public class KafkaSendingException extends DqsException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3577785571528534653L;

	public KafkaSendingException(String message) {
		super(message);
	}

	@Override
	public DqsError getCode() {
		return DqsError.TARGET_SERVER_ERROR;
	}
}