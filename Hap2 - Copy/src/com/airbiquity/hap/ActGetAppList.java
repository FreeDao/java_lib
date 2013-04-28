package com.airbiquity.hap;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Intent;
import android.util.Log;

import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.UrlMaker;


/**
 * Shows progress while we get app list from BE.
 */
public class ActGetAppList extends AbstractActNet
{
    private static final String TAG = "ActGetAppList";

    /** Last update time of list of apps. */
    private static final String KEY_LAST_UPDATE_TIME_APPS = "ActGetAppList.KEY_DATE_APPS";

    
    @Override
    protected boolean isCompleted() 
    {
        long expiration_period = A.MILS_HOUR;
        long expiration_time = A.a().cfgProg.getLong( KEY_LAST_UPDATE_TIME_APPS, 0 ) + expiration_period;
        long timeCur = System.currentTimeMillis();
        if( timeCur > expiration_time )
            return false;
        return A.a().dbApps.getAppsQntt() > 0;
    }

    
    /**
     * Get list of the apps.
     */
    @Override
    protected NetReq getReq()
    {        
        URL url = UrlMaker.getUrlForGetAppList();
        NetReq req = new NetReq( url, null );
        //Log.e( TAG, ""+req );
        return req;
    }
    

    @Override
    protected void onSuccess( NetResp resp ) 
    {
        try
        {
            String jsonRes = new String( resp.data );
            ArrayList<MetaApp> apps = MetaApp.parseList( jsonRes );
            for( MetaApp app : apps )
                A.a().dbApps.insertApp( app );

            A.a().cfgEdProg.putLong( KEY_LAST_UPDATE_TIME_APPS, System.currentTimeMillis() );
            A.a().cfgEdProg.commit();                
            moveOn();
        }
        catch( JSONException e )
        {
            Log.e( TAG, "", e );
            A.toast( "Error parsing response: "+e.toString() );
            finish();
        }
        
    }
    
    
    @Override
    protected void moveOn()
    {
        Intent i = new Intent( this, ActGetAppIcons.class );
        startActivity( i );
        finish();        
    }   
}