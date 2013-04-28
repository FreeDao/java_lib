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

//import java.util.Random;
import android.util.Log;

import com.airbiquity.mcs.common.IMCSConnectionAddress;
import com.airbiquity.mcs.common.AbstractMcsLayer;
import com.airbiquity.mcs.common.MCSException;
import com.airbiquity.mcs.utils.*;

/**
 * ConnectionPoint utilizes maintaining of TCP/IP connection by sending necessary SYN, ACK and FIN packets,
 * resending of packets and maintaining of Sliding Window System. 
 * It extracts incoming data data for upper layers and creates IP packets for outgoing data.
 */
public class ConnectionPointTCPIP extends AbstractMcsLayer implements Runnable
{
    private static final String TAG = "ConnectionPointTCPIP";
    private static final int STATE_LISTEN = 1;
    private static final int STATE_RECEIVED_SYN = 2;
    private static final int STATE_HANDSHAKEN = 3;
    private static final int STATE_SENT_SYN = 4;
    private static final int TCPWindowSize = 4096;
    private static final int MaxQueueSize = 16;
    private static final int MAX_DATAGRAM_SIZE = 4096; // MemoryPool.BufSizeBig;
    private static final int MAX_SEGMENT_SIZE = MAX_DATAGRAM_SIZE - (TCPIPPacket.MinIPHeaderLen + TCPIPPacket.MinTCPHeaderLen);
    private TCPIPLayer m_writer;
    private TCPIPAddress m_fromAddress;
    private TCPIPAddress m_toAddress;
    private int m_senderInitialSequenceNumber;
    private int m_senderAcknowledgementNumber;
    private int m_myInitialSequenceNumber;
    private int m_myAcknowledgementNumber;
    private int m_state = STATE_LISTEN;
    private int m_flags; // Initialized when starting processing of packet
    private DataQueueArr m_inDataBuffers = new DataQueueArr( MaxQueueSize );
    private DataQueueInt m_inDataSizes = new DataQueueInt( MaxQueueSize );
    private TCPSlidingWindow m_slidingWindow;
    private boolean m_isClosed = false;
    private boolean m_isStarted = false;
    private Thread m_thread;
    private int m_maximumSegmentSize;


    /**
     * Creates new instance of ConnectionPoint.
     * @param writer Underlying TCPIP layer
     * @param fromAddress Source TCP/IP address and port
     * @param toAddress Destination TCP/IP address and port
     * @throws MCSException
     */
    public ConnectionPointTCPIP( TCPIPLayer writer, IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress ) throws MCSException
    {
        m_writer = writer;
        
        if( !(fromAddress instanceof TCPIPAddress) || !(toAddress instanceof TCPIPAddress) )
            throw new MCSException( "Non-TCPIP connection addresses" );

        m_fromAddress = TCPIPAddressPool.copyAddress( (TCPIPAddress) fromAddress );
        m_toAddress = TCPIPAddressPool.copyAddress( (TCPIPAddress) toAddress );
        
        if( !m_fromAddress.m_isAddressSpecified || !m_toAddress.m_isAddressSpecified )
            throw new MCSException( "Unspecified addresses are not supported yet!" );

        if( !m_fromAddress.m_isPortSpecified || !m_toAddress.m_isPortSpecified )
            throw new MCSException( "Unspecified ports are not supported yet!" );

        m_thread = new Thread( this );
        m_thread.start();
    }


    /**
     * Checks if provided packet is for this connection.
     * @param packet Packet to be checked
     * @return True if this connection can handle provide packet
     */
    public boolean isIPPacketForMe( TCPIPPacket packet )
    {
        if( m_isClosed )
            return false;

        if( isAddressOK( m_fromAddress, packet.getSourceIPAddress(), packet.getSourcePort(), packet.getProtocolType() ) )
        {
            if( isAddressOK( m_toAddress, packet.getDestIPAddress(), packet.getDestPort(), packet.getProtocolType() ) )
                return true;
        }
        return false;
    }


    /**
     * Checks if provided TCPIPAddress is same as provided port, protocol type and address.
     * @param tcpipAddress
     * @param address
     * @param port
     * @param protocolType
     * @return
     */
    private boolean isAddressOK( TCPIPAddress tcpipAddress, byte[] address, int port, byte protocolType )
    {
        boolean isOK = tcpipAddress.canHandle( address ) && tcpipAddress.canHandle( port, protocolType );
        return isOK;
    }


    /**
     * Writes data to this layer. (Called from the Upper layer)
     * Wraps provided data into TCPIP packet and sends it to Sliding Window for processing.
     * @param buffer : buffer that contain the data to write.
     * @param size : size of the data in the buffer.
     */    
    public void writeData( byte[] buffer, int size )
    {
        try
        {
            int pos = 0;
            while( pos < size )
            {
                TCPIPPacket packet = createPacket( buffer, size, pos );
                packet.isPacketOK();
                if( m_slidingWindow == null )
                    m_slidingWindow = new TCPSlidingWindow( m_writer, packet.getWindow(), m_myInitialSequenceNumber + 1, this );

                m_slidingWindow.addIPPacket( packet );
                pos += packet.getDataLength();
            }
        }
        catch( MCSException ex )
        {
            Log.e( TAG, "", ex );
        }
    }


    /**
     * Creates TCP/IP packet for provided data.
     * 
     * @param buffer
     * @param size
     * @return
     * @throws MCSException
     */
    private TCPIPPacket createPacket( byte[] buffer, int size, int pos ) throws MCSException
    {
        int len = size - pos;
        if( len > m_maximumSegmentSize )
            len = m_maximumSegmentSize;

        TCPIPPacket packet = new TCPIPPacket();
        packet.setBuffer( null, 0 );
        packet.init( buffer, len, pos, TCPIPPacket.TCPProtocolID, m_toAddress.m_address, m_fromAddress.m_address, m_toAddress.m_portNumber, m_fromAddress.m_portNumber );
        packet.setSeqNo( m_myAcknowledgementNumber );
        packet.setAckNo( m_senderAcknowledgementNumber );
        packet.setWindow( TCPWindowSize );
        packet.setFlags( TCPIPPacket.TCP_ACK | TCPIPPacket.TCP_PSH );
        packet.createIPPacket();
        m_myAcknowledgementNumber += len;
        return packet;
    }


    /**
     * Reads data from this layer. (Called from the Upper layer)
     * @param buffer : buffer where to put the data.
     * @param size : size of the buffer.
     * @return size of the data written into the buffer.
     */    
    public int readData( byte[] buffer, int size )
    {
        synchronized( this )
        {
            if( m_inDataBuffers.isEmpty() )
                return 0;

            try
            {
                byte[] cur = m_inDataBuffers.get();
                int bufSize = m_inDataSizes.get();
                if( size < bufSize )
                    throw new MCSException( "Buffer size too small" );

                System.arraycopy( cur, 0, buffer, 0, bufSize );
                m_inDataBuffers.delete();
                m_inDataSizes.delete();
                return bufSize;
            }
            catch( MCSException ex )
            {
                Log.e( TAG, "", ex );
                return 0;
            }
        }
    }


    /**
     * Checks if a pair of addresses is same as connection's input and output addresses.
     * @param fromAddress
     * @param toAddress
     * @return
     */
    public boolean canHandle( IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress )
    {
        // Check parameters
        if( m_fromAddress == null || m_toAddress == null || fromAddress == null || toAddress == null )
            return false;

        if( !(fromAddress instanceof TCPIPAddress) || !(toAddress instanceof TCPIPAddress) )
            return false;

        // Check subset
        TCPIPAddress addrFrom = (TCPIPAddress) fromAddress;
        TCPIPAddress addrTo = (TCPIPAddress) toAddress;
        return addrFrom.isSubsetOf(m_fromAddress)  &&  addrTo.isSubsetOf(m_toAddress);
    }


    /**
     * Get "from" address.
     * @return
     */
    public IMCSConnectionAddress getFromAddress()
    {
        return m_fromAddress;
    }


    /**
     * Get "to" address.
     * @return
     */
    public IMCSConnectionAddress getToAddress()
    {
        return m_toAddress;
    }


    /**
     * Processes incoming TCPIP packet
     * @param packet
     * @throws MCSException
     */
    public void processPacket( TCPIPPacket packet ) throws MCSException
    {
        m_flags = packet.getFlags();
        try
        {
            switch( m_state )
            {
                case STATE_LISTEN:
                    processModeListen( packet );
                    break;
                case STATE_RECEIVED_SYN:
                    processModeReceivedSyn( packet );
                    break;
                case STATE_HANDSHAKEN:
                    processModeHandshaken( packet );
                    break;
                case STATE_SENT_SYN:
                    processModeSentSyn( packet );
                    break;
            }
        }
        catch( MCSException ex )
        {
            Log.e( TAG, "", ex );
        }
    }


    /**
     * Process Mode Sent Syn
     * @param packet
     * @throws MCSException
     */
    private void processModeSentSyn( TCPIPPacket packet ) throws MCSException
    {
        if( (m_flags & (TCPIPPacket.TCP_SYN | TCPIPPacket.TCP_ACK)) == (TCPIPPacket.TCP_SYN | TCPIPPacket.TCP_ACK) )
        {
            // Transmission control block
            m_senderInitialSequenceNumber = packet.getSeqNo();
            m_senderAcknowledgementNumber = m_senderInitialSequenceNumber + 1;
            m_slidingWindow = new TCPSlidingWindow( m_writer, packet.getWindow(), m_myInitialSequenceNumber + 1, this );
            m_maximumSegmentSize = packet.getMSS();
            // Create acknowledgment packet
            packet.reverseAddresses();
            packet.setFlags( TCPIPPacket.TCP_ACK );
            packet.setSeqNo( m_myInitialSequenceNumber );
            packet.setAckNo( m_senderInitialSequenceNumber + 1 );
            packet.setWindow( TCPWindowSize );
            packet.setMSS( MAX_SEGMENT_SIZE );
            byte[] buf = packet.getBuffer();
            packet.setBuffer( null, 0 );
            packet.createIPPacket();
            // Send the acknowledgment
            m_writer.sendMessage( packet );
            packet.freeBuffer();
            packet.setBuffer( buf, 0 );
            m_state = STATE_HANDSHAKEN;
            synchronized( this )
            {
                m_isStarted = true;
                // MCSLogger.log("ConnectionPointTCPIP", "SENT_SYN --> HANDSHAKEN");
            }
        }
    }


    /**
     * Processes IP packet while internal state machine is in LISTEN mode.
     * @param packet
     * @throws MCSException
     */
    private void processModeListen( TCPIPPacket packet ) throws MCSException
    {
        // Start communication
        if( (m_flags & TCPIPPacket.TCP_SYN) == TCPIPPacket.TCP_SYN )
        {
            // Transmission control block
            m_senderInitialSequenceNumber = packet.getSeqNo();
            m_senderAcknowledgementNumber = m_senderInitialSequenceNumber + 1;
            m_myInitialSequenceNumber = 0;
            // m_myInitialSequenceNumber = m_rand.nextInt();
            m_myAcknowledgementNumber = m_myInitialSequenceNumber + 1;
            m_slidingWindow = new TCPSlidingWindow( m_writer, packet.getWindow(), m_myInitialSequenceNumber + 1, this );
            m_maximumSegmentSize = packet.getMSS();
            // Create acknowledgment packet
            packet.reverseAddresses();
            packet.setFlags( TCPIPPacket.TCP_ACK + TCPIPPacket.TCP_SYN );
            packet.setSeqNo( m_myInitialSequenceNumber );
            packet.setAckNo( m_senderInitialSequenceNumber + 1 );
            packet.setWindow( TCPWindowSize );
            packet.setMSS( MAX_SEGMENT_SIZE );
            byte[] buf = packet.getBuffer();
            packet.setBuffer( null, 0 );
            packet.createIPPacket();
            // Send the acknowledgment
            m_writer.sendMessage( packet );
            packet.freeBuffer();
            packet.setBuffer( buf, 0 );
            m_state = STATE_RECEIVED_SYN;
        }
        else if( (m_flags & TCPIPPacket.TCP_FIN) == TCPIPPacket.TCP_FIN )
        {
            sendFINPacket( packet );
        }
        else
        {
            resetConnection( packet );  // Error - reset connection
        }
    }


    /**
     * Sends FIN IP packet.
     * @param packet
     */
    private void sendFINPacket( TCPIPPacket packet )
    {
        // Send FIN message
        packet.reverseAddresses();
        packet.setFlags( TCPIPPacket.TCP_FIN | TCPIPPacket.TCP_ACK );
        packet.setSeqNo( m_myAcknowledgementNumber );
        packet.setAckNo( m_senderAcknowledgementNumber );
        packet.setWindow( TCPWindowSize );
        try
        {
            packet.createIPPacket();
            m_writer.sendMessage( packet );
        }
        catch( MCSException ex )
        {
            Log.e( TAG, "", ex );
        }
    }


    /**
     * Sends RST packet and changes internal state machine's state.
     * @param packet
     */
    private void resetConnection( TCPIPPacket packet )
    {
        // Send RST message
        packet.reverseAddresses();
        packet.setFlags( TCPIPPacket.TCP_RST );
        packet.setSeqNo( 0 );
        packet.setAckNo( 0 );
        packet.setWindow( 0 );
        try
        {
            packet.createIPPacket();
            m_writer.sendMessage( packet );
        }
        catch( MCSException ex )
        {
            Log.e( TAG, "", ex );
        }
        m_state = STATE_LISTEN;
    }


    /**
     * Process packet while internal state is SYN Received
     * @param packet
     * @throws MCSException
     */
    private void processModeReceivedSyn( TCPIPPacket packet ) throws MCSException
    {
        if( (m_flags & TCPIPPacket.TCP_ACK) == TCPIPPacket.TCP_ACK )
        {
            // Check acknowledgment number
            if( packet.getAckNo() == m_myInitialSequenceNumber + 1 )
            {
                m_state = STATE_HANDSHAKEN;
                synchronized( this )
                {
                    m_isStarted = true;
                    // MCSLogger.log("ConnectionPointTCPIP", "RECEIVED_SYN --> HANDSHAKEN");
                }
                return;
            }
        }
        
        if( m_flags != TCPIPPacket.TCP_SYN )
            resetConnection( packet );
        else
            processModeListen( packet );
    }


    /**
     * Processes IP packet when internal state is ESTABLISHED.
     * @param packet
     */
    private void processModeHandshaken( TCPIPPacket packet )
    {
        if( (m_flags & TCPIPPacket.TCP_RST) == TCPIPPacket.TCP_RST )
        {
            closeConnection( packet );
            return;
        }
        
        if( (m_flags & TCPIPPacket.TCP_SYN) == TCPIPPacket.TCP_SYN )
            return; // Ignore this IP packet
        
        if( (m_flags & TCPIPPacket.TCP_ACK) == TCPIPPacket.TCP_ACK )
            m_slidingWindow.acknowledgeIPPacket( packet.getAckNo(), packet.getWindow(), packet.getFlags() );
        
        if( packet.getDataLength() > 0 )
            processData( packet );
        
        if( (m_flags & TCPIPPacket.TCP_FIN) == TCPIPPacket.TCP_FIN )
        {
            closeConnection( packet );
            return;
        }
    }


    /**
     * Processes data IP packet.
     * @param packet
     */
    private void processData( TCPIPPacket packet )
    {
        // Check sequence number
        int seqNo = packet.getSeqNo();
        if( seqNo != m_senderAcknowledgementNumber )
            return; // Wait for missed packages

        // Copy data
        byte[] source = packet.getBuffer();
        int offset = packet.getDataOffset();
        int size = packet.getDataLength();
        try
        {
            // Get data
            byte[] data = new byte[size];
            System.arraycopy( source, offset, data, 0, size );
            m_inDataBuffers.insert( data );
            m_inDataSizes.insert( size );
            m_senderAcknowledgementNumber += size;
            sendAcknowledgementPacket( packet );
            // Send notification
            tellDataReceived();
        }
        catch( MCSException ex )
        {
            Log.e( TAG, "", ex );
        }
    }


    /**
     * Creates ACK packet.
     * @param packet
     * @throws MCSException
     */
    private void sendAcknowledgementPacket( TCPIPPacket packet ) throws MCSException
    {
        packet.reverseAddresses();
        packet.setSeqNo( m_myAcknowledgementNumber );
        packet.setAckNo( m_senderAcknowledgementNumber );
        packet.setWindow( TCPWindowSize );
        packet.setFlags( TCPIPPacket.TCP_ACK );
        byte[] buf = packet.getBuffer(); // store buffer
        packet.setBuffer( null, 0 );
        packet.createIPPacket();
        m_writer.sendMessage( packet );
        // restore buffer
        packet.freeBuffer();
        packet.setBuffer( buf, 0 );
    }


    /**
     * Closes IP connection.
     * @param packet
     */
    private void closeConnection( TCPIPPacket packet )
    {
        m_state = STATE_LISTEN;
        // sendFINPacket( packet ); TODO: SS: I have commented it out because it causes NullPonterException (see below) on 1st run after "clear data". Read about FIN and decide if we really need to send it. If yes figure out how and when.   
        try
        {
            m_writer.closeConnection( this );
        }
        catch( MCSException ex )
        {
            Log.e( TAG, "", ex );
        }
    }
    /*
        10-04 11:37:50.953: E/AndroidRuntime(9447): java.lang.NullPointerException
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.slip.SLIPLayer.writeData(SLIPLayer.java:419)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.TCPIPLayer.doWriteData(TCPIPLayer.java:360)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.TCPIPLayer.doWriteData(TCPIPLayer.java:347)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.TCPIPLayer.sendMessage(TCPIPLayer.java:179)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.ConnectionPointTCPIP.sendFINPacket(ConnectionPointTCPIP.java:385)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.ConnectionPointTCPIP.closeConnection(ConnectionPointTCPIP.java:540)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.ConnectionPointTCPIP.processModeHandshaken(ConnectionPointTCPIP.java:471)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.ConnectionPointTCPIP.processPacket(ConnectionPointTCPIP.java:273)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.TCPIPLayer.processPacket(TCPIPLayer.java:328)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.TCPIPLayer.processIncommingData(TCPIPLayer.java:267)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at com.airbiquity.mcs.tcpip.TCPIPLayer.run(TCPIPLayer.java:239)
        10-04 11:37:50.953: E/AndroidRuntime(9447):     at java.lang.Thread.run(Thread.java:1019)
     */

    /**
     * Worker thread for processing outgoing TCP/IP packets and possibly resending unacknowledged packets.
     */
    public void run()
    {
        while( true )
        {
            try
            {
                Thread.sleep( 10 );
            }
            catch( InterruptedException ex )
            {
                return;
            }
            synchronized( this )
            {
                if( m_isClosed )
                    break;

                if( !m_isStarted )
                    continue;

                try
                {
                    m_slidingWindow.processIPPackets();
                }
                catch( MCSException ex )
                {
                    Log.e( TAG, "", ex );
                    close();
                }
            }
        }
    }


    public void close()
    {
        synchronized( this )
        {
            if( m_isClosed )
                return;
            m_isClosed = true;
        }
        tellConnectionClosed();
        removeAllListeners();
        try
        {
            while( !m_inDataBuffers.isEmpty() )
            {
                m_inDataBuffers.delete();
            }
            if( m_slidingWindow != null )
                m_slidingWindow.close();

            // Send FIN packet
//            TCPIPPacket fin = new TCPIPPacket();
//            fin.setSourceIPAddress( m_toAddress ); // m_toAdderss is local address
//            fin.setDestIPAddress( m_fromAddress ); // m_fromAddress is remote address
//            fin.setFlags( TCPIPPacket.TCP_FIN );
//            fin.setSeqNo( m_myAcknowledgementNumber );
//            fin.setAckNo( m_senderAcknowledgementNumber );
//            fin.setAckNo( m_senderAcknowledgementNumber );
//            fin.setWindow( TCPWindowSize );
//            fin.setBuffer( null, 0 );
//            fin.createIPPacket();
            
            // Send SYN package
            // m_writer.sendMessage( fin ); TODO: SS: I have commented it out because it causes NullPonterException (see below) on 1st run after "clear data". Read about FIN and decide if we really need to send it. If yes figure out how and when.
            
            // m_state = STATE_SENT_SYN;
            TCPIPAddressPool.freeAddress( m_fromAddress );
            TCPIPAddressPool.freeAddress( m_toAddress );
            m_fromAddress = null;
            m_toAddress = null;
        }
        catch( MCSException ex )
        {
            Log.e( TAG, "", ex );
        }
    }
    
//    10-08 10:24:20.292: E/AndroidRuntime(15668): FATAL EXCEPTION: TCPIPLayer
//    10-08 10:24:20.292: E/AndroidRuntime(15668): java.lang.NullPointerException
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.slip.SLIPLayer.writeData(SLIPLayer.java:419)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.TCPIPLayer.doWriteData(TCPIPLayer.java:360)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.TCPIPLayer.doWriteData(TCPIPLayer.java:347)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.TCPIPLayer.sendMessage(TCPIPLayer.java:179)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.ConnectionPointTCPIP.close(ConnectionPointTCPIP.java:634)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.TCPIPLayer.closeConnection(TCPIPLayer.java:126)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.ConnectionPointTCPIP.closeConnection(ConnectionPointTCPIP.java:543)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.ConnectionPointTCPIP.processModeHandshaken(ConnectionPointTCPIP.java:471)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.ConnectionPointTCPIP.processPacket(ConnectionPointTCPIP.java:273)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.TCPIPLayer.processPacket(TCPIPLayer.java:328)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.TCPIPLayer.processIncommingData(TCPIPLayer.java:267)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at com.airbiquity.mcs.tcpip.TCPIPLayer.run(TCPIPLayer.java:239)
//    10-08 10:24:20.292: E/AndroidRuntime(15668):    at java.lang.Thread.run(Thread.java:1019)
    


    public void sendConnect()
    {
        if( m_state != STATE_LISTEN && m_state != STATE_SENT_SYN )
        {
            Log.e( "TCPIPConnectionPoint sendConnect()", "Invalid internal state: " + m_state );
            return;
        }
        try
        {
            m_myInitialSequenceNumber = 0;
            // m_myInitialSequenceNumber = m_rand.nextInt();
            m_myAcknowledgementNumber = m_myInitialSequenceNumber + 1;
            // Prepare SYN package
            TCPIPPacket packet = new TCPIPPacket();
            packet.setBuffer( null, 0 );
            packet.setSourceIPAddress( m_toAddress ); // m_toAdderss is local address
            packet.setDestIPAddress( m_fromAddress ); // m_fromAddress is remote address
            packet.setFlags( TCPIPPacket.TCP_SYN );
            packet.setSeqNo( m_myInitialSequenceNumber );
            packet.setAckNo( 0 );
            packet.setWindow( TCPWindowSize );
            packet.setMSS( MAX_SEGMENT_SIZE );
            packet.createIPPacket();
            // Send SYN package
            m_writer.sendMessage( packet );
            m_state = STATE_SENT_SYN;
        }
        catch( MCSException ex )
        {
            Log.e( TAG, "", ex );
        }
    }


    public boolean isReady()
    {
        return m_state == STATE_HANDSHAKEN;
    }
}
