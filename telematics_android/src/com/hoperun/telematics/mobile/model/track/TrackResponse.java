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
package com.hoperun.telematics.mobile.model.track;

import java.util.List;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class TrackResponse extends BaseResponse{
	private List<TrackInfo> trackList;
	
	/**
	 * Creates a new instance of TrackResponse.
	 */
	public TrackResponse() {
		super();
	}
	
	public TrackResponse(List<TrackInfo> trackList){
		this.trackList=trackList;
	}
	
	public List<TrackInfo> getTrackList() {
		return trackList;
	}

	public void setTrackList(List<TrackInfo> trackList) {
		this.trackList = trackList;
	}

	public String toJsonStr() {
		Gson gson = new Gson();
		return gson.toJson(this, TrackResponse.class);
	}

}
