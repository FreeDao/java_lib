package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActDemoHome extends FragmentActivity
{
    private Button btnMangeApps;
    private Button btnCreateAcc;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_demo_home );
        
        btnMangeApps = (Button) findViewById( R.id.btnMangeApps );
        btnCreateAcc = (Button) findViewById( R.id.btnCreateAcc );

        OnClickListener oclBtns = new OnClickListener(){public void onClick( View v ) {clicked(v);}};
        btnMangeApps.setOnClickListener( oclBtns );
        btnCreateAcc.setOnClickListener( oclBtns );
    }
    
    public void clicked( View v )
    {
        Intent i = new Intent();
        switch( v.getId() )
        {
            case R.id.btnMangeApps:
                i.setClass( this, ActGetAppList.class );
                break;
            case R.id.btnCreateAcc:
                i.setClass( this, ActReg.class );
                break;
            default:
                return;
        }
        startActivity( i );
    }
}
