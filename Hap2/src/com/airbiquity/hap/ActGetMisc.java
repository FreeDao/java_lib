package com.airbiquity.hap;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

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
public class ActGetMisc extends Activity
{
    private static final String TAG = "ActSplash";

    private View v_progress; 
    private View v_error; 
    private TextView tvErrMsg;
    
    /** Task that gets Miscellaneous static content. */
    private TaskNet taskGetMisc;
    
    /** Task that gets Terms and Conditions. */
    private TaskNet taskGetTerms;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_get_misc );

        v_progress = findViewById( R.id.v_progress );
        v_error    = findViewById( R.id.v_error );
        tvErrMsg   = (TextView) findViewById( R.id.err_msg );
        
        ((Button  ) findViewById( R.id.btnRetry     )).setTypeface( A.a().fontNorm );
        
        showProgess();
        getMisc();
    }

    @Override
    protected void onPause()
    {
        if( null != taskGetMisc )
            taskGetMisc.setListener( null );
        if( null != taskGetTerms )
            taskGetTerms.setListener( null );

        super.onPause();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        
        if( null != taskGetMisc )
            taskGetMisc.setListener( listenerMisc );
        if( null != taskGetTerms )
            taskGetTerms.setListener( listenerTerms );
    }

    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnRetry( View v ) 
    {
        showProgess();
        getMisc();
    }


    /**
     * Retrieves static information for a specific country which includes URLs for: 
     * links to online app markets, 
     * the SP Gateway and the NOP mobile portal in case of link-away in SP app.
     */
    private void getMisc()
    {
        if( A.a().cfgProg.getString( A.KEY_MISC, "" ).length() > 0 )
        {            
            getTerms();
            return;
        }

        URL url = UrlMaker.getUrlForMisc();
        NetReq req = new NetReq( url, null, HttpHeader.CONTENT_JSON );
        
        if( null != taskGetMisc )
            taskGetMisc.setListener( null );
        taskGetMisc = new TaskNet();
        taskGetMisc.setListener( listenerMisc );
        taskGetMisc.execute( req );        
    }
    
    
    private void getTerms()
    {
        if( A.a().cfgProg.getString( A.KEY_TERMS, "" ).length() > 0 )
        {            
            moveOn();
            return;
        }

        URL url = UrlMaker.getUrlForTerms();
        NetReq req = new NetReq( url, null, HttpHeader.CONTENT_JSON );
        
        if( null != taskGetTerms )
            taskGetTerms.setListener( null );
        taskGetTerms = new TaskNet();
        taskGetTerms.setListener( listenerTerms );
        taskGetTerms.execute( req );        
    }
    
    
    /**
     * Listener for the countries.
     */
    private final ListenerTaskNet listenerMisc = new ListenerTaskNet()
    {
        @Override
        public void onDone( NetResp result )
        {
            taskGetMisc = null;
            if( HttpURLConnection.HTTP_OK != result.code )
            {
                showError( "Error: "+result );
                return;
            }
            
            try
            {
                String jsonMisc = new String( result.data );
                JSONObject jMisc = new JSONObject( jsonMisc );
                A.a().cfgEdProg.putString( A.KEY_MISC, jsonMisc );
                A.a().cfgEdProg.commit();
                
                getTerms();
            }
            catch( JSONException e )
            {
                Log.e( TAG, "", e );
                showError( "Error parsing response: "+e.toString() );
            }
        }
    };
    
    /**
     * Listener for the Terms and Conditions.
     */
    private final ListenerTaskNet listenerTerms = new ListenerTaskNet()
    {
        @Override
        public void onDone( NetResp result )
        {
            taskGetTerms = null;
            if( HttpURLConnection.HTTP_OK != result.code )
            {
                showError( "Error: "+result );
                return;
            }
            
            try
            {
                String jsonTerms = new String( result.data );
                JSONObject jTerms = new JSONObject( jsonTerms );
                A.a().cfgEdProg.putString( A.KEY_TERMS, jsonTerms );
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
        Intent i = new Intent( this, ActTerms.class );
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
