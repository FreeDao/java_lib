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
package com.hoperun.telematics.mobile.framework.location;

import java.io.Serializable;

import com.hoperun.telematics.mobile.framework.service.LocationService.ELocationProviderType;

import android.location.Location;

/**
 * 
 * @author hu_wg
 * 
 */
public class DefaultLocationEventArgs implements ILocationEventArgs, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5907597477128318486L;
	private Location location;
	private ELocationProviderType locationType;
	private ELocationStatus locationStatus;

	public DefaultLocationEventArgs(Location location, ELocationProviderType locationType, ELocationStatus locationStatus) {
		super();
		this.location = location;
		this.locationType = locationType;
		this.locationStatus = locationStatus;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public ELocationProviderType getLocationType() {
		return this.locationType;
	}

	@Override
	public ELocationStatus getLocationStatus() {
		return this.locationStatus;
	}

}
