package com.airbiquity.mcs.http.android;

import android.util.Log;

import com.airbiquity.util_net.HttpHeader;
import com.airbiquity.util_net.JsonMaker;

/**
 * Handler that process "handsetProfile" requests.
 */
public class HandlerGetLocation implements IRequestHandler
{
    private static final String TAG = "HandlerGetLocation";


    /**
     * Checks if a request can be processed.
     */
    public boolean canProcess( String url )
    {
        boolean isYes = url.contains( "/hap/api/1.0/handsetLocation" ); 
        Log.d( TAG, "canProcess="+isYes+" : " + url );
        return isYes;
    }


    /**
     * Processes a request ...
     */
    public Response processRequest( Request request, int connectionPoint )
    {
        String strResp = JsonMaker.getLocation();
        byte[] payload = strResp.getBytes();
        Response resp = new Response( "OK", 200, HttpHeader.VAL_JSON, payload, true );
        Log.d( TAG, "processRequest " + request + " conPt=" + connectionPoint+" resp="+strResp );
        return resp;
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
