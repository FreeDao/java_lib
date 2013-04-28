package com.airbiquity.hap;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.airbiquity.util_net.HttpHeader;
import com.airbiquity.util_net.JsonMaker;
import com.airbiquity.util_net.ListenerTaskNet;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.TaskNet;
import com.airbiquity.util_net.UrlMaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ActSignIn extends FragmentActivity
{
    private static final String TAG = "ActSignIn";
    
    public static final String KEY_USER_ID = "ActSignIn.KEY_USER_ID";
    public static final String KEY_PASSWRD = "ActSignIn.KEY_PASSWRD";

    private View v_main; 
    private View v_progress; 
    private View v_error; 
    private EditText editUserId;
    private EditText editPaswrd;
    private TaskNet task;
    private TextView tvErrMsg;
    
    
    @Override
    protected void onCreate( Bundle b )
    {
        super.onCreate( b );
        setContentView( R.layout.act_signin );

        v_main     = findViewById( R.id.v_main );
        v_progress = findViewById( R.id.v_progress );
        v_error    = findViewById( R.id.v_error );
        tvErrMsg   = (TextView) findViewById( R.id.err_msg );
        editUserId  = (EditText) findViewById( R.id.editEmail );
        editPaswrd  = (EditText) findViewById( R.id.editPswrd );
        
        
        ((Button) findViewById( R.id.btnSignIn )).setTypeface( A.a().fontNorm );
        ((Button) findViewById( R.id.btnOk     )).setTypeface( A.a().fontNorm );
        
        Bundle extras = getIntent().getExtras();
        if( null != extras )
        {
            Bundle nextBundle = extras.getBundle( ActMsg.KEY_NEXT_BUNDLE );
            String userId = nextBundle.getString( KEY_USER_ID );
            String paswrd = nextBundle.getString( KEY_PASSWRD );
            
            if( null != userId && null != paswrd )
            {
                editUserId.setText( userId );            
                editPaswrd.setText( paswrd );
                startTask();
            }
        }
        else
        {
			if (!A.getUserEmails().isEmpty()) {
				editUserId.setText(A.getUserEmails().getFirst());
			}
        }
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
    public void btnSignIn( View v ) 
    {
        startTask();
    }
    public void btnOk( View v ) 
    {
        showMain();
    }
    
    private void startTask()
    {
        String username = editUserId.getText().toString().trim();
        String password = editPaswrd.getText().toString().trim();
        
        String jsonReq = JsonMaker.makeSignin( username, password );
        URL url = UrlMaker.getUrlForLogin();
        NetReq req = new NetReq( url, jsonReq, HttpHeader.CONTENT_JSON );

        task = new TaskNet();
        task.setListener( listener );
        task.execute( req );
        showProgess();        
    }

    
    /**
     * Listener for the login task.
     */
    private final ListenerTaskNet listener = new ListenerTaskNet()
    {
        @Override
        public void onDone( NetResp result )
        {
            if( HttpURLConnection.HTTP_OK == result.code )
            {
                try
                {
                    String strRes = new String( result.data );
                    JSONObject resJson = new JSONObject( strRes );
                    String mipId = resJson.optString( "mipId", "" );
                    String authToken = resJson.optString( "authToken", "" );
                    A.setLoginInfo( mipId, authToken );
                    Intent i = new Intent( ActSignIn.this, ActHome.class );
                    startActivity( i );
                    finish();
                }
                catch( JSONException e )
                {
                    Log.e( TAG, "", e );
                    showError( "Error parsing response: "+e.toString() );
                }                
            }
            else
            {
                showError( "Error: "+result );
            }
            task = null;
        }
    };
    

    /**
     * Show main view.
     */
    private void showMain()
    {
        hideAll();
        v_main.setVisibility( View.VISIBLE );
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
        v_main    .setVisibility( View.GONE );
        v_progress.setVisibility( View.GONE );
        v_error   .setVisibility( View.GONE );
    }
}
