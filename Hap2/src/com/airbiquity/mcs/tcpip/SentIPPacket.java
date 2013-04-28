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

import java.util.Date;

/**
 * TCP/IP Packet for sending.
 */
public class SentIPPacket
{
    final public TCPIPPacket IPPacket;
    final public int Start;
    final public int End;
    public long AcknowledgmentTime; // Time when we expect this packet to be Acknowledged or consider lost if the Acknowledgment is not received.  
    public int ResentCount;

    /**
     * Constructor.
     * @param packet : packet to send.
     * @param maxTimeout : timeout in milliseconds
     */
    public SentIPPacket( TCPIPPacket packet, int maxTimeout )
    {
        IPPacket = packet;
        Start = packet.getSeqNo();
        End = Start + packet.getDataLength();
        Date dt = new Date();
        AcknowledgmentTime = dt.getTime() + maxTimeout;
        ResentCount = 0;
    }
}
