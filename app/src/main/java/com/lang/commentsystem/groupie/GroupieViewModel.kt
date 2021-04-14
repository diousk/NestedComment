package com.lang.commentsystem.groupie

import androidx.lifecycle.viewModelScope
import com.lang.commentsystem.data.DataProvider
import com.lang.commentsystem.epoxy.model.CommentCacheData
import com.lang.commentsystem.epoxy.repo.CommentCache
import com.lang.commentsystem.utils.ReduxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupieViewModel @Inject constructor(
    val commentCache: CommentCache
) : ReduxViewModel<GroupieViewState>(GroupieViewState()) {

    private val _events = Channel<String>(Channel.RENDEZVOUS)
    val events = _events.receiveAsFlow()

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

    fun mainComment(noteId: String) = viewModelScope.launch(defaultErrorHandler) {
        delay(1000)
        val commentData = DataProvider.createUserComment(noteId)
        val commentCacheData = CommentCacheData(commentData, emptyList())
        commentCache.updateComment(commentCacheData)
        val newList = listOf(commentCacheData) + state.value.comments
        Timber.d("newList size ${newList.size}")
        setState {
            copy(
                comments = newList.toMutableList()
            )
        }
        _events.send("") // scroll to top
    }

    suspend fun nestedComment(commentId: String): CommentCacheData {
        val commentData = DataProvider.createUserComment(commentId)
        val commentCacheData = commentCache.getCachedComment(commentId)!!
        commentCache.appendNestedComment(commentId, listOf(commentData), commentCacheData.nextPage)
        val target = state.value.comments.indexOfFirst { it.commentData.commentId == commentId }
        state.value.comments[target] = commentCache.getCachedComment(commentId)!!
        _events.send(commentId) // scroll to comment
        return commentCache.getCachedComment(commentId)!!
    }
}