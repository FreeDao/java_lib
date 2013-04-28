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

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;

public class ProxyHttpRequestFactory implements HttpRequestFactory
{
    private static final String[] RFC2616_COMMON_METHODS = { "GET" };
    private static final String[] RFC2616_ENTITY_ENC_METHODS = { "POST", "PUT" };
    private static final String[] RFC2616_SPECIAL_METHODS = { "HEAD", "OPTIONS", "DELETE", "TRACE", "CONNECT" };


    public ProxyHttpRequestFactory()
    {
        super();
    }


    /**
     * 
     * @param methods
     * @param method
     * @return
     */
    private static boolean isOneOf( final String[] methods, final String method )
    {
        for( int i = 0; i < methods.length; i++ )
        {
            if( methods[i].equalsIgnoreCase( method ) )
                return true;
        }
        return false;
    }


    public HttpRequest newHttpRequest( final RequestLine requestline ) throws MethodNotSupportedException
    {
        if( requestline == null )
            throw new IllegalArgumentException( "Request line may not be null" );

        String method = requestline.getMethod();
        if( isOneOf( RFC2616_COMMON_METHODS, method ) )
            return new BasicHttpRequest( requestline );
        else if( isOneOf( RFC2616_ENTITY_ENC_METHODS, method ) )
            return new BasicHttpEntityEnclosingRequest( requestline );
        else if( isOneOf( RFC2616_SPECIAL_METHODS, method ) )
            return new BasicHttpRequest( requestline );
        else
            throw new MethodNotSupportedException( method + " method not supported" );
    }


    public HttpRequest newHttpRequest( final String method, final String uri ) throws MethodNotSupportedException
    {
        if( isOneOf( RFC2616_COMMON_METHODS, method ) )
            return new BasicHttpRequest( method, uri );
        else if( isOneOf( RFC2616_ENTITY_ENC_METHODS, method ) )
            return new BasicHttpEntityEnclosingRequest( method, uri );
        else if( isOneOf( RFC2616_SPECIAL_METHODS, method ) )
            return new BasicHttpRequest( method, uri );
        else
            throw new MethodNotSupportedException( method + " method not supported" );
    }
}
