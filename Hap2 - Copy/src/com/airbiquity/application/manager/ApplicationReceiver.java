package com.airbiquity.application.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.airbiquity.hap.A;

public class ApplicationReceiver extends BroadcastReceiver {
	
	private List<String> appList = new ArrayList<String>();
	{
		appList.add("com.airbiquity.hap.sample");
		appList.add("com.pandora.android");
		appList.add("com.clearchannel.iheartradio.connect");
	}
	

	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		Log.d("TAG", "HUconnectionState = " + A.a().getHuConState());
		
		if (A.a().getHuConState()) {

			String action = null;
			boolean isReplacing = false;
			Uri data = null;
			String packageName = null;
			boolean isExisting = false;

			// get action
			if (intent != null) {
				action = intent.getAction();
				isReplacing = intent.getExtras().getBoolean(
						Intent.EXTRA_REPLACING);
				data = intent.getData();
			}
			// get package name
			if (data != null) {
				String scheme = data.getScheme();
				String str = data.toString();
				packageName = str.substring(scheme.length() + 1);
			}

			Log.d("ApplicationRecevier", "package :" + packageName);

			// check if this application in the list
			for (String pkName : appList) {
				if (pkName.equalsIgnoreCase(packageName)) {
					isExisting = true;
					break;
				}
			}

			if (action != null && isExisting) {

				if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
					Log.e("TAG", "Installed or Upgrade, package name:"
							+ packageName);
					// send update handset profile event
					sendHandsetProfileUpdateEvent();

				} else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {

					if (!isReplacing) {
						Log.d("TAG", "truely uninstall");
						Log.e("TAG", "Uninstall package name = " + packageName);
						// send update handset profile event to HU
						sendHandsetProfileUpdateEvent();
					} else {
						Log.d("TAG", "uninstall for install, will install soon");
					}
				}
			}
		}
	}
	
	//send handset profile update event
	private void sendHandsetProfileUpdateEvent(){
		try {
			String appName = "hap";
			int sequenceNumber = 0;
			String contentType = "application/json";
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("event", "handsetProfileUpdate");
			byte[] payload = jsonObj.toString().getBytes();
			ApplicationMessage msg = new ApplicationMessage(appName, sequenceNumber, payload, contentType);
			A.a().qLongPolling.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
