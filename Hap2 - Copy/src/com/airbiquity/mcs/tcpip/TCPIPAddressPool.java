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
 * Manages a pool of TCPIPAddress which are reused by the application. 
 * This increases performance by removing memory allocation and garbage collection.
 */
public class TCPIPAddressPool
{
    private static final int PoolSize = 1024 * 3; // TODO: this should be allocated dynamically.
    private static boolean m_isInitialzied = false;
    private static final boolean m_dumpInfo = false;
    private static final DataQueueIPAddress m_freeAddresses = new DataQueueIPAddress( PoolSize );


    /**
     * Init.
     * @throws MCSException
     */
    private static void init() throws MCSException
    {
        synchronized( m_freeAddresses )
        {
            for( int i = 0; i < PoolSize; i++ )
                m_freeAddresses.insert( new TCPIPAddress() );

            m_isInitialzied = true;
        }
    }


    /**
     * getAddress
     * @return
     * @throws MCSException
     */
    public static TCPIPAddress getAddress() throws MCSException
    {
        synchronized( m_freeAddresses )
        {
            if( !m_isInitialzied )
                init();

            if( m_freeAddresses.size() > 0 )
            {
                TCPIPAddress addr = m_freeAddresses.delete();
                if( m_dumpInfo )
                    Log.d( "- FREE ADDRESSES", "" + m_freeAddresses.size() );

                return addr;
            }
        }
        throw new MCSException( "No free TCPIPAddress object" );
    }


    /**
     * freeAddress
     * @param addr
     * @throws MCSException
     */
    public static void freeAddress( TCPIPAddress addr ) throws MCSException
    {
        if( null == addr )
            return;

        synchronized( m_freeAddresses )
        {
            if( !m_isInitialzied )
                throw new MCSException( "TCPIPAddressPool is not initialized yet" );

            m_freeAddresses.insert( addr );
            if( m_dumpInfo )
                Log.d( "+ FREE ADDRESSES", "" + m_freeAddresses.size() );
        }
    }


    /**
     * copyAddress
     * @param address
     * @return
     * @throws MCSException
     */
    public static TCPIPAddress copyAddress( TCPIPAddress address ) throws MCSException
    {
        TCPIPAddress addr = getAddress();
        addr.copyFrom( address );
        return addr;
    }
}
