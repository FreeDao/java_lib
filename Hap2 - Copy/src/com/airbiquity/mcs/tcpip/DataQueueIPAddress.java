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

import com.airbiquity.mcs.common.MCSException;

/**
 * Provides circular buffer for storing data of any type.
 */
public class DataQueueIPAddress
{
    private int qMaxSize;// max queue size
    private int fp = 0; // front pointer
    private int rp = 0; // rear pointer
    private int qs = 0; // size of queue
    private TCPIPAddress[] q; // actual queue


    /**
     * Constructor.
     * @param size
     */
    public DataQueueIPAddress( int size )
    {
        qMaxSize = size;
        fp = 0;
        rp = 0;
        qs = 0;
        q = new TCPIPAddress[qMaxSize];
    }


    /**
     * delete
     * @return
     * @throws MCSException
     */
    public TCPIPAddress delete() throws MCSException
    {
        if( !emptyq() )
        {
            qs--;
            fp = (fp + 1) % qMaxSize;
            return q[fp];
        }
        else
        {
            throw new MCSException( "DataQueueIPAddress - Queue underflow" );
        }
    }


    /**
     * get
     * @return
     * @throws MCSException
     */
    public TCPIPAddress get() throws MCSException
    {
        if( !emptyq() )
        {
            return q[(fp + 1) % qMaxSize];
        }
        else
        {
            throw new MCSException( "DataQueueIPAddress - Queue underflow" );
        }
    }


    /**
     * insert
     * @param o
     * @throws MCSException
     */
    public void insert( TCPIPAddress o ) throws MCSException
    {
        if( !fullq() )
        {
            qs++;
            rp = (rp + 1) % qMaxSize;
            q[rp] = o;
        }
        else
            throw new MCSException( "DataQueueIPAddress - Overflow" );
    }


    /**
     * contains
     * @param o
     * @return
     */
    public boolean contains( TCPIPAddress o )
    {
        if( qs == 0 )
        {
            return false;
        }
        if( fp < rp )
        {
            return contains( o, fp + 1, rp );
        }
        return contains( o, fp + 1, qMaxSize - 1 ) || contains( o, 0, rp );
    }


    /**
     * contains
     * @param o
     * @param start
     * @param end
     * @return
     */
    private boolean contains( TCPIPAddress o, int start, int end )
    {
        for( int i = start; i <= end; i++ )
        {
            if( q[i] == o )
            {
                return true;
            }
        }
        return false;
    }


    /**
     * emptyq
     * @return
     */
    public boolean emptyq()
    {
        return qs == 0;
    }


    /**
     * Check if queue is full.
     * @return true if so.
     */
    public boolean fullq()
    {
        return qs == qMaxSize;
    }


    /**
     * Get size.
     * @return
     */
    public int size()
    {
        return qs;
    }
}
