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
package com.airbiquity.mcs.tcpip;

import android.util.Log;

import com.airbiquity.mcs.common.IMCSConnectionAddress;
import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.McsLayerListener;
import com.airbiquity.mcs.common.MCSException;
import com.airbiquity.mcs.common.MCSMultiPointLayerBase;
import com.airbiquity.mcs.utils.DataQueueArr;

/**
 * Implementation of IMCSMultiPointLayer which supports IP, TCP and UDP datagrams.
 */
public class TCPIPLayer extends MCSMultiPointLayerBase implements McsLayerListener, Runnable
{
    private static final String TAG = "TCPIPLayer";
    private static final int MAX_CONNECTIONS = 1024;
    private ConnectionPointTCPIP[] m_connections = new ConnectionPointTCPIP[MAX_CONNECTIONS];
    private final byte[] m_inBuffer;
    private final int m_bufSize;
    // private DataQueueIPPacket m_outgoingQueue = new DataQueueIPPacket(MAX_CONNECTIONS * 32);
    private DataQueueArr m_outgoingBuffers = new DataQueueArr( MAX_CONNECTIONS * 32 );
    private Thread m_thread;

    
    /**
     * Constructs new instance of TCPIPLayer
     * @throws MCSException
     */
    public TCPIPLayer() throws MCSException
    {
        m_inBuffer = new byte[16*1024];
        m_bufSize = m_inBuffer.length;
        m_thread = new Thread( this );
        m_thread.setName("TCPIPLayer");
        m_thread.start();
    }


    /**
     * Handles closing of underlying transport layer.
     */
    public void onConnectionClosed()
    {
        synchronized( this )
        {
            lowerLayer = null;
            for( int i = 0; i < MAX_CONNECTIONS; i++ )
            {
                if( m_connections[i] != null )
                {
                    ConnectionPointTCPIP point = m_connections[i];
                    m_connections[i] = null;
                    notifyForConnectionClosed( point, point.getFromAddress(), point.getToAddress() );
                    point.close(); // This one frees to and from addresses and it must be called last!
                }
            }
        }
    }


    /**
     * Handles coming of new data. Passes IP packets to appropriate connection point. 
     * Notifies listeners for new connection requests.
     */
    public void onDataReceived()
    {
        // Do nothing
    }


    /**
     * Creates TCPIPAddress from provided parameters.
     * @param ipAddress
     * @param port
     * @param protocolType
     * @return
     * @throws MCSException
     */
    private TCPIPAddress createAddress( byte[] ipAddress, int port, byte protocolType ) throws MCSException
    {
        TCPIPAddress addr = TCPIPAddressPool.getAddress();
        addr.setIsPortSpecified( true );
        addr.setIsAddressSpecified( true );
        addr.setProtocolType( protocolType );
        addr.setPort( port );
        addr.setIPAddress( ipAddress );
        return addr;
    }


    /**
     * Closes specified connection point.
     */
    public void closeConnection( McsLayer connectionPoint ) throws MCSException
    {
        if( !(connectionPoint instanceof ConnectionPointTCPIP) )
            throw new MCSException( "Invalid connection point in closeConnection()" );

        ConnectionPointTCPIP point = (ConnectionPointTCPIP) connectionPoint;
        synchronized( this )
        {
            for( int i = 0; i < MAX_CONNECTIONS; i++ )
            {
                if( m_connections[i] == point )
                {
                    point = m_connections[i];
                    m_connections[i] = null;
                    notifyForConnectionClosed( point, point.getFromAddress(), point.getToAddress() );
                    point.close(); // This one frees TO and FROM addresses and it must be called last!
                    return;
                }
            }
            throw new MCSException( "Invalid connection point in closeConnection() - may be already closed" );
        }
    }


    /**
     * Creates new connection point
     */
    public McsLayer createConnectionPoint( IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress ) throws MCSException
    {
        synchronized( this )
        {
            int minPos = MAX_CONNECTIONS;
            
            // Search created connection points
            for( int i = 0; i < MAX_CONNECTIONS; i++ )
            {
                if( m_connections[i] != null )
                {
                    ConnectionPointTCPIP point = m_connections[i];
                    if( point.canHandle( fromAddress, toAddress ) )
                        throw new MCSException( "Connection in use" );
                }
                else if( i < minPos )
                {
                    minPos = i;
                }
            }
            
            // Create new connection point
            if( minPos < MAX_CONNECTIONS )
            {
                ConnectionPointTCPIP con = new ConnectionPointTCPIP( this, fromAddress, toAddress );
                m_connections[minPos] = con;
                return con;
            }
            throw new MCSException( "Too many connection points" );
        }
    }


    /**
     * Sends TCPIPPacket to underlying data layer.
     * @param packet
     */
    public void sendMessage( TCPIPPacket packet ) throws MCSException
    {
        if( packet.getDataLength() == 0 )
        {
            doWriteData( packet ); // Immediately write packet and then free its buffer and return it to packet pool
        }
        else
        {
            byte[] source = packet.getBuffer();
            int len = packet.getBufferLength();
            byte[] buf = new byte[len];
            System.arraycopy( source, 0, buf, 0, len );
            synchronized( m_outgoingBuffers )
            {
                // packet.m_isLocked = true;
                // m_outgoingQueue.insert( packet );
                m_outgoingBuffers.insert( buf );
            }
        }
    }


    public void run()
    {
        while( true )
        {
            try
            {
                Thread.sleep( 10 );
            }
            catch( InterruptedException e )
            {
                // Do nothing
            }
            
            if( lowerLayer == null )
                return; // Closed
            
            // Check if there are outgoing packets
            // TCPIPPacket packet = null;
            byte[] buf = null;
            synchronized( m_outgoingBuffers )
            {
                if( m_outgoingBuffers.size() > 0 )
                {
                    try
                    {
                        // packet = m_outgoingBuffers.delete();
                        buf = m_outgoingBuffers.delete();
                    }
                    catch( MCSException ex )
                    {
                        // Should never come here!
                    }
                }
            }
            // if( packet != null ) {
            // doWriteData(packet);
            // }
            if( buf != null )
                doWriteData( buf, buf.length, " SIZE: " + buf.length );

            if( lowerLayer == null )
                return; // Closed
            processIncommingData();
        }
    }


    /**
     * processIncommingData
     */
    private void processIncommingData()
    {
        int len = 0;
        synchronized( lowerLayer )
        {
            if( lowerLayer == null )
                return;

            len = lowerLayer.readData( m_inBuffer, m_bufSize );
        }
        if( len > 0 )
        {
            try
            {
                TCPIPPacket packet = new TCPIPPacket();
                packet.setBuffer( m_inBuffer, len );
                if( packet.isPacketOK() )
                {
                    // Log.d(" IN IP PACKET", packet.toString());
                    // Check if any open connection can process this packet
                    if( processPacket( packet ) )
                    {
                        packet.setBuffer( null, 0 );
                        return;
                    }
                    
                    // Notify for new connection request
                    if( (packet.getFlags() & TCPIPPacket.TCP_SYN) == TCPIPPacket.TCP_SYN )
                    {
                        int payloadSize = packet.getDataLength();
                        byte[] payload = new byte[payloadSize];
                        if( payloadSize > 0 )
                            System.arraycopy( packet.getBuffer(), packet.getDataOffset(), payload, 0, payloadSize );

                        TCPIPAddress from = createAddress( packet.getSourceIPAddress(), packet.getSourcePort(), packet.getProtocolType() );
                        TCPIPAddress to = createAddress( packet.getDestIPAddress(), packet.getDestPort(), packet.getProtocolType() );
                        notifyForNewConnection( from, to, payload );
                        TCPIPAddressPool.freeAddress( from );
                        TCPIPAddressPool.freeAddress( to );
                    }
                    
                    // Check if any open connection can process this packet
                    // TODO: optimize this to check only new connections
                    if( processPacket( packet ) )
                    {
                        packet.setBuffer( null, 0 );
                        return;
                    }
                }
                else
                {
                    if( packet.getProtocolType() == TCPIPPacket.PT_TCP )
                        Log.e( "WRN IP PACKET", packet.toString() );
                }
                packet.setBuffer( null, 0 );
            }
            catch( MCSException ex )
            {
                Log.e( TAG, "", ex );
            }
        }
    }


    /**
     * processPacket
     * @param packet
     * @return
     * @throws MCSException
     */
    private synchronized boolean processPacket( TCPIPPacket packet ) throws MCSException
    {
        for( int i = 0; i < MAX_CONNECTIONS; i++ )
        {
            if( m_connections[i] != null )
            {
                ConnectionPointTCPIP point = m_connections[i];
                if( point.isIPPacketForMe( packet ) )
                {
                    // m_inBuffer = MemoryPool.getMem( MemoryPool.BufSizeBig, "TCPIPPacket" );
                    // m_bufSize = m_inBuffer.length;
                    point.processPacket( packet );
                    // packet.freeBuffer();
                    // TCPIPPacketPool.freePacket(packet);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * doWriteData
     * @param packet
     */
    private void doWriteData( TCPIPPacket packet )
    {
        byte[] buffer = packet.getBuffer();
        int len = packet.getBufferLength();
        doWriteData( buffer, len, packet.toString() );
        packet.m_isLocked = false;
    }


    private void doWriteData( byte[] buffer, int len, String message )
    {
        synchronized( this )
        {
            if( lowerLayer == null )
                return;
        }
        // Log.d( "OUT IP PACKET", message );
        lowerLayer.writeData( buffer, len );
    }
    
    
    @Override
    public String toString()
    {
        return TAG+" Qsize="+m_outgoingBuffers.size();
    }
}
