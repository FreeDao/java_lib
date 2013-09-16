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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.model.track.TrackInfo;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class LocationOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mMapOverlays = new ArrayList<OverlayItem>();

	private Context mContext;
	private List<TrackInfo> mTrackList;
	private int mHeight;
	private View mPopView;
	private MapView mMapView;
	private MapController mMapController;
	private Drawable mDrawable;

	private LocationOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}

	public LocationOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.mContext = context;
	}

	public LocationOverlay(Drawable drawable, List<TrackInfo> list, Context context, MapView mapView) {
		super(boundCenterBottom(drawable));
		this.mTrackList = list;
		this.mContext = context;
		this.mMapView = mapView;
		this.mDrawable = drawable;
		mMapController = mapView.getController();
		mHeight = drawable.getIntrinsicHeight();
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.ui_track_pop_layout, null);

		for (int i = 0; i < mTrackList.size(); i++) {
			mMapOverlays.add(new OverlayItem(mTrackList.get(i).getGeoPoint(), "", "point1"));
		}

		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mMapOverlays.get(i);
	}

	@Override
	public int size() {
		return mMapOverlays.size();
	}

	public void addOverlay(OverlayItem item) {
		mMapOverlays.add(item);
		this.populate();
	}

	@Override
	protected boolean onTap(int index) {
		TrackInfo info = mTrackList.get(index);
		mMapController.setCenter(info.getGeoPoint());
		mMapView.removeView(mPopView);
		mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, info.getGeoPoint(), 0, -mHeight, MapView.LayoutParams.BOTTOM_CENTER));

		TextView time = (TextView) mPopView.findViewById(R.id.track_time);
		TextView address = (TextView) mPopView.findViewById(R.id.track_address);

		time.setText(info.getTime().toString());
		address.setText(info.getAddress());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amap.mapapi.map.ItemizedOverlay#draw(android.graphics.Canvas,
	 * com.amap.mapapi.map.MapView, boolean)
	 */
	@Override
	public void draw(Canvas arg0, MapView arg1, boolean arg2) {
		// TODO Auto-generated method stub
		super.draw(arg0, arg1, arg2);

		boundCenterBottom(mDrawable);
	}
}
