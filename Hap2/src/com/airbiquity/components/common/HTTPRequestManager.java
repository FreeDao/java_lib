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
package com.airbiquity.components.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.airbiquity.util_net.NetResp;

import android.util.Log;

/**
 * 
 */
public class HTTPRequestManager
{
    private static final String TAG = "HTTPRequestManager";
    private static final int TIME_OUT = 10000;
    private static final int BUFFER = 1024;


    // hide the default constructor
    private HTTPRequestManager()
    {
    }


    /**
     * Send a post request and return the response. 
     * If the response has failure HTTPResponseMessage.getMessage() will be null.
     * 
     * @param url
     * @param payload
     * @param contentType
     * @return
     */
    public static NetResp sendPostRequest( String url, byte[] payload, String contentType )
    {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout( httpParams, TIME_OUT );
        HttpConnectionParams.setSoTimeout( httpParams, TIME_OUT );
        HttpClient client = HTTPSClientManager.getNewHttpClient();
        HttpPost httpRequest = new HttpPost( url );
        httpRequest.setEntity( new ByteArrayEntity( payload ) );
        httpRequest.setHeader( "Content-Type", contentType );
        HttpResponse response = null;
        try
        {
            response = client.execute( httpRequest );
        }
        catch( Exception e )
        {
            Log.e( TAG, " ", e );
        }
        return getResponse( response );
    }


    private static NetResp getResponse( HttpResponse response )
    {
        if( response == null )
            return new NetResp( HttpURLConnection.HTTP_NOT_FOUND, "null response".getBytes() );

        StringBuffer sb = new StringBuffer();
        try
        {
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            int c = 0;
            byte[] b = new byte[BUFFER];
            while( (c = inputStream.read( b )) != -1 )
            {
                sb.append( new String( b, 0, c ) );
            }
            inputStream.close();
        }
        catch( IllegalStateException e )
        {
            Log.e( TAG, " ", e );
        }
        catch( IOException e )
        {
            Log.e( TAG, " ", e );
        }
        return new NetResp( response.getStatusLine().getStatusCode(), sb.toString().getBytes() );
    }
}
