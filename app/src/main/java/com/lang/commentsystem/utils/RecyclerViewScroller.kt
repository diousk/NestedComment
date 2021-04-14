package com.lang.commentsystem.utils

import androidx.recyclerview.widget.RecyclerView

interface RecyclerViewScroller {
    fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int)
}