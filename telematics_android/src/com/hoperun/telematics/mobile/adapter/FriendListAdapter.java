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

import java.util.ArrayList;
import java.util.List;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.PinyinSortHelper;
import com.hoperun.telematics.mobile.model.buddy.Friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class FriendListAdapter extends BaseAdapter implements SectionIndexer {
	private List<Friend> friendlist;
	private Context mContext;
	private List<String> nameList;
	private TextView catalog;
	private TextView nickname;
	private TextView address;
	private ImageView image;

	public FriendListAdapter(Context context, List<Friend> list) {
		this.mContext = context;
		this.friendlist = list;

		nameList = new ArrayList<String>();
		for(Friend friend:list){
			nameList.add(friend.getNickName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return friendlist.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return friendlist.get(position);
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
		
		convertView = LayoutInflater.from(mContext).inflate(R.layout.ui_friend_list_item_layout, null);

		catalog = (TextView) convertView.findViewById(R.id.friend_index);
		image = (ImageView) convertView.findViewById(R.id.friend_image);
		nickname = (TextView) convertView.findViewById(R.id.friend_name_id);
		address = (TextView) convertView.findViewById(R.id.friend_add_id);
		
		String nickName = nameList.get(position);
		String mcatalog = PinyinSortHelper.converterToFirstSpell(nickName).substring(0, 1);
		if (position == 0) {
			catalog.setVisibility(View.VISIBLE);
			catalog.setText(mcatalog);
		} else {
			String lastCatalog = PinyinSortHelper.converterToFirstSpell(nameList.get(position - 1)).substring(0, 1);
			if (mcatalog.equals(lastCatalog)) {
				catalog.setVisibility(View.GONE);
			} else {
				catalog.setVisibility(View.VISIBLE);
				catalog.setText(mcatalog);
			}
		}
		// TODO image
		nickname.setText(nickName);
		address.setText(friendlist.get(position).getAddress());

		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.SectionIndexer#getPositionForSection(int)
	 */
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < nameList.size(); i++) {
			String l = PinyinSortHelper.converterToFirstSpell(nameList.get(i)).substring(0, 1);
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.SectionIndexer#getSections()
	 */
	@Override
	public Object[] getSections() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.SectionIndexer#getSectionForPosition(int)
	 */
	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

}
