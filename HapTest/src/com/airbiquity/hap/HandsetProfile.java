package com.airbiquity.hap;



public class HandsetProfile extends HapHttp{
	
	private String url = "http://192.168.0.3:8080/hap/api/1.0/handsetProfile";
	
	public String getHandsetProfile(){
		return sendHttpGetRequest(url);
	}
	
}
