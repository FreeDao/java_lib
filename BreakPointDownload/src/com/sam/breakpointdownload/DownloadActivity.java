package com.sam.breakpointdownload;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sam.impl.DownloadProgressListener;
import com.sam.impl.FileDownloader;

public class DownloadActivity extends Activity {
	String TAG = "DownloadActivity";
	private ProgressBar downloadbar;
	private EditText pathText;
	private TextView resultView;
	private Button button;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				int size = msg.getData().getInt("size");
				downloadbar.setProgress(size);
				float result = (float)downloadbar.getProgress()/(float)downloadbar.getMax();
				int p = (int) result*100;
				resultView.setText(p+"%");
				if(downloadbar.getProgress()==downloadbar.getMax())
					Toast.makeText(DownloadActivity.this, "sucess", 1).show();
				break;
			case -1: 
				Toast.makeText(DownloadActivity.this, "down load error", 1).show();
				break;
			
			}
			
			
		}
		
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		button = (Button) findViewById(R.id.button);
		downloadbar = (ProgressBar) findViewById(R.id.downloadbar);
		pathText = (EditText) findViewById(R.id.path);
		resultView = (TextView) findViewById(R.id.result);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = "http://www.baidu.com";
				Log.i(TAG, "----Environment.getExternalStorageState()---"+Environment.getExternalStorageState());
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					File dir = Environment.getExternalStorageDirectory();
					download(path, dir);
				} else {
					Toast.makeText(DownloadActivity.this, "sd card error", 1)
							.show();
				}
			}
		});
	}

	private void download(final String path, final File dir) {
		new Thread(new Runnable(){

			@Override
			public void run() {
				FileDownloader loader = new FileDownloader(DownloadActivity.this, path, dir, 3);
				int length = loader.getFileSize();
				downloadbar.setMax(length);
				try {
					loader.download(new DownloadProgressListener(){

						@Override
						public void onDownloadSize(int size) {
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);
							handler.sendMessage(msg);
						}
					});
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = -1;
					msg.getData().putString("error", "error happended");
					handler.sendMessage(msg);
				}
			}
			
		}).start();
		
		
		

	}

}
