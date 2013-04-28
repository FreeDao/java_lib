package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Add/Change Credit Card. 
 */
public class ActEditCard extends FragmentActivity
{
    private Button btnSave;
    private Button btnCancel;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_edit_card );
        
        btnSave   = (Button) findViewById( R.id.btnSave );
        btnCancel = (Button) findViewById( R.id.btnCancel );

        OnClickListener oclBtns = new OnClickListener(){public void onClick( View v ) {clicked(v);}};
        btnSave  .setOnClickListener( oclBtns );
        btnCancel.setOnClickListener( oclBtns );
    }
    
    public void clicked( View v )
    {
        Intent i = new Intent();
        switch( v.getId() )
        {
            case R.id.btnSave:
                i.setClass( this, ActMsg.class );
                startActivity( i );
                break;
            case R.id.btnCancel:
                finish();
                break;
            default:
                return;
        }        
    }
}
