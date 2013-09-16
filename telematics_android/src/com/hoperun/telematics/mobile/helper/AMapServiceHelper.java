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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;
import com.amap.mapapi.map.Projection;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class AMapServiceHelper {
	
	public void drawCircleOnMap(GeoPoint point, int radius, Object mapObj) {
		MapView mapView = (MapView) mapObj;
		CircleOverlay circleOverlay = new CircleOverlay(point, radius);
		mapView.getOverlays().add(circleOverlay);
	}

	class CircleOverlay extends Overlay {
		private GeoPoint mPoint;
		private float mRadius;

		public CircleOverlay(GeoPoint point, float radius) {
			super();
			this.mPoint = point;
			this.mRadius = radius;
		}

		@Override
		public void draw(Canvas canvas, MapView mapview, boolean arg2) {
			super.draw(canvas, mapview, arg2);
			Paint mcircle = new Paint();
			mcircle.setARGB(128, 150, 99, 157);
			mcircle.setStyle(Style.FILL);
			mcircle.setAntiAlias(true);

			Projection projection = mapview.getProjection();
			Point point = new Point();
			projection.toPixels(mPoint, point);

			float rad = projection.metersToEquatorPixels(mRadius);
			canvas.drawCircle(point.x, point.y, rad, mcircle);

			mcircle.setAlpha(255);
			mcircle.setColor(Color.RED);
			mcircle.setStyle(Style.STROKE);
			canvas.drawCircle(point.x, point.y, rad, mcircle);
		}
	}
}
