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
package com.hoperun.telematics.mobile.model.location;

import java.util.Map;

import com.amap.mapapi.core.GeoPoint;
import com.hoperun.telematics.mobile.framework.net.helper.UtilHelper;

/**
 * 
 * @author HopeRun
 * 
 */
public class GeoLocation {

	private double lat;
	private double lng;
	private String name;
	private String address;
	private Map<Object, Object> extInfo;
	private String friendId;
	private String imageId;
	private String note;;
	private String time;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Map<Object, Object> getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(Map<Object, Object> extInfo) {
		this.extInfo = extInfo;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public GeoPoint getGeopoint() {
		return UtilHelper.NewGeoPoint(lat, lng);
	}

}
