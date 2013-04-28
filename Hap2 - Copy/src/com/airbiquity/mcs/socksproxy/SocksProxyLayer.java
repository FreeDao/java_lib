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
package com.airbiquity.mcs.socksproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

import com.airbiquity.mcs.common.MCSException;
import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.McsLayerListener;

/**
 * SocksProxyLayer
 */
public class SocksProxyLayer implements McsLayerListener
{
    private static final String TAG = "SocksProxyLayer";
    private static final int LISTEN_PORT = 1080;
    private final byte[] m_buffer;
    private final int m_size;
    private McsLayer lowerLayer; // This will be TCPIP Layer
    private boolean m_isStopped = false;
    private Socket m_socket;
    private InputStream m_is;
    private OutputStream m_out;


    /**
     * Constructor.
     */
    public SocksProxyLayer()
    {
        m_buffer = new byte[16*1024];
        m_size = m_buffer.length;        
    }


    /**
     * Set the lower layer and and allocate memory.
     * If there is a lower layer set already - unset it first. 
     * @param lower_layer : the lower layer (aka data layer, aka transport layer)
     * @throws MCSException
     */
    public void setLowerLayer( McsLayer lower_layer ) throws MCSException
    {
        if( lowerLayer != null )
            lowerLayer.removeListener( this );

        lowerLayer = lower_layer;
        if( lowerLayer != null )
            lowerLayer.addListener( this );        
    }


    /**
     * Open a socket on local host at port 1080 and get its input and output streams.
     */
    public void init()
    {
        try
        {
            InetAddress serverAddr = InetAddress.getLocalHost();
            m_socket = new Socket( serverAddr, LISTEN_PORT );
            m_is = m_socket.getInputStream();
            m_out = m_socket.getOutputStream();
            SocksproxyListenThread thread = new SocksproxyListenThread();
            thread.start();
            //Log.d( TAG, "SocksproxyListenThread started." );
        }
        catch( Exception e )
        {
            Log.e( TAG, "", e );
        }
    }


    /**
     * Read data from the lower layer and write it into the output stream.
     */
    public void onDataReceived()
    {
        if( m_isStopped )
            return;
        
        int cnt = lowerLayer.readData( m_buffer, m_size );
        if( cnt > 0 )
        {
            try
            {
                m_out.write( m_buffer, 0, cnt );
            }
            catch( IOException e )
            {
                Log.e( TAG, "", e );
            }
        }
    }
    

    /**
     * SocksproxyListenThread
     */
    private class SocksproxyListenThread extends Thread
    {
        public void run()
        {
            Log.d( TAG, "SocksproxyListenThread.run()" );
            final int INPUT_BUFFER_SIZE = 4056;
            byte[] inputBuffer = new byte[INPUT_BUFFER_SIZE];
            while( !m_isStopped )
            {
                try
                {
                    if( m_is.available() < 1024 )
                        Thread.sleep( 100 );
                    
                    int bytesRead = m_is.read( inputBuffer, 0, INPUT_BUFFER_SIZE );
                    //Log.d( TAG, "SocksproxyListenThread read:"+bytesRead );
                    if( bytesRead > 0  &&  null != lowerLayer )
                        lowerLayer.writeData( inputBuffer, bytesRead );
                    else
                        Thread.sleep( 100 );
                }
                catch( Exception e )
                {
                    Log.e( TAG, "", e );
                    break;
                }
            }
        }
    }


    /***************************************************************
     * IMCSDataLayerNotification methods.
     ***************************************************************/
    /**
     * Fires when connection has been terminated
     */
    synchronized public void onConnectionClosed()
    {
        if( !m_isStopped )
        {
            m_isStopped = true;
            lowerLayer.removeListener( this );
            lowerLayer.close();
            lowerLayer = null;
            Log.d( TAG, "SocksproxyListenThread.onConnectionClosed()" );
        }
    }
}
