package com.hoperun.telematics.mobile.framework.net;

public class NetworkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6400283854536954818L;

	public NetworkException() {
		super();
	}

	public NetworkException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NetworkException(String detailMessage) {
		super(detailMessage);
	}

	public NetworkException(Throwable throwable) {
		super(throwable);
	}

}
