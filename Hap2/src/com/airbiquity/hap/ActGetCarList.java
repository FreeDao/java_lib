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
 * Shows progress while we get car list from BE.
 */
public class ActGetCarList extends AbstractActNet
{
    private static final String TAG = "ActGetCarList";

    /** Last update time of car list. */
    private static final String KEY_LAST_UPDATE_TIME = "ActGetCarList.KEY_DATE";

    
    @Override
    protected boolean isCompleted() 
    {
        return false;
//        long expiration_period = 0;//A.MILS_HOUR;
//        long expiration_time = A.a().cfgProg.getLong( KEY_LAST_UPDATE_TIME, 0 ) + expiration_period;
//        long timeCur = System.currentTimeMillis();
//        if( timeCur > expiration_time )
//            return false;
//        return A.a().cfgProg.getString( KEY_LIST, "" ).length() > 0;
    }

    
    @Override
    protected NetReq getReq()
    {            
        URL url = UrlMaker.getUrlForGetUsersVehicleSubscriptions();
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
            ArrayList<MetaCarSub> subs = MetaCarSub.parseList( jsonRes );
            for( MetaCarSub sub : subs )
                A.a().dbSubs.insertSub( sub );
            
            A.a().cfgEdProg.putLong( KEY_LAST_UPDATE_TIME, System.currentTimeMillis() );
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
        Intent i = new Intent( this, ActMySubscrs.class );
        startActivity( i );
        finish();        
    }   
}