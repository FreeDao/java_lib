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

/**
 * Provides circular buffer for storing TCP/IP Packets.
 */
public class DataQueueIPPacket
{
    private static final String TAG = "TCPIPPacket";
    private final int qMaxSize;// max queue size
    private int fp = 0; // front pointer
    private int rp = 0; // rear pointer
    private int qs = 0; // size of queue
    private TCPIPPacket[] q; // actual queue


    /**
     * Constructor.
     * @param size : the max number of Packets in this ring-buffer.
     */
    public DataQueueIPPacket( int size )
    {
        qMaxSize = size;
        fp = 0;
        rp = 0;
        qs = 0;
        q = new TCPIPPacket[qMaxSize];
    }


    /**
     * Remove the front item from the queue and return it.
     * @return the front item from the queue.
     * @throws MCSException if queue is empty.
     */
    public TCPIPPacket delete() throws MCSException
    {
        if( !isEmpty() )
        {
            qs--;
            fp = (fp + 1) % qMaxSize;
            return q[fp];
        }
        else
        {
            MCSException e = new MCSException( "Queue underflow" );
            Log.e( TAG, "delete", e );
            throw e;
        }
    }


    /**
     * Get current? packet.
     * @return
     * @throws MCSException
     */
    public TCPIPPacket get() throws MCSException
    {
        if( !isEmpty() )
        {
            return q[(fp + 1) % qMaxSize];
        }
        else
        {
            MCSException e = new MCSException( "DataQueueIPPacket - Queue underflow" );
            Log.e( TAG, "get", e );
            throw e;
        }
    }


    /**
     * Put data into the queue.
     * @param o
     * @throws MCSException
     */
    public void insert( TCPIPPacket o ) throws MCSException
    {
        if( !isFull() )
        {
            qs++;
            rp = (rp + 1) % qMaxSize;
            q[rp] = o;
        }
        else
        {
            MCSException e = new MCSException( "DataQueueIPPacket - Overflow" );
            Log.e( TAG, "insert", e );
            throw e;
        }
    }


    /**
     * Check if the queue is empty.
     * @return
     */
    public boolean isEmpty()
    {
        return qs == 0;
    }


    /**
     * Check if the queue is full.
     * @return true if full.
     */
    public boolean isFull()
    {
        return qs == qMaxSize;
    }


    /**
     * Get queue size.
     * @return
     */
    public int size()
    {
        return qs;
    }
}
