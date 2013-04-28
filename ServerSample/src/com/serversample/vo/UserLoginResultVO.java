package com.serversample.vo;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *
 */
public class UserLoginResultVO implements Serializable {
	private static final long serialVersionUID = -3544534817304102852L;
	
	private boolean success;
	private String errorMsg;

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg
	 *            the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
