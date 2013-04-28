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

/**
 * This class contains all main parameters of a HTTP request such as URL, request method (GET, PUT, DELETE, etc.) and
 * request data.
 */
public class Request
{
    public final String url;
    public final String method;
    public final String content_type;
    public final byte[] data;


    /**
     * Constructs new HTTP request object
     * @param url
     * @param method
     * @param data
     */
    public Request( String url, String method, String contentType, byte[] data )
    {
        this.url = (url != null) ? url : "";
        this.method = (method != null) ? method : "";
        this.content_type = contentType;
        this.data = (data != null) ? data : new byte[0];
    }
    
    @Override
    public String toString()
    {
        String cont = "size="+data.length;
        if( "application/json".equalsIgnoreCase( content_type ) && data.length>1 )
            cont = new String( data );
        return "URL="+url+" method="+method+" type="+content_type+" cont "+cont;
        
    }
}
