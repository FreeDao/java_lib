package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;


/**
 * Shows progress while sending a POST request to BE.
 * If errors occurs - it will be shown to user.
 * If a message is provided then it will be shown upon success.
 * 
 * Parameters:
 * Key:NetReq.KEY_REQ   Type:Parcelable     Val:NetReq  (required)
 * Key:ActMsg.KEY_MSG   Type:String         Val:String  (optional)
 * 
 */
public class ActPost extends AbstractActNet
{
    private static final String TAG = "ActPost";
    public static final int REQUEST_CODE = 1;
    
    private NetReq req;
    private String msg;

    @Override
    protected void onCreate( Bundle b )
    {
        setResult(RESULT_CANCELED);
        Bundle extras = getIntent().getExtras();
        req = extras.getParcelable( NetReq.KEY_REQ );
        msg = extras.getString( ActMsg.KEY_MSG );
        super.onCreate( b );
    }    
    
    @Override
    protected boolean isCompleted() 
    {
        return false;
    }

    
    /**
     * Get the request.
     */
    @Override
    protected NetReq getReq()
    {        
        return req;
    }
    

    @Override
    protected void onError(NetResp response) 
    {
        Log.e( TAG, ""+response );
        Intent i = new Intent( this, ActMsg.class );
        i.putExtra( ActMsg.KEY_MSG, ""+response );
        startActivity( i );
        finish();
    }
    
    
    @Override
    protected void moveOn()
    {
        if( null != msg )
        {
            Intent i = new Intent( this, ActMsg.class );
            i.putExtra( ActMsg.KEY_MSG, msg );
            startActivity( i );
        }
        setResult( RESULT_OK );
        finish();        
    }   
}