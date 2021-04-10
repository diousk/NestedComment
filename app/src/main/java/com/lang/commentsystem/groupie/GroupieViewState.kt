package com.lang.commentsystem.groupie

import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.data.ContentData

data class GroupieViewState(
    val contentData: ContentData? = null,
    val comments: List<CommentData> = emptyList()
)
