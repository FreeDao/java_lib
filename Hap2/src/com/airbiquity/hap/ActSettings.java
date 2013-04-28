package com.airbiquity.hap;

import com.airbiquity.util_net.UrlMaker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ActSettings extends Activity
{
    //public static final String KEY_SERVER_URL = "KEY_SERVER_URL";
    private Spinner spnrPresetUrls;
    private EditText txtInput;
    String[] presets = new String[] {
                                     "not selected",
                                     "http://",
                                     "http://nissanmip-mipgw-pre.viaaq.net:80/",
                                     "https://nissanmip-mipgw-pre.viaaq.net:443/",
                                     "http://nissan-mipgw-test.viaaq.net:9022/",
                                     "https://nissan-mipgw-test.viaaq.net:9025/",
                                     "http://nissanmipdevgw.airbiquity.com:9018/"
                                    };


    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_settings );
        spnrPresetUrls  = (Spinner)  findViewById( R.id.preset_urls );
        txtInput        = (EditText) findViewById( R.id.server_url );
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, presets);
        aa.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spnrPresetUrls.setAdapter( aa );
        
        OnItemSelectedListener oisl = new OnItemSelectedListener()
        {
            @Override public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
                if( position>0 )
                    txtInput.setText( presets[position] );  
            }

            @Override public void onNothingSelected( AdapterView<?> parent ) { }
        }; 
        spnrPresetUrls.setOnItemSelectedListener( oisl );
    }

    

    @Override
    protected void onResume()
    {
        super.onResume();        
        String savedUrl = A.a().cfgProg.getString( UrlMaker.KEY_SERVER_URL, "" );
        if( savedUrl.length() > 0 )
            txtInput.setText( savedUrl );        
    }



    @Override
    protected void onPause()
    {
        String url = txtInput.getText().toString();
        A.a().cfgEdProg.putString( UrlMaker.KEY_SERVER_URL, url );
        A.a().cfgEdProg.commit();
        super.onPause();
    }
}

