/*****************************************************************************
 *
 *                      HOPERUN PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to HopeRun
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from HopeRun.
 *
 *            Copyright (c) 2012 by HopeRun.  All rights reserved.
 *
 *****************************************************************************/
package com.hoperun.telematics.mobile.framework.net.callback;

import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback.ECallbackStatus;

/**
 * 
 * @author hu_wg
 * 
 */
public class AsyncNetCallbackArgs extends DefaultNetCallbackArgs {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5991476850567870286L;
	private int interval;
	private String resId;
	private ENetworkServiceType serviceType;

	public AsyncNetCallbackArgs(String payload, ECallbackStatus status, String errorMessage, int interval,
			String resId, ENetworkServiceType serviceType) {
		super(payload, status, errorMessage);
		this.interval = interval;
		this.resId = resId;
		this.serviceType = serviceType;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public ENetworkServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ENetworkServiceType serviceType) {
		this.serviceType = serviceType;
	}
}
