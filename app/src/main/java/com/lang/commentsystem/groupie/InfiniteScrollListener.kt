package com.lang.commentsystem.groupie

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

abstract class InfiniteScrollListener(private val linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = AtomicBoolean(true) // True if we are still waiting for the last set of data to load.
    private val visibleThreshold =
        1 // The minimum amount of items to have below your current scroll position before loading more.
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var currentPage = 0 // TODO: change to cursor string
    private val loadMore = Runnable { onLoadMore(currentPage) }
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = linearLayoutManager.itemCount
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
        if (loading.get()) {
            Timber.d("totalItemCount $totalItemCount, previousTotal $previousTotal")
            if (totalItemCount > previousTotal || totalItemCount == 0) {
                loading.set(false)
                previousTotal = totalItemCount
            }
        }

        // End has been reached
        if (!loading.get() && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            recyclerView.post(loadMore)
            loading.set(true)
        }
    }

    fun updated() {
        Timber.d("updated totalItemCount $totalItemCount, previousTotal $previousTotal")
        previousTotal = totalItemCount
    }

    abstract fun onLoadMore(current_page: Int)
}