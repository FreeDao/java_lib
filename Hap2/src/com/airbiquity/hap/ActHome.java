package com.airbiquity.hap;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbiquity.connectionmgr.coonmgr.android.ConnectionManager;
import com.airbiquity.util_net.ListenerTaskNet;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.TaskNet;
import com.airbiquity.util_net.UrlMaker;

public class ActHome extends FragmentActivity
{
    private static final String TAG = "ActHome";

    private Button btnMangeApps;
    private Button btnMangeCars;
    private Button btnMangeAcc;
    private Button btnSignOut;
    
    private View v_main; 
    private View v_progress; 
    private View v_error; 
    private TextView tvErrMsg;
    private TaskNet task;
    
    // Get the default adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 2;
     
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_home );
        
        v_main     = findViewById( R.id.scrollView1 );
        v_progress = findViewById( R.id.v_progress );
        v_error    = findViewById( R.id.v_error );
        tvErrMsg   = (TextView) findViewById( R.id.err_msg );
        
        btnMangeApps = (Button) findViewById( R.id.btnMangeApps );
        btnMangeCars = (Button) findViewById( R.id.btnMangeCars );
        btnMangeAcc  = (Button) findViewById( R.id.btnMangeAcc );
        btnSignOut   = (Button) findViewById( R.id.btnSignOut );
        
        btnMangeApps.setTypeface( A.a().fontNorm );
        btnMangeCars.setTypeface( A.a().fontNorm );
        btnMangeAcc .setTypeface( A.a().fontNorm );
        btnSignOut  .setTypeface( A.a().fontNorm );
        
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
       
    }
    
    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // startService() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
        	// Otherwise, startService
        	// successfully login
            startService( new Intent( "com.airbiquity.hap.AgentService" ) );
        	
        }
    }

    
    /** Button touch listeners. They are called when the user touches button with the same name. */
    public void btnClkMangeApps( View v ) 
    {
        startActivity( new Intent(this, ActGetAppList.class) );
    }
    public void btnClkMangeCars( View v ) 
    {
        startActivity( new Intent(this, ActGetCarList.class) );
    }
    public void btnClkMangeAcc( View v ) 
    {
        startActivity( new Intent(this, ActMyAccount.class) );
    }
    public void btnClkSignOut( View v ) 
    {
        URL url = UrlMaker.getUrlForLogout();
        NetReq req = new NetReq( url,"");

        task = new TaskNet();
        task.setListener( listener );
        task.execute( req );
        showProgess();    
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a bluetooth server
            	// startService( new Intent( "com.airbiquity.hap.AgentService" ) );
            	// bluetooth state receiver will start the service
            } else {
                // User did not enable Bluetooth or an error occured
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                A.setLoginInfo( "", "" );
                startActivity( new Intent(this, ActWelcome.class) );
                finish();
            }
        }
    }
    
    /**
     * Listener for the Logout task.
     */
    private final ListenerTaskNet listener = new ListenerTaskNet()
    {
        @Override
        public void onDone( NetResp result )
        {
            ConnectionManager.disconnect();
            A.setLoginInfo( "", "" );
            startActivity( new Intent(ActHome.this, ActWelcome.class) );
            finish();
            if( HttpURLConnection.HTTP_NO_CONTENT != result.code )
                Log.e( TAG, ""+result );
        }
    };
    
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
    
    /**
     * Show progress view.
     */
    private void showProgess()
    {
        hideAll();
        v_progress.setVisibility( View.VISIBLE );
    }
    
    public void btnOk( View v ) 
    {
        showMain();
    }
    
    /**
     * Show main view.
     */
    private void showMain()
    {
        hideAll();
        v_main.setVisibility( View.VISIBLE );
    }
}
