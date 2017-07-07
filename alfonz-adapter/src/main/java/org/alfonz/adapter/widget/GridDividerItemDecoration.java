package org.alfonz.adapter.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


// TODO: fix drawing when grid has only a few items or odd number of items, test vertical/horizontal orientation, reverse, RTL
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration
{
	private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

	private Drawable mDivider;
	private int mMargin;


	public GridDividerItemDecoration(Context context, int margin)
	{
		final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
		mDivider = typedArray.getDrawable(0);
		typedArray.recycle();
		mMargin = margin;
	}


	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state)
	{
		outRect.set(mMargin, mMargin, mMargin, mMargin);
	}


	@Override
	public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state)
	{
		if(recyclerView.getLayoutManager() == null)
		{
			return;
		}

		drawVertical(canvas, recyclerView);
		drawHorizontal(canvas, recyclerView);
	}


	public void setDrawable(@NonNull Drawable drawable)
	{
		mDivider = drawable;
	}


	private void drawVertical(Canvas canvas, RecyclerView recyclerView)
	{
		final int top = recyclerView.getPaddingTop();
		final int bottom = recyclerView.getHeight() - recyclerView.getPaddingBottom();
		final int childCount = recyclerView.getChildCount();

		for(int i = 0; i < childCount; i++)
		{
			final View child = recyclerView.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int left = child.getRight() + params.rightMargin + mMargin;
			final int right = left + mDivider.getIntrinsicWidth();

			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);
		}
	}


	private void drawHorizontal(Canvas canvas, RecyclerView recyclerView)
	{
		if(recyclerView.getChildCount() == 0)
		{
			return;
		}

		final int left = recyclerView.getPaddingLeft();
		final int right = recyclerView.getWidth() - recyclerView.getPaddingRight();
		final int index = isReverse(recyclerView) && isVertical(recyclerView) ? recyclerView.getChildCount() - 1 : 0;
		final View child = recyclerView.getChildAt(index);

		if(child.getHeight() == 0)
		{
			return;
		}

		final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
		final int parentBottom = recyclerView.getHeight() - recyclerView.getPaddingBottom();
		int top = child.getBottom() + params.bottomMargin + mMargin;
		int bottom = top + mDivider.getIntrinsicHeight();

		while(bottom < parentBottom)
		{
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);

			top += mMargin + params.topMargin + child.getHeight() + params.bottomMargin + mMargin;
			bottom = top + mDivider.getIntrinsicHeight();
		}
	}


	private boolean isVertical(RecyclerView recyclerView)
	{
		if(recyclerView.getLayoutManager() instanceof GridLayoutManager)
		{
			GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
			return gridLayoutManager.getOrientation() == GridLayoutManager.VERTICAL;
		}
		else
		{
			throw new IllegalStateException(getIllegalStateExceptionMessage());
		}
	}


	private boolean isReverse(RecyclerView recyclerView)
	{
		if(recyclerView.getLayoutManager() instanceof GridLayoutManager)
		{
			GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
			return gridLayoutManager.getReverseLayout();
		}
		else
		{
			throw new IllegalStateException(getIllegalStateExceptionMessage());
		}
	}


	private String getIllegalStateExceptionMessage()
	{
		return String.format("%s can only be used with a %s",
				this.getClass().getSimpleName(),
				GridLayoutManager.class.getSimpleName());
	}
}