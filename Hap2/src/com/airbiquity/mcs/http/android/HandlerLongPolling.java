package com.airbiquity.mcs.http.android;


import com.airbiquity.application.manager.ApplicationMessage;
import com.airbiquity.hap.A;

import android.util.Log;

/**
 * Handler that process "getEvent" (aka long-polling) requests.
 */
public class HandlerLongPolling implements IRequestHandler
{
    private static final String TAG = "HandlerLongPolling";
    private IHttpLayer httpLayer;   // HTTP layer to use for asynchronous responses.


    /**
     * Checks if a request can be processed.
     */
    public boolean canProcess( String url )
    {
        boolean isYes = url.contains( "/hap/api/1.0/getEvent" ); 
        Log.d( TAG, "canProcess="+isYes+" : " + url );
        return isYes;        
    }


    /**
     * Processes a request asynchronously.
     * @return null immediately to tell the caller that we will send the response asynchronously later.
     */
    public Response processRequest( final Request request, final int connectionPoint )
    {
        Log.d( TAG, "Received LP req: " + request + " conPt=" + connectionPoint );        
        Runnable r = new Runnable() { public void run() { processRequestAsync( request, connectionPoint ); } };
        Thread t = new Thread( r );
        t.start();
        return null;    // Tells the caller that we will send the response asynchronously later.
    }

    
    /**
     * Processes a request.
     * This methods blocks until the response is ready.
     * @param request
     * @param connectionPoint
     */
    private void processRequestAsync( Request request, int connectionPoint )
    {
        try
        {
            ApplicationMessage msg = A.a().qLongPolling.take(); // Blocks until we get an async message from nomadic app.
            
            // setup default error response
            Response resp = new Response( "Error", 500, "text/plain", "Internal Server Error".getBytes(), true );
            
            if ( (null != msg) && msg.isValid() )
            {
	            resp = new Response( "OK", 200, msg.contentType, msg.payload, true );
	            resp.AdditionalHeaders = new String[]{"Server: "+msg.appName};
	            Log.d( TAG, "processRequest res: " + msg.appName + " conPt=" + connectionPoint );
            }

            // send response to the head unit
            httpLayer.sendResponse( connectionPoint, resp );
        }
        catch( InterruptedException e )
        {
            // Do nothing - the thread is cancelled. (We don't cancel this thread, so this will never happen)            
        }        
    }

    /**
     * Sets HTTP layer to be used for asynchronous responses. 
     * @param httpLayer : HTTP layer to use for asynchronous responses.
     */    
    public void setHttpLayer( IHttpLayer layer )
    {
        httpLayer = layer;
    }


    @Override
    public String toString()
    {
        return TAG;
    }
}
