/**
 * 
 */
package com.vincent.coretest.exception;

/**
 * com.vincent.coretest.exception CommandSynTaxErrException.java
 *
 * @author vincenttseng 2019年7月30日
 */
public class CommandSynTaxErrException extends DqsException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3577785571528534653L;

	public CommandSynTaxErrException(String message) {
		super(message);
	}

	@Override
	public DqsError getCode() {
		return DqsError.COMMAND_SYNTAX_ERROR;
	}
}