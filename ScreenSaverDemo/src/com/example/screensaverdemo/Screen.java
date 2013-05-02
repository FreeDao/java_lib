package com.example.screensaverdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;

public class Screen extends Activity {
	WakeLock wakeLock = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Screen");
        wakeLock.acquire();
        
       /* Log.d("test", "sam  :"+getScreenOffTime());
        setScreenOffTime(15000);
        System.out.println("samm dd : "+getScreenOffTime());
        Log.d("test", "sam  :"+getScreenOffTime());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_screen, menu);
        return true;
    }


    private void setScreenOffTime(int paramInt){
      try{
          Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, paramInt);
        }catch (Exception localException){
          localException.printStackTrace();
          System.out.println("setScreenOffTime error  sam");
        }
    }
    
    private int getScreenOffTime(){
        int screenOffTime=0;
        try{
            screenOffTime = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        }
        catch (Exception localException){
        	System.out.println("getScreenOffTime error  sam");
        }
        return screenOffTime;
      }

    

    
}
