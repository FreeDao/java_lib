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

import android.util.Log;

import com.airbiquity.hap.A;
import com.airbiquity.mcs.common.AbstractMcsLayer;
import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.McsLayerListener;

/**
 * This layer sits on top of Bluetooth layer and converts MspMsg into byte stream and vice versa.
 * That is, when the upper layer writes MspMsg into this layer - this layer converts it into bytes 
 * and writes it into the lower layer.
 * And when the lower layer writes bytes into this layer - this layer converts them into MspMsg 
 * and gives it to the upper layer.  
 */
public class MspLayer extends AbstractMcsLayer implements McsLayerListener
{
    private static final String TAG = "MspLayer";
    private final static int IN_BUF_SZ = 16*1024;  // Size of the input buffer.
    private McsLayer lowerLayer;

    /** Segments of an MSP Message */
    private ConcurrentLinkedQueue<MspSegment> segs;
    
    /** Message that is ready to be sent to the upper layer. */
    private MspMsg outMsg;
    
    /** Input buffer fullness. */       
    private int bufInPos = 0;                       

    /** Store data that is coming from the lower layer until we process it. */  
    private byte[] bufIn = new byte[IN_BUF_SZ];


    /**
     * Constructor.
     */
    public MspLayer()
    {
    }


    /**
     * Set the lower layer and start listening to it.
     * If there is a lower layer set already - unset it first. 
     * @param lower_layer : the lower layer (aka data layer, aka transport layer)
     */
    public void setLowerLayer( McsLayer lower_layer )
    {
        if( lowerLayer != null )
            lowerLayer.removeListener( this );

        lowerLayer = lower_layer;
        if( lowerLayer != null )
            lowerLayer.addListener( this );
    }


    @Override
    public void onConnectionClosed()
    {
        tellConnectionClosed();
    }


    /**
     * Get data from the lower layer and add it to a buffer. 
     * Once we have a whole segment in the buffer we take it out of the buffer and process it.
     * Note: This method is called from the lower layer to signal that it has new data available for retrieval.
     */
    @Override public void onDataReceived()
    {
        try
        {
            // Read the data from the lower layer.
            int len = 0;
            byte[] bufNewData = new byte[IN_BUF_SZ];    // Buffer for the new data form the lower layer.
            synchronized( lowerLayer )
            {
                len = lowerLayer.readData( bufNewData, IN_BUF_SZ );
            }
            //Log.d( TAG, "onDataReceived() sz="+ len );
            
            if( len <= 0 )
                return; // False alarm. Why this would ever happen?

            // Add new data to the input buffer.
            System.arraycopy( bufNewData, 0, bufIn, bufInPos, len );    
            bufInPos += len;

            // Retrieve segments from the input buffer and process them one-by-one.
            while( true )
            {
                // Check if we have enough data for a header.
                if( bufInPos <= MspSegment.HEADER_SZ )
                    break;     // Not enough data even for a header.
    
                // Try to read a segment.
                MspSegment segment = new MspSegment( bufIn );
                final int segLen = segment.getTotalSize();
                if( bufInPos < segLen )
                    break;     // Not enough data for the full segment.
                
                if( !segment.checkCRC() )
                    Log.e( TAG, "CRC missmatch: read="+segment.crcRead+" calculated="+segment.crcCalculated );
                
                // We have got a whole segment - now remove it from the buffer.
                bufInPos -= segLen;
                System.arraycopy( bufIn, segLen, bufIn, 0, bufInPos );
                
                // Process segments depending on the type.
                switch( segment.getCmdType() )
                {
                    case MspConst.CMD_TYPE_CONTROL:
                        processControlMsg( segment );
                        break;
    
                    case MspConst.CMD_TYPE_TEMPLATE_DATA:
                    case MspConst.CMD_TYPE_AUD_DATA:
                    case MspConst.CMD_TYPE_APP_DATA:
                        processSegment( segment );
                        break;
                }
            }                
        }
        catch( Exception e )
        {
            Log.e( TAG, "", e );
        }
    }


    /**
     * Add segment to the list. 
     * Once we have all segments of a message - send the message to the upper layer.
     * @param segment
     */
    private void processSegment( MspSegment segment )
    {
        //Log.d( TAG, "processSegment: def="+segment.msgDef+" sz="+segment.payloadSz+" segsQntt="+segment.segsQntt );
        if( null == segs )
            segs = new ConcurrentLinkedQueue<MspSegment>();
        
        segs.add( segment );
        if( segment.segIdx+1 >= segment.segsQntt )   // If this is the last segment of the message.
        {
            outMsg = new MspMsg( segs );
            tellDataReceived();
            segs = null;
        }
    }


    /** 
     * This method is not used. 
     * We use the other one: 
     *      public MSPRequest readData()
     */
    @Override public int readData( byte[] buffer, int size )
    {
        throw new UnsupportedOperationException( "Use this instead: public MSPRequest readData()" );
    }


    /**
     * Method for upper layer to retrieve data from this layer. 
     * @return MspMsg (assembled from the segments) or null if no message is available.
     */
    public MspMsg readData()
    {
        //Log.d( TAG, "mSegData.length:" + mSegData.length + " m_available:" + m_available + " m_read:" + m_read );
        return outMsg;
    }


    /** 
     * This method is not used. 
     * We use the other one: 
     *      public void writeData( MspMsg msg )
     */
    @Override public void writeData( byte[] buffer, int size )
    {
        lowerLayer.writeData( buffer, size );
    }


    /**
     * Writes data to this layer. (Called from the Upper layer) 
     * Every message passed in will wait in the queue until handled by the worker thread.
     * @param msg : MSP message
     */
    public void writeData( MspMsg msg )
    {
        //Log.d( TAG, "writeData:" + bytesToString( msg.getContent(), msg.getContent().length ) );
        int bytesSent = 0;
        short curSegIdx = 1;
        final short segsQntt = (short) ((msg.payload.length + MspSegment.MAX_SEGMENT_PAYLOAD_SIZE-1) / MspSegment.MAX_SEGMENT_PAYLOAD_SIZE);
        for( int bytesLeftToSend = msg.payload.length; bytesLeftToSend>0; )
        {
            int bytesToSendNow = Math.min( bytesLeftToSend, MspSegment.MAX_SEGMENT_PAYLOAD_SIZE );
            byte[] buf = MspSegment.createShortMsg( msg.payload, bytesSent, bytesToSendNow, msg.msgType,
                                                    curSegIdx, segsQntt, msg.tranId );
            lowerLayer.writeData( buf, buf.length );
            bytesSent += bytesToSendNow;
            bytesLeftToSend -= bytesToSendNow;
            curSegIdx++;
            Log.i( TAG, "MesgSz="+buf.length );
        }
    }



    @Override
    public void close()
    {
        onConnectionClosed();
    }


    /**
     * Process MSP Control packet.
     * @param segment
     */
    private void processControlMsg( MspSegment segment )
    {
        Log.d( TAG, "processControlMsg " + String.format("%04X", segment.msgDef) );
        byte[] msg;

        bufInPos = 0;
        switch( segment.msgDef )
        {
            case MspConst.INIT_CONNECTION:
                msg = MspSegment.make2byteMsg( segment.tranId, MspConst.INIT_CONNECTION_RSP, MspConst.OK );
                lowerLayer.writeData( msg, msg.length );
                
                //String url = "file:///"+A.getAppDir().getAbsolutePath()+"/www/index.html";
                String url = "file:///android_asset/www/index.html";
                A.a().isPanHuJustConnected = true;
                
                Log.i( TAG, url ); 
                A.a().panAppManager.loadUrlOnUiThread( url );
                break;
                
            case MspConst.TER_CONNECTION:
				// TODO: this should prevent us from sending any more data to the head unit
				msg = MspSegment.make2byteMsg( segment.tranId, MspConst.TER_CONNECTION_RSP, MspConst.OK );
				lowerLayer.writeData( msg, msg.length );
				break;
                
            case MspConst.KEEPALIVE:
                msg = MspSegment.makeEmptyMsg( segment.tranId, MspConst.KEEPALIVE_RSP );
                lowerLayer.writeData( msg, msg.length );
                break;
                
            case MspConst.INIT_CONNECTION_RSP:
            case MspConst.TER_CONNECTION_RSP:
            case MspConst.KEEPALIVE_RSP:
                break;
                
            default:
                break;
        }
    }



//    private synchronized void saveData( MspSegment pack )
//    {
//        msgDef = pack.getMsgDef();
//        mSegData = new byte[pack.getMessageLength()];
//        m_available = pack.getMessageLength();
//        mTransactionId = pack.getTranId();
//        System.arraycopy( pack.getMsgBody(), 0, mSegData, 0, pack.getMessageLength() );
//        tellDataReceived();
//        //sendAck();
//    }

//    private int segIndx = 0;
//    private byte[] mSegData;
//    private short mTransactionId;
//    private short msgDef;


//    private void sendCRCError()
//    {
//        // TODO: WHERE IS THE DEFINITION OF CRC ERROR???
//        byte[] msg = MSPHelperUtils.make2byteMsg( mTransactionId, MspConst.ERR_CRC, MspConst.ERR_CRC ); 
//        lowerLayer.writeData( msg, msg.length );
//    }


//    private void sendNextSegmentCommand()
//    {
//        //Log.d( TAG, "sendNextSegmentCommand() " );
//
//        byte[] body = new byte[]{ (byte)0x82, 0x00 };
//        byte[] data = MSPHelperUtils.createShortMsg( body, MspConst.OK_CONTROL_MESSAGE, mTransactionId );
//        writeData( data, data.length );
//    }


//    private void sendAck()
//    {
//        // TODO: WHERE IS THE DEFINITION OF ACK???
//        byte[] msg = MSPHelperUtils.make2byteMsg( mTransactionId, MspConst.OK_SESSION_ESTABLISH, MspConst.OK ); 
//        lowerLayer.writeData( msg, msg.length );
//    }
}
