package com.lang.commentsystem.groupie

import com.lang.commentsystem.data.ContentData
import com.lang.commentsystem.epoxy.model.CommentCacheData

data class GroupieViewState(
    val contentData: ContentData? = null,
    val comments: MutableList<CommentCacheData> = mutableListOf()
)
