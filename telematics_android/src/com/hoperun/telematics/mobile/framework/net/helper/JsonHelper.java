package com.hoperun.telematics.mobile.framework.net.helper;

import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoperun.telematics.mobile.framework.net.vo.NetRequest;
import com.hoperun.telematics.mobile.framework.net.vo.NetResponse;
import com.hoperun.telematics.mobile.model.violation.ViolationResponse;
import com.hoperun.telematics.mobile.model.weather.WeatherRequest;
import com.hoperun.telematics.mobile.model.weather.WeatherResponse;

public class JsonHelper {
	public static NetRequest packageRequest(String payload) {
		NetRequest request = new NetRequest();
		request.setUUID(generateUUID());
		request.setTimestamp(generateTimeStamp());
		request.setToken(AuthHelper.getInstance().getToken());
		request.setPayload(payload);
		return request;
	}

	public static NetRequest packageResultRequest(String resId) {
		NetRequest request = new NetRequest();
		request.setUUID(generateUUID());
		request.setTimestamp(generateTimeStamp());
		request.setToken(AuthHelper.getInstance().getToken());
		request.setResId(resId);
		return request;
	}

	public static String parseRequest(NetRequest request) {

		Gson gson = new Gson();
		String jsonString = gson.toJson(request);
		return jsonString;
	}

	public static NetResponse parseResponse(String jsonString) {
		Gson gson = new Gson();

		// {"UUID":"aaa","token":"aaa","status":0,"timestamp":1111111,"payload":"aaa","uuid":"aaa"}
		NetResponse response = gson.fromJson(jsonString, NetResponse.class);
		String token = response.getToken();
		if (token != null) {
			AuthHelper.getInstance().setToken(token);
		}
		return response;
	}

	public static void main(String[] args) {
		WeatherRequest payload = new WeatherRequest();
		payload.setLatitude("11.11111111");
		payload.setLongitude("55.55555555");
		Gson gson = new Gson();
		System.out.println(parseRequest(packageRequest(gson.toJson(payload))));

		// WeatherResponse resPayload = WeatherResponse.getTestModel();

		// System.out.println(parseRequest(packageRequest(gson.toJson(resPayload))));

		System.out
				.println(parseResponse("{\"UUID\":\"aaa\",\"token\":\"aaa\",\"status\":0,\"timestamp\":1111111,\"payload\":\"aaa\",\"uuid\":\"aaa\"}"));

		String response = "{\"UUID\":\"c7b2e517-6d03-451c-bcc2-0f71667c4c74\",\"errorMessage\":\"\",\"interval\":\"0\",\"payload\":\"{\\\"currentTemperature\\\":\\\"23℃\\\",\\\"weatherInfos\\\":[{\\\"condition\\\":\\\"小雨\\\",\\\"date\\\":\\\"2012-06-29\\\",\\\"highTemperature\\\":\\\"24℃\\\",\\\"humidness\\\":\\\"\\\",\\\"lowTemperature\\\":\\\"21℃\\\",\\\"washState\\\":0,\\\"week\\\":\\\"星期五\\\",\\\"wind\\\":\\\"北风小于3级\\\"},{\\\"condition\\\":\\\"多云\\\",\\\"date\\\":\\\"2012-06-30\\\",\\\"highTemperature\\\":\\\"33℃\\\",\\\"humidness\\\":\\\"\\\",\\\"lowTemperature\\\":\\\"21℃\\\",\\\"washState\\\":0,\\\"week\\\":\\\"星期六\\\",\\\"wind\\\":\\\"南风小于3级\\\"},{\\\"condition\\\":\\\"晴转多云\\\",\\\"date\\\":\\\"2012-07-01\\\",\\\"highTemperature\\\":\\\"33℃\\\",\\\"humidness\\\":\\\"\\\",\\\"lowTemperature\\\":\\\"20℃\\\",\\\"washState\\\":0,\\\"week\\\":\\\"星期日\\\",\\\"wind\\\":\\\"南风小于3级\\\"}]}\",\"resId\":\"\",\"status\":\"0\",\"timestamp\":\"1340956473706\"}";
		System.out.println(response);
		NetResponse re = JsonHelper.parseResponse(response);
		System.out.println(re.getPayload());
		WeatherResponse r = gson.fromJson(re.getPayload(), WeatherResponse.class); 
		System.out.println(r);
		
		String res1 = "{\"curIndex\":\"1\",\"totalSize\":\"6\",\"violationList\":[{\"content\":\"\",\"date\":\"2012-02-29 11:03:09\",\"fine\":\"\",\"id\":\"856EE931155DADC3DC11A22D644C60A9\",\"legalBasis\":\"\",\"location\":\"南雷南路－万年桥路（北口）\",\"subtractedScore\":\"\",\"summary\":\"机动车不按交通信号灯规定通行的\"},{\"content\":\"\",\"date\":\"2012-02-26 15:44:00\",\"fine\":\"\",\"id\":\"FB88AD561E941310CFC2480C3C59980E\",\"legalBasis\":\"\",\"location\":\"舜水南路\",\"subtractedScore\":\"\",\"summary\":\"不按规定停放影响其他车辆和行人通行的\"}]}";
		Gson gson1 = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		ViolationResponse violationResponse = gson1.fromJson(res1, ViolationResponse.class);


		System.out.println(violationResponse);
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public static long generateTimeStamp() {
		return new Date().getTime();
	}

}
