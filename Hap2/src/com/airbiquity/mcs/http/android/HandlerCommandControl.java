package com.airbiquity.mcs.http.android;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.airbiquity.application.manager.ApplicationManager;
import com.airbiquity.application.manager.ApplicationMessage;
import com.airbiquity.hap.A;
import com.airbiquity.hap.IHapCallback;

/**
 * Handler that process "commandControl" requests.
 */
public class HandlerCommandControl implements IRequestHandler
{
    private static final String TAG = "HandlerCommandControl";
    private static final String URL_PATH = "/hap/api/1.0/commandControl/";
    
    /** Sequence number that we use to match response and request. It is unique for each request-response pair. */
    private volatile int seqNum = 100;


    /**
     * Checks if a request can be processed.
     */
    public boolean canProcess( String url )
    {
        boolean isYes = url.contains( URL_PATH ); 
        Log.d( TAG, "canProcess="+isYes+" : " + url );
        return isYes;
    }


    /**
     * Processes a request that was sent from HUP to nomadic app.
     * All we do here is forward the request to the right nomadic app and send back the response.
     */
    public Response processRequest( Request request, int connectionPoint )
    {
        try
        {
            String url =request.url;
            int idx = url.indexOf( URL_PATH );
            if( idx < 0 )
            {
                Log.d( TAG, "Bad url: "+url );
                return new Response( "Error", 500, "text/plain", ("Bad url: "+url).getBytes(), true );
            }
            idx += URL_PATH.length();
            String appName = url.substring( idx ); 
            Log.d( TAG, "processRequest req="+request+" app="+appName+" conPt="+connectionPoint+" payld="+request.data );
            
            ApplicationManager appManager = ApplicationManager.getInstance();
            IHapCallback c = appManager.getCallback( appName );
            if( null == c )
            {
                // TODO: Try to start the app? 
                Log.d( TAG, "No callback for "+appName );
                return new Response( "Error", 500, "text/plain", ("No callback for "+appName).getBytes(), true );
            }
            
            final LinkedBlockingQueue<ApplicationMessage> q = new LinkedBlockingQueue<ApplicationMessage>();
            A.a().mapQueues.put( seqNum, q );                       // Create a queue for the response to this request.            
            //byte[] payload = strPayload.getBytes();
            c.onHapCommandReceived( seqNum, request.data, request.content_type );  // Send message to the nomadic app.            
            ApplicationMessage msg = q.poll( 10, TimeUnit.SECONDS );// Get blocked until we have a response from the nomadic app.
            A.a().mapQueues.remove( seqNum );                       // Delete the queue.
            seqNum++;
            Response resp;

            if( null == msg )
            {
                Log.d( TAG, "Timeout" );
                resp = new Response( "Error", 500, "text/plain", "Timeout".getBytes(), true );
            }
            else
            {
                resp = new Response( "OK", 200, msg.contentType, msg.payload, true );
                Log.d( TAG, "processRequest resp: " + msg.appName + " seq#=" + msg.sequenceNumber+" data="+msg.payload );
            }
            return resp;
        }
        catch( Exception e )
        {
            Log.e( TAG, "processRequest", e );
            return new Response( "Error", 500, "text/plain", e.toString().getBytes(), true );
        }
    }


    /**
     * Not used because it does not send asynchronous messages
     */
    public void setHttpLayer( IHttpLayer layer )
    {
        Log.d( TAG, "setHttpParams " + layer );
        // Do nothing
    }
    
    
    @Override
    public String toString()
    {
        return TAG;
    }
}
