package com.airbiquity.hap;

import org.json.JSONException;
import org.json.JSONObject;

public class HeadUnitInfomation extends HapHttp{
	
	
	private String url = "http://192.168.0.3:8080/hap/api/1.0/headUnitInformation";
	
	
	private String huInfoResult;
	
	public String sendHeadUnitInfomation(){
		
		try {
			JSONObject huInfo = new JSONObject();
			huInfo.put("headUnitType", "DA2.2");
			huInfo.put("headUnitSerialNumber", "012345678901");
			huInfo.put("vehicleMake", "1");
			
			huInfoResult = this.sendHttpPostRequest(url, huInfo.toString(),"application/json");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return huInfoResult;
	}

	

}
