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
 * Layer to which you can read and write, and register a listener to get notified on connection-closed and data-received.
 */
public interface McsLayer
{
    /**
     * Reads data from this layer. (Called from the Upper layer)
     * @param buffer : buffer where to put the data.
     * @param size : size of the buffer.
     * @return size of the data written into the buffer.
     */
    int readData( byte[] buffer, int size );


    /**
     * Writes data to this layer. (Called from the Upper layer)
     * @param buffer : buffer that contain the data to write.
     * @param size : size of the data in the buffer.
     */
    void writeData( byte[] buffer, int size );


    /**
     * Register given listener (usually upper layer) to be notified about events: connection-closed, data-received.
     */
    void addListener( McsLayerListener listener );


    /**
     * Unregister given listener.
     */
    void removeListener( McsLayerListener listener );


    /**
     * Close everything and release all resources.
     */
    void close();

}
