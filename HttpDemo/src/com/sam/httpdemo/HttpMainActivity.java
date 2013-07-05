package com.sam.httpdemo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class HttpMainActivity extends Activity {
	private final static String  path= "http://www.baidu.com/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String TAG = "Http";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http_main);
		HttpClient client = new DefaultHttpClient();
		
		HttpPost postRequest = new HttpPost(path);
		HttpGet getRequest = new HttpGet(path);
		
		try {
			HttpResponse response = client.execute(getRequest);
			HttpEntity entity = response.getEntity();
			Log.i(TAG, "---------------------"+entity.getContentType());
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.http_main, menu);
		return true;
	}

}



















