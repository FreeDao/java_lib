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

package com.hoperun.telematics.mobile.map.seivice.impl;

import java.util.List;

import com.hoperun.telematics.mobile.map.seivice.IHopeRunMapInterface;
import com.hoperun.telematics.mobile.model.location.GeoLocation;

import android.content.Context;

/**
 * ClassName:GoogleMapServiceImpl
 * 
 * @description
 * @author he_chen
 * @Date 2012 Jun 15, 2012 12:51:21 PM
 * 
 */

public class GoogleMapServiceImpl implements IHopeRunMapInterface {

	/* (non-Javadoc)
	 * @see com.hoperun.telematics.mobile.map.seivice.IHopeRunMapInterface#drawPointOnMap(java.util.List, android.graphics.drawable.Drawable, java.lang.Object, android.content.Context)
	 */
	@Override
	public void drawPointsOnMap(List<GeoLocation> geoLocations,  Object mapObj, Context context,int id,int pinid) {
		// TODO Auto-generated method stub
		
	}

}
