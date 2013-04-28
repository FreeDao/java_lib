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
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class HTTPSClientManager
{
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;

    
    /**
     * Get Https client that trust all the server
     * 
     * @return
     */
    public static HttpClient getNewHttpClient()
    {
        try
        {
            KeyStore trustStore = KeyStore.getInstance( KeyStore.getDefaultType() );
            // trustStore.load(getResources().getAssets().open("myKeyStore"), null);
            trustStore.load( null, null );
            SSLSocketFactory sf = new MySSLSocketFactory( trustStore );
            sf.setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion( params, HttpVersion.HTTP_1_1 );
            HttpProtocolParams.setContentCharset( params, HTTP.UTF_8 );
            HttpProtocolParams.setUseExpectContinue( params, true );
            SchemeRegistry registry = new SchemeRegistry();
            registry.register( new Scheme( "http", PlainSocketFactory.getSocketFactory(), HTTP_PORT ) );
            registry.register( new Scheme( "https", sf, HTTPS_PORT ) );
            ClientConnectionManager ccm = new ThreadSafeClientConnManager( params, registry );
            return new DefaultHttpClient( ccm, params );
        }
        catch( Exception e )
        {
            return new DefaultHttpClient();
        }
    }


    /**
     * Use KeyStore to get the new HttpClient
     * 
     * @param is KeyStore InputStream
     * @param password password
     * @return HttpClient the HTTPS client
     */
    public static HttpClient getNewHttpClient( InputStream is, char[] password )
    {
        try
        {
            KeyStore trustStore = KeyStore.getInstance( KeyStore.getDefaultType() );
            trustStore.load( is, password );
            SSLSocketFactory sf = new MySSLSocketFactory( trustStore );
            sf.setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion( params, HttpVersion.HTTP_1_1 );
            HttpProtocolParams.setContentCharset( params, HTTP.UTF_8 );
            HttpProtocolParams.setUseExpectContinue( params, true );
            SchemeRegistry registry = new SchemeRegistry();
            registry.register( new Scheme( "http", PlainSocketFactory.getSocketFactory(), HTTP_PORT ) );
            registry.register( new Scheme( "https", sf, HTTPS_PORT ) );
            ClientConnectionManager ccm = new ThreadSafeClientConnManager( params, registry );
            return new DefaultHttpClient( ccm, params );
        }
        catch( Exception e )
        {
            return new DefaultHttpClient();
        }
    }

    private static class MySSLSocketFactory extends SSLSocketFactory
    {
        SSLContext sslContext = SSLContext.getInstance( "TLS" );

        public MySSLSocketFactory( KeyStore truststore ) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
        {
            super( truststore );
            TrustManager tm = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted( java.security.cert.X509Certificate[] chain, String authType ) throws java.security.cert.CertificateException
                {
                }

                @Override
                public void checkServerTrusted( java.security.cert.X509Certificate[] chain, String authType ) throws java.security.cert.CertificateException
                {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };
            sslContext.init( null, new TrustManager[] { tm }, null );
        }


        @Override
        public Socket createSocket( Socket socket, String host, int port, boolean autoClose ) throws IOException, UnknownHostException
        {
            return sslContext.getSocketFactory().createSocket( socket, host, port, autoClose );
        }


        @Override
        public Socket createSocket() throws IOException
        {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
