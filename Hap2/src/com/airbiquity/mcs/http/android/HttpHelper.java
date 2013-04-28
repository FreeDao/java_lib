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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.conn.DefaultResponseParser;
import org.apache.http.impl.io.HttpRequestParser;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

/**
 * Utility class used for processing HTTP request and response objects specific for Android platform.
 */
public class HttpHelper
{
    private static final String TAG = "HttpHelper";

    /**
     * Utility class for storing content information
     */
    protected static class ContentInfo
    {
        public final int Pos;
        public final int Len;


        public ContentInfo( int pos, int len )
        {
            this.Pos = pos;
            this.Len = len;
        }
    }


    /**
     * Parses HTTP response from an input byte stream
     * @param bytes
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static HttpResponse parseResponse( final byte[] bytes ) throws IOException, HttpException
    {
        HttpParams params = new BasicHttpParams();
        SessionInputBuffer inbuffer = new SessionInputBuffer_AT( bytes );
        HttpMessageParser parser = new DefaultResponseParser( inbuffer, BasicLineParser.DEFAULT, new DefaultHttpResponseFactory(), params );
        HttpResponse response = (HttpResponse) parser.parse();
        Header h = response.getFirstHeader( "Content-Length" );
        ContentInfo info = getContentLength( bytes, h );
        if( null == info )
        {
            return null; // Error - not fully received
        }
        if( response.getEntity() == null )
        {
            if( info.Len > 0 )
            {
                byte[] content = new byte[info.Len];
                System.arraycopy( bytes, info.Pos, content, 0, info.Len );
                ByteArrayEntity entity = new ByteArrayEntity( content );
                response.setEntity( entity );
            }
        }
        return response;
    }


    /**
     * Parses HTTP response from a string
     * @param data
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static HttpResponse parseResponse( final String data ) throws IOException, HttpException
    {
        return parseResponse( data.getBytes() );
    }


    /**
     * Parses HTTP request from the input byte buffer.
     * @param bytes : buffer that contain an HTTP request or its part.
     * @param bufSize : size of the data in the input buffer.
     * @return the parsed request on null if the given buffer doesn't contain a full HTTP request.
     * @throws IOException
     * @throws HttpException
     */
    public static Request parseRequest( final byte[] bytes, int bufSize ) throws IOException, HttpException
    {
        HttpParams params = new BasicHttpParams();
        SessionInputBuffer inbuffer = new SessionInputBuffer_AT( bytes, bufSize );
        HttpRequestParser parser = new HttpRequestParser( inbuffer, BasicLineParser.DEFAULT, new ProxyHttpRequestFactory(), params );
        HttpRequest request = (HttpRequest) parser.parse();
        byte[] content = null;
        if( request instanceof HttpEntityEnclosingRequest )
        {
            HttpEntityEnclosingRequest entReq = (HttpEntityEnclosingRequest) request;
            Header h = entReq.getFirstHeader( "Content-Length" );
            ContentInfo info = getContentLength( bytes, h );
            if( null == info || info.Pos + info.Len > bufSize )
            {
                return null; // Error - not fully received
            }
            HttpEntity ent = entReq.getEntity();
            if( ent == null )
            {
                if( info.Len > 0 )
                {
                    content = new byte[info.Len];
                    System.arraycopy( bytes, info.Pos, content, 0, info.Len );
                    ByteArrayEntity entity = new ByteArrayEntity( content );
                    entReq.setEntity( entity );
                }
            }
            else
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ent.writeTo( stream );
                content = stream.toByteArray();
            }
        }
        // Log.i( "================== Response", request.toString() );
        RequestLine line = request.getRequestLine();
        Header hcl = request.getFirstHeader( "Content-Type" );
        String contentType = null == hcl ? "" : hcl.getValue();
        Request ret = new Request( line.getUri(), line.getMethod(), contentType, content );
        return ret;
    }


    /**
     * Parses HTTP request from an input byte stream
     * @param bytes
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static Request parseRequest( final byte[] bytes ) throws IOException, HttpException
    {
        return parseRequest( bytes, bytes.length );
    }


    /**
     * Parses HTTP request from an input string
     * @param data
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static Request parseRequest( final String data ) throws IOException, HttpException
    {
        return parseRequest( data.getBytes() );
    }


    /**
     * Retrieves content-length
     * @param bytes
     * @param h
     * @return
     */
    protected static ContentInfo getContentLength( final byte[] bytes, final Header h )
    {
        // Get content length from header
        int contLen = 0;
        if( h != null )
        {
            String val = h.getValue();
            int start = 0;
            int strLen = val.length();
            while( start < strLen && Character.isSpace( val.charAt( start ) ) )
                start++;
            contLen = Integer.parseInt( val.substring( start ) );
        }
        // Search for header/content divider
        int maxByte = bytes.length - 4;
        for( int i = 0; i < maxByte; i++ )
        {
            if( bytes[i] == '\r' && bytes[i + 1] == '\n' && bytes[i + 2] == '\r' && bytes[i + 3] == '\n' )
            {
                int pos = i + 4;
                int len = bytes.length - pos;
                if( contLen > len )
                {
                    // not fully received
                    return null;
                }
                if( contLen < len )
                {
                    len = contLen;
                }
                return new ContentInfo( pos, len );
            }
        }
        // Header/content divider is not found -> Not fully received
        return null;
    }


    /**
     * Converts HttpRequest object to array of bytes
     * @param req
     * @return
     */
    public static byte[] getBytes( HttpRequest req )
    {
        StringBuilder bld = new StringBuilder();
        // Get header
        bld.append( req.getRequestLine().toString() );
        bld.append( "\r\n" );
        Header[] headers = req.getAllHeaders();
        for( int i = 0; i < headers.length; i++ )
        {
            Header h = headers[i];
            bld.append( h.toString() );
            bld.append( "\r\n" );
        }
        bld.append( "\r\n" );
        String header = bld.toString();
        byte[] hdr = header.getBytes();
        int byteCount = hdr.length;
        // Check contents
        if( req instanceof HttpEntityEnclosingRequest )
        {
            HttpEntityEnclosingRequest entReq = (HttpEntityEnclosingRequest) req;
            HttpEntity ent = entReq.getEntity();
            if( ent != null )
            {
                int entCount = (int) ent.getContentLength();
                if( entCount > 0 )
                {
                    byte[] res = new byte[byteCount + entCount];
                    System.arraycopy( hdr, 0, res, 0, byteCount );
                    try
                    {
                        InputStream is = ent.getContent();
                        is.read( res, byteCount, entCount );
                        return res;
                    }
                    catch( Exception e )
                    {
                        Log.e( TAG, "", e );
                    }
                }
            }
        }
        // No contents - return the header
        return hdr;
    }
}
