package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Demo Mode Introduction screen.
 */
public class ActDemoIntro extends FragmentActivity
{
    private Button btnWatchDemoVideo;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_demo_intro );
        
        btnWatchDemoVideo = (Button) findViewById( R.id.btnWatchDemoVideo );

        OnClickListener oclBtns = new OnClickListener(){public void onClick( View v ) {clicked(v);}};
        btnWatchDemoVideo.setOnClickListener( oclBtns );
    }
    
    public void clicked( View v )
    {
        Intent i = new Intent();
        switch( v.getId() )
        {
            case R.id.btnWatchDemoVideo:
                i.setClass( this, ActDemoVideo.class );
                break;
                
            default:
                return;
        }
        startActivity( i );
        finish();
    }
}
