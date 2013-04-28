package com.airbiquity.hap;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbiquity.util_net.HttpHeader;
import com.airbiquity.util_net.ListenerTaskNet;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.TaskNet;
import com.airbiquity.util_net.UrlMaker;


/**
 * Shows progress while we download the static content.
 */
public class ActSplash extends Activity
{
    private static final String TAG = "ActSplash";

    private View v_progress; 
    private View v_error; 
    private TextView tvErrMsg;
    
    /** Task that gets Countries. */
    private TaskNet task;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_splash );

        v_progress = findViewById( R.id.v_progress );
        v_error    = findViewById( R.id.v_error );
        tvErrMsg   = (TextView) findViewById( R.id.err_msg );
        
        ((Button  ) findViewById( R.id.btnRetry     )).setTypeface( A.a().fontNorm );
        ((TextView) findViewById( R.id.tv_powered_by)).setTypeface( A.a().fontNorm );
        
        showProgess();
        getCountries();
    }

    @Override
    protected void onPause()
    {
        if( null != task )
            task.setListener( null );

        super.onPause();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        
        if( null != task )
            task.setListener( listener );
    }

    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnRetry( View v ) 
    {
        showProgess();
        getCountries();
    }


    /**
     * Get list of countries (if we don't have them already).
     */
    private void getCountries()
    {
        if( A.a().cfgProg.getString( A.KEY_COUNTRIES, "" ).length() > 0 )
        {            
            moveOn();
            return;
        }
        
        URL url = UrlMaker.getUrlForGetCountries();
        NetReq req = new NetReq( url, null, HttpHeader.CONTENT_JSON );
        //Log.e( TAG, ""+req );
        
        if( null != task )
            task.setListener( null );
        task = new TaskNet();
        task.setListener( listener );
        task.execute( req );        
    }
    
    
    /**
     * Listener for the countries.
     */
    private final ListenerTaskNet listener = new ListenerTaskNet()
    {
        @Override
        public void onDone( NetResp result )
        {
            task = null;
            if( HttpURLConnection.HTTP_OK != result.code )
            {
                showError( "Error: "+result );
                return;
            }
            
            try
            {
                String jsonCountries = new String( result.data );
                ArrayList<MetaCountry> list = MetaCountry.parseList( jsonCountries );
                A.a().cfgEdProg.putString( A.KEY_COUNTRIES, jsonCountries );
                A.a().cfgEdProg.commit();
                moveOn();
            }
            catch( JSONException e )
            {
                Log.e( TAG, "", e );
                showError( "Error parsing response: "+e.toString() );
            }
        }
    };
    
    
    /**
     * Called when we finish downloading all static content - now move on to the next screen.
     */
    private void moveOn()
    {
        Intent i = new Intent( this, ActCountry.class );
        startActivity( i );
        finish();        
    }

    
    /**
     * Show progress view.
     */
    private void showProgess()
    {
        hideAll();
        v_progress.setVisibility( View.VISIBLE );
    }


    /**
     * Show the error view.
     */
    private void showError( String msg )
    {
        tvErrMsg.setText( msg );
        hideAll();
        v_error.setVisibility( View.VISIBLE );
    }

    
    /**
     * Hide all layouts.
     */
    private void hideAll()
    {
        v_progress.setVisibility( View.GONE );
        v_error   .setVisibility( View.GONE );
    }
}
