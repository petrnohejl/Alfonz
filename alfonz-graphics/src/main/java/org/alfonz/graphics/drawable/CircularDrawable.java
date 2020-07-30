package org.alfonz.graphics.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class CircularDrawable extends Drawable {
	private Bitmap mBitmap;
	private float mDiameter;
	private float mBorderWidth;
	private float mBorderGap;
	private BitmapShader mBitmapShader;
	private Paint mBitmapPaint;
	private Paint mBorderPaint;

	public CircularDrawable(@NonNull Bitmap bitmap) {
		this(bitmap, Math.min(bitmap.getWidth(), bitmap.getHeight()), 0F, 0F, 0);
	}

	public CircularDrawable(@NonNull Bitmap bitmap, float diameter) {
		this(bitmap, diameter, 0F, 0F, 0);
	}

	public CircularDrawable(@NonNull Bitmap bitmap, float borderWidth, float borderGap, int borderColor) {
		this(bitmap, Math.min(bitmap.getWidth(), bitmap.getHeight()), borderWidth, borderGap, borderColor);
	}

	public CircularDrawable(@NonNull Bitmap bitmap, float diameter, float borderWidth, float borderGap, int borderColor) {
		mBitmap = bitmap;
		mDiameter = diameter;
		mBorderWidth = borderWidth;
		mBorderGap = borderGap;

		mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		mBitmapPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setDither(true);
		mBitmapPaint.setShader(mBitmapShader);

		if (mBorderWidth > 0F) {
			mBorderPaint = new Paint();
			mBorderPaint.setStyle(Paint.Style.STROKE);
			mBorderPaint.setAntiAlias(true);
			mBorderPaint.setStrokeWidth(mBorderWidth);
			mBorderPaint.setColor(borderColor);
		}
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);

		float centerX = mBitmap.getWidth() / 2F;
		float centerY = mBitmap.getHeight() / 2F;
		float diameterMinX = Math.min(mDiameter, mBitmap.getWidth());
		float diameterMinY = Math.min(mDiameter, mBitmap.getHeight());

		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.setTranslate(-centerX + diameterMinX / 2, -centerY + diameterMinY / 2);

		mBitmapShader.setLocalMatrix(matrix);
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		if (mBorderPaint != null) {
			canvas.drawCircle(getIntrinsicWidth() / 2F, getIntrinsicHeight() / 2F, (mDiameter / 2F) - mBorderWidth - mBorderGap, mBitmapPaint);
			canvas.drawCircle(getIntrinsicWidth() / 2F, getIntrinsicHeight() / 2F, (mDiameter / 2F) - (mBorderWidth / 2F), mBorderPaint);
		} else {
			canvas.drawCircle(getIntrinsicWidth() / 2F, getIntrinsicHeight() / 2F, mDiameter / 2F, mBitmapPaint);
		}
	}

	@Override
	public void setAlpha(int alpha) {
		mBitmapPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter colorFilter) {
		mBitmapPaint.setColorFilter(colorFilter);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public int getIntrinsicWidth() {
		return (int) Math.min(mBitmap.getWidth(), mDiameter);
	}

	@Override
	public int getIntrinsicHeight() {
		return (int) Math.min(mBitmap.getHeight(), mDiameter);
	}
}
