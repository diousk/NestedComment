package com.lang.commentsystem.groupie.view

import android.view.View
import com.airbnb.epoxy.IdUtils
import com.lang.commentsystem.R
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.databinding.LayoutNestedCommentBinding
import com.xwray.groupie.viewbinding.BindableItem

class NestedCommentItem(
    val commentData: CommentData
) : BindableItem<LayoutNestedCommentBinding>() {
    override fun getId(): Long = IdUtils.hashString64Bit(commentData.commentId)

    override fun bind(viewBinding: LayoutNestedCommentBinding, position: Int) {
        viewBinding.userName.text = commentData.userName
        viewBinding.content.text = commentData.content
    }

    override fun getLayout(): Int = R.layout.layout_nested_comment

    override fun initializeViewBinding(view: View): LayoutNestedCommentBinding {
        return LayoutNestedCommentBinding.bind(view)
    }
}