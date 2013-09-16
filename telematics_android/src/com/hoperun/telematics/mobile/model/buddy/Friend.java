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

import java.io.Serializable;

import com.amap.mapapi.core.GeoPoint;
import com.hoperun.telematics.mobile.framework.net.helper.UtilHelper;

/**
 * 
 * @author wang_xiaohua
 * 
 */

public class Friend implements Serializable {
	/**
	 * default serialVersion ID
	 */
	private static final long serialVersionUID = 1L;

	private String friendId;
	private String nickName;
	private String address;
	private String imageId;
	private String note;
	private String longitude;
	private String latitude;

	public Friend() {
		super();
	}

	/**
	 * Creates a new instance of Friend.
	 */
	public Friend(String friendId, String nickName, String address, String imageId, String note, String longitude,
			String latitude) {
		this.friendId = friendId;
		this.nickName = nickName;
		this.address = address;
		this.imageId = imageId;
		this.note = note;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public GeoPoint getGeoPoint() {
		return UtilHelper.NewGeoPoint(Double.valueOf(latitude), Double.valueOf(longitude));
	}
}
