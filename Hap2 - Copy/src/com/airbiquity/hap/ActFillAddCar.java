package com.airbiquity.hap;

import java.net.URL;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airbiquity.util_net.JsonMaker;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.UrlMaker;

/**
 * Let user to enter info for "Add Car" call to BE.
 */
public class ActFillAddCar extends FragmentActivity
{
    private EditText editYear;
    private EditText editModel;
    private EditText editNick;
    private EditText editVin;
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_fill_add_car );

        ((Button) findViewById( R.id.btnOk  )).setTypeface( A.a().fontNorm );
        
        editYear  = (EditText) findViewById( R.id.editYear );
        editModel = (EditText) findViewById( R.id.editModel );
        editNick  = (EditText) findViewById( R.id.editNick );
        editVin   = (EditText) findViewById( R.id.editVin );

        editVin.setVisibility( A.isCountryNeedZip() ? View.VISIBLE : View.GONE );
    }

    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnOk( View v )
    {
        String strYear = editYear .getText().toString().trim();
        String model   = editModel.getText().toString().trim();
        String nick    = editNick .getText().toString().trim();
        String vin     = editVin  .getText().toString().trim();
        
        if( strYear.length() != 4 && strYear.length() != 0 )
        {
            A.toast( R.string.badYear );
            return;
        }
        
        if( nick.length() < 1 )
        {
            A.toast( R.string.badNick );
            return;
        }

        if( vin.length() < 1 && A.isCountryNop() )
        {
            A.toast( R.string.badVin );
            return;
        }
        
        int year = strYear.length() > 0 ? Integer.valueOf( strYear ) : 0;

        String jsonReq = JsonMaker.makeAddCar( year, model, nick, vin );
        URL url = UrlMaker.getUrlForAddCar();
        NetReq req = new NetReq( url, jsonReq );

        Intent i = new Intent( this, ActPost.class );
        String msg = getString( R.string.carAdded );
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
