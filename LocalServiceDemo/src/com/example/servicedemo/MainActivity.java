/***
 * @author yang_jun2
 * 
 * local service
 * 
 * 不需要和activity 交互
 * **/

package com.example.servicedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
//	Button button ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.button1)).setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startService(new Intent("com.example.servicedemo"));
					}
				});
        ((Button)findViewById(R.id.button2)).setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						stopService(new Intent("com.example.servicedemo"));
					}
				});
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        Log.i(TAG, "sam MainActivity onCreateOptionsMenu"); 
        return true;
    }

    
}
















