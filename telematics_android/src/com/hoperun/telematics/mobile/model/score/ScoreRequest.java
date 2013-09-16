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
package com.hoperun.telematics.mobile.model.score;

import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ScoreRequest extends BaseRequest {
	private int score;

	/**
	 * Creates a new instance of ScoreRequest.
	 */
	public ScoreRequest() {
		super();
	}

	/**
	 * Creates a new instance of ScoreRequest.
	 */
	public ScoreRequest(int score) {
		super();
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
