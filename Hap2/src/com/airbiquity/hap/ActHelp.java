package com.airbiquity.hap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActHelp extends FragmentActivity
{
    private Button btnOk;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_help );
        
        btnOk = (Button) findViewById( R.id.btnOk );

        OnClickListener oclBtns = new OnClickListener(){public void onClick( View v ) {clicked(v);}};
        btnOk.setOnClickListener( oclBtns );
    }
    
    public void clicked( View v )
    {
        //Intent i = new Intent();
        switch( v.getId() )
        {
            case R.id.btnOk:
                finish();
                break;
            default:
                return;
        }
        //startActivity( i );
    }
}
