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
package com.airbiquity.mcs.utils;

import com.airbiquity.mcs.common.MCSException;

/**
 * Provides circular buffer for storing data of any type.
 */
public class DataQueueArr
{
    private int qMaxSize;// max queue size
    private int fp = 0; // front pointer
    private int rp = 0; // rear pointer
    private int qs = 0; // size of queue
    private byte[][] q; // actual queue (array of byte arrays)


    /**
     * Constructor.
     * @param size : the number of byte arrays in this ring-buffer.
     */
    public DataQueueArr( int size )
    {
        qMaxSize = size;
        fp = 0;
        rp = 0;
        qs = 0;
        q = new byte[qMaxSize][];
    }


    /**
     * Remove the front item from the queue and return it.
     * @return the front item from the queue.
     * @throws MCSException if queue is empty.
     */
    public byte[] delete() throws MCSException
    {
        if( !isEmpty() )
        {
            qs--;
            fp = (fp + 1) % qMaxSize;
            byte[] ret = q[fp];
            q[fp] = null;
            return ret;
        }
        else
        {
            throw new MCSException( "DataQueueArr - Queue underflow" );
        }
    }


    /**
     * Get current? byte array.
     * @return
     * @throws MCSException
     */
    public byte[] get() throws MCSException
    {
        if( !isEmpty() )
            return q[(fp + 1) % qMaxSize];
        else
            throw new MCSException( "DataQueueArr - Queue underflow" );
    }


    /**
     * Put data into the queue.
     * @param o
     * @throws MCSException
     */
    public void insert( byte[] o ) throws MCSException
    {
        if( !isFull() )
        {
            qs++;
            rp = (rp + 1) % qMaxSize;
            q[rp] = o;
        }
        else
            throw new MCSException( "DataQueueArr - Overflow" );
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
