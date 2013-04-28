package com.airbiquity.hap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbiquity.util_net.ListenerTaskNet;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.TaskNet;


/**
 * An Abstract Activity for network communications.
 * Shows progress while sending network request and waiting for the response.
 */
public abstract class AbstractActNet extends Activity
{
    //private static final String TAG = "AbstractActNet";
    private View v_progress; 
    private View v_error; 
    private TextView tvErrMsg;    
    private TaskNet task;
    
    
    @Override
    protected void onCreate( Bundle b )
    {
        super.onCreate( b );
        setContentView( R.layout.act_net_req );

        v_progress = findViewById( R.id.v_progress );
        v_error    = findViewById( R.id.v_error );
        tvErrMsg   = (TextView) findViewById( R.id.err_msg );
        
        ((Button)findViewById( R.id.btnRetry )).setTypeface( A.a().fontNorm );
        
        showProgess();
        startTask();
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

    @Override
    protected void onDestroy() 
    {
        if( null != task )
            task.cancel( true );
        
        super.onDestroy();
    }
    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnRetry( View v ) 
    {
        showProgess();
        startTask();
    }

    
    /**
     * Override this method to return true if the task has been completed already.
     * Default implementation just returns false. 
     * @return true if the response has been already received and processed.
     */
    protected boolean isCompleted()
    {
        return false;
    }
    

    /**
     * Override this method to provide request to process.
     * @return request to process;
     */
    abstract protected NetReq getReq();
    

    /**
     * Start the task.
     */
    private void startTask()
    {
        if( isCompleted() )
        {            
            moveOn();
            return;
        }
        
        NetReq req = getReq();
        //Log.e( TAG, ""+req );
        
        if( null != task )
            task.setListener( null );
        task = new TaskNet();
        task.setListener( listener );
        task.execute( req );        
    }
    
    
    /**
     * Listener for the response.
     */
    private final ListenerTaskNet listener = new ListenerTaskNet()
    {
        @Override
        public void onDone( NetResp response )
        {
            task = null;
            if( response.code / 100 == 2  )   // If result code is 2xx
                onSuccess( response );
            else
                onError( response );
        }
    };
    
    
    /**
     * Called when the task finishes with response code==2xx
     * Default implementation just calls moveOn().
     * Override this method to process the response. 
     * @param resp : response that is in 2xx range. 
     */
    protected void onSuccess( NetResp resp )
    {
        moveOn();
    }
    
    
    /**
     * Called if the task has been completed successfully.
     * Default implementation just calls finish(). 
     * Override this method to customize it. 
     */
    protected void moveOn()
    {
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
     * Called when the task finishes with response code!=2xx 
     * Default implementation shows the error message. 
     * Override this method to customize it.
     */
    protected void onError( NetResp response )
    {
        tvErrMsg.setText( "Error: "+response );
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
