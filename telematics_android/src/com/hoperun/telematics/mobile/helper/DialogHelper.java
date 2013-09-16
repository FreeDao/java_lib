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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.widget.Toast;

import com.hoperun.telematics.mobile.R;

/**
 * 
 * @author he_chen
 * 
 */
public class DialogHelper {

	public interface DialogCallback {
		public void onOk();

		public void onCancle();
	}

	/**
	 * alertDialog
	 * @param context
	 * @param msg
	 * @param btn
	 */
	public static void alertDialog(Context context, int msg, int cancle,
			int ok, final DialogCallback call) {
		new AlertDialog.Builder(context).setTitle("警告").setMessage(msg)
				.setPositiveButton(ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						call.onOk();
					}
				}).setNegativeButton(cancle, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						call.onCancle();
					}
				}).show();
	}

	/**
	 * alertDialog
	 * 
	 * @param context
	 * @param msg
	 * @param btn
	 */
	public static void alertDialog(Context context, String msg, int cancle,
			int ok, final DialogCallback call) {
		new AlertDialog.Builder(context).setTitle("Warning").setMessage(msg)
				.setPositiveButton(ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						call.onOk();
						dialog.dismiss();
					}
				}).setNegativeButton(cancle, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						call.onCancle();
						dialog.dismiss();
					}
				}).show();
	}

	/**
	 * alertDialog
	 * 
	 * @param context
	 * @param msg
	 * @param btn
	 */
	public static void alertDialog(Context context, int title, int msg, int btn) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
				.setPositiveButton(btn, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	/**
	 * alertDialog
	 * 
	 * @param context
	 * @param msg
	 * @param btn
	 */
	public static void alertDialog(Context context, int msg, int btn) {
		new AlertDialog.Builder(context).setTitle(R.string.warning).setMessage(msg)
				.setPositiveButton(btn, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	/**
	 * alertDialog
	 * 
	 * @param context
	 * @param msg
	 * @param btn
	 */
	public static void alertDialog(Context context, String msg, int btn) {
		new AlertDialog.Builder(context).setMessage(msg)
				.setPositiveButton(btn, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	/**
	 * progressDialog
	 * 
	 * @param context
	 * @param progressDialogType
	 * @param msg
	 */
	public static void progressDialog(ProgressDialog mProgressDialog,
			Context context, int progressDialogType, String msg) {

		// Set the progress bar style, the style is round, rotating
		mProgressDialog.setProgressStyle(progressDialogType);
		// Set ProgressDialog message
		mProgressDialog.setMessage(msg);

		// Let ProgressDialog Show
		mProgressDialog.show();
	}

	/**
	 * progressDialog
	 * 
	 * @param context
	 * @param progressDialogType
	 * @param msg
	 */
	public static void progressDialog(ProgressDialog mProgressDialog,
			Context context, int progressDialogType, String title, String msg) {

		// Set the progress bar style, the style is round, rotating
		mProgressDialog.setProgressStyle(progressDialogType);

		mProgressDialog.setTitle(title);
		// Set ProgressDialog message
		mProgressDialog.setMessage(msg);

		// Let ProgressDialog Show
		mProgressDialog.show();
	}

	/**
	 * showToastText
	 * 
	 * @param context
	 * @param showText
	 */
	public static void showToastText(Context context, String showText) {
		Toast toast = Toast.makeText(context, showText, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * @param planRouteActivity
	 * @param title
	 * @param msg
	 * @param retry
	 * @param cancel
	 */
	public static void alertDialog(Context context, int title, String msg,
			OnClickListener retry, OnClickListener cancel) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setCancelable(false)
				.setPositiveButton(R.string.retry, retry)
				.setNegativeButton(R.string.cancel, cancel).show();
	}
	
	
	public static void alertDialog(Context context, int msg,
			OnClickListener retry) {
		new AlertDialog.Builder(context).setTitle(R.string.warning).setMessage(msg).setCancelable(false)
				.setPositiveButton(R.string.retry, retry)
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}}).show();
	}
	
	public static void alertDialog(Context context, String msg,
			OnClickListener retry) {
		new AlertDialog.Builder(context).setTitle(R.string.warning).setMessage(msg).setCancelable(false)
				.setPositiveButton(R.string.retry, retry)
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}}).show();
	}
}
