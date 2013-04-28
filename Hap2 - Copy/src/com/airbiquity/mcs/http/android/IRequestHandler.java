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

/**
 * Defines interface for handlers of HTTP requests.
 */
public interface IRequestHandler {
	
	/**
	 * Processes HTTP request
	 * @param request HTTP request to be processed
	 * @param connectionID HTTP connection point ID
	 * @return HTTP response
	 */
	Response processRequest(Request request, int connectionID);

	/**
	 * Checks if a given URL can be processed by this instance
	 * @param url URL to be processed
	 * @return true if it can be processed; false otherwise
	 */
	boolean canProcess(String url);

	/**
	 * Sets HTTP layer to be used for asynchronous responses. 
	 * @param httpLayer : HTTP layer to use for asynchronous responses.
	 */
	void setHttpLayer(IHttpLayer httpLayer);
}
