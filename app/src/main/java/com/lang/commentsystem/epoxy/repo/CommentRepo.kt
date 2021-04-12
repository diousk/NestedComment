package com.lang.commentsystem.epoxy.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lang.commentsystem.data.CommentData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CommentRepo {
    fun getComments(
        noteId: String,
        pageSize: Int
    ): Flow<PagingData<CommentData>>
}

class CommentRepoImpl @Inject constructor(
    private val commentCache: CommentCache
) : CommentRepo {
    override fun getComments(noteId: String, pageSize: Int): Flow<PagingData<CommentData>> {
        return Pager(
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize * 2,
                enablePlaceholders = false
            )
        ) {
            CommentDataSource(commentCache)
        }.flow
    }
}