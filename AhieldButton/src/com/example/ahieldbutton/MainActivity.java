package com.example.ahieldbutton;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	String TAG = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
    	Log.d(TAG, " key was clicked");
    	switch(keyCode){
//    	case KeyEvent.KEYCODE_HOME:return true;
    	case KeyEvent.KEYCODE_BACK:
    		Log.d(TAG, "back key was clicked");
    		return false;
//    	case KeyEvent.KEYCODE_CALL:return true;
//    	case KeyEvent.KEYCODE_SYM: return true;
//    	case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
//    	case KeyEvent.KEYCODE_VOLUME_UP: return true;
//    	case KeyEvent.KEYCODE_STAR: return true;
    	}
    	return super.onKeyDown(keyCode, event);
    	}

    	//屏蔽home键的代码:
//    @Override
//    	public void onAttachedToWindow() {
//    	this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//    	super.onAttachedToWindow();
//    	}

}
