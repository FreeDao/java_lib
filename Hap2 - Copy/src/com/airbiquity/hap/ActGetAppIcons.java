package com.airbiquity.hap;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbiquity.util_net.ListenerTaskNet;
import com.airbiquity.util_net.ListenerTaskNets;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.TaskNet;
import com.airbiquity.util_net.TaskNets;
import com.airbiquity.util_net.UrlMaker;


/**
 * Shows progress while we get app icons from BE.
 */
public class ActGetAppIcons extends AbstractActNets
{
    private static final String TAG = "ActGetAppList";

    /** Last update time of the icons. */
    private static final String KEY_LAST_UPDATE_TIME_ICONS = "ActGetAppList.KEY_DATE_ICONS";


    /** List of apps. */
    private LinkedList<MetaApp> apps;

    
    @Override
    protected boolean isCompleted() 
    {
        long expiration_period = A.MILS_HOUR * 24;
        long expiration_time = A.a().cfgProg.getLong( KEY_LAST_UPDATE_TIME_ICONS, 0 ) + expiration_period;
        long timeCur = System.currentTimeMillis();
        if( timeCur > expiration_time )
            return false;
        
        apps = A.a().dbApps.getAppsWithoutIcons();
        return apps.size() == 0;
    }

    
    @Override
    protected List<NetReq> getReq()
    {
        if( null == apps )
            apps = A.a().dbApps.getAppsWithoutIcons();
        LinkedList<NetReq> reqs = new LinkedList<NetReq>();
        for( MetaApp app : apps )
        {
            URL url = UrlMaker.str2url( app.urlDownIcon );
            NetReq req = new NetReq( url, null );
            reqs.add( req );
        }
        return reqs;
    }

    @Override
    protected void done( List<NetResp> results ) 
    {
        for( NetResp result : results )
            onIconDown( result );
        A.a().cfgEdProg.putLong( KEY_LAST_UPDATE_TIME_ICONS, System.currentTimeMillis() );
        A.a().cfgEdProg.commit();                
        moveOn();        
    }
    
    
    private void onIconDown( NetResp result )
    {
        if( HttpURLConnection.HTTP_OK != result.code )
        {
            Log.e( TAG, "Error: "+result );
            return;
        }
        
            Bitmap bmpIn = BitmapFactory.decodeByteArray( result.data, 0, result.data.length );
            
//            String jsonRes = new St ring( result.data );
//            apps = MetaApp.parseList( jsonRes );
//            for( MetaApp app : apps )
//            {
//                MetaApp appOld = A.a().dbApps.getMetaAppById( app.id );
//                A.a().dbApps.insertApp( app );
//                if( null == appOld )
//                    appsNew.add( app );
//            }
            moveOn();
    }
    

    @Override
    protected void moveOn()
    {
        Intent i = new Intent( this, ActMyApps.class );
        startActivity( i );
        finish();        
    }
}
