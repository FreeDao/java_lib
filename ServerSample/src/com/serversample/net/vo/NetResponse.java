package com.serversample.net.vo;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *
 */
public class NetResponse implements Serializable {
	private static final long serialVersionUID = 9185321563375726720L;

	private String resp;

	/**
	 * @return the resp
	 */
	public String getResp() {
		return resp;
	}

	/**
	 * @param resp the resp to set
	 */
	public void setResp(String resp) {
		this.resp = resp;
	}
}
