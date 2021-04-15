package com.lang.commentsystem.utils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun PagingDataEpoxyController<*>.observeAppendState(): Flow<LoadState> = callbackFlow {
    val listener: (CombinedLoadStates) -> Unit = { state: CombinedLoadStates ->
        offer(state.append)
    }
    addLoadStateListener(listener)
    awaitClose { removeLoadStateListener(listener) }
}