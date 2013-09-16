package com.sam.mobilemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sam.mobilemanager.R;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MobileInfo extends Activity {
	private static final String TAG = "MobileInfo";
	private SimpleAdapter simpleAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_info);
		listView = (ListView) findViewById(R.id.listview_info);
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		Integer imgIDs = R.drawable.ic_launcher;
		Iterator<String> it = getInstalledAppName().iterator();
		while (it.hasNext()) {
			String name = it.next();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("image", imgIDs);
			list.add(map);
		}
		simpleAdapter = new SimpleAdapter(this, list, R.layout.sub_phone_info,new String[]{"name","image"}	, new int[]{R.id.app_name,R.id.app_image});
		listView.setAdapter(simpleAdapter);
	}

	private List<PackageInfo> getAllinfo() {
		List<PackageInfo> allApps = new ArrayList<PackageInfo>();
		PackageManager packmanager = this.getPackageManager();
		List<PackageInfo> info = packmanager.getInstalledPackages(0);
		Iterator<PackageInfo> it = info.iterator();
		while (it.hasNext()) {
			PackageInfo appinfo = it.next();
			allApps.add(appinfo);
		}
		return allApps;
	}

	// return system app name ,it was stored in a list
	private List<String> getSysAppName() {
		List<String> sysApp = new ArrayList<String>();
		Iterator<PackageInfo> it = getAllinfo().iterator();
		while (it.hasNext()) {
			PackageInfo info = it.next();
			if (!((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)) {
				String name = info.applicationInfo.loadLabel(
						getPackageManager()).toString();
				sysApp.add(name);
			}
		}
		return sysApp;
	}

	// return a list of the installed app name
	private List<String> getInstalledAppName() {
		List<String> installedApp = new ArrayList<String>();
		Iterator<PackageInfo> it = getAllinfo().iterator();
		while (it.hasNext()) {
			PackageInfo info = it.next();
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				String name = info.applicationInfo.loadLabel(
						getPackageManager()).toString();
				installedApp.add(name);
			}
		}

		return installedApp;
	}

	
	
}
