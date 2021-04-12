package com.lang.commentsystem.epoxy

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.epoxy.model.UiModel
import com.lang.commentsystem.epoxy.repo.CommentCache
import com.lang.commentsystem.epoxy.repo.CommentRepo
import com.lang.commentsystem.utils.ReduxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EpoxyViewModel @Inject constructor(
    val commentCache: CommentCache,
    val commentRepo: CommentRepo
) : ReduxViewModel<EpoxyViewState>(EpoxyViewState()) {

    val comments = commentRepo.getComments("noteId1", 5)
        .map { paging -> paging.map<CommentData, UiModel> { UiModel.Comment(it) } }
        .cachedIn(viewModelScope)

    fun loadNestedComment(commentId: String, page: Int) {
        Timber.d("loadNestedComment $commentId, page $page")
    }
}