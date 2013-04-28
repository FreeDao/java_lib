package com.sam.json;


public class JsonDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		
		json.put("city", "nanjing");
		json.put("age", 18);
		json.put("name", "sam");
		System.out.println(json);
		JSONObject result = new JSONObject();
		result.put("result", array);
		System.out.println(result);
	}

}
