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

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Abstract class that provides implementation of registration and notification methods of McsLayer interface.
 */
public abstract class AbstractMcsLayer implements McsLayer
{
    /** Listeners (usually the upper layers) to be notified about events: connection-closed, data-received. */
    private ConcurrentLinkedQueue<McsLayerListener> listeners = new ConcurrentLinkedQueue<McsLayerListener>(); 


    /**
     * Register given listener (usually upper layer) to be notified about events: connection-closed, data-received.
     */
    @Override public void addListener( McsLayerListener listener )
    {
        listeners.add( listener );
    }


    /**
     * Unregister given listener.
     */
    @Override public void removeListener( McsLayerListener listener )
    {
        listeners.remove( listener );
    }


    /**
     * Unregister all the listeners. (usually the upper layers)
     */
    protected void removeAllListeners()
    {
        listeners.clear();
    }


    /**
     * Notify all the listeners (usually the upper layers) that data has been received.
     */
    protected void tellDataReceived()
    {
        for( McsLayerListener listener : listeners )
            listener.onDataReceived();
    }


    /**
     * Notify all the listeners (usually the upper layers) that connection of this layer has been closed.
     */
    protected void tellConnectionClosed()
    {
        for( McsLayerListener listener : listeners )
            listener.onConnectionClosed();
    }
}
