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
package com.hoperun.telematics.mobile.model.poi;

import java.util.List;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class PoiResponse extends BaseResponse {

	private int curIndex;
	private int totalSize;
	private int type;
	private List<Poi> poiList;

	/**
	 * Creates a new instance of PoiResponse.
	 */
	public PoiResponse(int curIndex, int totalSize, int type, List<Poi> poiList) {
		super();
		this.curIndex = curIndex;
		this.totalSize = totalSize;
		this.type = type;
		this.poiList = poiList;
	}

	/**
	 * Creates a new instance of PoiResponse.
	 */
	public PoiResponse() {
	}

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the poiList
	 */
	public List<Poi> getPoiList() {
		return poiList;
	}

	/**
	 * @param poiList
	 *            the poiList to set
	 */
	public void setPoiList(List<Poi> poiList) {
		this.poiList = poiList;
	}

	public String toJsonStr() {
		Gson gson = new Gson();
		return gson.toJson(this, PoiResponse.class);
	}

}
