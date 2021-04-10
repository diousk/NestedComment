package com.lang.commentsystem.groupie

import android.view.View
import com.airbnb.epoxy.IdUtils
import com.lang.commentsystem.R
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.databinding.LayoutRootCommentBinding
import com.xwray.groupie.viewbinding.BindableItem

class CommentItem(
    val commentData: CommentData
) : BindableItem<LayoutRootCommentBinding>() {
    override fun getId(): Long = IdUtils.hashString64Bit(commentData.commentId)

    override fun bind(viewBinding: LayoutRootCommentBinding, position: Int) {
        viewBinding.userName.text = commentData.userName
        viewBinding.content.text = commentData.content
    }

    override fun getLayout(): Int = R.layout.layout_root_comment

    override fun initializeViewBinding(view: View): LayoutRootCommentBinding {
        return LayoutRootCommentBinding.bind(view)
    }
}