package com.sam.aidlclient;

import com.sam.aidlservice.Dog;
import com.sam.aidlservice.ITypeService;
import com.sam.aidlservice.ITypeServiceListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;




public class AidlClientActivity extends Activity implements OnClickListener,ServiceConnection {
	String TAG = "AidlClientActivity";
	private CustomizedProgressDialog dialog;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private ITypeService typeService;

	private Handler mHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
				dialog = null;
			}
			
			Bundle bundle = msg.getData();
			if(bundle == null){
				return false;
			}
			Toast.makeText(AidlClientActivity.this, bundle.getString("data"), Toast.LENGTH_LONG).show();
			return true;
		}
	});
	
	private ITypeServiceListener.Stub typeServiceListener = new ITypeServiceListener.Stub() {
		
		@Override
		public void requestCompleted(String message) throws RemoteException {
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("data",message);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aidl_client);
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		button1.setEnabled(false);

		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
		button2.setEnabled(false);

		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(this);
		button3.setEnabled(false);

		button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(this);
		button4.setEnabled(false);
	}

	@Override
	protected void onStart() {
		super.onStart();
		bindService(new Intent("com.sam.aidl.TYPE_SERVICE"), this,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(this);
		Log.i(TAG, "-------ONSTOP-------");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			callBaseTypes();
			break;
		case R.id.button2:
			callObjectType();
			break;
		case R.id.button3:
			callObjectTypeAndReturn();
			break;
		case R.id.button4:
//			doRequest();
			break;

		default:
			break;
		}
	}

	private void callBaseTypes() {
		try {
			typeService.basicTypes(2, 321, 's', true, "asdda");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void callObjectType(){
		try {
		Dog	aDog = new Dog("xiaohuang",3,"blue");
			typeService.objectType(aDog);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void callObjectTypeAndReturn(){
		StringBuffer sb = new StringBuffer();
		try {
			for(int i = 0;i < 10; i++){
				sb.append(((Dog)typeService.getDogWithType(i)).toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
	}
	
//	private void doRequest(){
//		try {
//			typeService.request(listener);
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aidl_client, menu);
		return true;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.i(TAG, "service connected . Name = " + name + "  Service: "
				+ service);
		typeService = ITypeService.Stub.asInterface(service);
		button1.setEnabled(true);
		button2.setEnabled(true);
		button3.setEnabled(true);
		button4.setEnabled(true);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.i(TAG, "service dis connected . Name = " + name);
		button1.setEnabled(false);
		button2.setEnabled(false);
		button3.setEnabled(false);
		button4.setEnabled(false);
	}

	public class CustomizedProgressDialog extends Dialog {

		public CustomizedProgressDialog(Context context, int theme) {
			super(context);
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			ProgressBar progressBar = new ProgressBar(context);
			progressBar.setPadding(5, 5, 5, 5);
			setContentView(progressBar);
			Log.i(TAG, "set progressBar");
		}

	}

}
