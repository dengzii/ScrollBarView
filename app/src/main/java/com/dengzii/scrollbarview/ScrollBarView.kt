package com.dengzii.scrollbarview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import androidx.annotation.ColorRes

class ScrollBarView : View {

    private var mWidth = 20f.dp2px()
    private val mHeight = 800f.dp2px()

    private val mPaint = Paint()
    private var mY = 0f
    private var mLastY = 0f

    private var mBarHeight = 100f.dp2px()

    private var mContentHeight = 0
    private var mScrollView: ScrollView? = null

    private var mBarTouched = false

    init {
        mPaint.isAntiAlias = true
        mPaint.color = resources.getColor(android.R.color.black, null)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastY = event.y
                mBarTouched = true
            }
            MotionEvent.ACTION_MOVE -> {
                val dest = mLastY - event.y
                mY -= dest
                mLastY = event.y
                if (mY < 0) {
                    mY = 0f
                }
                if (mY + mBarHeight > height) {
                    mY = height - mBarHeight
                }
                val yPercent = mY / (height - mBarHeight)

                if (mScrollView != null) {
                    mScrollView!!.scrollTo(
                        0,
                        ((mContentHeight - mScrollView!!.measuredHeight) * yPercent).toInt()
                    )
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                mBarTouched = false
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(
            0f, mY, width.toFloat(), mY + mBarHeight,
            width.toFloat(), width.toFloat(), mPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val modeH = MeasureSpec.getMode(heightMeasureSpec)
        var sizeH = MeasureSpec.getSize(heightMeasureSpec)
        val modeW = MeasureSpec.getMode(widthMeasureSpec)
        var sizeW = MeasureSpec.getSize(widthMeasureSpec)

        if (modeH == MeasureSpec.AT_MOST) {
            sizeH = mHeight.toInt()
        }
        if (modeW == MeasureSpec.AT_MOST) {
            sizeW = mWidth.toInt()
        }
        setMeasuredDimension(sizeW, sizeH)
    }

    fun setBarHeight(dp: Float) {
        mBarHeight = dp.dp2px()
        invalidate()
    }

    fun setBarColor(@ColorRes color: Int) {
        mPaint.color = resources.getColor(color, null)
        invalidate()
    }

    fun setWithScrollView(scrollView: ScrollView) {

        mContentHeight = 0
        mScrollView = scrollView
        scrollView.post {

            for (i in 0 until scrollView.childCount) {
                val child = scrollView.getChildAt(i)
                mContentHeight += child.measuredHeight
            }
            scrollView.setOnScrollChangeListener { _, _, sy: Int, _, _ ->
                if (mBarTouched) {
                    return@setOnScrollChangeListener
                }
                val scrollPercent =
                    (sy.toFloat() / (mContentHeight.toFloat() - scrollView.measuredHeight))
                mY = (height.toFloat() - mBarHeight) * scrollPercent
                invalidate()
            }
        }
    }

    private fun Float.dp2px(): Float {
        return (0.5f + this * getDeviceDensity())
    }

    private fun getDeviceDensity(): Float {
        return Resources.getSystem().displayMetrics.density
    }

}