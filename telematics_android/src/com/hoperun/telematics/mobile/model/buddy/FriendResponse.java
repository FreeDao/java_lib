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

import java.util.List;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class FriendResponse extends BaseResponse {
	private List<Friend> friendList;

	/**
	 * Creates a new instance of FriendResponse.
	 */
	public FriendResponse() {
		super();
	}

	public FriendResponse(List<Friend> list) {
		this.friendList = list;
	}

	public List<Friend> getFriendList() {
		return friendList;
	}

}
