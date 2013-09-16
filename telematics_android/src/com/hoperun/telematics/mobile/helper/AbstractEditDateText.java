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

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;

/**
 * 
 * @author fan_leilei
 * 
 */
public abstract class AbstractEditDateText {

	private TextView textView;
	private Context context;
	private DatePickerDialog dpDialog;
	private int initYear;
	private int initMonth;
	private int initDay;

	public AbstractEditDateText(Context context, TextView textView, int year, int month, int day) {
		this.context = context;
		this.textView = textView;
		this.textView.setOnClickListener(onClickListener);
		initYear = year;
		initMonth = month;
		initDay = day;
	}

	public AbstractEditDateText(Context context, TextView textView, Date date) {
		this.context = context;
		this.textView = textView;
		textView.setOnClickListener(onClickListener);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		initYear = calendar.get(Calendar.YEAR);
		initMonth = calendar.get(Calendar.MONTH);
		initDay = calendar.get(Calendar.DAY_OF_MONTH);
	}

	public AbstractEditDateText(Context context, TextView textView, String dateStr) {
		this.context = context;
		this.textView = textView;
		textView.setOnClickListener(onClickListener);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.getDate(dateStr));
		initYear = calendar.get(Calendar.YEAR);
		initMonth = calendar.get(Calendar.MONTH);
		initDay = calendar.get(Calendar.DAY_OF_MONTH);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			dpDialog = new DatePickerDialog(context, callBack, initYear, initMonth, initDay);
			dpDialog.show();
		}

	};

	private DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			String dateStr = String.format(context.getString(R.string.datePikerFormat), year, monthOfYear + 1,
					dayOfMonth);
			textView.setText(dateStr);
			setDateStr(dateStr);
		}

	};

	protected abstract void setDateStr(String dateStr);
}
