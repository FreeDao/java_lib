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

import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ScoreResponse extends BaseResponse {
	private List<Score> scoreList;

	/**
	 * Creates a new instance of ScoreResponse.
	 */
	public ScoreResponse() {
		super();
	}

	/**
	 * Creates a new instance of ScoreResponse.
	 */
	public ScoreResponse(List<Score> scoreList) {
		super();
		this.scoreList = scoreList;
	}

	public List<Score> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<Score> scoreList) {
		this.scoreList = scoreList;
	}

	public static ScoreResponse parseJsonToObject(String json) {
		ScoreResponse scoreResponse = null;
		try {
			Gson gson = new Gson();
			scoreResponse = gson.fromJson(json, ScoreResponse.class);
		} catch (Exception e) {
			Log.e("ScoreResponse", e.getMessage(), e);
		}
		return scoreResponse;
	}
}
