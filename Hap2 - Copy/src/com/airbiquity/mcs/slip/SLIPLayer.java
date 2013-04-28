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
package com.airbiquity.mcs.slip;

import android.util.Log;

import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.McsLayerListener;
import com.airbiquity.mcs.common.AbstractMcsLayer;
import com.airbiquity.mcs.common.MCSException;
import com.airbiquity.mcs.utils.ByteUtils;

/**
 * Implements reading and parsing of SLIP datagrams as well as constructing and sending new SLIP datagrams.
 */
public class SLIPLayer extends AbstractMcsLayer implements Runnable, McsLayerListener
{
    private static final String TAG = "SLIPLayer";

    static protected class Buffer
    {
        public int Pos = 0;
        public byte[] Arr;
    }

    private static final int WaitTime = 10; // ms
    private static final byte cMarkerEnd = (byte) 192;
    private static final byte cMarkerEsc = (byte) 219;
    private static final byte cMarkerEscEnd = (byte) 220;
    private static final byte cMarkerEscEsc = (byte) 221;
    private static final byte[] m_response = { 'C', 'L', 'I', 'E', 'N', 'T', 'S', 'E', 'R', 'V', 'E', 'R' };
    private static final int m_responseLen = m_response.length;
    private static final int NotStarted = 0;
    private static final int Started = 1;
    private static final int Escape = 2;
    private static final int Ended = 3;
    private McsLayer lowerLayer;
    private boolean m_isStarted;
    private Thread m_thread;
    private int m_state = NotStarted;
    private final Buffer m_inBuffer = new Buffer();
    private final Buffer m_inTransportBuffer = new Buffer();
    private final Buffer m_outBuffer = new Buffer();
    private boolean m_hasGarbage = false;
    private int m_counter = 0;
    private int m_readBufferSize;


    /**
     * Constructs new instance of SLIP layer
     * @throws MCSException
     */
    public SLIPLayer() throws MCSException
    {
        m_inBuffer.Arr = new byte[16*1024];
        m_outBuffer.Arr = new byte[16*1024];
        m_inTransportBuffer.Arr = new byte[16*1024];
        m_readBufferSize = m_inTransportBuffer.Arr.length;
    }


    /**
     * Starts worker thread
     */
    public synchronized void start()
    {
        if( m_isStarted )
            return;
        
        m_isStarted = true;
        m_thread = new Thread( this );
        m_thread.setName("SLIP Layer");
        m_thread.start();        
    }


    /**
     * Main worker thread. It checks for garbage in input data, 
     * resets state and sends "CLIENTSERVER" message back for any Windows SLIP connection.
     */
    public void run()
    {
        m_counter = 0;
        for( ;; )
        {
            // Check for timeout and send CLIENTSERVER message to Windows
            synchronized( this )
            {
                if( null == lowerLayer )
                    return; // Connection is closed - exit now

                ++m_counter;
                if( m_counter > (1000 / WaitTime) )
                {
                    if( m_hasGarbage )
                    {
                        lowerLayer.writeData( m_response, m_responseLen );
                        m_hasGarbage = false;
                    }
                    if( m_inBuffer.Pos > 0 )
                    {
                        Log.d( TAG, "Discarding garbage data with length " + m_inBuffer.Pos );
                        m_inBuffer.Pos = 0;
                        m_state = NotStarted;
                    }
                    m_counter = 0;
                }
            }
            // Wait for the next period
            try
            {
                Thread.sleep( WaitTime );
            }
            catch( InterruptedException ex )
            {
                break;
            }
        }
    }


    /**
     * Parses next byte from a SLIP datagram
     * @param b Byte to be parsed
     */
    private void parseByte( byte b )
    {
        switch( m_state )
        {
            case NotStarted:
            case Ended:
                if( cMarkerEnd == b )
                {
                    // log("SLIPLayer: ->Started from " + m_state);
                    m_state = Started;
                }
                else
                {
                    synchronized( this )
                    {
                        m_hasGarbage = true;
                    }
                    // Log.d("SLIPLayer","Found garbage data (missing END marker)");
                }
                break;
            case Started:
                synchronized( this )
                {
                    m_hasGarbage = false;
                }
                if( cMarkerEsc == b )
                {
                    m_state = Escape;
                    // log("SLIPLayer: Started->Escape, length " + m_inBuffer.Pos);
                }
                else if( cMarkerEnd == b )
                {
                    if( m_inBuffer.Pos == 0 )
                    {
                        // log("SLIPLayer: Started->Ended->Started");
                    }
                    else
                    {
                        // log("SLIPLayer: Started->Ended, length " + m_inBuffer.Pos);
                        m_state = Ended;
                    }
                }
                else
                {
                    addByteIn( b );
                }
                break;
            case Escape:
                if( cMarkerEscEsc == b )
                {
                    addByteIn( cMarkerEsc );
                }
                else if( cMarkerEscEnd == b )
                {
                    addByteIn( cMarkerEnd );
                }
                else
                {
                    // just skip this byte for now
                    // TODO: may be we should restart message receiving?
                    addByteIn( b );
                    int num = ByteUtils.toUnsignedInteger( b );
                    Log.e( TAG, "\nInvalid escape secuence: 219, " + Integer.toString( num ) );
                }
                m_state = Started;
                // log("SLIPLayer: Escape->Started, length " + m_inBuffer.Pos);
                break;
        }
    }


    /**
     * Adds a parsed byte to a message
     * @param b Byte to be added
     */
    private synchronized void addByteIn( byte b )
    {
        if( m_inBuffer.Pos < m_inBuffer.Arr.length )
        {
            m_inBuffer.Arr[m_inBuffer.Pos] = b;
            m_inBuffer.Pos++;
        }
        else
        {
            m_inBuffer.Pos = 0;
            m_state = NotStarted;
            Log.e( TAG, "Input SLIP message too long" );
        }
    }


    /**
     * Adds a byte to output SLIP datagram
     * @param b Byte to be added
     * @return
     */
    private boolean addByteOut( byte b )
    {
        if( m_outBuffer.Pos < m_outBuffer.Arr.length )
        {
            m_outBuffer.Arr[m_outBuffer.Pos] = b;
            m_outBuffer.Pos++;
            return true;
        }
        else
        {
            Log.e( TAG, "Output SLIP message too long" );
            return false;
        }
    }


    /**
     * Set the lower layer and start listening to it. 
     * If there is a lower layer set already - unset it first.
     * @param lower_layer : the lower layer (aka data layer, aka transport layer)
     */    
    public void setLowerLayer( McsLayer layer )
    {
        if( lowerLayer != null )
            lowerLayer.removeListener( this );

        lowerLayer = layer;
        if( layer != null )
            layer.addListener( this );
    }


    public void onConnectionClosed()
    {
        synchronized( this )
        {
            if( null == lowerLayer )
                return; // already closed

            lowerLayer.removeListener( this );
            m_isStarted = false;
            m_hasGarbage = false;
            m_readBufferSize = 0;
            lowerLayer.close();
            lowerLayer = null;
        }
        tellConnectionClosed();
        removeAllListeners();
    }


    public void onDataReceived()
    {
        tellDataReceived();
    }


    /**
     * Reads full SLIP message, decodes it and returns internal data.
     */
    public int readData( byte[] buffer, int size )
    {
        if( lowerLayer == null )
            return 0;

        boolean hasPacket;
        int len;
        do
        {
            hasPacket = processInputData();
            if( hasPacket )
            {
                if( size < m_inBuffer.Pos )
                {
                    m_inBuffer.Pos = 0;
                    Log.e( TAG, "ERROR: received datagram is larger than requested in readData()" );
                    return 0;
                }
                else
                {
                    // log( "SLIP Layer", "New SLIP packet" );
                    System.arraycopy( m_inBuffer.Arr, 0, buffer, 0, m_inBuffer.Pos );
                    int n = m_inBuffer.Pos;
                    m_inBuffer.Pos = 0;
                    return n;
                }
            }
            synchronized( lowerLayer )
            {
                len = lowerLayer.readData( m_inTransportBuffer.Arr, m_readBufferSize );
            }
            m_inTransportBuffer.Pos = len;
            if( len > 0 )
            {
                synchronized( this )
                {
                    m_counter = 0;
                }
            }
        }
        while( len > 0 );
        return 0;
    }


    /**
     * Parses next sequence if data coming from transport layer.
     * @return True if a good SLIP message is received; False otherwise
     */
    private boolean processInputData()
    {
        int len = m_inTransportBuffer.Pos;
        byte[] buf = m_inTransportBuffer.Arr;
        boolean hasPacket = false;
        for( int i = 0; i < len; i++ )
        {
            byte b = buf[i];
            parseByte( b );
            if( m_state == Ended && m_inBuffer.Pos > 0 )
            {
                hasPacket = true;
                // Move unread data
                len = len - i - 1;
                if( len > 0 )
                {
                    for( int j = 0; j < len; j++ )
                        buf[j] = buf[i + 1 + j];
                }
                m_inTransportBuffer.Pos = len;
                break;
            }
        }
        return hasPacket;
    }


    /**
     * Builds SLIP message from provided data and writes it to underlying data transport protocol.
     */
    public void writeData( byte[] buffer, int size )
    {
        int start = 0;
        if( null == buffer || (start + size) > buffer.length )
        {
            Log.e( TAG, "Buffer too small" );
            return;
        }
        m_outBuffer.Pos = 0;
        addByteOut( cMarkerEnd );
        int end = start + size;
        if( end > buffer.length )
            end = buffer.length;
        for( int i = start; i < end; i++ )
        {
            byte b = buffer[i];
            switch( b )
            {
                case cMarkerEnd:
                    if( !addByteOut( cMarkerEsc ) || !addByteOut( cMarkerEscEnd ) )
                    {
                        Log.e( TAG, "Too long SLIP message" );
                        return;
                    }
                    break;
                case cMarkerEsc:
                    if( !addByteOut( cMarkerEsc ) || !addByteOut( cMarkerEscEsc ) )
                    {
                        Log.e( TAG, "Too long SLIP message" );
                        return;
                    }
                    break;
                default:
                    if( !addByteOut( b ) )
                    {
                        Log.e( TAG, "Too long SLIP message" );
                        return;
                    }
            }
        }
        if( !addByteOut( cMarkerEnd ) )
        {
            Log.e( TAG, "Too long SLIP message" );
            return;
        }
        lowerLayer.writeData( m_outBuffer.Arr, m_outBuffer.Pos );   // NullPointerException here if we try to call sendFINPacket() in ConnectionPointTCPIP.closeConnection()
    }


    public void close()
    {
        onConnectionClosed();
    }
}
