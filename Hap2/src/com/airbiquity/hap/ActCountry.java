package com.airbiquity.hap;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class ActCountry extends FragmentActivity
{
    private static final String TAG = "ActCountry";
    private ListView list;
    private ArrayList<MetaCountry> countries;

    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_country );

        // Check if user has already selected the country.
        String saved_country = A.getCountryId();
        if( saved_country.length() > 0 )
        {
            //Log.e( TAG, "saved_country="+saved_country );
            moveOn();
            return;
        }

        ((Button)findViewById(R.id.btnConfirm)).setTypeface( A.a().fontNorm );
        
        list = (ListView) findViewById( R.id.listCountries );
        
        String jsonCountries = A.a().cfgProg.getString( A.KEY_COUNTRIES, "" );
        
        try
        {
            countries = MetaCountry.parseList( jsonCountries );
        }
        catch( JSONException e )
        {
            Log.e( TAG, "", e );    // Should never happen because we have parsed it before.
            throw new IllegalStateException( e );
        }

        ArrayAdapter<MetaCountry> aa = new ArrayAdapter<MetaCountry>(this, android.R.layout.simple_list_item_single_choice, countries);
        list.setAdapter( aa );
        //list.setItemsCanFocus(true);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Pre-Select the default country.
        String defaultCountry = A.getDefaultCountry();
        for( int i = 0; i < countries.size(); i++ )
        {
            String id = countries.get(i).id;
            if( id.equals( defaultCountry ) )
            {
                list.setItemChecked( i, true );
                list.setSelection( i );
                //Log.e( TAG, "setSelection="+id+" "+i );
                break;
            }
        }    
        
    }

    @Override
    protected void onPause()
    {
        
        super.onPause();
    }

    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnConfirm( View v ) 
    {
        //MetaCountry c = (MetaCountry) list.getSelectedItem();
        int pos = list.getCheckedItemPosition();
        if( pos >= 0 )
        {
            MetaCountry c = countries.get( pos );
            A.setCountry( c.id, c.isNeedZip, c.isNop, c.isLinkAway );
            //Log.e( TAG, "c.id="+c.id );
            moveOn();
        }
        else
        {
            A.toast( R.string.choose_country );
            //Log.e( TAG, "pos="+pos );
        }
    }
    
    private void moveOn()
    {
        Intent i = new Intent( this, ActGetMisc.class );
        startActivity( i );
        finish();        
    }

}
