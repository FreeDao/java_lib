package com.airbiquity.util_net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import android.util.Log;

import com.airbiquity.hap.A;
import com.airbiquity.security.AppTokenGenerator;
import com.airbiquity.security.SecurityConstants;


public class HttpUtil
{
    private static final String TAG = "HttpUtil";    

    /**
     * Create and open HttpURLConnection or HttpsURLConnection. 
     * @param url
     * @return
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws KeyManagementException
     */
    public static HttpURLConnection openCon( URL url ) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, KeyManagementException
    {
        String protocol = url.getProtocol();
        if( "http".equals( protocol ) )
            return (HttpURLConnection) url.openConnection();
        
        if( !"https".equals( protocol ) )
            throw new IOException( "Unsuported protocol: "+protocol );
        
        KeyStore keyStore = KeyStore.getInstance( "BKS" );
        InputStream in = A.a().getAssets().open( "myKeystore145.bks" );
        try
        {
            keyStore.load( in, "abqpass".toCharArray() );
        }
        finally
        {
            in.close();
        }        
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(keyStore);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, tmf.getTrustManagers(), null);

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        
        //con.setSSLSocketFactory(sc.getSocketFactory());
        con.setRequestProperty( HttpHeader.NAME_ACCESS_KEY_ID, SecurityConstants.HTTPS_ACCESS_KEY_ID );
        AppTokenGenerator.getInstance().setSecurityVersionValue(new String("1"));
        //currently we are using default pre-shared-key
        //AppTokenGenerator.getInstance().setSecurityPreSharedKey("PRE_SHARED_KEY");
        con.setRequestProperty( HttpHeader.NAME_APP_TOKEN, AppTokenGenerator.getInstance().getHttpsAppToken() );
        
        //InputStream in = urlConnection.getInputStream();
        //Log.d( TAG, "SSL Context: "+sc );
        return con;
    }
    
    /**
     * Perform network communication - send request (GET or POST) and return the response.
     * (Should be called on a background thread.)
     * @param req : request with URL and optional JSON POST data.
     * @return response.
     */
    public static NetResp com( NetReq req )
    {
        InputStream stream = null; // HTTP connection download stream.
        HttpURLConnection con = null;

        try
        {
            con = openCon( req.url );                           // Open connection to URL (HTTP or HTTPS).
            
            for( HttpHeader h : req.headers )                   // Add headers if any.
                con.setRequestProperty( h.name, h.val );
            
            if( null != req.post )
            {
                con.setDoOutput( true );
                OutputStream os = con.getOutputStream();
                os.write( req.post.getBytes() );                 // Write POST data if any.
            }
            
            final int code = con.getResponseCode();             // Get response code.
            
            if( HttpURLConnection.HTTP_NO_CONTENT == code )
                stream = null;
            else if( HttpURLConnection.HTTP_OK == code )       
                stream = con.getInputStream();
            else
                stream = con.getErrorStream();

            byte[] res = getBody( stream );
            return new NetResp( code, res );
        }
        catch( Exception e )
        {
            Log.e( TAG, "URL="+req.url+"\n", e );
            return new NetResp( 0, e.toString().getBytes() );
        }
        finally
        {
            // Close connection to server.
            if( stream != null )
                try
                {
                    stream.close();
                }
                catch( IOException e )
                {
                    Log.e( TAG, "", e );
                }
            if( null != con )
                con.disconnect();
        }        
    }

    
    /**
     * Get content from the given input stream. 
     * @param stream : stream to read.
     * @return content as byte array.
     * @throws IOException
     */
    private static byte[] getBody( InputStream stream ) throws IOException
    {
        if( null == stream )
            return new byte[0];
        
        final int BUF_SIZE = 1024 * 16;
        byte[] buffer = new byte[BUF_SIZE];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while( true )
        {
            int read = stream.read( buffer, 0, BUF_SIZE );              // Read from server into buffer.
            if( -1 == read )
                break;
            bos.write( buffer, 0, read );
        }
        byte[] res = bos.toByteArray();
        bos.close();
        return res;
    }
}
