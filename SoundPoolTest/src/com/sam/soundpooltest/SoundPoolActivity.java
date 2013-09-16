package com.sam.soundpooltest;


import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SoundPoolActivity extends Activity implements OnClickListener {
	private static final String TAG = "SoundPoolActivity";
	private Button normalStart;
	private Button alarmStart;
	private Button alarmStop;
	private Button uniqueStart;
	private Button uniqueStop;
	SoundPool pool;
	int alarmSourceId;
	int playAlarmSoundpoolId;
	int uniqueSourceId;
	int playUniqueId;
	int normalSourceId;
	int playNormalId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound_pool);
		initButtons();
		intiClickListeners();
		initSound();
	}

	
	private void initSound(){
		pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); // new
		alarmSourceId = pool.load(this, R.raw.alarm, 2);
		uniqueSourceId = pool.load(this, R.raw.f1_new_mms, 1);
		normalSourceId = pool.load(this, R.raw.notification,0);
		
	} 
	
	private void playNormalSound() {
		Log.i(TAG, "--------------playNormalSound----------------");
		playNormalId = pool.play(normalSourceId,1, 1, 0, 1, 1);
		if(playNormalId==0){
			Log.e(TAG, "--------------playNormalSound error----------------");
			 Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
		}
		
		
	}

	private void playUniqueSound(){
//		stopOtherSound();
		playUniqueId = pool.play(uniqueSourceId,1, 1, 1, 1, 1);
		Log.i(TAG, "-----uniqueSourceId:"+uniqueSourceId+"-----playUniqueId:"+playUniqueId);
		if(playUniqueId==0){
			Log.e(TAG, "--------------playUniqueSound error----------------");
			 Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
		}
		
	}
	

	private void playAlarmSound(){
		playAlarmSoundpoolId = pool.play(alarmSourceId,1, 1, 2, 1, 1);
		Log.i(TAG, "-----alarmSourceId:"+alarmSourceId+"-----playAlarmSoundpoolId:"+playAlarmSoundpoolId);
		if(playAlarmSoundpoolId==0){
			Log.e(TAG, "--------------playAlarmSound error----------------");
			 Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
		}
	}

	private void stopOtherSound(){
		pool.stop(playAlarmSoundpoolId);
		pool.stop(playUniqueId);
		Log.i(TAG, "alarmSourceId:"+alarmSourceId+"---playAlarmSoundpoolId:"+playAlarmSoundpoolId+"-----uniqueSourceId:"+uniqueSourceId+"------playUniqueId:"+playUniqueId);
		
	}
	
	private void intiClickListeners() {
		alarmStart.setOnClickListener(this);
		alarmStop.setOnClickListener(this);
		uniqueStart.setOnClickListener(this);
		uniqueStop.setOnClickListener(this);
		normalStart.setOnClickListener(this);
	}

	private void initButtons() {
		normalStart = (Button)findViewById(R.id.normal_Start);
		alarmStart = (Button)findViewById(R.id.alarm_start);
		alarmStop= (Button)findViewById(R.id.alarm_stop);
		uniqueStart= (Button)findViewById(R.id.unique_start);
		uniqueStop= (Button)findViewById(R.id.unique_stop);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alarm_start:
			playAlarmSound();
			break;
		case R.id.alarm_stop:
			stopOtherSound();
			break;
		case R.id.unique_start:
			playUniqueSound();
			break;
		case R.id.unique_stop:
			stopOtherSound();
			break;
		case R.id.normal_Start:
			playNormalSound();
			break;

		default:
			break;
		}
	}


	

	
	
}
