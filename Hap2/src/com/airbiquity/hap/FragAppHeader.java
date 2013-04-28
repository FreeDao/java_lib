package com.airbiquity.hap;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Header Fragment for each activity.
 *
 */
public class FragAppHeader extends Fragment
{
    private static final String TAG = "FragAppHeader";
    
    ImageView icHelp;   // Help icon/button.
    ImageView icCon;    // Connection state icon.
    TextView  tvCon;    // Connection state text.
    
    /** Listener for the changes in connection state. */
    private HuConStateListener conListener = new HuConStateListener()
    {
        @Override public void conStateChanged( boolean s ){ setConState( s ); }
    };

    
    /**
     * Show connected/disconnected BT icon to reflect given connection state.
     * @param is_connected
     */
    private void setConState( boolean is_connected )
    {
        if( is_connected )
        {
            tvCon.setText( R.string.con_yes );
            icCon.setImageResource( R.drawable.ic_con_yes );
        }
        else
        {
            tvCon.setText( R.string.con_no );
            icCon.setImageResource( R.drawable.ic_con_no );
        }
    }

    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.frag_app_header, container, false );
        
        TextView tvTitle = (TextView) v.findViewById(R.id.title);
        tvTitle.setTypeface( A.a().fontBold );

        icCon = (ImageView) v.findViewById( R.id.ic_con_state );        
        tvCon =  (TextView) v.findViewById( R.id.tv_con_state );
        tvCon.setTypeface( A.a().fontBold );

        icHelp = (ImageView) v.findViewById( R.id.ic_help );
        OnClickListener ocl = new OnClickListener(){public void onClick( View v ) {clicked(v);}};
        icHelp.setOnClickListener( ocl );
                
        return v;
    }
    
    
    @Override
    public void onStart()
    {
        super.onStart();
        EasyTracker.getInstance().activityStart( getActivity() );
        A.a().addHuConStateListener( conListener );
        setConState( A.a().getHuConState() );
        //Log.d( TAG, "onStart" );
    }


    @Override
    public void onStop()
    {
        //Log.d( TAG, "onStop" );
        A.a().removeHuConStateListener( conListener );
        EasyTracker.getInstance().activityStop( getActivity() );
        super.onStop();
    }


    public void clicked( View v )
    {
        Intent i = new Intent();
        switch( v.getId() )
        {
            case R.id.ic_help:
                i.setClass( getActivity(), ActHelp.class );
                break;
            default:
                return;
        }
        startActivity( i );
    }    
}


