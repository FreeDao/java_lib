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
package com.hoperun.telematics.mobile.model;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.framework.net.vo.BasePOJO;

/**
 * 
 * @author he_chen
 * 
 */
public class BaseResponse extends BasePOJO {

	private int errorCode;
	private String errorDescription;

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @param errorDescription
	 *            the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * parseJsonToObject
	 * 
	 * @param json
	 * @param clas
	 * @return
	 * @throws Exception
	 */
	public static BaseResponse parseJsonToObject(String json, Class<? extends BaseResponse> clas) throws Exception {
		try {
			Gson gson = new Gson();

			BaseResponse obj = gson.fromJson(json, clas);

			return obj;
		} catch (Exception e) {
			throw new Exception();
		}
	}
}
