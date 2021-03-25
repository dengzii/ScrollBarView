package com.dengzii.scrollbarview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import androidx.core.view.children

class ScrollBarView : View {

    private var mWidth = 20f.dp2px()

    private val mPaint = Paint()
    private val mHeight = 800f.dp2px()
    private var mY = 0f
    private var mLastY = 0f

    private var mBarColor = android.R.color.black
    private var mBarHeight = 100f.dp2px()

    private var mContentHeight = 0
    private var mScrollView: ScrollView? = null

    private var mBarTouched = false

    init {
        mPaint.color = resources.getColor(mBarColor, null)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

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
                println("==>$yPercent")

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
        canvas.drawRoundRect(0f, mY, mWidth, mY + mBarHeight, mWidth, mWidth, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val modeH = MeasureSpec.getMode(heightMeasureSpec)
        var sizeH = MeasureSpec.getSize(heightMeasureSpec)
        if (modeH == MeasureSpec.AT_MOST) {
            sizeH = mHeight.toInt()
        }
        setMeasuredDimension(mWidth.toInt(), sizeH)
    }

    fun setWithScrollView(scrollView: ScrollView) {

        mContentHeight = 0
        mScrollView = scrollView
        scrollView.post {
            scrollView.children.forEach {
                mContentHeight += it.measuredHeight
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