package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * The "Manage My Account" screen.
 */
public class ActMyAccount extends FragmentActivity
{
    private Button btnAccSettings;
    private Button btnPayBil;

    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_my_account );
        
        btnAccSettings = (Button) findViewById( R.id.btnAccSettings );
        btnPayBil      = (Button) findViewById( R.id.btnPayBil );

        OnClickListener oclBtns = new OnClickListener(){public void onClick( View v ) {clicked(v);}};
        btnAccSettings.setOnClickListener( oclBtns );
        btnPayBil     .setOnClickListener( oclBtns );
    }
    
    public void clicked( View v )
    {
        Intent i = new Intent();
        switch( v.getId() )
        {
            case R.id.btnAccSettings:
                i.setClass( this, ActAccDetails.class );
                break;
            case R.id.btnPayBil:
                i.setClass( this, ActPayBil.class );
                break;
            default:
                return;
        }
        startActivity( i );
    }
}
