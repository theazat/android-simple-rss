package com.lelloman.launcher.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lelloman.common.widget.bottomdrawer.BottomDrawerFrame
import com.lelloman.common.widget.bottomdrawer.BottomDrawerLayout
import com.lelloman.launcher.R
import java.lang.Math.pow

@SuppressLint("ClickableViewAccessibility")
class AppsDrawerView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs), BottomDrawerFrame.BottomDrawerFrameContent {

    private val headerView by lazy { findViewById<View>(R.id.header) }
    private val fullView by lazy { findViewById<View>(R.id.recycler_view_apps) }

    private val appsRecyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view_apps) }

    override val headerHeight = context.resources.getDimensionPixelSize(R.dimen.apps_drawer_header_height)

    val recyclerView: RecyclerView by lazy { appsRecyclerView }

    private var overscrolling = false

    private var lastEventY = 0f
    private val parentAsViewGroup get() = parent as ViewGroup

    init {
        inflate(context, R.layout.view_apps_drawer, this)
        fullView.alpha = 0f
        recyclerView.layoutManager = object : GridLayoutManager(context, 1) {
            override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
                val scrollRange = super.scrollVerticallyBy(dy, recycler, state)
                if (dy - scrollRange < 0) {
                    overscrolling = true
                    parentAsViewGroup.dispatchTouchEvent(MotionEvent.obtain(
                        System.currentTimeMillis(),
                        System.currentTimeMillis(),
                        MotionEvent.ACTION_DOWN,
                        0f,
                        lastEventY,
                        0
                    ))
                    return 0
                }
                return scrollRange
            }
        }
    }

    override fun onStateChanged(state: BottomDrawerLayout.State) {
        overscrolling = false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        lastEventY = event.y
        if (event.action == MotionEvent.ACTION_UP) {
            overscrolling = false
        }
        return overscrolling
    }

    override fun setVerticalOffset(verticalOffset: Float) {
        val percent = verticalOffset / (height.toFloat() - headerHeight)

        headerView.alpha = pow(percent.toDouble(), 2.0).toFloat()
//        setBackgroundColor((255 - (percent * 255).toInt()) * 0x1000000)
        fullView.alpha = 1f - percent

        val opened = percent <= 0f
        fullView.visibility = if (percent == 1f) View.GONE else View.VISIBLE
        headerView.visibility = if (opened) View.GONE else View.VISIBLE

        if(percent == 0f || percent == 1f){
            Log.d("ASD", "percent $percent")
        }
    }
}