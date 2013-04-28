package com.airbiquity.hap;

import java.net.URL;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbiquity.db.DbCars;
import com.airbiquity.util_net.JsonMaker;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.UrlMaker;

/**
 * My Vehicle Subscription Details.
 */
public class ActSubInfo extends FragmentActivity
{
    //private static final String TAG = "ActSubInfo";
    
    private MetaCarSub sub;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_sub_info );

        Bundle extras = getIntent().getExtras();
        int idSub = extras.getInt( DbCars.KEY_SUB_ID );
        
        sub = A.a().dbSubs.getMetaSubById( idSub );

        TextView tvNick = (TextView) findViewById( R.id.tvInfo );
        ((Button) findViewById( R.id.btnUpdate )).setTypeface( A.a().fontNorm );
        ((Button) findViewById( R.id.btnDelete )).setTypeface( A.a().fontNorm );
        
        tvNick.setText( sub.nickname );
    }
    
    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnUpdate( View v ) 
    {
//        Intent i = new Intent();
//        i.setClass( this, ActIsOwner.class );
//        startActivity( i );
    }
    public void btnDelete( View v ) 
    {
        String jsonReq = JsonMaker.makeSubDeleteReq( sub.id );
        URL url = UrlMaker.getUrlForRequestSubCancel();
        NetReq req = new NetReq( url, jsonReq );

        Intent i = new Intent( this, ActPost.class );        
        String msg = getString( R.string.subDeleteSent );
        i.putExtra( NetReq.KEY_REQ, req );
        i.putExtra( ActMsg.KEY_MSG, msg );
        startActivityForResult( i, ActPost.REQUEST_CODE );
    }
    
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if( requestCode == ActPost.REQUEST_CODE )
        {
            if( resultCode == RESULT_OK )
                finish();
        }
    }    
}
