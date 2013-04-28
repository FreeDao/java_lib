package com.airbiquity.util_net;

import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;



/**
 * Task that process a bunch of Network request.
 * 
 *                           AsyncTask<Params, Progress, Result>
 */
public class TaskNets extends AsyncTask<NetReq, Integer, List<NetResp>>
{
    private static final String TAG = "TaskNet";

    private ListenerTaskNets l;
    private boolean done;
    private List<NetResp> res;


    /**
     * Constructor.
     */
    public TaskNets()
    {
        done = false;
        l = null;
    }


    /**
     * Sets listener to be notified on tasks progress. 
     * @param listener - listener to be notified on tasks progress. Set to null to disable notifications.
     */
    public void setListener( ListenerTaskNets listener )
    {
        l = listener;
        if( done && null != l )
            l.onDone( res );
    }


    protected List<NetResp> doInBackground( NetReq... requests )
    {
        List<NetResp> results = new LinkedList<NetResp>();
        for( NetReq req : requests )
        {
            Log.d( TAG, ""+req );        
            NetResp resp = HttpUtil.com( req );
            Log.d( TAG, ""+resp );
            results.add( resp );
        }
        return results;
    }


    protected void onPostExecute( List<NetResp> results )
    {
        done = true;
        res = results;

        if( null != l )
            l.onDone( res );
    }
}
