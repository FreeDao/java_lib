package com.anhuioss.aidl.client;

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
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anhuioss.aidl.Dog;
import com.anhuioss.aidl.ITypeService;
import com.anhuioss.aidl.ITypeServiceListener;

public class AidlTypeClientActivity extends Activity implements OnClickListener, ServiceConnection {
    
	private Handler mHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
			Bundle bundle = msg.getData();
			if (bundle == null) {
				return false;
			}
			Toast.makeText(AidlTypeClientActivity.this, bundle.getString("data"), Toast.LENGTH_SHORT).show();
			return true;
		}
	});
	
	private CustomizedProgressDialog dialog;
	
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private ITypeService typeService;
	private ITypeServiceListener.Stub typeServiceListener = new ITypeServiceListener.Stub() {
		
		@Override
		public void requestCompleted(String message) throws RemoteException {
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("data", message);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
    	bindService(new Intent("com.anhuioss.aidl.TYPE_SERVICE"), this, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	unbindService(this);
    	if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
    }

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.button1:
			callBaseType();
			break;
		case R.id.button2:
			callObjectType();
			break;
		case R.id.button3:
			callObjectTypeAndReturn();
			break;
		case R.id.button4:
			dialog = new CustomizedProgressDialog(this, R.style.dialog);
			if (dialog != null && !dialog.isShowing()) {
				dialog.show();
			}
			callBack();
			break;
		default:
			break;
		}
		
	}

	private void callBack() {
		try {
			typeService.request(typeServiceListener);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void callObjectTypeAndReturn() {
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < 10; i++) {
				sb.append(((Dog)typeService.getDogWithType(i)).toString());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
	}

	private void callObjectType() {
		try {
			typeService.objectType(new Dog("Tom", 3, "WHITE"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void callBaseType() {
		try {
			typeService.basicTypes(123, 123456, 'A', true, 123.456f, 123456.789d, "string");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		typeService = ITypeService.Stub.asInterface(service);
		button1.setEnabled(true);
		button2.setEnabled(true);
		button3.setEnabled(true);
		button4.setEnabled(true);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		button1.setEnabled(false);
		button2.setEnabled(false);
		button3.setEnabled(false);
		button4.setEnabled(false);
	}
	
	private class CustomizedProgressDialog extends Dialog{
		
		public CustomizedProgressDialog(Context context, int theme) {
			super(context, theme);
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			ProgressBar progressBar = new ProgressBar(context);
			progressBar.setPadding(5, 5, 5, 5);
			setContentView(progressBar);
		}
		
		public CustomizedProgressDialog(Context context) {
			super(context);
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			ProgressBar progressBar = new ProgressBar(context);
			progressBar.setPadding(5, 5, 5, 5);
			setContentView(progressBar);
		}
		
	}
	
}