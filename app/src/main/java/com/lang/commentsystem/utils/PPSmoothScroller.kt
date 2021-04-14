package com.lang.commentsystem.utils

import android.content.Context
import android.util.DisplayMetrics
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.math.min

class PPSmoothScroller(
    private val context: Context,
    private val coroutineScope: LifecycleCoroutineScope,
    private val millisecondsPerInch: Float = DEFAULT_MILLISECONDS_PER_INCH,
    private val millisecondsPerItem: Long = DEFAULT_MILLISECONDS_PER_ITEM,
    private val maxScrollDuration: Long = DEFAULT_MILLISECONDS_MAX_SCROLL_DURATION
) : RecyclerViewScroller {

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return millisecondsPerInch / (displayMetrics?.densityDpi ?: 1)
            }
        }.apply { targetPosition = position }

        val itemCount = recyclerView?.adapter?.itemCount ?: 0
        val smoothScrollTime = min(itemCount * millisecondsPerItem, maxScrollDuration)
        val layoutManager = recyclerView?.layoutManager as? LinearLayoutManager

        coroutineScope.launchWhenResumed {
            layoutManager?.startSmoothScroll(smoothScroller)
            delay(100)
            Timber.d("layoutManager?.isSmoothScrolling ${layoutManager?.isSmoothScrolling}")
            layoutManager?.scrollToPositionWithOffset(position, 0)
        }
    }

    companion object {
        const val DEFAULT_MILLISECONDS_MAX_SCROLL_DURATION = 1000L
        const val DEFAULT_MILLISECONDS_PER_INCH = 10F
        const val DEFAULT_MILLISECONDS_PER_ITEM = 5L
    }
}