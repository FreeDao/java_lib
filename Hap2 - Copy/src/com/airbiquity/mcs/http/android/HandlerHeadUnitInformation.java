package com.airbiquity.mcs.http.android;

import org.json.JSONException;

import android.util.Log;

import com.airbiquity.hap.A;
import com.airbiquity.hap.MetaHuInfo;
import com.airbiquity.util_net.HttpHeader;

/**
 * Handler that processes "headUnitInformation" requests.
 */
public class HandlerHeadUnitInformation implements IRequestHandler
{
    private static final String TAG = "HandlerHeadUnitInformation";


    /**
     * Checks if a request can be processed.
     */
    public boolean canProcess( String url )
    {
        boolean isYes = url.contains( "/hap/api/1.0/headUnitInformation" ); 
        Log.d( TAG, "canProcess="+isYes+" : " + url );
        return isYes;
    }


    /**
     * Processes a request ...
     */
    public Response processRequest( Request request, int connectionPoint )
    {
        Log.d( TAG, "processRequest " + request + " conPt=" + connectionPoint );
        Response resp = new Response( "Error", 500, HttpHeader.VAL_TEXT, "Invalid request".getBytes(), true );
    	
    	if( request.content_type.equalsIgnoreCase(HttpHeader.VAL_JSON) )
    	{
	        try
	        {
	        	String strJson = new String(request.data );
	        	A.a().curHuInfo = MetaHuInfo.valueOf( strJson );
	        	A.a().lastHuInfo = A.a().curHuInfo;
	        	// TODO: use to make sure the correct OEM BA is running.
	        	resp = new Response( "OK", 200, null, null, true );
	        }
	        catch( JSONException e )
	        {
	            Log.e( TAG, "Received invalid Head Unit Information request." );
	        }
        }

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
