package com.hoperun.telematics.mobile.framework.net;

public enum ENetworkServiceType {
	Notification("/notification"), Login("/account/login"), TestAsync("/testAsync"), Poi("/poi"), Weather("/weather"), RemoteControl(
			"/remoteControl"), Violation("/violation"), Buddy("/buddy"), Location("/location"), Track("/track"), RoadRescue(
			"/roadRescue"), RoadRescueHistory("/roadRescueHistory"), Score("/score"), VehicleCondition(
			"/vehicleContition"), MaintenanceHistory("/maintenanceHistory"), MaintenanceOrders("/maintenanceOrders"), Fuel("/fuel");

	private String path;

	private ENetworkServiceType(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public String getResultPath() {
		// return this.path + "Result";
		return "/getResult";
	}
}
