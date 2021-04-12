package com.lang.commentsystem.epoxy.model

import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.data.ContentData

sealed class UiModel {
    data class Content(val contentData: ContentData) : UiModel()
    data class Comment(val commentData: CommentData) : UiModel()
    data class NestedComment(val commentData: CommentData) : UiModel()
    data class ExpandNestedComment(val commentData: CommentData, val nextPage: Int) : UiModel()
}

data class CommentCacheData(
    val commentData: CommentData,
    val nestedComments: List<CommentData>,
    val nestedEnded: Boolean = false, // indicate that the pagination is ended,
    val nextPage: Int = 1
) {
    val hasMore: Boolean
        get() = (nestedComments.size < commentData.replyCount) || nestedEnded
}