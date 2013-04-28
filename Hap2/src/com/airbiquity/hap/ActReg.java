package com.airbiquity.hap;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbiquity.util_net.HttpHeader;
import com.airbiquity.util_net.JsonMaker;
import com.airbiquity.util_net.ListenerTaskNet;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.TaskNet;
import com.airbiquity.util_net.UrlMaker;

/**
 * Create Account.
 */
public class ActReg extends FragmentActivity
{
    //private static final String TAG = "ActReg";

    private View v_main; 
    private View v_progress; 
    private View v_error; 
    private EditText editNameFirst;
    private EditText editNameLast;
    private EditText editZip;
    private EditText editUserId;
    private EditText editPswrd1;
    private EditText editPswrd2;
    private TextView tvErrMsg;
    private TaskNet task;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_reg );

        v_main     = findViewById( R.id.v_main );
        v_progress = findViewById( R.id.v_progress );
        v_error    = findViewById( R.id.v_error );
        tvErrMsg   = (TextView) findViewById( R.id.err_msg );
        editNameFirst = (EditText) findViewById( R.id.editNameFirst );
        editNameLast  = (EditText) findViewById( R.id.editNameLast );
        editZip    = (EditText) findViewById( R.id.editZip );
        editUserId = (EditText) findViewById( R.id.editEmail );
        editPswrd1 = (EditText) findViewById( R.id.editPswrd1 );
        editPswrd2 = (EditText) findViewById( R.id.editPswrd2 );

		if (!A.getUserEmails().isEmpty()) {
			editUserId.setText(A.getUserEmails().getFirst());
		}
        
        ((Button) findViewById( R.id.btnReg )).setTypeface( A.a().fontNorm );
        ((Button) findViewById( R.id.btnOk  )).setTypeface( A.a().fontNorm );
        
        editZip.setVisibility( A.isCountryNeedZip() ? View.VISIBLE : View.GONE );
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

    
    /**
     * Listener for the "register" task.
     */
    private final ListenerTaskNet listener = new ListenerTaskNet()
    {
        @Override
        public void onDone( NetResp result )
        {
            if( HttpURLConnection.HTTP_NO_CONTENT == result.code )
            {
                String userId = editUserId.getText().toString().trim();
                String passwrd = editPswrd1.getText().toString().trim();
                Bundle bundleNext = new Bundle();
                bundleNext.putString( ActSignIn.KEY_USER_ID, userId );
                bundleNext.putString( ActSignIn.KEY_PASSWRD, passwrd );
                
                Intent i = new Intent( ActReg.this, ActMsg.class );
                String msg = getString( R.string.AccountCreated );
                i.putExtra( ActMsg.KEY_MSG, msg );
                i.putExtra( ActMsg.KEY_NEXT_CLASS, ActSignIn.class );
                i.putExtra( ActMsg.KEY_NEXT_BUNDLE, bundleNext );
                startActivity( i );
                finish();
            }
            else
            {
                showError( "Error: "+result );
            }
            task = null;
        }
    };


    /* Button touch listeners. They are called when the user touches button with the same name. */    
    public void btnReg( View v )
    {
        String userNameFirst = editNameFirst.getText().toString().trim();
        String userNameLast = editNameLast.getText().toString().trim();
        String userZip = editZip.getText().toString().trim();
        String userId = editUserId.getText().toString().trim();
        String passwrd1 = editPswrd1.getText().toString().trim();
        String passwrd2 = editPswrd2.getText().toString().trim();
        if( !passwrd1.equals( passwrd2 ) )
        {
            A.toast( "Passwords don't match" );
            return;
        }
        
        String jsonReq = JsonMaker.makeReg( userId, passwrd1, userNameFirst, userNameLast, userZip );
        URL url = UrlMaker.getUrlForReg();
        NetReq req = new NetReq( url, jsonReq, HttpHeader.CONTENT_JSON );

        task = new TaskNet();
        task.setListener( listener );
        task.execute( req );
        showProgess();
    }
    public void btnOk( View v )
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
