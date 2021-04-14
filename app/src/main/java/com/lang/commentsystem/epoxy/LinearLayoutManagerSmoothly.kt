package com.lang.commentsystem.epoxy

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import timber.log.Timber


class LinearLayoutManagerSmoothly(
    context: Context,
    private val coroutineScope: LifecycleCoroutineScope
) : LinearLayoutManager(context, VERTICAL, false) {

    companion object {
        private const val DEFAULT_MAX_SCROLL_DURATION = 1000L
    }

    private var scrollingJob: Job? = null

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val smoothScroller: SmoothScroller = TopSnappedSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
        scrollingJob?.cancel()
        scrollingJob = coroutineScope.launchWhenResumed {
            delay(DEFAULT_MAX_SCROLL_DURATION)
            Timber.d("scrollToPositionWithOffset recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING ${recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING}")
            if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                recyclerView.stopScroll()
                scrollToPositionWithOffset(position, 0)
            }
        }
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        view?.addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        view?.removeOnScrollListener(scrollListener)
        super.onDetachedFromWindow(view, recycler)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            Timber.d("newState $newState")
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                scrollingJob?.cancel()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private inner class TopSnappedSmoothScroller(context: Context?) :
        LinearSmoothScroller(context) {
        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@LinearLayoutManagerSmoothly.computeScrollVectorForPosition(targetPosition)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return 50F / (displayMetrics?.densityDpi ?: 1)
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
}