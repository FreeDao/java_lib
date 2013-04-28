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

import java.util.ArrayList;

import android.util.Log;

import com.airbiquity.mcs.common.IMCSConnectionAddress;
import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.IMCSMultiPointLayer;
import com.airbiquity.mcs.common.IMcsConListener;
import com.airbiquity.mcs.common.MCSException;
import com.airbiquity.mcs.tcpip.TCPIPAddress;
import com.airbiquity.mcs.tcpip.TCPIPAddressPool;

/**
 * Android platform implementation of HttpLayer.
 */
public class HttpLayer implements IMcsConListener, IHttpLayer
{
    private static final String TAG = "HttpLayer";
    protected TCPIPAddress m_address = null;
    protected IMCSMultiPointLayer m_multiLayer;
    protected ArrayList<IHttpConnectionPoint> m_connections = new ArrayList<IHttpConnectionPoint>();
    protected ArrayList<IRequestHandler> m_handlers = new ArrayList<IRequestHandler>();
    protected int m_connectionPointIDSequence = 0;
    protected static NotFoundHandler m_notFoundHandler = new NotFoundHandler();


    /**
     * Constructs new instance
     * @param address IP address + TCP port
     * @param multi underlying multi-point data layer
     * @throws MCSException
     */
    public HttpLayer( TCPIPAddress address, IMCSMultiPointLayer multi ) throws MCSException
    {
        m_address = TCPIPAddressPool.copyAddress( address );
        m_multiLayer = multi;
        m_multiLayer.addListener( this );
        Log.d( TAG, "HttpLayer " + address + " " + multi );
    }


    /**
     * Stops HTTP Layer
     */
    public void terminate()
    {
        ArrayList<IHttpConnectionPoint> temp = null;
        synchronized( this )
        {
            if( null == m_connections )
                return;

            temp = m_connections;
            m_connections = null;
        }
        
        m_multiLayer.removeListener( this );
        
        try
        {
            TCPIPAddressPool.freeAddress( m_address );
        }
        catch( MCSException e1 )
        {
            Log.e( TAG, "", e1 );
        }
        
        for( IHttpConnectionPoint p : temp )
        {
            try
            {
                m_multiLayer.closeConnection( p.getDataLayer() );
            }
            catch( MCSException e )
            {
                Log.e( TAG, "", e );
            }
        }
        temp.clear();
        m_multiLayer = null;
    }


    /**
     * Handles peer requests for new connections
     */
    public void newConnectionRequested( IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress, byte[] payload )
    {
        Log.d( TAG, "newConnectionRequested " + fromAddress + " -> " + toAddress );
        McsLayer dataLayer = null;
        synchronized( this )
        {
            // Check state and destination address
            if( m_connections == null || !toAddress.isSameAs( m_address ) )
                return;
            try
            {
                dataLayer = m_multiLayer.createConnectionPoint( fromAddress, toAddress );
            }
            catch( MCSException e )
            {
                Log.e( TAG, "", e );
            }
            
            if( dataLayer != null )
            {
                HttpConnectionPoint p = new HttpConnectionPoint( dataLayer, this );
                int seqNo = -1;
                synchronized( this )
                {
                    seqNo = m_connectionPointIDSequence;
                    m_connectionPointIDSequence++;
                    m_connections.add( p );
                }
                p.setID( seqNo );
            }
        }
    }


    /**
     * Handles closing of HTTP connection point
     */
    public void onConnectionClosed( McsLayer connection, IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress )
    {
        // Do nothing
    }


    /**
     * Handles closing of HTTP connection point
     */
    synchronized public void onConnectionClosed( IHttpConnectionPoint p )
    {
        if( m_connections != null )
            m_connections.remove( p );
    }


    /**
     * Finds a request handler which can process provided URL
     */
    public IRequestHandler findHandler( Request req )
    {
        String url = HttpUtils.extractUrlPath( req.url );
        synchronized( m_handlers )
        {
            for( IRequestHandler h : m_handlers )
            {
                if( h.canProcess( url ) )
                    return h;
            }
        }
        
        // Unsupported URL
        Log.e( "HttpLayer.findHandler", "Unsupported URL - " + url );
        return m_notFoundHandler;
    }


    /**
     * Registers a request handler
     */
    public void registerHandler( IRequestHandler handler )
    {
        synchronized( m_handlers )
        {
            m_handlers.add( handler );
            handler.setHttpLayer( this );
            Log.d( TAG, "registerHandler " + handler );
        }
    }


    /**
     * Unregisters a request handler
     */
    public void unregisterHandler( IRequestHandler handler )
    {
        synchronized( m_handlers )
        {
            m_handlers.remove( handler );
            handler.setHttpLayer( null );
        }
    }


    /**
     * Sends asynchronous response to a connection point
     */
    synchronized public void sendResponse( int connectionID, Response resp )
    {
        for( IHttpConnectionPoint p : m_connections )
        {
            if( p.getID() == connectionID )
                p.sendResponse( resp );
        }
    }
    
    @Override
    public String toString()
    {
        return TAG+" "+m_address;
    }
}
