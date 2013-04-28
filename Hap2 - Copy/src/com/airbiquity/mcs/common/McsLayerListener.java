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
 * Listener for events: connection-closed, data-received.
 * Usually the upper layer listens to the events from the lower layer.
 */
public interface McsLayerListener
{
    /**
     * Notify the listener (usually the upper layer) that data from this layer is available for retrieval.
     */
    void onDataReceived();
    
    /**
     * Notify the listener (usually the upper layer) that the connection has been closed.
     */
    void onConnectionClosed();    
}
