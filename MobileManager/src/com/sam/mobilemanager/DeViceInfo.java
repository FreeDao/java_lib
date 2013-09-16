package com.sam.mobilemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DeViceInfo extends Activity {
	private static final String TAG = "DeViceInfo";
	ListView listviewDeviceinfo;
	SimpleAdapter simpleAdapter;
	List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	String deviceInfoTitle[] = { "device name", "phone number", "total memory", "free memory", "cpu1", "cpu2", "brand", "device id", "country code", "", "phont type" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_info);
		getAllmemory();
		listviewDeviceinfo = (ListView) findViewById(R.id.listview_deviceinfo);
		
		Iterator<String> it = getDeviceInfo().iterator();
		int i = 0;
		while(it.hasNext()){
			String value = it.next();
			String title = deviceInfoTitle[i];
			i++;
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("title", title);
			map.put("value", value);
			list.add(map);
		}
	 simpleAdapter = new SimpleAdapter(this, list, R.layout.device_info_sub, new String[]{"title","value"}, new int[]{R.id.text_device_title,R.id.text_device_value});
		
	 listviewDeviceinfo.setAdapter(simpleAdapter);
	}

	// returns a list ,about the device info
	private List<String> getDeviceInfo() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		List<String> listInfo = new ArrayList<String>();
		
		String device_name = Build.MODEL;
		String phone_number = tm.getLine1Number();
		String allMemory = getAllmemory();
		String avaliableMemory = getAvaliableMemory();
		String cpu1 = Build.CPU_ABI;
		String cpu2 = Build.CPU_ABI2;
		String brand = Build.BRAND;
		String deviceId = tm.getDeviceId();
		String country_flag = tm.getNetworkCountryIso();
		String phone_type = getPhoneType();
		// add to list
		listInfo.add(device_name);
		listInfo.add(phone_number);
		listInfo.add(allMemory);
		listInfo.add(avaliableMemory);
		listInfo.add(cpu1);
		listInfo.add(cpu2);
		listInfo.add(brand);
		listInfo.add(deviceId);
		listInfo.add(country_flag);
		listInfo.add(phone_type);
		return listInfo;
	}

	private String getPhoneType() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String phone_type;
		switch (tm.getPhoneType()) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			phone_type = "CDMA";
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			phone_type = "2G";
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			phone_type = "no phone type";
			break;
		case TelephonyManager.PHONE_TYPE_SIP:
			phone_type = "SIP";
			break;
		default:
			phone_type = "sorry,error";
			break;
		}
		return phone_type;

	}

	// get all memory, Unit : G
	private String getAllmemory() {

		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memory = new MemoryInfo();
		manager.getMemoryInfo(memory);
		long size = memory.totalMem;
		String allmeo = Formatter.formatFileSize(this, size);
		return allmeo;
	}
	
	private String getAvaliableMemory(){
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memory = new MemoryInfo();
		manager.getMemoryInfo(memory);
		long size = memory.availMem;
		String availMem = Formatter.formatFileSize(this, size);
		return availMem;
	}
	
}
