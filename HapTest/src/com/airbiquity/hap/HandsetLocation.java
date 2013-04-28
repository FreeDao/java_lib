package com.airbiquity.hap;

public class HandsetLocation extends HapHttp{
	
	
	private String url = "http://192.168.0.3:8080/hap/api/1.0/handsetLocation";
	
	public String getHandsetLocation(){
		return this.sendHttpGetRequest(url);
	}
}
