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

package com.hoperun.telematics.mobile.map.seivice;

import java.util.List;

import com.hoperun.telematics.mobile.model.location.GeoLocation;

import android.content.Context;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public interface IHopeRunMapInterface {

	/**
	 * @param geoLocations
	 * @param mapObj mapview
	 * @param context activity
	 * @param id popupWindow layout id
	 * @param pinid pin id
	 */
	void drawPointsOnMap(List<GeoLocation> geoLocations, Object mapObj, Context context,int id,int pinid);

	// TODO
}
