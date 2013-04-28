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
package com.airbiquity.connectionmgr.msp;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * MIP Serial Protocol (MSP) Message. 
 * Large Messages can be split into segments.
 * See Panasonic_DA2_MIP_Serial_protocol_specification.doc
 */
public class MspMsg
{
    //private static final String TAG = "MspMsg";
    //public static final int MAX_LEN = MemoryPool.BufferSizeBig - MspSegment.HEADER_SZ - MspSegment.CRC_SZ;
    //public final int            segQntt;    // Quantity of the segments.
    public final byte[]         payload;    // Payload.
    public final short          msgType;    // Message Type.
    public final short          tranId;     // Transaction ID.

    
    /**
     * Contract MSP Request object with the given parameters.
     * @param payload
     * @param msgType
     * @param tranId
     */
    public MspMsg( byte[] payload, short msgType, short tranId )
    {
        this.payload = payload;
        this.msgType = msgType ; 
        this.tranId  = tranId ; 
    }


    /**
     * Construct MSP message from given list of segments.
     */
    public MspMsg( ConcurrentLinkedQueue<MspSegment> segs )
    {
        MspSegment head = segs.peek();
        tranId = head.tranId;
        msgType = head.msgDef;
        //segQntt = segs.size();

        int msgPayloadSize = 0;
        for( MspSegment seg : segs )
            msgPayloadSize += seg.payloadSz;

        payload = new byte[msgPayloadSize];

        int i = 0;
        for( MspSegment seg : segs )
        {
            byte[] segPayload = seg.payload;
            System.arraycopy( segPayload, 0, payload, i, segPayload.length );
            i += segPayload.length;
        }        
    }    
}
