/*****************************************************************************
 *
 *                      AIRBIQUITY PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to Airbiquity
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from Airbiquity.
 *
 *            Copyright (c) 2012 by Airbiquity.  All rights reserved.
 *            
 *                   @author 
 *                   Jack Li
 *
 *****************************************************************************/
package com.airbiquity.mcs.http.android;

import com.airbiquity.mcs.common.McsLayer;

/**
 * Defines interface for HTTP connection point.
 */
public interface IHttpConnectionPoint {
	
	/**
	 * Notification that new data has been received
	 */
	void onDataReceived();
	
	/**
	 * Sends HTTP response to the underlying data layer
	 * @param resp HTTP response
	 */
	void sendResponse(Response resp);
	
	/**
	 * Retrieves connection ID
	 * @return connection ID
	 */
	int getID();
	
	/**
	 * Retrieves underlying data layer
	 * @return
	 */
	McsLayer getDataLayer();
}
