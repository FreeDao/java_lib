package com.sam.flashlight;

import java.io.DataOutputStream;

import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
//	private Camera m_Camera;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	String apkRoot="chmod 777 "+getPackageCodePath();
	try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	RootCommand(apkRoot);
		TurnOn();
	}

	public void TurnOn() {
		// m_Camera = Camera.open();
		// Camera.Parameters parameters = m_Camera.getParameters();
		// parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		// m_Camera.setParameters(parameters);
		// m_Camera.startPreview();
		try {
		Camera 	m_Camera = Camera.open();
			Camera.Parameters mParameters;
			mParameters = m_Camera.getParameters();
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			m_Camera.setParameters(mParameters);
		} catch (Exception ex) {
		}

	}

//	public void TurnOff() {
//		if (m_Camera != null) {
//			m_Camera.stopPreview();
//			m_Camera.release();
//		}
//
//	}

	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try{
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e)
		{
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally
		{
			try
			{
				if (os != null)
				{
					os.close();

				}

				process.destroy();

			} catch (Exception e)

			{

			}

		}

		return false;
	}

}
