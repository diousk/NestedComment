package com.lang.commentsystem.epoxy.view

import com.lang.commentsystem.R
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.databinding.LayoutNestedCommentBinding
import com.lang.commentsystem.databinding.LayoutRootCommentBinding
import com.lang.commentsystem.epoxy.CommentListener
import com.lang.commentsystem.epoxy.model.UiModel

data class NestedCommentHolder(
    val commentData: CommentData,
    val commentListener: CommentListener?,
    val index: Int
) : ViewBindingKotlinModel<LayoutNestedCommentBinding>(R.layout.layout_nested_comment){
    override fun LayoutNestedCommentBinding.bind() {
        userName.text = commentData.userName
        content.text = commentData.content
        root.setOnClickListener { commentListener?.onCommentClick(commentData, index) }
    }
}