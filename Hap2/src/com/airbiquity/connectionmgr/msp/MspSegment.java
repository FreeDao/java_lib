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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;

import com.airbiquity.connectionmgr.util.CRC16;
import com.airbiquity.connectionmgr.util.MspByteUtil;

/**
 * MSP Message Segment.
 * See Panasonic_DA2_MIP_Serial_protocol_specification.doc
 */
public class MspSegment
{
    private static final String TAG = "MspSegment";

    // Size of each header field.
    private final static int SIGNATURE_SZ = 4;  // Size of MSP-signature field.
    private final static int VERSION_SZ   = 2;  // Size of Protocol-Version field.
    private final static int BODY_LEN_SZ  = 4;  // Size of Segment-Payload-Size field.
    private final static int MSG_TYPE_SZ  = 2;  // Size of Message-Type field.
    private final static int SEG_IDX_SZ   = 2;  // Size of Segment-Index (aka Segment-Number) field.
    private final static int SEGS_QNTT_SZ = 2;  // Size of Segments-Quantity (aka Number-of-Segments) field.
    private final static int TRAN_ID_SZ   = 2;  // Size of Transaction-ID field.    

    /** Total size of the header fields */
    public final static int HEADER_SZ = SIGNATURE_SZ + VERSION_SZ + BODY_LEN_SZ + MSG_TYPE_SZ + 
                                         SEG_IDX_SZ + SEGS_QNTT_SZ + TRAN_ID_SZ;
    
    /** Size of CRC field. (CRC goes into the footer not the header) */ 
    public  final static int CRC_SZ = 2;

    // From Imai-san: Due to the head unit buffer limitation, 
    // I think we want to ensure that each segment contains a maximum of: 
    // <Panasonic MTU> - <IPCL Header> - <IPCL Footer> - <MSP Segment Header and CRC> = 1024 - 14 - 10 - 10 = 990 bytes.
    public static final int MAX_SEGMENT_SIZE = 984; // 984 = 990/8*8;
    public static final int MAX_SEGMENT_PAYLOAD_SIZE = MAX_SEGMENT_SIZE - HEADER_SZ - CRC_SZ;
    
    // Header fields.
    public final int   signature; // MSP signature - indicates start of a MIP Serial Protocol message.
    public final short version  ; // Protocol-Version
    public final int   payloadSz; // Segment-Payload-Size
    public final short msgDef   ; // Message-Type 
    public final short segIdx   ; // Segment-Index (aka Segment-Number) 
    public final short segsQntt ; // Segments-Quantity (aka Number-of-Segments) 
    public final short tranId   ; // Transaction-ID 
    
    public final byte[] payload;  // Segment's Payload. 
    
    public final short crcRead  ;      // CRC-16 that we have read from the CRC filed in the input data.
    public final short crcCalculated ; // CRC-16 that we have calculated.


    /**
     * Constructs MSP Segment from the given byte array.
     * @param buf : buffer that contains payload for the Segment.
     */
    public MspSegment( byte[] buf )
    {
        int i = 0;
        signature = MspByteUtil.getInt  ( buf, i );  i += SIGNATURE_SZ;                
        version   = MspByteUtil.getShort( buf, i );  i += VERSION_SZ;
        payloadSz = MspByteUtil.getInt  ( buf, i );  i += BODY_LEN_SZ;
        msgDef    = MspByteUtil.getShort( buf, i );  i += MSG_TYPE_SZ;        
        segIdx    = MspByteUtil.getShort( buf, i );  i += SEG_IDX_SZ;        
        segsQntt  = MspByteUtil.getShort( buf, i );  i += SEGS_QNTT_SZ;        
        tranId    = MspByteUtil.getShort( buf, i );  i += TRAN_ID_SZ;
        
        payload = new byte[payloadSz];        
        System.arraycopy( buf, i, payload, 0, payload.length );  i += payloadSz;
        
        crcRead = MspByteUtil.getShort( buf, i );
        crcCalculated = CRC16.getCRC16( buf, i );
    }



    /**
     * Get length of this segment (in bytes). That includes headers, payload, and CRC.
     * @return
     */
    public int getTotalSize()
    {
        return payloadSz + HEADER_SZ + CRC_SZ;
    }


    /**
     * Check if CRC is valid.
     * @return true if so.
     */
    public boolean checkCRC()
    {
        return crcRead == crcCalculated;
    }


    /**
     * Get command type.
     * @return value corresponding to a MspConst.CMD_TYPE_* constant. 
     */
    public byte getCmdType()
    {
        return (byte) ((msgDef >>> 12) & 0xf );
    }


    // ------------------------------ Static methods ---------------------------
    
    /**
     * Create a short message that fits into a single segment.
     * @param buf : buffer that contains message body.
     * @param offset : offset to the beginning of the message body in the buffer.
     * @param len : length of the message body in the buffer.
     * @param msgType : Message Type
     * @param segNum : segment serial number (1 based)
     * @param segQntt : segments quantity
     * @param tranId : Transaction ID 
     * @return byte[] that contain the message.
     */
    public static byte[] createShortMsg( byte[] buf, int offset, int len, short msgType, 
                                         short segNum, short segQntt, short tranId )
    {
        try
        {
            final int packet_size = MspSegment.HEADER_SZ + len + MspSegment.CRC_SZ;
            ByteArrayOutputStream bos = new ByteArrayOutputStream( packet_size );
            DataOutputStream dos = new DataOutputStream( bos );
            writeHeader( len, msgType, segNum, segQntt, tranId, dos );
            dos.write( buf, offset, len );
            dos.writeShort( 0 );    // Write fake CRC to make the length correct - it will be overwritten later.
            byte[] segData = bos.toByteArray();
            dos.close();
            bos.close();
            int headerAndPayloadSz = segData.length - CRC_SZ;   // Header+Payload = TotalSize-CrcSize  
            short crc = CRC16.getCRC16( segData, headerAndPayloadSz );
            byte[] crc_bytes = MspByteUtil.getBytes( crc );
            System.arraycopy( crc_bytes, 0, segData, headerAndPayloadSz, CRC_SZ );            
            return segData;
        }
        catch( IOException e )
        {
            Log.e( TAG, "", e ); // As we are using ByteArrayOutputStream - this should never happen.
            return null;
        }        
    }

    
    /**
     * Create a message that has 2 byte payload. 
     * @param tranId : Transaction ID (to match response message to a request message).
     * @param msgDef : Message definition.
     * @param the2bytePayload : the 2-byte payload.
     * @return Content of the message in byte[]
     */
    public static byte[] make2byteMsg( short tranId, short msgDef, short the2bytePayload )
    {
        byte[] body = MspByteUtil.getBytes( the2bytePayload );
        return createShortMsg( body, 0, body.length, msgDef, (short)1, (short)1, tranId );
    }


    /**
     * Create a message that has no payload. 
     * @param tranId : Transaction ID (to match response message to a request message).
     * @param msgDef : Message definition.
     * @return Content of the message in byte[]
     */
    public static byte[] makeEmptyMsg( short tranId, short msgDef )
    {
        byte[] body = new byte[0];
        return createShortMsg( body, 0, body.length, msgDef, (short)1, (short)1, tranId );
    }


    /**
     * Create MIP Serial Protocol Messages header.
     * @param msgBodyLen
     * @param msgType
     * @param segNum : segment serial number (1 based)
     * @param segQntt
     * @param tranId
     * @param dos : DataOutputStream where to write the header.
     */
    public static void writeHeader( 
            int msgBodyLen, 
            short msgType, 
            short segNum, 
            short segQntt, 
            int tranId,
            DataOutputStream dos )
    {
        try
        {
            dos.writeInt( MspConst.START_SIGNATURE ); // Start signature            
            dos.writeShort( 0x0101 );       // Version            
            dos.writeInt( msgBodyLen );     // Length of the message body.            
            dos.writeShort( msgType );      // Message Type
            dos.writeShort( segNum );       // Segment number of a message.
            dos.writeShort( segQntt );      // Number of Segments
            dos.writeShort( tranId );       // Transaction ID
        }
        catch( IOException e )
        {
            Log.e( TAG, "createHeader", e );    // As we are using ByteArrayOutputStream - this should never happen.
        }
    }
    
}
