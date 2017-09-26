package com.podarbetweenus.charts;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.podarbetweenus.components.XAxis;
import com.podarbetweenus.components.YAxis;
import com.podarbetweenus.data.BarEntry;
import com.podarbetweenus.data.Entry;
import com.podarbetweenus.highlight.Highlight;
import com.podarbetweenus.highlight.HorizontalBarHighlighter;
import com.podarbetweenus.interfaces.datasets.IBarDataSet;
import com.podarbetweenus.renderer.HorizontalBarChartRenderer;
import com.podarbetweenus.renderer.XAxisRendererHorizontalBarChart;
import com.podarbetweenus.renderer.YAxisRendererHorizontalBarChart;
import com.podarbetweenus.utils.TransformerHorizontalBarChart;
import com.podarbetweenus.utils.Utils;

/**
 * BarChart with horizontal bar orientation. In this implementation, x- and y-axis are switched, meaning the YAxis class
 * represents the horizontal values and the XAxis class represents the vertical values.
 * 
 * @author Philipp Jahoda
 */
public class HorizontalBarChart extends BarChart {

	public HorizontalBarChart(Context context) {
		super(context);
	}

	public HorizontalBarChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalBarChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void init() {
		super.init();

		mLeftAxisTransformer = new TransformerHorizontalBarChart(mViewPortHandler);
		mRightAxisTransformer = new TransformerHorizontalBarChart(mViewPortHandler);

		mRenderer = new HorizontalBarChartRenderer(this, mAnimator, mViewPortHandler);
		setHighlighter(new HorizontalBarHighlighter(this));

		mAxisRendererLeft = new YAxisRendererHorizontalBarChart(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
		mAxisRendererRight = new YAxisRendererHorizontalBarChart(mViewPortHandler, mAxisRight, mRightAxisTransformer);
		mXAxisRenderer = new XAxisRendererHorizontalBarChart(mViewPortHandler, mXAxis, mLeftAxisTransformer, this);
	}

	private RectF mOffsetsBuffer = new RectF();

	@Override
	public void calculateOffsets() {

		float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;

		calculateLegendOffsets(mOffsetsBuffer);

		offsetLeft += mOffsetsBuffer.left;
		offsetTop += mOffsetsBuffer.top;
		offsetRight += mOffsetsBuffer.right;
		offsetBottom += mOffsetsBuffer.bottom;

		// offsets for y-labels
		if (mAxisLeft.needsOffset()) {
			offsetTop += mAxisLeft.getRequiredHeightSpace(mAxisRendererLeft.getPaintAxisLabels());
		}

		if (mAxisRight.needsOffset()) {
			offsetBottom += mAxisRight.getRequiredHeightSpace(mAxisRendererRight.getPaintAxisLabels());
		}

		float xlabelwidth = mXAxis.mLabelRotatedWidth;

		if (mXAxis.isEnabled()) {

			// offsets for x-labels
			if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {

				offsetLeft += xlabelwidth;

			} else if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {

				offsetRight += xlabelwidth;

			} else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {

				offsetLeft += xlabelwidth;
				offsetRight += xlabelwidth;
			}
		}

		offsetTop += getExtraTopOffset();
		offsetRight += getExtraRightOffset();
		offsetBottom += getExtraBottomOffset();
		offsetLeft += getExtraLeftOffset();

		float minOffset = Utils.convertDpToPixel(mMinOffset);

		mViewPortHandler.restrainViewPort(
				Math.max(minOffset, offsetLeft),
				Math.max(minOffset, offsetTop),
				Math.max(minOffset, offsetRight),
				Math.max(minOffset, offsetBottom));

		if (mLogEnabled) {
			Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: "
					+ offsetBottom);
			Log.i(LOG_TAG, "Content: " + mViewPortHandler.getContentRect().toString());
		}

		prepareOffsetMatrix();
		prepareValuePxMatrix();
	}

	@Override
	protected void prepareValuePxMatrix() {
		mRightAxisTransformer.prepareMatrixValuePx(mAxisRight.mAxisMinimum, mAxisRight.mAxisRange, mXAxis.mAxisRange, mXAxis.mAxisMinimum);
		mLeftAxisTransformer.prepareMatrixValuePx(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisRange, mXAxis.mAxisRange, mXAxis.mAxisMinimum);
	}

	@Override
	protected void calcModulus() {
		float[] values = new float[9];
		mViewPortHandler.getMatrixTouch().getValues(values);

		mXAxis.mAxisLabelModulus =
				(int) Math.ceil((mData.getXValCount() * mXAxis.mLabelRotatedHeight)
				/ (mViewPortHandler.contentHeight() * values[Matrix.MSCALE_Y]));

		if (mXAxis.mAxisLabelModulus < 1)
			mXAxis.mAxisLabelModulus = 1;
	}

	@Override
	public RectF getBarBounds(BarEntry e) {

		IBarDataSet set = mData.getDataSetForEntry(e);

		if (set == null)
			return null;

		float barspace = set.getBarSpace();
		float y = e.getVal();
		float x = e.getXIndex();

		float spaceHalf = barspace / 2f;

		float top = x - 0.5f + spaceHalf;
		float bottom = x + 0.5f - spaceHalf;
		float left = y >= 0 ? y : 0;
		float right = y <= 0 ? y : 0;

		RectF bounds = new RectF(left, top, right, bottom);

		getTransformer(set.getAxisDependency()).rectValueToPixel(bounds);

		return bounds;
	}

	@Override
	public PointF getPosition(Entry e, YAxis.AxisDependency axis) {

		if (e == null)
			return null;

		float[] vals = new float[] { e.getVal(), e.getXIndex() };

		getTransformer(axis).pointValuesToPixel(vals);

		return new PointF(vals[0], vals[1]);
	}

	/**
	 * Returns the Highlight object (contains x-index and DataSet index) of the selected value at the given touch point
	 * inside the BarChart.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	@Override
	public Highlight getHighlightByTouchPoint(float x, float y) {

		if (mData == null) {
			Log.e(LOG_TAG, "Can't select by touch. No data set.");
			return null;
		} else
			return getHighlighter().getHighlight(y, x); // switch x and y
	}

	/**
	 * Returns the lowest x-index (value on the x-axis) that is still visible on the chart.
	 * 
	 * @return
	 */
	@Override
	public int getLowestVisibleXIndex() {

		float step = mData.getDataSetCount();
		float div = (step <= 1) ? 1 : step + mData.getGroupSpace();

		float[] pts = new float[] { mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom() };

		getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
		return (int) (((pts[1] <= 0) ? 0 : ((pts[1])) / div) + 1);
	}

	/**
	 * Returns the highest x-index (value on the x-axis) that is still visible on the chart.
	 * 
	 * @return
	 */
	@Override
	public int getHighestVisibleXIndex() {

		float step = mData.getDataSetCount();
		float div = (step <= 1) ? 1 : step + mData.getGroupSpace();

		float[] pts = new float[] { mViewPortHandler.contentLeft(), mViewPortHandler.contentTop() };

		getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
		return (int) ((pts[1] >= getXChartMax()) ? getXChartMax() / div : (pts[1] / div));
	}
}