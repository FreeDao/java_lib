package com.airbiquity.util_net;

import android.os.AsyncTask;
import android.util.Log;



/**
 * Task that sends Network request and waits for response.
 * 
 *                           AsyncTask<Params, Progress, Result>
 */
public class TaskNet extends AsyncTask<NetReq, Integer, NetResp>
{
    private static final String TAG = "TaskNet";

    private ListenerTaskNet l;
    private boolean done;
    private NetResp res;


    /**
     * Constructor.
     */
    public TaskNet()
    {
        done = false;
        l = null;
    }


    /**
     * Sets listener to be notified on tasks progress. 
     * @param listener - listener to be notified on tasks progress. Set to null to disable notifications.
     */
    public void setListener( ListenerTaskNet listener )
    {
        l = listener;
        if( done && null != l )
            l.onDone( res );
    }


    protected NetResp doInBackground( NetReq... requests )
    {
        NetReq req = requests[0];
        Log.d( TAG, ""+req );        
        NetResp resp = HttpUtil.com( req );
        Log.d( TAG, ""+resp );        
        return resp;
    }


    protected void onPostExecute( NetResp result )
    {
        done = true;
        res = result;

        if( null != l )
            l.onDone( res );
    }
}
