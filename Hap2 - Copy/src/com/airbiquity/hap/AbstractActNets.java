package com.airbiquity.hap;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbiquity.util_net.ListenerTaskNets;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.TaskNets;


/**
 * An Abstract Activity for network communications.
 * Shows progress while sending bunch of network requests and waiting for the responses.
 */
public abstract class AbstractActNets extends Activity
{
    //private static final String TAG = "AbstractActNet";
    private View v_progress; 
    private View v_error; 
    private TextView tvErrMsg;    
    private TaskNets task;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
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
    abstract protected List<NetReq> getReq();
    

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
        
        List<NetReq> reqs = getReq();
        //Log.e( TAG, ""+req );
        
        if( null != task )
            task.setListener( null );
        task = new TaskNets();
        task.setListener( listener );
        task.execute( reqs.toArray( new NetReq[0] ) );        
    }
    
    
    /**
     * Listener for the response.
     */
    private final ListenerTaskNets listener = new ListenerTaskNets()
    {
        @Override
        public void onDone( List<NetResp> results )
        {
            task = null;
            done( results );
        }
    };
    
    
    /**
     * Override this method to process the response. 
     * Default implementations just calls moveOn().
     * @param results : list of responses. (not checked for errors) 
     */
    protected void done( List<NetResp> results )
    {
        moveOn();
    }
    
    
    /**
     * Override this method to customize it. 
     * Called from onDone() and from startTask() if the task has been completed already.
     * Default implementations just calls finish(). 
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
