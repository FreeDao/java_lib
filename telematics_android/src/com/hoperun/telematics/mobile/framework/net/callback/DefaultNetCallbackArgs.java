package com.hoperun.telematics.mobile.framework.net.callback;

import com.hoperun.telematics.mobile.framework.net.callback.INetCallback.ECallbackStatus;

public class DefaultNetCallbackArgs implements INetCallbackArgs {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5931739406127553114L;

	private String payload;
	private ECallbackStatus status;
	private String errorMessage;

	public DefaultNetCallbackArgs() {
		super();
	}

	public DefaultNetCallbackArgs(String payload, ECallbackStatus status, String errorMessage) {
		super();
		this.payload = payload;
		this.status = status;
		this.errorMessage = errorMessage;
	}

	@Override
	public String getPayload() {
		// TODO Auto-generated method stub
		return payload;
	}

	@Override
	public ECallbackStatus getStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
