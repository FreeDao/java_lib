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
package com.hoperun.telematics.mobile.model.buddy;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class FriendRequest extends BaseRequest {
	private String accountId;

	public FriendRequest() {
		super();
	}

	/**
	 * Creates a new instance of FriendRequest.
	 */
	public FriendRequest(String accountId) {
		super();
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String parseObjecttoJson(FriendRequest friendRequest) {
		Gson gson = new Gson();
		String str = gson.toJson(friendRequest);
		return str;
	}

}
