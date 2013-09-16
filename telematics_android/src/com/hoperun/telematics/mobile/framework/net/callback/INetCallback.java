package com.hoperun.telematics.mobile.framework.net.callback;

public interface INetCallback {
	public void callback(INetCallbackArgs args);

	public enum ECallbackStatus {
		Success(0), Failure(-1), Processing(1);

		private int value;

		private ECallbackStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}
}
