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
 * Defines interface for HTTP layer. Each platforms has its own
 * implementation of HTTP layer.
 */
public interface IHttpLayer {
	
	/**
	 * Registers a request handler.
	 * @param handler request handler
	 */
	void registerHandler(IRequestHandler hander);
	
	/**
	 * Unregisters a request handler
	 * @param handler request handler
	 */
	void unregisterHandler(IRequestHandler handler);
	
	/**
	 * Finds a request handler which can manage provided HTTP request 
	 * @param req HTTP request
	 * @return a request handler or default request handler (404 Not Found)
	 */
	IRequestHandler findHandler(Request req);

	/**
	 * Sends HTTP response to a connection with provided ID
	 * @param connectionID ID of HTTP connection
	 * @param resp HTTP response to be sent
	 */
	void sendResponse(int connectionID, Response resp);
	
	/**
	 * Notification that a connection point has been closed.
	 * @param httpConnectionPoint HTTP connection point
	 */
	void onConnectionClosed(IHttpConnectionPoint httpConnectionPoint);
}
