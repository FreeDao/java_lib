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

import com.airbiquity.mcs.common.MCSException;
import com.airbiquity.mcs.utils.ByteUtils;

/**
 * Contains data and parameters ot TCPIP packet. Utilizes parsing of TCPIPpackets and creating new TCPIP packets.
 */
public class TCPIPPacket
{
    private static final String TAG = "TCPIPPacket";
    public static final byte TCP_FIN = 1;
    public static final byte TCP_SYN = 2;
    public static final byte TCP_RST = 4;
    public static final byte TCP_PSH = 8;
    public static final byte TCP_ACK = 16;
    public static final byte TCP_URG = 32;
    public static final int TCPProtocolID = 6;
    public static final int UDPProtocolID = 17;
    public static final byte PT_Unknown = 0;
    public static final byte PT_TCP = 1;
    public static final byte PT_UDP = 2;
    public static final String MEM_TAG = "TCPIPPacket";
    static final int MinIPHeaderLen = 20;
    static final int MinTCPHeaderLen = 20;
    static final int UDPHeaderLen = 8;
    boolean m_isLocked = false;
    private static final int DEFAULT_MSS = 536;
    private static final String[] cIPProtocolIDs = { "Reserved", "ICMP", "IGMP", "GGP", "IP-in-IP encapsulation", "5", "TCP", "7", "EGP", "9", "10", "11", "12", "13", "14", "15",
            "16", "UDP" };
    private byte[] m_buffer;
    private int m_totalLength = 0;
    private boolean m_isPacketOK = false;
    private int m_ipHeaderLength = 0;
    private int m_ipProtocolID = -1;
    private byte m_sourceAddress[] = { 0, 0, 0, 0 };
    private byte m_destAddress[] = { 0, 0, 0, 0 };
    private int m_dataOffset = 0;
    private int m_sourcePort = 0;
    private int m_destPort = 0;
    private int m_seqNo = 0;
    private int m_ackNo = 0;
    private int m_flags = 0;
    private int m_window = 0;
    private int m_udpLength = 0;
    private byte m_protocolType = PT_Unknown;
    private int m_packetID = 1;
    private int m_mss = 0;


    // private int m_checksum;
    /**
     * Sets internal memory buffer.
     */
    public void setBuffer( byte[] buffer, int len )
    {
        if( m_isLocked )
        {
            Log.e( "ERROR", "setBuffer() called while packet is locked" );
        }
        m_buffer = buffer;
        m_totalLength = len;
        m_isPacketOK = false;
        m_protocolType = PT_Unknown;
        // Check buffer size
        if( m_totalLength < MinIPHeaderLen )
        {
            return;
        }
        // Check IP packet
        if( isIPHeaderOK() )
        {
            m_isPacketOK = (TCPProtocolID == m_ipProtocolID) ? isTCPHeaderOK() : (UDPProtocolID == m_ipProtocolID) ? isUDPHeaderOK() : false;
        }
    }


    /**
     * Parses TCP header.
     * @return
     */
    private boolean isTCPHeaderOK()
    {
        m_mss = 0;
        // Protocol ID
        if( m_ipProtocolID != TCPProtocolID )
        {
            return false;
        }
        m_protocolType = PT_TCP;
        // Header length
        if( m_totalLength - m_ipHeaderLength < MinTCPHeaderLen )
        {
            return false;
        }
        // Data offset
        int dataOffset = (m_buffer[m_ipHeaderLength + 12] >> 4) & 0x0F;
        m_dataOffset = dataOffset * 4 + m_ipHeaderLength;
        if( m_dataOffset > m_totalLength )
        {
            return false;
        }
        // Source and Destination ports
        m_sourcePort = ByteUtils.getWord( m_buffer, m_ipHeaderLength );
        m_destPort = ByteUtils.getWord( m_buffer, m_ipHeaderLength + 2 );
        // Sequence numbers
        m_seqNo = ByteUtils.getInt( m_buffer, m_ipHeaderLength + 4, 4 );
        m_ackNo = ByteUtils.getInt( m_buffer, m_ipHeaderLength + 8, 4 );
        // The rest of data
        m_flags = m_buffer[m_ipHeaderLength + 13] & 0x3F; // only 6 bits
        m_window = ByteUtils.getInt( m_buffer, m_ipHeaderLength + 14, 2 );
        // m_checksum = ByteUtils.getInt(m_buffer, m_ipHeaderLength + 16, 2);
        // Options
        int pos = MinIPHeaderLen + MinTCPHeaderLen;
        while( pos < m_dataOffset )
        {
            pos += parseOption( pos );
        }
        if( pos != m_dataOffset )
        {
            return false;
        }
        // Check-sum and pseudo header
        int checksum = calculateTCPChecksum();
        if( checksum != 0xFFFF )
        {
            // TODO return false;
            Log.e( TAG, "Bad checksum?" );
        }
        // shall we care about options? Maximum Segment Size is probably
        // important option, but we use minimum segment size which should be
        // supported by all network devices.
        return true;
    }


    /**
     * 
     * @param pos
     * @return
     */
    private int parseOption( int pos )
    {
        switch( m_buffer[pos] )
        {
            case 0: // End Of Option List: A single byte option that marks the end of all options included in this segment.
                    // This only needs to be included when the end of the options doesn't coincide with the end of the TCP
                    // header.
            case 1: // No-Operation: A "pacer" that can be included between options to align a subsequent option on a 32-bit
                    // boundary if needed.
                return 1; // Do nothing
            case 2: // Maximum Segment Size: Conveys the size of the largest segment the sender of the segment wishes to
                    // receive. Used only in connection request (SYN) messages.
                if( (pos + 4 > m_dataOffset) || m_buffer[pos + 1] != 4 )
                {
                    // Error
                    return m_totalLength;
                }
                m_mss = ByteUtils.getWord( m_buffer, pos + 2 );
                return 4;
            default: // TODO: add support for other options
                int len = ByteUtils.toUnsignedInteger( m_buffer[pos + 1] );
                if( len + pos > m_dataOffset )
                {
                    // Error
                    return m_totalLength;
                }
                return len;
        }
    }


    /**
     * Calculates TCP checksum.
     * @return
     */
    private int calculateTCPChecksum()
    {
        int ret = 0;
        // 1. TCP header + data
        int tcpSegmentLength = m_totalLength - m_ipHeaderLength;
        int words = (tcpSegmentLength + 1) / 2; // Padding for possible odd count of bytes in data part of this message
        ret = ByteUtils.sumWords( ret, m_buffer, m_ipHeaderLength, words );
        // 2. Pseudo-header
        ret = ByteUtils.sumWords( ret, m_sourceAddress, 0, 2 );
        ret = ByteUtils.sumWords( ret, m_destAddress, 0, 2 );
        ret += TCPProtocolID;
        ret += tcpSegmentLength;
        // 3. One's complement
        ret = ByteUtils.onesComplement( ret );
        return ret;
    }


    /**
     * Parses UDP header.
     * @return
     */
    private boolean isUDPHeaderOK()
    {
        // Protocol ID
        if( m_ipProtocolID != UDPProtocolID )
        {
            return false;
        }
        m_protocolType = PT_UDP;
        // Header length
        if( m_totalLength - m_ipHeaderLength < UDPHeaderLen )
        {
            return false;
        }
        // Data offset
        m_dataOffset = 8 + m_ipHeaderLength;
        if( m_dataOffset > m_totalLength )
        {
            return false;
        }
        // Source and Destination ports
        m_sourcePort = ByteUtils.getWord( m_buffer, m_ipHeaderLength );
        m_destPort = ByteUtils.getWord( m_buffer, m_ipHeaderLength + 2 );
        // UPD datagram length
        m_udpLength = ByteUtils.getWord( m_buffer, m_ipHeaderLength + 4 );
        if( m_udpLength + m_ipHeaderLength != m_totalLength )
        {
            return false;
        }
        // Check-sum and pseudo header
        int checksum = ByteUtils.getWord( m_buffer, m_ipHeaderLength + 6 ) + 0;
        if( 0 != checksum )
        {
            int calcChecksum = calcUDPChecksum();
            if( 0xFFFF != calcChecksum )
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Calculates UDP checksum.
     * @return
     */
    private int calcUDPChecksum()
    {
        int sum = 0;
        // 1. Pseudo header
        sum = ByteUtils.sumWords( sum, m_sourceAddress, 0, 2 );
        sum = ByteUtils.sumWords( sum, m_destAddress, 0, 2 );
        sum += UDPProtocolID;
        sum += m_udpLength;
        // 2. Data
        for( int i = 0; i <= m_totalLength; i = i + 2 )
        {
            int word = ByteUtils.getWord( m_buffer, m_ipHeaderLength + i );
            sum = sum + word;
        }
        // 3. One's complement
        int ret = ByteUtils.onesComplement( sum );
        return ret;
    }


    /**
     * Parses IP header.
     * @return
     */
    private boolean isIPHeaderOK()
    {
        // IP version
        int version = m_buffer[0] >> 4;
        if( version != 4 )
        {
            return false;
        }
        // Header length
        int len = 4 * (m_buffer[0] & 0x0F);
        if( len > m_totalLength || len < MinIPHeaderLen )
        {
            return false;
        }
        m_ipHeaderLength = len;
        // IP protocol number
        m_ipProtocolID = m_buffer[9];
        if( m_ipProtocolID != TCPProtocolID && m_ipProtocolID != UDPProtocolID )
        {
            // The following line is test code
            printProtocolVersion();
        }
        // Total length
        int totalLength = ByteUtils.getWord( m_buffer, 2 );
        if( totalLength != m_totalLength )
        {
            return false;
        }
        // Source and Destination address
        int i = 0;
        for( ; i < 4; i++ )
        {
            m_sourceAddress[i] = m_buffer[12 + i];
            m_destAddress[i] = m_buffer[16 + i];
        }
        // Checksum
        // int checksum = ByteUtils.getWord( m_buffer, 10 );
        int calculatedSum = calculateIPChecksum();
        if( 65535 != calculatedSum )
        {
            return false;
        }
        // TODO: should we support other protocols?
        return (TCPProtocolID == m_ipProtocolID) || (UDPProtocolID == m_ipProtocolID);
    }


    /**
     * Calculates IP checksum.
     * @return
     */
    private int calculateIPChecksum()
    {
        int ret = 0;
        for( int i = 0; i < m_ipHeaderLength; i += 2 )
        {
            int tmp = ByteUtils.getWord( m_buffer, i );
            // Add checksum
            ret += tmp;
        }
        ret = ByteUtils.onesComplement( ret ); // One's complement sum
        return ret;
    }


    private void printProtocolVersion()
    {
        if( m_ipProtocolID >= 0 && m_ipProtocolID < cIPProtocolIDs.length )
        {
            Log.d( "IP Protocol Version", cIPProtocolIDs[m_ipProtocolID] );
        }
        else
        {
            Log.d( "IP Protocol Version", "Unknown protocol - " + Integer.toString( m_ipProtocolID ) );
        }
    }


    /**
     * Checks if this packet header were parsed succesfully.
     * @return
     */
    public boolean isPacketOK()
    {
        return m_isPacketOK;
    }


    /**
     * Retrieves TCP/IP glags such as SYN, ACK, FIN, RST
     * @return
     */
    public int getFlags()
    {
        return m_flags;
    }


    /**
     * 
     * @return
     */
    public byte[] getDestIPAddress()
    {
        return m_destAddress;
    }


    /**
     * 
     * @return
     */
    public byte[] getSourceIPAddress()
    {
        return m_sourceAddress;
    }


    /**
     * 
     * @return
     */
    public int getDestPort()
    {
        return m_destPort;
    }


    /**
     * 
     * @return
     */
    public int getSourcePort()
    {
        return m_sourcePort;
    }


    /**
     * 
     * @return
     */
    public byte getProtocolType()
    {
        return m_protocolType;
    }


    /**
     * 
     * @return
     */
    public int getSeqNo()
    {
        return m_seqNo;
    }


    /**
     * Exchanges source and destination pairs or IP address and port number.
     */
    public void reverseAddresses()
    {
        byte[] arr = m_sourceAddress;
        m_sourceAddress = m_destAddress;
        m_destAddress = arr;
        int port = m_sourcePort;
        m_sourcePort = m_destPort;
        m_destPort = port;
    }


    /**
     * 
     * @param flags
     */
    public void setFlags( int flags )
    {
        m_flags = flags;
    }


    /**
     * 
     * @param seqNo
     */
    public void setSeqNo( int seqNo )
    {
        m_seqNo = seqNo;
    }


    /**
     * 
     * @param ackNo
     */
    public void setAckNo( int ackNo )
    {
        m_ackNo = ackNo;
    }


    /**
     * 
     * @param size
     */
    public void setWindow( int size )
    {
        m_window = size;
    }


    /**
     * Builds needed IP and TCP headers.
     * @throws MCSException
     */
    public void createIPPacket() throws MCSException
    {
        if( m_isLocked )
        {
            throw new MCSException( "TCPIPPacket is locked while createMessage() is called" );
        }
        // Allocate new buffer if needed
        int len = (m_buffer != null) ? m_totalLength : (MinIPHeaderLen + MinTCPHeaderLen + getOptionsLength());
        if( m_buffer == null )
        {
            // m_buffer = MemoryPool.getMem(len, MEM_TAG);
            m_buffer = new byte[len];
            // Log.w(TAG, "createIPPacket getMem "+len );
            m_totalLength = len;
        }
        // Create TCP/IP headers
        createIPHeader( len );
        createTCPHeader();
        // Calculate check-sum. Pad data with zero for odd data length
        if( m_totalLength < m_buffer.length )
        {
            m_buffer[m_totalLength] = 0;
        }
        int checksum = calculateTCPChecksum();
        checksum = ByteUtils.bitwiseNot( checksum );
        ByteUtils.writeToArray( checksum, m_buffer, m_ipHeaderLength + 16, 2 );
        // Cross-check
        checksum = calculateTCPChecksum();
        if( checksum != 0xFFFF )
        {
            Log.e( TAG, "ERROR while calculating TCP checksum" );
        }
    }


    /**
     * Retrieves number of bytes needed for option fields
     * @return
     */
    private int getOptionsLength()
    {
        int ret = 0;
        if( m_mss != 0 )
        {
            ret += 4;
        }
        // TODO Add any other options
        return ret;
    }


    /**
     * Creates TCP header.
     */
    private void createTCPHeader()
    {
        m_dataOffset = m_ipHeaderLength + MinTCPHeaderLen + getOptionsLength();
        ByteUtils.writeToArray( m_sourcePort, m_buffer, m_ipHeaderLength, 2 );
        ByteUtils.writeToArray( m_destPort, m_buffer, m_ipHeaderLength + 2, 2 );
        ByteUtils.writeToArray( m_seqNo, m_buffer, m_ipHeaderLength + 4, 4 );
        ByteUtils.writeToArray( m_ackNo, m_buffer, m_ipHeaderLength + 8, 4 );
        m_buffer[m_ipHeaderLength + 12] = (byte) ((((m_dataOffset - MinIPHeaderLen) / 4) << 4) & 0xF0);
        m_buffer[m_ipHeaderLength + 13] = (byte) (m_flags & 0x3F);
        ByteUtils.writeToArray( m_window, m_buffer, m_ipHeaderLength + 14, 2 );
        ByteUtils.writeToArray( 0, m_buffer, m_ipHeaderLength + 16, 2 );
        ByteUtils.writeToArray( 0, m_buffer, m_ipHeaderLength + 18, 2 );
        // Options
        if( m_mss > 0 )
        {
            m_buffer[m_ipHeaderLength + 20] = 2;
            m_buffer[m_ipHeaderLength + 21] = 4;
            ByteUtils.writeToArray( m_mss, m_buffer, m_ipHeaderLength + 22, 2 );
        }
        // TODO: add any other supported options
    }


    /**
     * Creates IP header.
     * @param len
     */
    private void createIPHeader( int len )
    {
        m_ipHeaderLength = MinIPHeaderLen;
        int version = 4;
        m_buffer[0] = (byte) ((version << 4) + (m_ipHeaderLength / 4));
        m_buffer[1] = 0; // TOS
        ByteUtils.writeToArray( len, m_buffer, 2, 2 );
        ByteUtils.writeToArray( getNextPacketID(), m_buffer, 4, 2 );
        m_buffer[6] = m_buffer[7] = 0; // offset
        m_buffer[8] = 55; // TTL
        m_buffer[9] = TCPProtocolID; // TODO: add support for UDP
        m_buffer[10] = m_buffer[11] = 0; // Set with zeroes for calculating checksum
        for( int i = 0; i < 4; i++ )
        {
            m_buffer[12 + i] = m_sourceAddress[i];
            m_buffer[16 + i] = m_destAddress[i];
        }
        int checksum = ByteUtils.bitwiseNot( calculateIPChecksum() );
        ByteUtils.writeToArray( checksum, m_buffer, 10, 2 );
        // Test code
        checksum = calculateIPChecksum();
        if( checksum != 0xFFFF )
        {
            Log.e( TAG, "ERROR while recalculating IP Checksum" );
        }
        // End of test code
    }


    /**
     * Generates unique ID of IP packet
     * @return
     */
    private synchronized int getNextPacketID()
    {
        m_packetID++;
        return m_packetID;
    }


    /**
     * Release internal memory buffer for future reuse.
     */
    public void freeBuffer()
    {
        if( m_isLocked )
        {
            Log.e( TAG, "freeBuffer() called while packet is locked" );
        }
        // MemoryPool.freeMem( m_buffer, "TCPIPPacket" );
        // Log.w(TAG, "freeBuffer freeMem ------------" );
        m_buffer = null;
    }


    /**
     * 
     * @return
     */
    public byte[] getBuffer()
    {
        return m_buffer;
    }


    /**
     * 
     * @return
     */
    public int getBufferLength()
    {
        return m_totalLength;
    }


    /**
     * 
     * @return
     */
    public int getAckNo()
    {
        return m_ackNo;
    }


    /**
     * 
     * @return
     */
    public int getDataLength()
    {
        return m_totalLength - m_dataOffset;
    }


    /**
     * 
     * @return
     */
    public int getDataOffset()
    {
        return m_dataOffset;
    }


    /**
     * 
     * @return
     */
    public int getWindow()
    {
        return m_window;
    }


    /**
     * Initializes TCP/IP packet.
     * @param data
     * @param dataSize
     * @param ipProtocolID
     * @param tcpprotocolid2
     * @param addressSource
     * @param addressDest
     * @param portSource
     * @param portDest
     * @throws MCSException
     */
    public void init( byte[] data, int dataSize, int pos, int ipProtocolID, byte[] addressSource, byte[] addressDest, int portSource, int portDest ) throws MCSException
    {
        m_totalLength = dataSize + MinIPHeaderLen + MinTCPHeaderLen;
        m_mss = 0;
        if( m_isLocked )
        {
            Log.e( TAG, "init() called while packet is locked" );
        }
        for( int i = 0; i < 4; i++ )
        {
            m_sourceAddress[i] = addressSource[i];
            m_destAddress[i] = addressDest[i];
        }
        m_sourcePort = portSource;
        m_destPort = portDest;
        // m_buffer = MemoryPool.getMem(m_totalLength, MEM_TAG);
        m_buffer = new byte[m_totalLength];
        // Log.w(TAG, "init getMem "+m_totalLength );
        // DEBUG CODE
        // Fill with non-zeroes to check calculation of checksum
        // for(int k = 0; k < m_buffer.length; k++) {
        // m_buffer[k] = 11;
        // }
        // DEBUG CODE END
        m_flags = 0;
        m_ipHeaderLength = MinIPHeaderLen;
        m_dataOffset = m_ipHeaderLength + MinTCPHeaderLen;
        m_ipProtocolID = ipProtocolID;
        if( data != null && dataSize > 0 )
        {
            System.arraycopy( data, pos, m_buffer, MinTCPHeaderLen + m_ipHeaderLength, dataSize );
        }
    }


    /**
     * Builds a string with properties of TCP/IP packet
     */
    // @Override
    public String toString()
    {
        String ret = addressToString( m_sourceAddress );
        ret += ":";
        ret += String.valueOf( m_sourcePort );
        ret += " -> ";
        ret += addressToString( m_destAddress );
        ret += ":";
        ret += String.valueOf( m_destPort );
        // ret += toString( m_protocolType );
        ret += " (P" + String.valueOf( m_ipProtocolID ) + ")";
        int len = m_totalLength - m_dataOffset;
        ret += " TL " + m_totalLength;
        if( len > 0 )
        {
            ret += " DS " + len;
            ret += " DO " + m_dataOffset;
        }
        ret += " SeqNo " + m_seqNo + " AckNo " + m_ackNo;
        ret += flagsToString();
        return ret;
    }


    /**
     * Creates a string representation of TCP flags.
     * @return
     */
    private String flagsToString()
    {
        String ret = " ";
        if( (TCP_ACK & m_flags) == TCP_ACK )
            ret += " ACK";
        if( (TCP_FIN & m_flags) == TCP_FIN )
            ret += " FIN";
        if( (TCP_SYN & m_flags) == TCP_SYN )
            ret += " SYN";
        if( (TCP_RST & m_flags) == TCP_RST )
            ret += " RST";
        if( (TCP_PSH & m_flags) == TCP_PSH )
            ret += " PSH";
        if( (TCP_URG & m_flags) == TCP_URG )
            ret += " URG";
        return ret;
    }


    /**
     * Creates string for IP address.
     * @param address
     * @return
     */
    private String addressToString( byte[] address )
    {
        String ret = String.valueOf( ByteUtils.toUnsignedInteger( address[0] ) );
        for( int i = 1; i < 4; i++ )
        {
            ret += "." + String.valueOf( ByteUtils.toUnsignedInteger( address[i] ) );
        }
        return ret;
    }


    /**
     * 
     * @return
     */
    public int getOptionCount()
    {
        return m_dataOffset - (MinTCPHeaderLen + MinIPHeaderLen);
    }


    /**
     * Retrieves maximum segment size as specified in TCP header options
     * @return Maximum Segment Size
     */
    public int getMSS()
    {
        return (m_mss == 0) ? DEFAULT_MSS : m_mss;
    }


    /**
     * Sets value of maximum segment size property
     * @param mss Maximum Segment Size
     */
    public void setMSS( int mss )
    {
        m_mss = mss;
    }


    /**
     * 
     * @param pck
     * @return
     */
    public boolean isSameAs( TCPIPPacket pck )
    {
        if( m_ackNo != pck.m_ackNo || m_dataOffset != pck.m_dataOffset || m_destPort != pck.m_destPort || m_flags != pck.m_flags || m_ipHeaderLength != pck.m_ipHeaderLength
                || m_ipProtocolID != pck.m_ipProtocolID ||
                // m_packetID != pck.m_packetID ||
                // m_protocolType != pck.m_protocolType ||
                m_seqNo != pck.m_seqNo || m_sourcePort != pck.m_sourcePort || m_totalLength != pck.m_totalLength || m_udpLength != pck.m_udpLength || m_window != pck.m_window )
        {
            return false;
        }
        for( int i = 0; i < 4; i++ )
        {
            if( m_sourceAddress[i] != pck.m_sourceAddress[i] || m_destAddress[i] != pck.m_destAddress[i] )
            {
                return false;
            }
        }
        return true;
    }


    /**
     * 
     * @param addr
     */
    public void setSourceIPAddress( TCPIPAddress addr )
    {
        System.arraycopy( addr.m_address, 0, m_sourceAddress, 0, 4 );
        m_sourcePort = addr.m_portNumber;
    }


    /**
     * 
     * @param addr
     */
    public void setDestIPAddress( TCPIPAddress addr )
    {
        System.arraycopy( addr.m_address, 0, m_destAddress, 0, 4 );
        m_destPort = addr.m_portNumber;
    }
}
