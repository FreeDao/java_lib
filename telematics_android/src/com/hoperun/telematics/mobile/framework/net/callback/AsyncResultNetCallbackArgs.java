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
public class AsyncResultNetCallbackArgs extends AsyncNetCallbackArgs {

	/**
	 * Creates a new instance of AsyncResultNetCallbackArgs.
	 */
	public AsyncResultNetCallbackArgs(String payload, ECallbackStatus status, String errorMessage, int interval,
			String resId, ENetworkServiceType serviceType) {
		super(payload, status, errorMessage, interval, resId, serviceType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8027672315592209572L;


}
