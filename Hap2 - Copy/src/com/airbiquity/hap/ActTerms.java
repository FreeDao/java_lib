package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ActTerms extends FragmentActivity
{
    private static final String TAG = "ActTerms";

    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_terms );
        
        // Check if user has already accepted the terms.
        Boolean terms_accepted = A.a().cfgProg.getBoolean( A.KEY_TERMS_ACCEPTED, false );
        if( terms_accepted )
        {
            moveOn();
            return;
        }
        
        ((Button) findViewById( R.id.btnOk )).setTypeface( A.a().fontNorm );
        
        TextView tv = (TextView) findViewById( R.id.tv );
        String terms = A.a().cfgProg.getString( A.KEY_TERMS, "" );
        tv.setText( terms );
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }

    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnOk( View v ) 
    {
        A.a().cfgEdProg.putBoolean( A.KEY_TERMS_ACCEPTED, true );
        A.a().cfgEdProg.commit();
        moveOn();
    }
    
    private void moveOn()
    {
        Intent i = new Intent( this, ActWelcome.class );
        startActivity( i );
        finish();        
    }

}
