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

import android.location.Location;

import com.hoperun.telematics.mobile.framework.service.LocationService.ELocationProviderType;

/**
 * 
 * @author hu_wg
 * 
 */
public interface ILocationEventArgs {

	Location getLocation();

	ELocationProviderType getLocationType();

	ELocationStatus getLocationStatus();

	enum ELocationStatus {
		READY, WAIT_SETTING
	}
}
