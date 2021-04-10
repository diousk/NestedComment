package com.lang.commentsystem.groupie

import androidx.lifecycle.viewModelScope
import com.lang.commentsystem.data.DataProvider
import com.lang.commentsystem.utils.ReduxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupieViewModel @Inject constructor(
) : ReduxViewModel<GroupieViewState>(GroupieViewState()) {
    init {
        // simulate data fetching
        viewModelScope.launch(defaultErrorHandler) {
            delay(1000)
            setState {
                copy(
                    contentData = DataProvider.getContent(),
                    comments = DataProvider.getComment(0)
                )
            }
        }
    }
}