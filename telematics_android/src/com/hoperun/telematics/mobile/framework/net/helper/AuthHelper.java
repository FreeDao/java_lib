package com.hoperun.telematics.mobile.framework.net.helper;

public class AuthHelper {
	private static AuthHelper instance;
	private String token;

	private AuthHelper() {

	}

	public static AuthHelper getInstance() {
		if (instance == null) {
			instance = new AuthHelper();
		}

		return instance;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void validate(){
		//TODO check token is valid.
	}
	
}
