/**
 * 
 */
package com.vincent.coretest.exception;

/**
 * @author Vincent YW Tseng 2017年12月26日
 */
public class DqsException extends RuntimeException {

	private static final long serialVersionUID = 6131346870713049656L;

	private Throwable source;

	public DqsException(String message) {
		super(message);
	}

	public DqsException(Throwable source) {
		super(source.getMessage());
		this.source = source;
	}

	public Throwable getSource() {
		return source;
	}

	public DqsError getCode() {
		return DqsError.INTERNAL_SERVER_ERROR;
	}
}
