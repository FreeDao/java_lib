package com.sam.bindservicedemo1;

import com.sam.bindservicedemo1.BindService.MyBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BindActivity1 extends Activity {
	private static final String TAG = "BindActivity";
	private Button startBtn;
    private Button stopBtn;
    private boolean flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_activity1);
		
		flag = false;
        //设置
        startBtn = (Button)this.findViewById(R.id.startBtn);
        stopBtn = (Button)this.findViewById(R.id.stopBtn);
        startBtn.setOnClickListener(listener);
        stopBtn.setOnClickListener(listener);
		
		
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 switch (v.getId()) {
	            case R.id.startBtn:
	                bindService();
	                break;
	            case R.id.stopBtn:
	                unBind();
	                break;
	            default:
	                break;
	            }
		}
	};
	protected void bindService() {
		Intent intent = new Intent(BindActivity1.this,BindService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}
	private void unBind(){
        if(flag == true){
            unbindService(conn);
            flag = false;
        }
    }
	private ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.i("", "--------------------------1  service conneted------------------");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("", "--------------------------2  service conneted------------------");
			MyBinder binder = (MyBinder)service;
            BindService bindService = binder.getService();
            bindService.MyMethod();
            flag = true;			
		}
	};
	

}
