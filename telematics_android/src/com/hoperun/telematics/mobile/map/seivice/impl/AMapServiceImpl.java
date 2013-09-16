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

package com.hoperun.telematics.mobile.map.seivice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.RouteOverlay;
import com.amap.mapapi.route.Route;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.net.helper.UtilHelper;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.map.seivice.IHopeRunMapInterface;
import com.hoperun.telematics.mobile.model.location.GeoLocation;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class AMapServiceImpl implements IHopeRunMapInterface {
	private static AMapServiceImpl aMapServiceImpl;
	private String TAG = "AMapServiceImpl";
	private static GeoPoint startPos = UtilHelper.NewGeoPoint(32.6666, 116.3426);

	public static AMapServiceImpl getInstance() {
		if (aMapServiceImpl == null) {
			aMapServiceImpl = new AMapServiceImpl();
		}
		return aMapServiceImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.map.seivice.IHopeRunMapInterface#drawPointOnMap
	 * (java.util.List, android.graphics.drawable.Drawable, java.lang.Object,
	 * android.content.Context)
	 */
	@Override
	public void drawPointsOnMap(List<GeoLocation> geoLocations, Object mapObj, final Context context,
			final int layoutid, int pinLayoutId) {
		final List<View> poiViews = new ArrayList<View>();
		final MapView mapView = (MapView) mapObj;
		final View popView = LayoutInflater.from(context).inflate(layoutid, null);

		for (int i = 0; i < geoLocations.size(); i++) {
			final int index = i;
			final GeoLocation geo = geoLocations.get(index);
			final GeoPoint geoPoint = geo.getGeopoint();
			OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {

					// mMapController.setCenter(geoPoint);
					switch (layoutid) {
					case R.layout.ui_poi_map_popup_layout:
						showPoiPopupWindow(context, geo, mapView, index, popView, poiViews);
						break;
					case R.layout.ui_track_pop_layout:
						showTrackPopupWindow(context, geo, mapView, index, popView);
						break;
					case R.layout.ui_friend_popup_layout:
						showFriendPopupWindow(context, geo, mapView, index, popView, poiViews);
						break;
					case R.layout.ui_poi_gas_popup_layout:
						showGasPopupWindow(context, geo, mapView, index, popView, poiViews);
					default:
						break;
					}
				}
			};
			drawPointOnMap(context, mapView, geoPoint, i, pinLayoutId, poiViews, listener);
		}

	}

	/**
	 * addLocationPin
	 * 
	 * @param mMapView
	 * @param point
	 * @param index标记第几个point
	 * @param listener
	 */

	public void drawPointOnMap(Context context, MapView mapView, GeoPoint point, int index, int pinLayoutId,
			List<View> poiViews, OnClickListener listener) {
		View pinView = LayoutInflater.from(context).inflate(pinLayoutId, null);
		if (poiViews != null)
			poiViews.add(pinView);
		mapView.addView(pinView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
				MapView.LayoutParams.BOTTOM_CENTER));
		if (pinLayoutId == R.layout.ui_map_pin_layout) {
			TextView pin = (TextView) pinView.findViewById(R.id.mappinbackimage);
			pin.setText(UtilHelper.intTOChar(index));
		}

		if (listener != null) {
			pinView.setOnClickListener(listener);
		}

	}

	/**
	 * 停车场 弹出框
	 * 
	 * @param context
	 * @param geo
	 * @param mapView
	 * @param index
	 * @param popView
	 * @param pinView
	 *            list
	 */
	private void showPoiPopupWindow(final Context context, final GeoLocation geo, final MapView mapView,
			final int index, final View popView, final List<View> poiViews) {
		mapView.getChildAt(mapView.indexOfChild(poiViews.get(index))).setBackgroundResource(R.drawable.poi_select_pin);
		for (int i = 0; i < poiViews.size(); i++) {
			if (i != index) {
				mapView.getChildAt(mapView.indexOfChild(poiViews.get(i))).setBackgroundResource(R.drawable.poi_pin);
			}
		}
		mapView.removeView(popView);
		Drawable drawable = context.getResources().getDrawable(R.drawable.poi_pin);
		mapView.addView(popView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, geo.getGeopoint(), 0, -drawable.getIntrinsicHeight(),
				MapView.LayoutParams.BOTTOM_CENTER));

		TextView name = (TextView) popView.findViewById(R.id.poi_popup_name);
		TextView distance = (TextView) popView.findViewById(R.id.poi_item_distance);
		TextView address = (TextView) popView.findViewById(R.id.poi_item_address);
		TextView prices = (TextView) popView.findViewById(R.id.poi_item_price);
		TextView saturation = (TextView) popView.findViewById(R.id.poi_item_youhui);
		Button getRoute = (Button) popView.findViewById(R.id.poi_route);
		name.setText(geo.getName());
		address.setText(geo.getAddress());
		Map<Object, Object> map = geo.getExtInfo();
		String price = (String) map.get("price");
		price = price.replaceAll(",", "\n");
		prices.setText(price);
		saturation.setText((String) map.get("saturation"));
		// TODO distance 赋值
		getRoute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapView.removeView(popView);
				ProgressDialog pd = new ProgressDialog(context);
				DialogHelper.progressDialog(pd, context, ProgressDialog.STYLE_SPINNER, context.getResources()
						.getString(R.string.get_route));
				if (drawRouteSuccess(context, (MapActivity) context, mapView, startPos, geo.getGeopoint(),
						Route.DrivingLeastDistance)) {
					pd.cancel();
					mapView.invalidate();
					mapView.removeView(poiViews.get(index));
				} else {
					pd.cancel();
					DialogHelper.showToastText(context, context.getResources().getString(R.string.get_route_fail));
				}

			}
		});
		mapView.invalidate();
	}

	/**
	 * 行驶轨迹点弹出框
	 * 
	 * @param context
	 * @param geo
	 * @param mapView
	 * @param index
	 * @param popView
	 */
	private void showTrackPopupWindow(Context context, GeoLocation geo, MapView mapView, int index, View popView) {

		mapView.removeView(popView);
		Drawable drawable = context.getResources().getDrawable(R.drawable.track_pin);
		mapView.addView(popView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, geo.getGeopoint(), 0, -drawable.getIntrinsicHeight(),
				MapView.LayoutParams.BOTTOM_CENTER));
		TextView time = (TextView) popView.findViewById(R.id.track_time);
		TextView address = (TextView) popView.findViewById(R.id.track_address);

		time.setText(geo.getTime());
		address.setText(geo.getAddress());
		mapView.invalidate();

	}

	/**
	 * 车友 弹出框
	 * 
	 * @param context
	 * @param geo
	 * @param mapView
	 * @param index
	 * @param popView
	 * @param poiOverlays
	 */
	private void showFriendPopupWindow(final Context context, final GeoLocation geo, final MapView mapView,
			final int index, final View popView, final List<View> poiViews) {

		mapView.getChildAt(mapView.indexOfChild(poiViews.get(index))).setBackgroundResource(R.drawable.poi_select_pin);
		for (int i = 0; i < poiViews.size(); i++) {
			if (i != index) {
				mapView.getChildAt(mapView.indexOfChild(poiViews.get(i))).setBackgroundResource(R.drawable.poi_pin);
			}
		}

		mapView.removeView(popView);
		Drawable drawable = context.getResources().getDrawable(R.drawable.poi_select_pin);
		mapView.addView(popView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, geo.getGeopoint(), 0, -drawable.getIntrinsicHeight(),
				MapView.LayoutParams.BOTTOM_CENTER));

		TextView nickname = (TextView) popView.findViewById(R.id.friend_pop_title);
		TextView note = (TextView) popView.findViewById(R.id.friend_pop_note);
		TextView address = (TextView) popView.findViewById(R.id.friend_pop_address);
		Button getRoute = (Button) popView.findViewById(R.id.friend_pop_route);
		getRoute.setVisibility(View.GONE);
		nickname.setText(geo.getName());
		note.setText(geo.getNote());
		address.setText(geo.getAddress());
		// getRoute.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// mapView.removeView(popView);
		// ProgressDialog pd = new ProgressDialog(context);
		// DialogHelper.progressDialog(pd, context,
		// ProgressDialog.STYLE_SPINNER, context.getResources()
		// .getString(R.string.get_route));
		// if (drawRouteSuccess(context, (MapActivity) context, mapView,
		// startPos, geo.getGeopoint(),
		// Route.DrivingLeastDistance)) {
		// int length = calculateRouteLength(context, (MapActivity) context,
		// mapView, startPos, geo
		// .getGeopoint(), Route.DrivingNoFastRoad);
		// LogUtil.d("TAG", "ROUTE LENGTH:" + length);
		// pd.cancel();
		// mapView.invalidate();
		// mapView.getChildAt(mapView.indexOfChild(poiViews.get(index))).setVisibility(View.GONE);
		// } else {
		// pd.cancel();
		// DialogHelper.showToastText(context,
		// context.getResources().getString(R.string.get_route_fail));
		// }
		//
		// }
		// });
		// mapView.invalidate();
		mapView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					Rect rect = new Rect();
					popView.getGlobalVisibleRect(rect);
					if(!rect.contains((int)event.getRawX(), (int)event.getRawY())){
						mapView.removeView(popView);
						mapView.getChildAt(mapView.indexOfChild(poiViews.get(index))).setBackgroundResource(R.drawable.poi_pin);
					}
				}
				return false;
			}
		});
	}

	/**
	 * 加油站 弹出框
	 * 
	 * @param context
	 * @param geo
	 * @param mapView
	 * @param index
	 * @param popView
	 * @param poiViews
	 */
	private void showGasPopupWindow(Context context, GeoLocation geo, MapView mapView, int index, final View popView,
			List<View> poiViews) {

		mapView.getChildAt(mapView.indexOfChild(poiViews.get(index))).setBackgroundResource(R.drawable.poi_select_pin);
		for (int i = 0; i < poiViews.size(); i++) {
			if (i != index) {
				mapView.getChildAt(mapView.indexOfChild(poiViews.get(i))).setBackgroundResource(R.drawable.poi_pin);
			}
		}
		mapView.removeView(popView);

		Drawable drawable = context.getResources().getDrawable(R.drawable.poi_pin);
		mapView.addView(popView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, geo.getGeopoint(), 0, -drawable.getIntrinsicHeight(),
				MapView.LayoutParams.BOTTOM_CENTER));

		TextView name = (TextView) popView.findViewById(R.id.gas_popup_name);
		// TextView distance = (TextView)
		// popView.findViewById(R.id.poi_item_distance);
		TextView address = (TextView) popView.findViewById(R.id.poi_gas_address);
		TextView prices = (TextView) popView.findViewById(R.id.poi_gas_price);
		TextView description = (TextView) popView.findViewById(R.id.poi_gas_description);
		TextView saturation = (TextView) popView.findViewById(R.id.poi_gas_discountdesc);
		// Button getRoute = (Button) popView.findViewById(R.id.poi_route);
		name.setText(geo.getName());
		address.setText(geo.getAddress());
		Map<Object, Object> map = geo.getExtInfo();
		String price = (String) map.get("price");
		price = price.replaceAll(",", "\n");
		prices.setText(price);
		description.setText((String) map.get("description"));
		saturation.setText((String) map.get("discountdesc"));
		// TODO distance 赋值
		// getRoute.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// mapView.removeView(popView);
		// ProgressDialog pd = new ProgressDialog(context);
		// DialogHelper.progressDialog(pd, context,
		// ProgressDialog.STYLE_SPINNER, context.getResources()
		// .getString(R.string.get_route));
		// if (drawRouteSuccess(context, (MapActivity) context, mapView,
		// startPos, geo.getGeopoint())) {
		// pd.cancel();
		// mapView.invalidate();
		// mapView.removeView(poiViews.get(index));
		// } else {
		// pd.cancel();
		// DialogHelper.showToastText(context,
		// context.getResources().getString(R.string.get_route_fail));
		// }
		//
		// }
		// });
		mapView.invalidate();

	}

	public boolean drawRouteSuccess(Context context, final MapActivity activity, final MapView mapView,
			final GeoPoint startPos, final GeoPoint endPos, int model) {

		Route.FromAndTo fromAndTo = new Route.FromAndTo(startPos, endPos);
		try {
			List<Route> route = Route.calculateRoute(context, fromAndTo, model);
			if (route.size() != 0) {
				RouteOverlay routeOverlay = new RouteOverlay(activity, route.get(0));
				routeOverlay.addToMap(mapView);
				return true;
			}
		} catch (ExceptionInInitializerError e) {
			LogUtil.e(TAG, e.getMessage(), e);
			return false;
		} catch (AMapException e) {
			LogUtil.e(TAG, e.getMessage(), e);
			return false;
		} catch (NoClassDefFoundError e) {
			LogUtil.e(TAG, e.getMessage(), e);
			return false;
		}
		return false;
	}

	public void cleanMapView(MapView mapView) {
		mapView.removeAllViews();
		mapView.getOverlays().clear();
	}

	public int calculateRouteLength(Context context, MapActivity activity, MapView mapView, GeoPoint startPos,
			GeoPoint endPos, int model) {
		int length = 0;
		Route.FromAndTo fromAndTo = new Route.FromAndTo(startPos, endPos);
		try {
			List<Route> routes = Route.calculateRoute(context, fromAndTo, model);
			if (routes.size() != 0) {
				Route route = routes.get(0);
				length = route.getLength();
			}
		} catch (ExceptionInInitializerError e) {
			LogUtil.e(TAG, e.getMessage(), e);
		} catch (AMapException e) {
			LogUtil.e(TAG, e.getMessage(), e);
		} catch (NoClassDefFoundError e) {
			LogUtil.e(TAG, e.getMessage(), e);
		}
		return length;
	}

	public void setTrafficMode(MapView mapview) {
		mapview.setTraffic(true);
	}

	// public RouteMessageHandler listener = new RouteMessageHandler() {
	//
	// @Override
	// public boolean onRouteEvent(MapView arg0, RouteOverlay arg1, int arg2,
	// int arg3) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public void onDragEnd(MapView mapView, RouteOverlay overlay, int arg2,
	// GeoPoint arg3) {
	// try {
	// GeoPoint startPoint = overlay.getStartPos();
	// GeoPoint endPoint = overlay.getEndPos();
	// // overlay.renewOverlay(mapView);
	// drawRoute(startPoint, endPoint);
	// } catch (IllegalArgumentException e) {
	// routeOverlay.restoreOverlay(mapView);
	// } catch (Exception e1) {
	// overlay.restoreOverlay(mapView);
	// }
	// }
	//
	// @Override
	// public void onDragBegin(MapView arg0, RouteOverlay arg1, int arg2,
	// GeoPoint arg3) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onDrag(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint
	// arg3) {
	// // TODO Auto-generated method stub
	//
	// }
	// };

}
