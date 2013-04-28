package com.serversample.net.exception;

/**
 * 
 * @author Administrator
 *
 */
public class CustomerException extends Exception{
	private static final long serialVersionUID = 8374779189437241262L;

	/**
	 * 
	 * @param errorMsg
	 */
	public CustomerException(String errorMsg) {
		super(errorMsg);
	}
}
