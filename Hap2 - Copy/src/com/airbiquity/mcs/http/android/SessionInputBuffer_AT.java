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
package com.airbiquity.mcs.http.android;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.impl.io.AbstractSessionInputBuffer;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 * Utility class used with HTTP request and response parsing.
 */
public class SessionInputBuffer_AT extends AbstractSessionInputBuffer
{
    public static final int BUFFER_SIZE = 16;


    // protected IMCSDataLayer m_dataLayer = null;
    /**
     * Constructor.
     */
    public SessionInputBuffer_AT( final InputStream instream, int bufSize, final HttpParams params )
    {
        this.init( instream, bufSize, params );
    }


    /**
     * Constructor
     * @param instream
     * @param bufSize
     */
    public SessionInputBuffer_AT( final InputStream instream, int bufSize )
    {
        this( instream, bufSize, new BasicHttpParams() );
    }


    /**
     * Constructor
     * @param instream
     * @param params
     */
    public SessionInputBuffer_AT( final InputStream instream, final HttpParams params )
    {
        this( instream, BUFFER_SIZE, params );
    }


    /**
     * Constructor
     * @param instream
     */
    public SessionInputBuffer_AT( final InputStream instream )
    {
        this( instream, BUFFER_SIZE );
    }


    /**
     * Constructor
     * @param bytes
     */
    public SessionInputBuffer_AT( final byte[] bytes )
    {
        this( new ByteArrayInputStream( bytes ) );
    }


    /**
     * Constructor
     * @param data
     */
    public SessionInputBuffer_AT( final String data )
    {
        this( data.getBytes() );
    }


    /**
     * Constructor
     * @param bytes
     * @param bytesCount
     */
    public SessionInputBuffer_AT( byte[] bytes, int bytesCount )
    {
        this( new ByteArrayInputStream( bytes, 0, bytesCount ) );
    }


    public boolean isDataAvailable( int arg0 ) throws IOException
    {
        return true;
    }
}
