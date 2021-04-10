package com.lang.commentsystem.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentData(
    val commentId: String,
    val userName: String,
    val content: String,
    val replyCount: Int,
    val replyComment: CommentData?
)
