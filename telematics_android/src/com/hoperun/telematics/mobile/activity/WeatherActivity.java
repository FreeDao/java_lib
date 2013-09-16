package com.hoperun.telematics.mobile.activity;

import java.util.Date;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.mapapi.core.GeoPoint;
import com.google.gson.Gson;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.AnimationHelper;
import com.hoperun.telematics.mobile.helper.DateUtil;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LocationHelper;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.helper.SharedPreferencesUtil;
import com.hoperun.telematics.mobile.helper.TestDataManager;
import com.hoperun.telematics.mobile.model.weather.Weather;
import com.hoperun.telematics.mobile.model.weather.WeatherRequest;
import com.hoperun.telematics.mobile.model.weather.WeatherResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class WeatherActivity extends DefaultActivity {

	private static final String TAG = "WeatherActivity";
	private WeatherResponse weatherResponse;
	private TextView weatherLoactionText;
	private TextView currentTemperatureText;
	private TextView humidnessText;
	private TextView updateTimeText;
	private LinearLayout weatherItemList;
	private TextView washConditionText;
	private ImageView washConditionImage;
	private LinearLayout weatherTodayCondition;
	private ImageView refreshIcon;
	private boolean isLoading = false;
	private static final long TIME_OUT = 15000;
	private static final int ROTATION_RANGE = 10800;
	private static final int OTHER_HEIGHT_DP = 235;
	private int perHeight;

	private static final int[] weatherConditionImageIds = { R.drawable.weather_icon_sun, R.drawable.weather_icon_rain,
			R.drawable.weather_icon_thunder, R.drawable.weather_icon_cloudy, R.drawable.weather_icon_big_sun,
			R.drawable.weather_icon_big_rain, R.drawable.weather_icon_big_thunder, R.drawable.weather_icon_big_cloudy };

	/*
	 * (non-Javadoc
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ui_weather_layout);
		// initialize its views
		initViews();
		setTitleBar(this, getString(R.string.weather_title));
	}

	private void initViews() {
		weatherItemList = (LinearLayout) findViewById(R.id.weatherItemList);
		weatherLoactionText = (TextView) findViewById(R.id.weatherLoactionText);
		currentTemperatureText = (TextView) findViewById(R.id.currentTemperatureText);
		humidnessText = (TextView) findViewById(R.id.humidnessText);
		updateTimeText = (TextView) findViewById(R.id.updateTimeText);
		washConditionText = (TextView) findViewById(R.id.washConditionText);
		washConditionImage = (ImageView) findViewById(R.id.washConditionImage);
		weatherTodayCondition = (LinearLayout) findViewById(R.id.weatherTodayCondition);
		refreshIcon = (ImageView) findViewById(R.id.refreshIcon);

		initHeight();
		for (int i = 0; i < weatherItemList.getChildCount(); i++) {
			if (i != 0) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
						perHeight);
				weatherItemList.getChildAt(i).setLayoutParams(params);
			}
		}

	}

	/**
	 * 3 min after binded start getting information
	 */
	@Override
	protected void onBindServiceFinish(ComponentName className) {
		super.onBindServiceFinish(className);
		if(className.getClassName().equals(NetworkService.class.getName())){
			updateView(getSavedInfo());
		}
		
	}

	@Override
	public void startProgressDialog() {
		isLoading = true;
		AnimationListener listener = new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isLoading) {
					Message msg = Message.obtain(mHandler, MESSAGE_REQUEST_TIMEOUT, ENetworkServiceType.Weather);
					msg.sendToTarget();
				}
				isLoading = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		};
		AnimationHelper.startOnceRotateAnimation(refreshIcon, 0, ROTATION_RANGE, TIME_OUT, listener);
	}

	@Override
	public void stopProgressDialog() {
		isLoading = false;
		refreshIcon.clearAnimation();
	}

	public void refresh(View v) {
		if (!isLoading) {
			initReloadTimes();
			startProgressDialog();
			getWeatherInfo();
		}
	}

	/**
	 * 重写重新加载的时候调用的方法
	 */
	protected void reload() {
		getWeatherInfo();
	}

	/**
	 * @Title: getMapList
	 * @Description: get Map List
	 * @param
	 * @return void
	 * @throws
	 */
	private void getWeatherInfo() {
		String longitude = getString(R.string.testLongitude);
		String latitude = getString(R.string.testLatitude);
		WeatherRequest request = new WeatherRequest(longitude, latitude);
		String postJson = request.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.Weather, postJson, mCallBack);
	}

	private INetCallback mCallBack = new INetCallback() {

		@Override
		public void callback(final INetCallbackArgs args) {
			Context context = WeatherActivity.this;

			OnClickListener retryBtnListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					refresh(null);
				}
			};
			// 检查是否有非业务级的错误
			if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {
				stopProgressDialog();
				String payload = args.getPayload();
				if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
					DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
				} else {
					Gson gson = new Gson();
					weatherResponse = gson.fromJson(payload, WeatherResponse.class);
					if (NetworkCallbackHelper.isErrorResponse(context, weatherResponse)) {
						// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
						NetworkCallbackHelper.alertBusinessError(context, weatherResponse.getErrorCode());
					} else {
						saveWeatherInfo(payload);
						updateView(weatherResponse);
					}
				}
			} else {
				// 根据各接口情况选择重试或直接提示
				String errMsg = args.getErrorMessage();
				Log.e(TAG,errMsg);
				errMsg = getString(R.string.error_async_return_fault);
				startReload(errMsg, retryBtnListener);
			}
		}
	};

	private void updateView(WeatherResponse weatherResponse) {
		// new one thread to show location
		new Thread() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				super.run();
				LocationHelper loHelper = LocationHelper.getInstance();
				GeoPoint geoPoint = LocationHelper.parse2Geo(getString(R.string.testLatitude),
						getString(R.string.testLongitude));
				String location = loHelper.getAddressfromPoint(WeatherActivity.this, geoPoint);
				if (location != null && location.trim().length()!=0) {
					Log.i(TAG, "current location = " + location);
					final String locationCity = loHelper.getCityName(WeatherActivity.this, location);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							weatherLoactionText.setText(String.format("%s", locationCity));
						}
					});
				}
			}

		}.start();

		// String location = getString(R.string.testLocation);
		// weatherLoactionText.setText(String.format("%s", location));
		currentTemperatureText.setText(getString(R.string.weather_temperature_format,
				weatherResponse.getCurrentTemperature()));
		String humidness = weatherResponse.getWeatherList().get(0).getHumidness();
		if (humidness != null && humidness.trim().length()!=0) {
			humidnessText.setText(getString(R.string.weather_humidness_format, humidness));
		}
		updateTimeText.setText(getString(R.string.weather_updateTime_format, DateUtil.getZhTime(new Date())));
		setWashCondition(weatherResponse.getWeatherList().get(0).getWashState());
		// show the common information in today weather and weather forcast
		for (int i = 0; i < Math.min(weatherItemList.getChildCount(), weatherResponse.getWeatherList().size()); i++) {
			TextView dateTextView = (TextView) weatherItemList.getChildAt(i).findViewById(R.id.weatherDateText);
			TextView temperatureRangeText = (TextView) weatherItemList.getChildAt(i).findViewById(
					R.id.temperatureRangeText);
			TextView conditionText = (TextView) weatherItemList.getChildAt(i).findViewById(R.id.conditionText);
			TextView windText = (TextView) weatherItemList.getChildAt(i).findViewById(R.id.windText);
			TextView weekText = (TextView) weatherItemList.getChildAt(i).findViewById(R.id.weekText);
			ImageView conditionImage = (ImageView) weatherItemList.getChildAt(i).findViewById(R.id.conditionImage);

			String dateStr = DateUtil.getZhDate(weatherResponse.getWeatherList().get(i).getDate());
			String week = weatherResponse.getWeatherList().get(i).getWeek();
			dateTextView.setText(String.format("%s", dateStr));
			weekText.setText(String.format("%s", week));

			String max = weatherResponse.getWeatherList().get(i).getHighTemperature();
			String min = weatherResponse.getWeatherList().get(i).getLowTemperature();
			temperatureRangeText.setText(getString(R.string.weather_temperature_range_format, max, min));

			String conditionStr = weatherResponse.getWeatherList().get(i).getCondition();
			conditionText.setText(conditionStr);
			setWeatherImage(conditionStr, conditionImage);

			windText.setText(weatherResponse.getWeatherList().get(i).getWind());
		}
	}

	private void initHeight() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		int height = metric.heightPixels; // 屏幕高度（像素）
		if (height < OTHER_HEIGHT_DP * density) {
			// 屏幕太小了，只能显示当天
		} else {
			perHeight = (int) (height - OTHER_HEIGHT_DP * density) / 3;
		}
	}

	/**
	 * put correct wash suggestion in the imageView and textView
	 * 
	 * @param condition
	 */
	private void setWashCondition(int condition) {
		String washState = null;
		int washImageId = R.drawable.weather_icon_wash;
		switch (condition) {
		case Weather.WASH_STATE_LOW: {
			washState = getString(R.string.wash_state_low);
			washImageId = R.drawable.weather_icon_nowash;
			break;
		}
		case Weather.WASH_STATE_MIDDLE: {
			washState = getString(R.string.wash_state_high);
			break;
		}
		case Weather.WASH_STATE_HIGH: {
			washState = getString(R.string.wash_state_low);
			break;
		}
		default: {
			break;
		}
		}
		if (null != washState) {
			washConditionText.setText(getString(R.string.weather_wash_format, washState));
			washConditionImage.setImageResource(washImageId);
		}
	}

	/**
	 * put correct weather condition image in the imageView
	 * 
	 * @param conditionText
	 * @param view
	 */
	private void setWeatherImage(String conditionText, ImageView view) {
		int step = 0;
		// if it is to show today weather condition image, set step equal half
		// Count of total images to get the correct image id
		if (view.getParent().equals(weatherTodayCondition)) {
			step = weatherConditionImageIds.length / 2;
		}
		if (conditionText.contains(getString(R.string.weather_sunny))) {
			view.setImageResource(weatherConditionImageIds[0 + step]);
		}
		if (conditionText.contains(getString(R.string.weather_rain))) {
			view.setImageResource(weatherConditionImageIds[1 + step]);
		}
		if (conditionText.contains(getString(R.string.weather_thunder))) {
			view.setImageResource(weatherConditionImageIds[2 + step]);
		}
		if (conditionText.equals(getString(R.string.weather_cloudy))) {
			view.setImageResource(weatherConditionImageIds[3 + step]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.isLoading = false;
	}

	private static final String SHARE_KEY_WEATHER_INFO = "share_key_weather_info";

	private void saveWeatherInfo(String jsonStr) {
		SharedPreferencesUtil.getInstance().putString(SHARE_KEY_WEATHER_INFO, jsonStr);
	}

	private WeatherResponse getSavedInfo() {
		String payload = SharedPreferencesUtil.getInstance().getString(SHARE_KEY_WEATHER_INFO);
		if (payload == null || payload.trim().length()==0) {
			return TestDataManager.getInstance().getWeatherData();
		} else {
			Gson gson = new Gson();
			return gson.fromJson(payload, WeatherResponse.class);
		}
	}
}
