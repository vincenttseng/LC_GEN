/**
 * 
 */
package com.vincent.coretest.exception;

/**
 * com.vincent.coretest.exception CommandExecutionException.java
 *
 * @author vincenttseng 2019年7月30日
 */
public class CommandExecutionException extends DqsException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5926268167139678613L;

	public CommandExecutionException(String message) {
		super(message);
	}

	@Override
	public DqsError getCode() {
		return DqsError.COMMAND_EXECUTION_ERROR;
	}
}