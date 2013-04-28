package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Shows a message and OK button.
 * If KEY_NEXT_CLASS is set - start that activity when user clicks OK instead of going back.
 * If KEY_NEXT_BUNDLE is set - forward it to the next activity.
 */
public class ActMsg extends FragmentActivity
{
    public static final String KEY_MSG = "ActMsg.KEY_MSG";
    public static final String KEY_NEXT_CLASS = "ActMsg.KEY_NEXT_CLASS";    // Next activity to start.
    public static final String KEY_NEXT_BUNDLE = "ActMsg.KEY_NEXT_BUNDLE";  // Bundle to forward to the next activity.
    
    private Class<?> classNext;
    private Bundle bundleNext;
    
    @Override
    protected void onCreate( Bundle b )
    {
        super.onCreate( b );
        setContentView( R.layout.act_msg );
        Bundle extras = getIntent().getExtras();
        String msg = extras.getString( KEY_MSG );
        classNext = (Class<?>) extras.getSerializable( KEY_NEXT_CLASS );
        bundleNext = extras.getBundle( KEY_NEXT_BUNDLE );
        
        ((Button) findViewById( R.id.btnOk )).setTypeface( A.a().fontNorm );
        
        TextView tvMsg = (TextView) findViewById( R.id.tvMsg );
        tvMsg.setText( msg );
        
    }
    
    /* Button touch listeners. They are called when the user touches button with the same name. */    
    public void btnOk( View v )
    {
        if( null != classNext )
        {
            Intent i = new Intent( this, classNext );
            if( null != bundleNext )
                i.putExtra( KEY_NEXT_BUNDLE, bundleNext );
            startActivity( i );            
        }
        finish();
    }
}
