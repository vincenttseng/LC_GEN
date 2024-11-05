/**
 * 
 */
package com.vincent.coretest.exception;

/**
 * com.vincent.coretest.exception DeviceNotFoundException.java
 *
 * @author vincenttseng 2019年7月29日
 */
public class DeviceNotFoundException extends DqsException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7402901326257200445L;

	public DeviceNotFoundException(String message) {
		super(message);
	}

	@Override
	public DqsError getCode() {
		return DqsError.DEVICE_NOT_FOUND;
	}
}