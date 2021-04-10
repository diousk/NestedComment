package com.lang.commentsystem.groupie

import androidx.lifecycle.viewModelScope
import com.lang.commentsystem.data.DataProvider
import com.lang.commentsystem.utils.ReduxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupieViewModel @Inject constructor(
) : ReduxViewModel<GroupieViewState>(GroupieViewState()) {

    fun getComment(page: Int) = viewModelScope.launch(defaultErrorHandler) {
        Timber.d("getComment $page")
        delay(1000)
        val comments = DataProvider.getComment(page)
        val newList = state.value.comments + comments
        setState {
            copy(
                contentData = DataProvider.getContent(),
                comments = newList
            )
        }
    }
}