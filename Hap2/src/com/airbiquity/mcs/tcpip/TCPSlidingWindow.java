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

import java.util.*;

import android.util.Log;

import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.MCSException;

/**
 * Provides functionality of Sliding Window System for sending TCP/IP packets, 
 * acknowledgment of sent packets and resending of unacknowledged TCP/IP packets.
 */
public class TCPSlidingWindow
{
    private static final String TAG = "TCPSlidingWindow";
    private static final int MaxPacketsToSend = 10;
    private static final int MaxPacketsToWait = 8*1024;
    private static final int MaxPacketsToResend = 1;
    private static final int MaxTimeout = 3000; // Milliseconds
    private static final int MaxResendAttempts = 20;
    private static final int MaxResendBeforeSleep = 4;
    private int m_wndSize;
    private int m_startPos;
    private int m_sendNextPos;
    private DataQueueIPPacket m_waitQ;
    private DataQueueSentIPPacket m_sentQ;
    private TCPIPLayer m_writer;
    private int m_lastAck = 0;
    McsLayer m_connectionPoint;


    /**
     * Creates new instance of TCPSlidingWindow.
     * @param writer
     * @param size
     * @param startPos
     */
    public TCPSlidingWindow( TCPIPLayer writer, int size, int startPos, McsLayer connectionPoint )
    {
        m_waitQ = new DataQueueIPPacket( MaxPacketsToWait );
        m_sentQ = new DataQueueSentIPPacket( MaxPacketsToSend * 2 );
        m_writer = writer;
        m_wndSize = size;
        m_startPos = startPos;
        m_sendNextPos = m_startPos;
        m_connectionPoint = connectionPoint;
    }


    /**
     * Resends any unacknowledged TCP/IP packets.
     * @return true if at least one package was resent.
     */
    private boolean resendIPPackets() throws MCSException
    {
        // Check if there is any unacknowledged packet
        int len = m_sentQ.size();
        if( len <= 0 )
            return false;

        // Check resend time
        SentIPPacket pck = m_sentQ.get();
        Date dt = new Date();
        long cur = dt.getTime();
        if( pck.AcknowledgmentTime > cur )
            return false;
        
        // Resend packages
        int count = (len < MaxPacketsToResend) ? len : MaxPacketsToResend;
        for( int i = 0; i < count; i++ )
        {
            pck = m_sentQ.get( i );
            pck.AcknowledgmentTime = cur + MaxTimeout;
            pck.ResentCount++;
            m_writer.sendMessage( pck.IPPacket );
            if( i == 0 && pck.ResentCount >= MaxResendBeforeSleep )
            {
                // TODO: should we close or reset connection?
                /*
                 * // Test code int ln = pck.IPPacket.getBufferLength(); byte[] buffer = new byte[ln];
                 * System.arraycopy(pck.IPPacket.getBuffer(), 0, buffer, 0, ln); TCPIPPacket pack = new
                 * TCPIPPacket(); pack.setBuffer(buffer, ln); if( !pack.isPacketOK() || !pack.isSameAs(pck.IPPacket)
                 * ) { MCSLogger.log( "", "ERROR in outgoing TCPIP packet" ); } buffer = null; pack = null;
                 */
                // Wait for the other side...
                if( pck.ResentCount % MaxResendBeforeSleep == 0 )
                {
                    try
                    {
                        Thread.sleep( 4000 );
                    }
                    catch( InterruptedException e )
                    {
                        // DO nothing
                    }
                }
                if( pck.ResentCount > MaxResendAttempts )
                {
                    // TODO: Reset connection instead of closing connection
                    m_connectionPoint.close();
                    m_connectionPoint = null;
                    // Log.d( "TCPSlidingWindow", "Closing connection!" );
                    return true;
                }
            }
        }
        // Log.d( "TCPSlidingWindow", "Resent packets: " + count );        
        return true;    // Tell that at least one package was resent.
    }


    /**
     * Sends as many packets as possible. This is controlled by the peers's sliding window parameters.
     */
    public synchronized void processIPPackets() throws MCSException
    {
        if( resendIPPackets() )
        {
            // return; // already sent data, do not send other data
        }
        
        if( m_connectionPoint == null )
            return;
        
        // Send as many packets as possible
        int usableLength = usableWindow();
        int waitLength = m_waitQ.size();
        int sentLength = m_sentQ.size();
        while( usableLength > 0 && sentLength < MaxPacketsToSend && waitLength > 0 )
        {
            TCPIPPacket packet = m_waitQ.get();
            if( packet.getDataLength() > usableLength )
                break; // Not enough room for packet

            SentIPPacket pck = new SentIPPacket( packet, MaxTimeout );
            m_sentQ.insert( pck );
            waitLength--;
            sentLength++;
            m_waitQ.delete();
            m_sendNextPos = pck.End + 1;
            usableLength = usableWindow();
            m_writer.sendMessage( packet );
        }
    }


    /**
     * Acknowledges receiving of sent TCP/IP packets. Frees theses packets for future reuse.
     * @param byteToAcknowledge
     * @param newWndSize
     * @param flags
     */
    public synchronized void acknowledgeIPPacket( int byteToAcknowledge, int newWndSize, int flags )
    {
        if( m_sentQ.size() == 0 || m_connectionPoint == null )
            return; // Nothing to acknowledge

        if( byteToAcknowledge < m_startPos || byteToAcknowledge > m_sendNextPos )
        {
            if( m_lastAck == byteToAcknowledge )
                Log.w( TAG, "Same ACK received "+m_lastAck );
            else
                Log.e( TAG, "Invalid ACK "+m_lastAck );
            return; // Invalid range
        }
        
        if( (byteToAcknowledge + newWndSize) < (m_startPos + m_wndSize - 1) )
            Log.e( TAG, "TCP sliding window goes beyond MaxInt and becomes negative" );
        
        m_lastAck = byteToAcknowledge;
        if( m_sentQ.size() > 0 )
        {
            // Slide the window
            SentIPPacket pck = getSentPacket( false );
            while( pck.End <= byteToAcknowledge )
            {
                // Remove any acknowledged packets
                try
                {
                    m_sentQ.delete();
                }
                catch( MCSException e )
                {
                    // Should never happen!
                }

                pck.IPPacket.freeBuffer();
                m_startPos = pck.End + 1;
                
                // Check next packet
                if( m_sentQ.size() > 0 )
                    pck = getSentPacket( false );
                else
                    break;
            }
        }
        m_wndSize = newWndSize;
    }


    /**
     * getSentPacket
     * @param deletePacket
     * @return
     */
    private SentIPPacket getSentPacket( boolean deletePacket )
    {
        SentIPPacket pck = null;
        try
        {
            pck = deletePacket ? m_sentQ.delete() : m_sentQ.get();
        }
        catch( MCSException e )
        {
            Log.e( TAG, "", e );
        }
        return pck;
    }


    /**
     * Adds TCP/IP packet to the output queue.
     * @param packet
     */
    public synchronized void addIPPacket( TCPIPPacket packet ) throws MCSException
    {
        if( m_connectionPoint == null )
        {
            Log.e( "Warning", "Output data IP packet is sent after connection is closed" );
            packet.freeBuffer();
            return;
        }
        
        if( packet.getBuffer() == null )
        {
            Log.e( "ERROR", "Empty IP Packet buffer" );
            return;
        }
        m_waitQ.insert( packet );
    }


    /**
     * usableWindow
     * @return
     */
    public int usableWindow()
    {
        return m_wndSize - m_sendNextPos + m_startPos;
    }


    /**
     * close
     */
    public synchronized void close()
    {
        m_connectionPoint = null;
        while( m_sentQ.size() > 0 )
        {
            SentIPPacket pck = getSentPacket( true );
            pck.IPPacket.freeBuffer();
        }
        
        while( m_waitQ.size() > 0 )
        {
            try
            {
                TCPIPPacket pck = m_waitQ.delete();
                pck.freeBuffer();
            }
            catch( MCSException e )
            {
                Log.e( TAG, "", e );
            }
        }
    }
}
