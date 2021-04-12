package com.lang.commentsystem.epoxy.repo

import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.epoxy.model.CommentCacheData
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentCache @Inject constructor(
    // TODO: add single thread dispatcher
)  {
    private val cache = ConcurrentHashMap<String, CommentCacheData>()

    fun getCachedComment(commentId: String): CommentCacheData? = cache[commentId]

    fun updateComment(data: CommentCacheData, requireExisting: Boolean = false) {
        Timber.d("data $data, requireExisting $requireExisting")
        if (requireExisting && cache.containsKey(data.commentData.commentId)) {
            cache[data.commentData.commentId] = data
        } else {
            cache[data.commentData.commentId] = data
        }
    }

    fun appendNestedComment(commentId: String, comments: List<CommentData>, nextPage: Int) {
        val commentCache = cache[commentId] ?: return
        val currentNestedComments = commentCache.nestedComments.toMutableList()
        val newNestedComments = (currentNestedComments + comments).distinctBy { it.commentId }
        cache[commentId] = commentCache.copy(nestedComments = newNestedComments, nextPage = nextPage)
    }

    fun removeComment(commentId: String) {
        cache.remove(commentId)
    }

    fun clear() {
        cache.clear()
    }
}