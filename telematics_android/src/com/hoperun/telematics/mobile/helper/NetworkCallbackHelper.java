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

import android.content.Context;
import android.content.DialogInterface.OnClickListener;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback.ECallbackStatus;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class NetworkCallbackHelper {
	private static final int ERROR_SERVICE_TYPE_LOGIN_FAILED = 2001;
	private static final int ERROR_SERVICE_TYPE_ACCOUNT_DELETED = 2002;
	private static final int ERROR_SERVICE_TYPE_NO_CONNET_OF_3RD_SERVICE = 3001;
	private static final int ERROR_SERVICE_TYPE_MSG_OF_3RD_SERVICE = 3002;
	private static final int ERROR_SERVICE_TYPE_NO_HEADUNIT_INFO = 4001;
	private static final int ERROR_SERVICE_TYPE_NO_HEADUNIT_CONNECTED = 4002;
	private static final int ERROR_SERVICE_TYPE_UNAUTHORIZED = 4100;
	private static final int ERROR_SERVICE_TYPE_UNKNOW = 5000;

	public static void showInfoFromResponse(Context context, INetCallbackArgs args,
			Class<? extends BaseResponse> clazz, final IErrorEventListener displayer) {

	}

	public static boolean haveSystemError(Context context, ECallbackStatus state) {
		boolean tag = true;
		switch (state) {
		// 服务器成功返回信息
		case Success: {
			tag = false;
			break;
		}
			// 服务器不能调用第三方服务获取数据
		case Failure: {
			break;
		}
			// 服务器正在获取数据,取消该项，因为不会返回该情况
			// case Processing: {
			// break;
			// }
		default: {
			break;
		}
		}
		return tag;
	}

	/**
	 * alert business error
	 * 
	 * @param context
	 * @param errror
	 */
	public static void alertBusinessError(Context context, int errror) {
		alertBusinessError(context, errror, null);
	}

	/**
	 * alert business error
	 * 
	 * @param context
	 * @param errorCode
	 * @param retryBtnListener
	 */
	public static void alertBusinessError(Context context, int errorCode, OnClickListener retryBtnListener) {
		int alertStrId = 0;
		switch (errorCode) {
		case ERROR_SERVICE_TYPE_LOGIN_FAILED: {
			alertStrId = R.string.username_password_error;
			break;
		}
		case ERROR_SERVICE_TYPE_ACCOUNT_DELETED: {
			alertStrId = R.string.error_account_deleted;
			break;
		}
		case ERROR_SERVICE_TYPE_UNAUTHORIZED: {
			alertStrId = R.string.error_unauthorized;
			break;
		}
		case ERROR_SERVICE_TYPE_NO_CONNET_OF_3RD_SERVICE: {
			alertStrId = R.string.error_no_connet_of_3rd_service;
			break;
		}
		case ERROR_SERVICE_TYPE_MSG_OF_3RD_SERVICE: {
			alertStrId = R.string.error_msg_of_3rd_service;
			break;
		}
		case ERROR_SERVICE_TYPE_NO_HEADUNIT_INFO: {
			alertStrId = R.string.error_no_hu_info;
			break;
		}
		case ERROR_SERVICE_TYPE_NO_HEADUNIT_CONNECTED: {
			alertStrId = R.string.error_no_hu_connected;
			break;
		}
		case ERROR_SERVICE_TYPE_UNKNOW: {
			alertStrId = R.string.error_unknow;
			break;
		}
		default: {
			return;
		}
		}
		if (retryBtnListener != null) {
			alertRetryError(context, alertStrId, retryBtnListener);
		} else {
			alertError(context, alertStrId);
		}
	}

	private static void alertRetryError(Context context, int alertStrId, OnClickListener retryBtnListener) {
		DialogHelper.alertDialog(context, alertStrId, retryBtnListener);
	}

	private static void alertError(Context context, int alertStrId) {
		DialogHelper.alertDialog(context, alertStrId, R.string.ok);
	}

	/**
	 * 判断是否是业务异常的返回信息
	 */
	public static boolean isErrorResponse(Context context, BaseResponse response) {
		if (response != null && response.getErrorDescription() != null) {
			return true;
		} else {
			return false;
		}
	}

	public interface IErrorEventListener {

		public abstract void onResponseReturned(BaseResponse response);

		public abstract void onRetryButtonClicked();
	}

	public static boolean isPayloadNullOrEmpty(String payload){
		if( null == payload || payload.trim().length()==0){
			return true;
		}else{
			return false;
		}
	}
}
