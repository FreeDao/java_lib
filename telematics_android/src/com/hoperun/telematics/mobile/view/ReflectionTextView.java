package com.hoperun.telematics.mobile.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author chen_guigui
 * 
 */
public class ReflectionTextView extends TextView {
	private static final String TAG = "ReflectionTextView";

	public ReflectionTextView(Context context) {
		super(context);
	}

	public ReflectionTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ReflectionTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		// make the shadow reverse of Y
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// make sure you can use the cache
		setDrawingCacheEnabled(true);
		// create bitmap from cache,this is the most important of this
		Bitmap originalImage = Bitmap.createBitmap(getDrawingCache());
		// create the shadow
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, 0, width, 2 * height / 3, matrix, false);
		// draw the shadow
		canvas.drawBitmap(reflectionImage, 0, 2 * height / 3, null);
		// process shadow bitmap to make it shadow like
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, 8 * height / 12, 0, height, 0x70ffffff, 0x00ffffff,
		        TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, 2 * height / 3, width, height, paint);
	}

}
