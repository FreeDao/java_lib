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

import com.hoperun.telematics.mobile.map.seivice.impl.AMapServiceImpl;
import com.hoperun.telematics.mobile.map.seivice.impl.GoogleMapServiceImpl;

/**
 * ClassName:ServiceFactory
 * 
 * @description
 * @author he_chen
 * @Date 2012 Jun 15, 2012 10:16:52 AM
 * 
 */

public class MapServiceFactory {

	public static IHopeRunMapInterface getAmapService() {
		return new AMapServiceImpl();
	}

	public static IHopeRunMapInterface getGoogleMapService() {
		return new GoogleMapServiceImpl();
	}
}
