/*****************************************************************************
 *
 *                      HOPERUN PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to HopeRun
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from HopeRun.
 *
 *            Copyright (c) 2012 by HopeRun.  All rights reserved.
 *
 *****************************************************************************/
package com.hoperun.telematics.mobile.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hoperun.telematics.mobile.model.buddy.Friend;
import com.hoperun.telematics.mobile.model.buddy.FriendResponse;
import com.hoperun.telematics.mobile.model.fuel.FuelResponse;
import com.hoperun.telematics.mobile.model.location.LocationResponse;
import com.hoperun.telematics.mobile.model.maintenance.history.MaintenanceHistoryItem;
import com.hoperun.telematics.mobile.model.maintenance.history.MaintenanceHistoryResponse;
import com.hoperun.telematics.mobile.model.maintenance.order.MaintenanceOrderItem;
import com.hoperun.telematics.mobile.model.maintenance.order.MaintenanceOrderResponse;
import com.hoperun.telematics.mobile.model.poi.Poi;
import com.hoperun.telematics.mobile.model.poi.PoiRequest.EPoiRequestType;
import com.hoperun.telematics.mobile.model.poi.PoiResponse;
import com.hoperun.telematics.mobile.model.roadrescue.RoadRescueResponse;
import com.hoperun.telematics.mobile.model.roadrescuehistory.RescueInfo;
import com.hoperun.telematics.mobile.model.roadrescuehistory.RoadRescueHistoryResponse;
import com.hoperun.telematics.mobile.model.score.Score;
import com.hoperun.telematics.mobile.model.score.ScoreResponse;
import com.hoperun.telematics.mobile.model.states.StatesResponse;
import com.hoperun.telematics.mobile.model.states.VehicleState;
import com.hoperun.telematics.mobile.model.track.TrackInfo;
import com.hoperun.telematics.mobile.model.track.TrackResponse;
import com.hoperun.telematics.mobile.model.violation.ViolationInfo;
import com.hoperun.telematics.mobile.model.violation.ViolationResponse;
import com.hoperun.telematics.mobile.model.weather.Weather;
import com.hoperun.telematics.mobile.model.weather.WeatherResponse;

/**
 * TestDataManager
 * 
 * @author fan_leilei
 * 
 */
public class TestDataManager {

	private static TestDataManager manager;
	public static boolean IS_TEST_MODE = false;

	private TestDataManager() {

	}

	/**
	 * get single instance
	 * 
	 * @return
	 */
	public static TestDataManager getInstance() {
		if (manager == null) {
			manager = new TestDataManager();
		}
		return manager;
	}

	/**
	 * get test weather data
	 */
	public WeatherResponse getWeatherData() {
		Weather w1 = new Weather("晴", "33.5", "24.2", "2012-06-06", "星期三", "东南风3到4级", "10%",
		        Weather.WASH_STATE_HIGH);
		Weather w2 = new Weather("雨", "27", "21", "2012-06-07", "星期四", "东风4到5级", "60%",
		        Weather.WASH_STATE_LOW);
		Weather w3 = new Weather("多云", "30", "23", "2012-06-08", "星期五", "东风4到5级", "20%",
		        Weather.WASH_STATE_LOW);
		Weather w4 = new Weather("雷雨", "24", "20", "2012-06-09", "星期六", "东风2到3级", "62%",
		        Weather.WASH_STATE_LOW);
		List<Weather> wList = new ArrayList<Weather>();
		wList.add(w1);
		wList.add(w2);
		wList.add(w3);
		wList.add(w4);
		WeatherResponse r = new WeatherResponse(wList, "30");

		return r;
	}

	public ScoreResponse getScoreResponse() {
		List<Score> scoreList = new ArrayList<Score>();
		Score score1 = new Score("0001", "高级太阳眼镜", 1000, "200*200");
		Score score2 = new Score("0002", "高级水晶杯", 1200, "200*200");
		Score score3 = new Score("0003", "高档车体贴膜", 1000, "200*200");
		Score score4 = new Score("0004", "全能望远镜", 2000, "200*200");
		Score score5 = new Score("0005", "我爱语音王", 1000, "200*200");
		Score score6 = new Score("0006", "实用助手", 1000, "200*200");
		Score score7 = new Score("0007", "太阳能加热器", 1000, "200*200");
		Score score8 = new Score("0008", "Android4.0手机一部", 1000, "200*200");
		scoreList.add(score1);
		scoreList.add(score2);
		scoreList.add(score3);
		scoreList.add(score4);
		scoreList.add(score5);
		scoreList.add(score6);
		scoreList.add(score7);
		scoreList.add(score8);
		ScoreResponse scoreResponse = new ScoreResponse(scoreList);
		return scoreResponse;
	}

	public RoadRescueHistoryResponse getRoadRescueHistoryResponse() {
		List<RescueInfo> list = new ArrayList<RescueInfo>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2012, 0, 12, 12, 02, 30);
		long timeFrom = calendar.getTimeInMillis();
		calendar.set(2012, 0, 12, 12, 42, 50);
		long timeTo = calendar.getTimeInMillis();
		RescueInfo rescueInfo1 = new RescueInfo(timeFrom, timeTo, "南京市雨花台银杏山庄", "115.36598423", "16.3648861654",
				"高德先生", 5, "很满意");
		calendar.set(2012, 0, 22, 8, 30, 37);
		timeFrom = calendar.getTimeInMillis();
		calendar.set(2012, 0, 22, 10, 02, 10);
		timeTo = calendar.getTimeInMillis();
		RescueInfo rescueInfo2 = new RescueInfo(timeFrom, timeTo, "南京市雨花台安德门大街", "115.36598478", "16.3648861698",
				"张三先生", 5, "很满意");
		calendar.set(2012, 1, 12, 12, 02, 30);
		timeFrom = calendar.getTimeInMillis();
		calendar.set(2012, 1, 12, 12, 42, 50);
		timeTo = calendar.getTimeInMillis();
		RescueInfo rescueInfo3 = new RescueInfo(timeFrom, timeTo, "南京市雨花台凤翔山庄", "115.36598463", "16.3648861654",
				"李丽女士", 5, "很满意");
		calendar.set(2012, 3, 7, 21, 22, 30);
		timeFrom = calendar.getTimeInMillis();
		calendar.set(2012, 3, 7, 21, 42, 50);
		timeTo = calendar.getTimeInMillis();
		RescueInfo rescueInfo4 = new RescueInfo(timeFrom, timeTo, "南京市雨花台将军大道", "115.36598423", "16.3648861654",
				"李四先生", 4, "满意");
		calendar.set(2012, 4, 12, 12, 02, 30);
		timeFrom = calendar.getTimeInMillis();
		calendar.set(2012, 4, 12, 12, 42, 50);
		timeTo = calendar.getTimeInMillis();
		RescueInfo rescueInfo5 = new RescueInfo(timeFrom, timeTo, "南京市雨花台银杏山庄", "115.36598423", "16.3648861654",
				"高德先生", 5, "很满意");
		calendar.set(2012, 4, 17, 12, 02, 30);
		timeFrom = calendar.getTimeInMillis();
		calendar.set(2012, 4, 17, 12, 42, 50);
		timeTo = calendar.getTimeInMillis();
		RescueInfo rescueInfo6 = new RescueInfo(timeFrom, timeTo, "南京市浦口区三河桥", "115.36598423", "16.3648861654", "高德先生",
				5, "很满意");
		calendar.set(2012, 5, 12, 12, 02, 30);
		timeFrom = calendar.getTimeInMillis();
		calendar.set(2012, 5, 12, 12, 42, 50);
		timeTo = calendar.getTimeInMillis();
		RescueInfo rescueInfo7 = new RescueInfo(timeFrom, timeTo, "南京市雨花台珠江路", "115.36598423", "16.3648861654", "高德先生",
				5, "很满意");
		list.add(rescueInfo1);
		list.add(rescueInfo2);
		list.add(rescueInfo3);
		list.add(rescueInfo4);
		list.add(rescueInfo5);
		list.add(rescueInfo6);
		list.add(rescueInfo7);
		RoadRescueHistoryResponse roadRescueHistoryResponse = new RoadRescueHistoryResponse(1, 100, list);
		return roadRescueHistoryResponse;
	}

	public RoadRescueResponse getRoadRescueResponse() {
		RoadRescueResponse roadRescueResponse = new RoadRescueResponse("高德先生", "13612341234", "南京市雨花台安德门大街");
		return roadRescueResponse;
	}

	public ViolationResponse getViolationData() {
		Calendar calendar = Calendar.getInstance();
		List<ViolationInfo> list = new ArrayList<ViolationInfo>();
		calendar.set(2012, 6, 10, 3, 52, 1);
		ViolationInfo violationInfo = new ViolationInfo("1", calendar.getTime(), "违章停车", false, "在十字路口停车", "南京市铁心桥",
				"违法中华人民共和国交通法第某X条", 3, 200);
		list.add(violationInfo);
		calendar.set(2012, 9, 13, 14, 2, 12);
		violationInfo = new ViolationInfo("2", calendar.getTime(), "闯红灯", true, "违法闯红灯", "南京市安德门大街",
				"违法中华人民共和国交通法第某Y条", 3, 2000);
		list.add(violationInfo);
		calendar.set(2012, 6, 21, 6, 35, 32);
		violationInfo = new ViolationInfo("3", calendar.getTime(), "违章停车", false, "在交通线路上停车", "南京市三河桥",
				"违法中华人民共和国交通法第某Z条", 3, 300);
		list.add(violationInfo);
		calendar.set(2012, 7, 9, 4, 12, 19);
		violationInfo = new ViolationInfo("4", calendar.getTime(), "超速行驶", false, "在高速上超速行驶", "南京市将军大道",
				"违法中华人民共和国交通法第某Q条", 5, 1500);
		list.add(violationInfo);
		calendar.set(2012, 3, 1, 21, 2, 45);
		violationInfo = new ViolationInfo("5", calendar.getTime(), "醉驾", true, "严重酒后驾驶", "南京市翠玲迎合，严重酒后驾驶",
				"违法中华人民共和国交通法第某W条", 10, 5000);
		list.add(violationInfo);
		ViolationResponse violationResponse = new ViolationResponse(0, list.size(), list);
		return violationResponse;
	}

	/**
	 * get test data
	 * 
	 * @return
	 */
	public MaintenanceHistoryResponse getMaintenanceHistory(int index) {
		int totalSize = 43;
		List<MaintenanceHistoryItem> list = new ArrayList<MaintenanceHistoryItem>();
		if (index == 9) {
			MaintenanceHistoryItem item1 = new MaintenanceHistoryItem("2012-06-06", 10000, "维修发动机", "王府大街151号", "无");
			MaintenanceHistoryItem item2 = new MaintenanceHistoryItem("2011-07-16", 11000, "更换车胎", "王府大街141号", "一般");
			MaintenanceHistoryItem item3 = new MaintenanceHistoryItem("2010-07-26", 12010, "安全保养", "王府大街121号", "很满意");
			list.add(item1);
			list.add(item2);
			list.add(item3);
			return new MaintenanceHistoryResponse(index, totalSize, list);
		}
		if (index % 2 != 0) {
			MaintenanceHistoryItem item1 = new MaintenanceHistoryItem("2012-06-06", 10300, "维修发动机", "王府大街111号", "无");
			MaintenanceHistoryItem item2 = new MaintenanceHistoryItem("2011-07-16", 11000, "安全保养", "王府大街111号", "一般");
			MaintenanceHistoryItem item3 = new MaintenanceHistoryItem("2010-07-26", 12000, "安全保养", "王府大街1号", "很满意");
			MaintenanceHistoryItem item4 = new MaintenanceHistoryItem("2010-08-26", 12100, "维修发动机", "王府大街5号", "很满意");
			MaintenanceHistoryItem item5 = new MaintenanceHistoryItem("2010-08-26", 12000, "安全保养", "王府大街111号", "很满意");
			list.add(item1);
			list.add(item2);
			list.add(item3);
			list.add(item4);
			list.add(item5);
			return new MaintenanceHistoryResponse(index, totalSize, list);
		} else {
			MaintenanceHistoryItem item1 = new MaintenanceHistoryItem("2005-12-06", 10300, "维修发动机", "王府大街111号", "无");
			MaintenanceHistoryItem item2 = new MaintenanceHistoryItem("2005-12-16", 11000, "维修灯", "王府大街111号", "一般");
			MaintenanceHistoryItem item3 = new MaintenanceHistoryItem("2003-07-26", 12000, "安全保养", "王府大街1号", "很满意");
			MaintenanceHistoryItem item4 = new MaintenanceHistoryItem("2002-01-26", 12100, "维修发动机", "王府大街5号", "很满意");
			MaintenanceHistoryItem item5 = new MaintenanceHistoryItem("2001-08-26", 12000, "安全保养", "王府大街111号", "很满意");
			list.add(item1);
			list.add(item2);
			list.add(item3);
			list.add(item4);
			list.add(item5);
		}
		return new MaintenanceHistoryResponse(index, totalSize, list);
	}

	public PoiResponse getPoiResponse() {

		PoiResponse poiResponse = new PoiResponse();

		List<Poi> poiList = new ArrayList<Poi>();

		Poi poi = new Poi();
		poi.setName("停车场1");
		poi.setAddress("软件大道_001");
		poi.setLatitude("39.9022");
		poi.setLongitude("116.3922");

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("distance", "120");
		poi.setExtInfo(map);

		Poi poi2 = new Poi();
		poi2.setName("停车场2");
		poi2.setAddress("软件大道_002");
		poi2.setLatitude("39.607723");
		poi2.setLongitude("116.397741");

		Map<Object, Object> map2 = new HashMap<Object, Object>();
		map2.put("distance", "150");
		poi2.setExtInfo(map2);

		Poi poi3 = new Poi();
		poi3.setName("停车场3");
		poi3.setAddress("软件大道_003");
		poi3.setLatitude("39.607666");
		poi3.setLongitude("116.397666");

		Map<Object, Object> map3 = new HashMap<Object, Object>();
		map3.put("distance", "150");
		poi3.setExtInfo(map3);

		poiList.add(poi);
		poiList.add(poi2);
		poiList.add(poi3);
		poiResponse.setType(EPoiRequestType.Parking.getValue());
		poiResponse.setPoiList(poiList);

		return poiResponse;
	}

	public StatesResponse getVehicleState() {
		VehicleState state1 = new VehicleState("VC222111", "P004", "发动机", "发动机过热", 6,
				"请您尽快地降低车速，把汽车驶到树荫下，或驶到相对比较凉快的地方，把汽车停下来。" + "打开发动机罩盖，让发动机怠速运转，等待发动机温度逐渐恢复正常。在发动机水温恢复正常之后，"
						+ "将发动机熄火。然后再等待一段时间，直到发动机的水温充分地下降了之后，再检查发动机的冷却水量。"
						+ "这时发动机的冷却水量大都严重不足。按著规定量把冷却水补足之后，再检查一下冷却系统，确认没有其他故障之后，" + "可以起动发动机，继续行驶。");

		VehicleState state2 = new VehicleState("VC3111", "P002", "轮胎", "胎压不足", 3, "请您尽快更换轮胎或增加胎压");
		VehicleState state3 = new VehicleState("VC211", "P003", "车灯", "尾灯损坏", 6, "请您尽快到附近4S店进行修理");
		List<VehicleState> stateList = new ArrayList<VehicleState>();
		stateList.add(state1);
		stateList.add(state2);
		stateList.add(state3);
		StatesResponse response = new StatesResponse(stateList);
		return response;
	}

	public String getRegisterDate() {
		return "1990-12-22";
	}

	/**
	 * get test data
	 * 
	 * @return
	 */
	public MaintenanceOrderResponse getMaintenanceOrder(int curIndex) {
		int index = 1;
		int totalSize = 3;
		List<MaintenanceOrderItem> list = new ArrayList<MaintenanceOrderItem>();
		MaintenanceOrderItem item1 = new MaintenanceOrderItem("2010-06-06", "卡子门大街 大众4S店");
		MaintenanceOrderItem item2 = new MaintenanceOrderItem("2010-08-16", "西安门大众4S店");
		MaintenanceOrderItem item3 = new MaintenanceOrderItem("2010-08-26", "中华门大众4S店");
		list.add(item1);
		list.add(item2);
		list.add(item3);
		return new MaintenanceOrderResponse(index, totalSize, list);
	}
	
	/**
	 * 车友圈测试数据
	 * @return
	 */
	public FriendResponse getFriendResponse(){
		List<Friend> mfriendList = new ArrayList<Friend>();
		Friend friend1 = new Friend("111", "张三", "南京市雨花台安德门", "Femall", "爱生活，更爱开车，只为兜风", "118.9657", "31.2465");
		Friend friend2 = new Friend("222", "B", "南京市雨花台安德门", "Femall", "爱生活，更爱开车，只为兜风", "118.9755", "31.2443");
		Friend friend3 = new Friend("333", "李四", "南京市雨花台安德门", "Femall", "爱生活，更爱开车，只为兜风", "118.9532", "31.3456");
		Friend friend4 = new Friend("444", "D", "南京市雨花台安德门", "Femall", "爱生活，更爱开车，只为兜风", "118.9507", "31.3427");
		Friend friend5 = new Friend("555", "赵六", "南京市雨花台安德门", "Femall", "爱生活，更爱开车，只为兜风", "118.9530", "31.3510");
		Friend friend6 = new Friend("666", "E", "南京市雨花台安德门", "Femall", "爱生活，更爱开车，只为兜风", "118.9800", "31.3400");
		mfriendList.add(friend1);
		mfriendList.add(friend2);
		mfriendList.add(friend3);
		mfriendList.add(friend4);
		mfriendList.add(friend5);
		mfriendList.add(friend6);
		
		return new FriendResponse(mfriendList);
	}
	
	public TrackResponse getTrackResponse(){
		List<TrackInfo> list = new ArrayList<TrackInfo>();
		TrackInfo track1 = new TrackInfo(Timestamp.valueOf("2012-06-08 09:30:00"), "南京雨花台铁心桥", "32.345678", "112.345678");
		TrackInfo track2 = new TrackInfo(Timestamp.valueOf("2012-06-08 09:30:05"), "南京市雨花台安德门", "32.445678", "112.445678");
		TrackInfo track3 = new TrackInfo(Timestamp.valueOf("2012-06-08 09:30:10"), "南京市雨花台小行", "32.545678", "112.545678");
		list.add(track1);
		list.add(track2);
		list.add(track3);
		return new TrackResponse(list);
	}
	
	public LocationResponse getLocationResponse(){
		return new LocationResponse(Timestamp.valueOf("2012-06-08 09:30:00"), "南京雨花台铁心桥", "32.345678", "112.345678");
	}
	
	
	/**
	 * 
	 * @return
	 */
	public FuelResponse getFuelInfo(){
		return new FuelResponse(50, 30, 25.5f, 85.5f);
	}
}
