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
 * Default request handler which returns 404 Not Found HTTP response
 */
public class NotFoundHandler implements IRequestHandler {

	/**
	 * Checks if a request can be processed. Always returns true.
	 */
	public boolean canProcess(String url) {
		return true;
	}

	/**
	 * Processes a request by returning 404 Not Found error.
	 */
	public Response processRequest(Request request, int connectionPoint) {
		Response resp = new Response("Not Found", 404, null, null, true);
		return resp;
	}
	
	/**
	 * Not used because it does not send asynchronous messages
	 */
	public void setHttpLayer(IHttpLayer layer) {
		// Do nothing
	}
}
