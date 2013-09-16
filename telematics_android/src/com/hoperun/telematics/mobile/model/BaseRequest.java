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
public class BaseRequest extends BasePOJO {

	public String toJsonStr(){
		Gson gson = new Gson();
		return gson.toJson(this,this.getClass());
	}
}
