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
package com.hoperun.telematics.mobile.map.overlay;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.mapapi.core.PoiItem;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.PoiOverlay;
import com.amap.mapapi.map.MapView.LayoutParams;
import com.hoperun.telematics.mobile.R;

public class AMapPoiOverlay extends PoiOverlay {
	private Context context;
	private Drawable drawable;
	private int number = 0;
	private List<PoiItem> poiItem;
	private LayoutInflater mInflater;
	private int height;

	public AMapPoiOverlay(Context context, Drawable drawable, List<PoiItem> poiItem) {
		super(drawable, poiItem);
		this.context = context;
		this.poiItem = poiItem;
		mInflater = LayoutInflater.from(context);
		height = drawable.getIntrinsicHeight();
	}

	@Override
	protected Drawable getPopupBackground() {
		drawable = context.getResources().getDrawable(R.drawable.tip_pointer_button);
		return drawable;
	}

	@Override
	protected View getPopupView(final PoiItem item) {
		View view = mInflater.inflate(R.layout.popup, null);
		TextView nameTextView = (TextView) view.findViewById(R.id.PoiName);
		TextView addressTextView = (TextView) view.findViewById(R.id.PoiAddress);
		nameTextView.setText(item.getTitle());
		String address = item.getSnippet();
		if (address == null || address.length() == 0) {
			address = "无";
		}
		addressTextView.setText(address);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.LinearLayoutPopup);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

		return view;
	}

	@Override
	public void addToMap(MapView arg0) {
		super.addToMap(arg0);
	}

	@Override
	protected LayoutParams getLayoutParam(int arg0) {
		LayoutParams params = new MapView.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, poiItem.get(number).getPoint(), 0, -height,
				LayoutParams.BOTTOM_CENTER);

		return params;
	}

	@Override
	protected Drawable getPopupMarker(PoiItem arg0) {
		return super.getPopupMarker(arg0);
	}

	@Override
	protected boolean onTap(int index) {
		number = index;
		return super.onTap(index);
	}

}