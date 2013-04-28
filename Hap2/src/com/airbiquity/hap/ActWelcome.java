package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActWelcome extends FragmentActivity
{
    private static final String TAG = "ActWelcome";
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_welcome );
        
        ((Button) findViewById( R.id.btnCreateAcc   )).setTypeface( A.a().fontNorm );
        ((Button) findViewById( R.id.btnUseAsGuest  )).setTypeface( A.a().fontNorm );
        ((Button) findViewById( R.id.btnSignIn      )).setTypeface( A.a().fontNorm );
        ((Button) findViewById( R.id.btnDemoMode    )).setTypeface( A.a().fontNorm );

        ((TextView) findViewById( R.id.tx0 )).setTypeface( A.a().fontNorm );
        ((TextView) findViewById( R.id.tx1 )).setTypeface( A.a().fontNorm );
        ((TextView) findViewById( R.id.tx2 )).setTypeface( A.a().fontNorm );
        ((TextView) findViewById( R.id.tx3 )).setTypeface( A.a().fontNorm );        
        
        
        // If the user already logged in - start home activity.
        String mipId = A.getMipId();
        Log.i( TAG, "MIP_ID=" + mipId );
        if( mipId.length() > 0 )
        {
            Intent i = new Intent( this, ActHome.class );
            startActivity( i );
            finish();
        }
    }
    
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        // If the user is logged in - finish this activity.
        if( A.getMipId().length() > 0 )
            finish();
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_act_welcome, menu );
        return true;
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case R.id.settings:
                Intent intent = new Intent( ActWelcome.this, ActSettings.class );
                startActivity( intent );
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnCreateAcc( View v ) 
    {
        startActivity( new Intent(this, ActReg.class) );
    }
    public void btnUseAsGuest( View v ) 
    {
        startActivity( new Intent(this, ActGuestHome.class) );
    }
    public void btnSignIn( View v ) 
    {
        startActivity( new Intent(this, ActSignIn.class) );
    }
    public void btnDemoMode( View v ) 
    {
        startActivity( new Intent(this, ActDemoIntro.class) );
    }
}
