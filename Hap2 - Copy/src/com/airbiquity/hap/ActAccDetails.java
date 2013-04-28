package com.airbiquity.hap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ActAccDetails extends FragmentActivity
{
    private static final String TAG = "ActReg";

    private View v_main; 
    private View v_progress; 
    private View v_error; 
    private Button btnSave;
    private Button btnCancel;
    private Button btnDelete;
    private Button btnOk;
    private EditText editEmail;
    private EditText editPswrd1;
    private EditText editPswrd2;
    private TextView tvErrMsg;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_acc_details );

        v_main     = findViewById( R.id.v_main );
        v_progress = findViewById( R.id.v_progress );
        v_error    = findViewById( R.id.v_error );
        tvErrMsg   = (TextView) findViewById( R.id.err_msg );
        btnSave    = (Button) findViewById( R.id.btnSave );
        btnCancel  = (Button) findViewById( R.id.btnCancel );
        btnDelete  = (Button) findViewById( R.id.btnAccDel );
        btnOk      = (Button) findViewById( R.id.btn_ok );
        editEmail  = (EditText) findViewById( R.id.editEmail );
        editPswrd1 = (EditText) findViewById( R.id.editPswrd1 );
        editPswrd2 = (EditText) findViewById( R.id.editPswrd2 );

        OnClickListener oclSave = new OnClickListener(){public void onClick( View v ) {save();}};
        btnSave.setOnClickListener( oclSave );
        
        OnClickListener oclOk = new OnClickListener(){public void onClick( View v ) {showMain();}};
        btnOk.setOnClickListener( oclOk );
    }

//    @Override
//    protected void onPause()
//    {
//        if( null != taskReg )
//            taskReg.setListener( null );
//
//        super.onPause();
//    }


//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        
//        if( null != taskReg )
//            taskReg.setListener( listener );
//    }

    
    /* *
     * Listener for the "register" task.
     * /
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
                    A.setMipId( mipId );
                    Intent i = new Intent( ActAccDetails.this, ActHome.class );
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
                showError( "Connection error: "+result.code );
            }
            taskReg = null;
        }
    };*/


    /**
     * Handle clicks on the "Save" button.
     */
    private void save()
    {
        String username = editEmail.getText().toString().trim();
        String passwrd1 = editPswrd1.getText().toString().trim();
        String passwrd2 = editPswrd2.getText().toString().trim();
        if( !passwrd1.equals( passwrd2 ) )
        {
            A.toast( "Passwords don't match" );
            return;
        }

        // TODO: use TaksEditUserInfo (or something) instead.
//        TaskReg.Params params = new TaskReg.Params( username, passwrd1 );
//        
//        taskReg = new TaskReg();
//        taskReg.setListener( listener );
//        taskReg.execute( params );
        showProgess();
    }
    

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
