package com.lang.commentsystem.epoxy.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.data.DataProvider
import com.lang.commentsystem.epoxy.model.CommentCacheData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class CommentDataSource(
    private val commentCache: CommentCache
) : PagingSource<Int, CommentData>() {
    override fun getRefreshKey(state: PagingState<Int, CommentData>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentData> {
        return try {
            val nextPageNumber = params.key ?: 1
            Timber.d("CommentDataSource load nextPageNumber $nextPageNumber")
            val comments = fetchComments(nextPageNumber, params.loadSize)
            LoadResult.Page(
                data = comments,
                prevKey = null,
                nextKey = if (comments.size < params.loadSize) null else nextPageNumber + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    private suspend fun fetchComments(page: Int, pageSize: Int): List<CommentData> =
        withContext(Dispatchers.Default) {
            DataProvider.getComment(page).onEach { data ->
                val nestedComments = data.replyComment?.let { listOf(it) } ?: emptyList()
                val commentCacheData = CommentCacheData(data, nestedComments)
                commentCache.updateComment(commentCacheData)
            }
        }
}