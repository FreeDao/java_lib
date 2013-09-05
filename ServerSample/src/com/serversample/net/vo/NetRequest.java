package com.serversample.net.vo;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 * 
 */
public class NetRequest implements Serializable {
	private static final long serialVersionUID = -6917198793693261767L;

	private String payload;

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @param payload
	 *            the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

}
