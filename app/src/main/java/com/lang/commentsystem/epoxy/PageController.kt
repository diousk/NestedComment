package com.lang.commentsystem.epoxy

import android.os.Handler
import android.os.Looper
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import timber.log.Timber

abstract class PageController<Model: Any>: PagingDataEpoxyController<Model>(
    modelBuildingHandler = Handler(Looper.getMainLooper()),
    diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
) {
    init {
        addLoadStateListener {
            Timber.d("load state $it")
            // TODO: add network state handling for placeholder display
        }
    }
}