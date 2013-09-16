package com.hoperun.telematics.mobile.framework.net.vo;

import java.io.Serializable;

public class NetRequest extends BasePOJO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6917198793693261767L;
	private String UUID;
	private String token;
	private long timestamp;
	private String payload;
	private String resId;

	public NetRequest() {

	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

}
