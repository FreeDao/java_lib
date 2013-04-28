package com.airbiquity.hap;

import com.airbiquity.connectionmgr.coonmgr.android.ConnectionManager;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BluetoothStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle bundle = intent.getExtras();
		
		if(bundle != null){
			int state = bundle.getInt(BluetoothAdapter.EXTRA_STATE);
			
			switch(state){
			case BluetoothAdapter.STATE_ON:
				//TODO start service here, do other UI changes here 
				Log.d("TAG","State On start service here . state = "+state);
				//check if user loged in or not
				if(A.getMipId()!=null && A.getMipId()!="" ){
					context.startService( new Intent( "com.airbiquity.hap.AgentService" ) );
				}
				break;
			case BluetoothAdapter.STATE_OFF:
				//TODO stop service here, do other UI chagnes here
				Log.d("TAG","State Off stop service here . state = "+state);
				//context.stopService( new Intent( "com.airbiquity.hap.AgentService" ) );
				ConnectionManager.disconnect();
				break;
			default:
				//DO nothing here
			}
		}
	}

}
