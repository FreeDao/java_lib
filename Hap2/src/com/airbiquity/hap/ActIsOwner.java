package com.airbiquity.hap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Shows question: "Are you the owner?"
 */
public class ActIsOwner extends Activity
{    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_is_owner );
        ((Button) findViewById( R.id.btnNo  )).setTypeface( A.a().fontNorm );
        ((Button) findViewById( R.id.btnYes )).setTypeface( A.a().fontNorm );
    }
    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnNo( View v ) 
    {
        A.a().isOwner = false;
        finish();
    }
    public void btnYes( View v ) 
    {
        A.a().isOwner = true;
        Intent i = new Intent();
        i.setClass( this, ActFillAddCar.class );
        startActivity( i );
        finish();
    }    
}
