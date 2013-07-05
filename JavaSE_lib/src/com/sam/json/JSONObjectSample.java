package com.sam.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class JSONObjectSample {
	
	public static void main(String args[]){
		JSONObject jsonObject = new JSONObjectSample().createJSONObject();	// 静态方法 类名加方法调用
		System.out.println("jsonobject=== "+jsonObject);
		jsonObject.element("address", "nanjing");	// add element
		System.out.println("add element and the obj becomes: "+jsonObject);

		JSONArray jsonArray = new JSONArray();
		jsonArray.add(0,"this is a json array value.");
		System.out.println("json array: "+jsonArray);
		
		jsonObject.element("jsonArray", jsonArray);
		System.out.println("add an json array to jsonObj:"+jsonObject);
		
		JSONArray array = jsonObject.getJSONArray("jsonArray");
		System.out.println("array: "+array);
		System.out.println("JsonObject:"+jsonObject);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static JSONObject createJSONObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", "sam");
		jsonObject.put("age", 23);
		
		
		return jsonObject;
	}
	
	

}






























