package com.airbiquity.hap;

import org.json.JSONException;
import org.json.JSONObject;

import junit.framework.TestCase;

public class HapUnitTest extends TestCase {
	
	
	public void testHandsetProfile(){
		
		HandsetProfile handsetProfile = new HandsetProfile();
		String hp = handsetProfile.getHandsetProfile();
		int statusCode = handsetProfile.getStatusCode();
		String contentType = handsetProfile.getContentType();
		
		System.out.println(hp);
		System.out.println(statusCode);
		System.out.println(contentType);
	}
	
	public void testHeadUnitInfo(){
		HeadUnitInfomation huInfo = new HeadUnitInfomation();
		
		String result = huInfo.sendHeadUnitInfomation();
		int statusCode = huInfo.getStatusCode();
		String contentType = huInfo.getContentType();
		

		System.out.println(result);
		System.out.println(statusCode);
		System.out.println(contentType);
	}
	
	public void testIHeartRadio(){
		
		try {
			String appName = "com.clearchannel.iHeartRadio";
			
			JSONObject command = new JSONObject();
			command.put("command", "myStationsMenu");
			
			CommandControlHandler commandControlHandler = new CommandControlHandler(appName);
			
			String result = commandControlHandler.sendCommand(command.toString().getBytes(),"application/json");
			int statusCode = commandControlHandler.getStatusCode();
			String contentType = commandControlHandler.getContentType();
			
			System.out.println(result);
			System.out.println(statusCode);
			System.out.println(contentType);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
	}
	
	public void testGateway(){
		
	}
	
	public void testHandsetLocation(){
		HandsetLocation handset = new HandsetLocation();
		String handsetLocation = handset.getHandsetLocation();
		int statusCode = handset.getStatusCode();
		String contentType = handset.getContentType();
		
		System.out.println(handsetLocation);
		System.out.println(statusCode);
		System.out.println(contentType);
		
		
		
	}

}
