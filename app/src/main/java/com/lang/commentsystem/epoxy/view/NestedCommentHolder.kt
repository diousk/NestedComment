package com.lang.commentsystem.epoxy.view

import com.lang.commentsystem.R
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.databinding.LayoutRootCommentBinding
import com.lang.commentsystem.epoxy.model.UiModel

class NestedCommentHolder(
    val commentData: CommentData,
) : ViewBindingKotlinModel<LayoutRootCommentBinding>(R.layout.layout_nested_comment){
    override fun LayoutRootCommentBinding.bind() {
        userName.text = commentData.userName
        content.text = commentData.content
    }
}