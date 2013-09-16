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
package com.hoperun.telematics.mobile.adapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author fan_leilei
 * 
 */
public abstract class AbsGroupListAdapter<T> extends AbsHaveListAdapter<T> {

	protected static final String TAG = "AbstractMonthGroupAdapter";
	private LayoutInflater inflater;
	protected List<T> list;
	protected Context context;
	protected String curTitle = "";
	private Comparator<T> comparator;
	

	public AbsGroupListAdapter(Context context, List<T> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		comparator = getSortComparator();
		if(comparator!=null){
			Collections.sort(list, comparator);
		}
	}
	
	/**
	 * 获取用于排序的comparator,默认返回null，即不排序
	 * @return
	 */
	protected Comparator<T> getSortComparator(){
		return null;
	}
	
	/**
	 * 获取有日期栏的view
	 * @param mInflater
	 * @param position
	 * @return
	 */
	protected abstract View getTitleItem(LayoutInflater mInflater,int position);
	/**
	 * 获取没有日期栏的view
	 * @param mInflater
	 * @param position
	 * @return
	 */
	protected abstract View getNoTitleItem(LayoutInflater mInflater,int position);
	/**
	 * 获取某一项的日期标题字符串
	 * @param position
	 * @return
	 */
	protected abstract String getTitleStr(int position);
	
	/**
	 * 检查新旧纪录的日期标题是否相同
	 * @param position
	 * @return
	 */
	private boolean checkTitleEqual(int position){
		String newTitle = getTitleStr(position);
		if(position == 0){
			curTitle = newTitle;
			return false;
		}else{
			String lastTitle = getTitleStr(position-1);
			if(lastTitle.equals(newTitle)){
				return true;
			}else{
				curTitle = newTitle;
				return false;
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(checkTitleEqual(position)){
			convertView = getNoTitleItem(inflater,position);
		}else{
			convertView = getTitleItem(inflater,position);
		}
		return convertView;
	}

	/**
	 * @param historyList
	 */
	@Override
	public void setList(List<T> list) {
		this.list = list;
		curTitle = "";
		if(comparator!=null){
			Collections.sort(list, comparator);
		}
	}
	
	@Override
	public List<T> getList(){
		return list;
	}
}
