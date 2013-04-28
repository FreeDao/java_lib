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


public abstract class MCSMultiPointLayerBase implements IMCSMultiPointLayer, McsLayerListener
{
    protected static final int MAX_OBJECTS = 40;
    protected IMcsConListener[] m_notifiables = new IMcsConListener[MAX_OBJECTS];
    protected McsLayer lowerLayer;


    /**
     * Finds the 1st unused index in the m_notifiables array and put there the given listener.
     * @param notification : listener to put in the array of listeners.
     */
    public void addListener( IMcsConListener notification )
    {
        synchronized( this )
        {
            int minPos = MAX_OBJECTS;
            for( int i = 0; i < MAX_OBJECTS; i++ )
            {
                if( m_notifiables[i] == notification )
                    return;

                if( m_notifiables[i] == null && i < minPos )
                    minPos = i;
            }
            if( minPos < MAX_OBJECTS )
                m_notifiables[minPos] = notification;
        }
    }


    public void removeListener( IMcsConListener notification )
    {
        synchronized( this )
        {
            for( int i = 0; i < MAX_OBJECTS; i++ )
            {
                if( m_notifiables[i] == notification )
                    m_notifiables[i] = null;
            }
        }
    }


    /**
     * Set the lower layer and start listening to it. If there is a lower layer set already - unset it first.
     * @param lower_layer : the lower layer (aka data layer, aka transport layer)
     */
    public void setLowerLayer( McsLayer layer )
    {
        synchronized( this )
        {
            if( lowerLayer != null )
                lowerLayer.removeListener( this );

            lowerLayer = layer;
            if( layer != null )
                layer.addListener( this );
        }
    }


    /**
     * Notifies listeners that connection is closed.
     * @param point
     */
    // @SuppressWarnings("unchecked")
    protected void notifyForConnectionClosed( McsLayer point, IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress )
    {
        IMcsConListener[] copy = new IMcsConListener[MAX_OBJECTS];
        synchronized( this )
        {
            System.arraycopy( m_notifiables, 0, copy, 0, MAX_OBJECTS );
        }
        for( int i = 0; i < MAX_OBJECTS; i++ )
        {
            if( copy[i] != null )
                copy[i].onConnectionClosed( point, fromAddress, toAddress );
        }
    }


    /**
     * Notifies listeners that new connection is requested.
     * @param point
     */
    protected void notifyForNewConnection( IMCSConnectionAddress from, IMCSConnectionAddress to, byte payload[] )
    {
        IMcsConListener[] copy = new IMcsConListener[MAX_OBJECTS];
        synchronized( this )
        {
            System.arraycopy( m_notifiables, 0, copy, 0, MAX_OBJECTS );
        }
        for( int i = 0; i < MAX_OBJECTS; i++ )
        {
            if( copy[i] != null )
                copy[i].newConnectionRequested( from, to, payload );
        }
    }

}
