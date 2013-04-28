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
package com.airbiquity.mcs.common;

/**
 * Listener that can be notified if:
 *      a new connection has been requested or 
 *      a connection has been closed
 */
public interface IMcsConListener {
	/**
	 * Notification that a new connection is requested.
	 * 
	 * Note: do not store fromAddress and toAddress; make copies instead
	 * because these two objects are reused.
	 * 
	 * @param fromAddress - source address
	 * @param toAddress - destination address
	 * @param payload - initial connection payload if any
	 */
	void newConnectionRequested(IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress, byte[] payload);
	
	
	/**
	 * Notification that connection is closed.
	 * 
	 * Note: do not store fromAddress and toAddress; make copies instead
	 * because these two objects are reused.
	 * 
	 * @param connection - connection being closed
	 * @param fromAddress - source address
	 * @param toAddress - destination address
	 */
	void onConnectionClosed(McsLayer connection, IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress);
}
