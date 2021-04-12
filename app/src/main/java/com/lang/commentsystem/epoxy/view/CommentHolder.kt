package com.lang.commentsystem.epoxy.view

import com.lang.commentsystem.R
import com.lang.commentsystem.databinding.LayoutRootCommentBinding
import com.lang.commentsystem.epoxy.model.UiModel

data class CommentHolder(
    val commendModel: UiModel.Comment
) : ViewBindingKotlinModel<LayoutRootCommentBinding>(R.layout.layout_root_comment){
    override fun LayoutRootCommentBinding.bind() {
        userName.text = commendModel.commentData.userName
        content.text = commendModel.commentData.content
    }
}