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
 *****************************************************************************/
package com.airbiquity.connectionmgr.detector.android;

import socks.ProxyServer;
import socks.server.ServerAuthenticator;
import socks.server.ServerAuthenticatorNone;
import android.util.Log;

import com.airbiquity.connectionmgr.msp.MspConst;
import com.airbiquity.connectionmgr.msp.MspLayer;
import com.airbiquity.hap.A;
import com.airbiquity.mcs.common.AbstractMcsLayer;
import com.airbiquity.mcs.common.MCSException;
import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.McsLayerListener;
import com.airbiquity.mcs.http.android.HandlerCommandControl;
import com.airbiquity.mcs.http.android.HandlerGetLocation;
import com.airbiquity.mcs.http.android.HandlerHandsetProfile;
import com.airbiquity.mcs.http.android.HandlerHeadUnitInformation;
import com.airbiquity.mcs.http.android.HandlerLongPolling;
import com.airbiquity.mcs.http.android.HttpLayer;
import com.airbiquity.mcs.slip.SLIPLayer;
import com.airbiquity.mcs.socksproxy.SocksProxyPortListener;
import com.airbiquity.mcs.tcpip.TCPIPAddress;
import com.airbiquity.mcs.tcpip.TCPIPLayer;
import com.airbiquity.mcs.utils.ByteUtils;

/**
 * The Detector Layer which response for Protocol detection and stack establishment.
 */
public class DetectorLayer extends AbstractMcsLayer implements McsLayerListener
{
    private static final String TAG = "DetectorLayer";
    private static final int SLIP = 1;
    private static final int MSP = 0;
    private static final int LISTEN_PORT = 1080;
    private McsLayer lowerLayer;
    private boolean m_hasProtocol = false;
    private ServerAuthenticator m_ServerAuthenticator;
    private ProxyServer m_ProxyServer;
    private SLIPLayer mSlip;
    private TCPIPLayer mTcpip;
    
    private byte[] arr;     // Buffer.
    private int pos = 0;    // Position in the buffer.


    public DetectorLayer()
    {
        arr = new byte[16*1024];    // TODO: Do we really need 16k ?
    }


    /**
     * Set the lower layer and start listening to it.
     * If there is a lower layer set already - unset it first. 
     * @param layer : the lower layer (aka data layer, aka transport layer)
     */    
    public void setLowerLayer( McsLayer layer )
    {
        if( lowerLayer != null )
            lowerLayer.removeListener( this );
        lowerLayer = layer;
        if( layer != null )
            layer.addListener( this );
    }


    // Called from the lower layer to tell that the connection has been closed.
    public void onConnectionClosed()
    {
        synchronized( this )
        {
            if( null == lowerLayer )
                return; // already closed
            
            lowerLayer.removeListener( this );
            lowerLayer = null;
            m_hasProtocol = false;
        }
        tellConnectionClosed();
        removeAllListeners();
    }


    /**
     * Detect Protocol.
     * 
     * @return The type of protocol detected: SLIP = 1; MSP = 0; OTHERWISE = -1
     */
    private int detectProtocol()
    {
        if( null == lowerLayer )
            return -1;
        
        synchronized( lowerLayer )
        {
            pos = lowerLayer.readData( arr, arr.length );
        }
        
        // TODO: Do we really need to search all 16k for the marker? Shouldn't it be in a specific place?
        for( int p = 0; p < pos - 4; p++ )
        {
            int det1 = ByteUtils.getInt( arr, p, 4 );
            if( (det1 ^ MspConst.START_SIGNATURE) == 0 )
                return MSP;
        }
        
        // TODO: The chance that we find 0xC0 byte in 16k buffer of random numbers is almost 100%
        // Shouldn't we look for it in a specific place?
        for( int q = 0; q < pos; q++ )
        {
            int det2 = ByteUtils.getInt( arr, q, 1 );
            if( (det2 ^ 0xC0) == 0 )
                return SLIP;
        }
        return -1;
    }


    /**
     * When we receive the very 1st packet - we detect the protocol.
     * For all subsequent calls - we just forward the data to the appropriate layer.
     */
    public void onDataReceived()
    {
        if( m_hasProtocol )
        {
            tellDataReceived();
            return;
        }

        switch( detectProtocol() )
        {
            case SLIP: 
                setupSlip();            
                break;

            case MSP: 
                setupMsp();
                break;
            
            default:
                Log.d( TAG, "Unknown Protocol Detected" );
                pos = 0;
        }        
    }

    
    /**
     * Set up layers for MSP: MspLayer & PanAppManager.
     */
    private void setupMsp()
    {
        Log.d( TAG, "MSP detected..." );
        MspLayer mspLayer = new MspLayer();
        mspLayer.setLowerLayer( this );
        A.a().panAppManager.setLowerLayer( mspLayer );
        m_hasProtocol = true;
        tellDataReceived();        
    }

    
    /**
     * Set up layers for SLIP: SLIPLayer, TCPIPLayer, SocksProxy, HttpLayer.
     */
    private void setupSlip()
    {
        try
        {
            Log.d( TAG, "SLIP detected..." );
            mSlip = new SLIPLayer(); // TODO: if mSlip and mTcpip are not null, should we detach them first?
            mSlip.setLowerLayer( this );
            mTcpip = new TCPIPLayer();
            mTcpip.setLowerLayer( mSlip );

            // start socks proxy
            if( m_ProxyServer == null )
            {
                m_ServerAuthenticator = new ServerAuthenticatorNone();
                m_ProxyServer = new ProxyServer( m_ServerAuthenticator );
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        Log.d( TAG, "Socks started on port : " + LISTEN_PORT );
                        m_ProxyServer.start( LISTEN_PORT );
                    }
                }.start();
            }
            SocksProxyPortListener listener = new SocksProxyPortListener();
            listener.setLowerLayer( mTcpip );

            // Make HeadUnit-to-NomadicApps connection.
            TCPIPAddress addr = new TCPIPAddress();
            addr.setIsAddressSpecified( true );
            final byte[] MyAddress = new byte[] { -64, -88, 0, 3 }; // this corresponds to 192.168.0.3
            addr.setIPAddress( MyAddress );
            addr.setIsPortSpecified( true );
            addr.setPort( 8080 );
            addr.setProtocolType( TCPIPAddress.ProtocolTypeTCP );
            
            // TODO: We never call terminate() on HTTP layer, so we might have a memory leak on re-connects.
            HttpLayer http = new HttpLayer( addr, mTcpip );
            http.registerHandler( new HandlerHandsetProfile() );
            http.registerHandler( new HandlerCommandControl() );
            http.registerHandler( new HandlerGetLocation() );
            http.registerHandler( new HandlerHeadUnitInformation() );
            
            // Make long-polling connection. That is for NomadicApps-to-HeadUnit asynchronous messages.
            addr.setPort( 8090 );
            HttpLayer httpLP = new HttpLayer( addr, mTcpip );
            httpLP.registerHandler( new HandlerLongPolling() );
            mSlip.start();
            m_hasProtocol = true;        
        }
        catch( MCSException e )
        {
            Log.e(TAG, " ", e);
            return;
        }
    }

    
    /**
     * Reads data from this layer. (Called from the Upper layer)
     * @param buffer : buffer where to put the data.
     * @param size : size of the buffer.
     * @return size of the data written into the buffer.
     */
    public int readData( byte[] buffer, int size )
    {
        int ret = 0;
        if( pos > 0 )
        {
            if( size < pos )
            {
                pos = 0;
                Log.e( TAG, "Buffer is too small:" + size + "<" + pos );
                return 0;
            }
            else
            {
                System.arraycopy( arr, 0, buffer, 0, pos );
                int n = pos;
                pos = 0;
                ret = n;
            }
        }
        else
        {
            synchronized( lowerLayer )
            {
                int len = lowerLayer.readData( arr, arr.length );
                pos = len;
                if( pos > 0 )
                    ret = readData( buffer, size );
            }
        }
        return ret;
    }


    /**
     * Writes data to this layer. (Called from the Upper layer)
     * @param buffer : buffer that contain the data to write.
     * @param size : size of the data in the buffer.
     */
    public void writeData( byte[] buffer, int size )
    {
        if( null == lowerLayer )
            return; // Connection closed.
        
        synchronized( lowerLayer )
        {
            lowerLayer.writeData( buffer, size );
        }
    }


    public void close()
    {
        onConnectionClosed();
    }


    public void stop()
    {
        if( null != m_ProxyServer )
            m_ProxyServer.stop();
        close();
    }
}
