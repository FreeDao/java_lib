package com.airbiquity.util_net;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import com.airbiquity.application.manager.ApplicationMessage;
import com.airbiquity.hap.A;
import com.airbiquity.hap.MetaCarInfo;
import com.airbiquity.hap.MetaHuInfo;
import com.airbiquity.hap.MetaCarSub;

import android.os.AsyncTask;
import android.util.Log;



/**
 * Task that's started each time HU connects to the Agent.
 * 
 *                              AsyncTask<Params, Progress, Result>
 */
public class TaskOnHuCon extends AsyncTask<Void, Integer, Exception>
{
    private static final String TAG = "TaskOnHuCon";


    /**
     * Constructor.
     */
    public TaskOnHuCon()
    {
        
    }


    protected Exception doInBackground( Void... requests )
    {
        try
        {
            // Wait until HU is connected and get its info.
            MetaHuInfo hu = null;
            while( null == hu )
            {
                try
                {
                    Thread.sleep( 100 );
                }
                catch( InterruptedException ex )
                {
                    return null;
                }
                hu = A.a().curHuInfo;
            }

            // Ask BE if this HU is activated already.
            {
                URL url = UrlMaker.getUrlForGetCarInfo( hu.sn );
                NetReq req = new NetReq( url, null );
                Log.d( TAG, ""+req );        
                NetResp resp = HttpUtil.com( req );
                Log.d( TAG, ""+resp );
                String jsonData = new String( resp.data );
                Log.d( TAG, ""+jsonData );
                MetaCarInfo m = MetaCarInfo.valueOf( jsonData );
                if( m.isActivated )
                    return null;    // This car is already activated.
            }

            // Get list of User’s Vehicle Subscriptions.
            ArrayList<MetaCarSub> listSubscriptions;
            {
                URL url = UrlMaker.getUrlForGetUsersVehicleSubscriptions();
                NetReq req = new NetReq( url, null );
                Log.d( TAG, ""+req );        
                NetResp resp = HttpUtil.com( req );
                Log.d( TAG, ""+resp );
                String jsonData = new String( resp.data );
                Log.d( TAG, ""+jsonData );
                listSubscriptions = MetaCarSub.parseList( jsonData );
            }

            // Find 1st inactive car.
            MetaCarSub fi = null;  // First Inactive car.
            for( MetaCarSub s : listSubscriptions )
            {
                if( !s.isActivated )
                    fi = s;
            }            
            if( null == fi )
                return new IllegalStateException("No inactive cars found");

            // Call BE to activate this HU.
            {
                URL url = UrlMaker.getUrlForActivateCar();
                String jsonReq = JsonMaker.makeActivateCar( hu.sn, hu.type, fi.nickname, fi.model, fi.year );
                NetReq req = new NetReq( url, jsonReq );
                Log.d( TAG, ""+req );        
                NetResp resp = HttpUtil.com( req );
                Log.d( TAG, ""+resp );
                String jsonData = new String( resp.data );
                Log.d( TAG, ""+jsonData );
                //MetaVehicleId idCar = MetaVehicleId( jsonData );                
            }
        }
        catch( JSONException e )
        {
            return e;
        }
        
        return null;
    }


    protected void onPostExecute( Exception e )
    {
    	if( null == e )
            A.toast( "Activation OK" );
        else
        {
            A.toast( "Activation Error: "+e );
            Log.e( TAG, "", e );
        }
    	
		try
		{
			// queue the activation completion event to be sent to the head unit
			ApplicationMessage msg = new ApplicationMessage(
					"hap",
					0,
					JsonMaker.makeVehicleActivationCompletionEvent( (null == e) ? "success" : "failure" ).getBytes(),
					HttpHeader.VAL_JSON);
			A.a().qLongPolling.put(msg);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
    }
}
