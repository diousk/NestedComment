package com.lang.commentsystem.groupie

import androidx.lifecycle.viewModelScope
import com.lang.commentsystem.data.DataProvider
import com.lang.commentsystem.epoxy.model.CommentCacheData
import com.lang.commentsystem.epoxy.repo.CommentCache
import com.lang.commentsystem.utils.ReduxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupieViewModel @Inject constructor(
    val commentCache: CommentCache
) : ReduxViewModel<GroupieViewState>(GroupieViewState()) {

    fun getComment(page: Int) = viewModelScope.launch(defaultErrorHandler) {
        Timber.d("getComment page $page")
        delay(1000)
        val comments = DataProvider.getComment(page).map {
            val nestedComments = it.replyComment?.let { comment -> listOf(comment) } ?: emptyList()
            CommentCacheData(it, nestedComments.toMutableList())
        }
        val newList = state.value.comments + comments
        newList.forEach { commentCache.updateComment(it) }

        setState {
            copy(
                contentData = DataProvider.getContent(),
                comments = newList.toMutableList()
            )
        }
    }

    suspend fun loadNestedComment(commentId: String, page: Int): CommentCacheData? {
        Timber.d("loadNestedComment $commentId, page $page")
        val cachedComment = commentCache.getCachedComment(commentId) ?: return null
        Timber.d("targetComment nestedComments.size ${cachedComment.nestedComments.size}")

        val comments = DataProvider.getNestedComment(commentId, page)
        commentCache.appendNestedComment(commentId, comments, page + 1)
        val target = state.value.comments.indexOfFirst { it.commentData.commentId == commentId }
        state.value.comments[target] = commentCache.getCachedComment(commentId)!!
        return commentCache.getCachedComment(commentId)
    }
}