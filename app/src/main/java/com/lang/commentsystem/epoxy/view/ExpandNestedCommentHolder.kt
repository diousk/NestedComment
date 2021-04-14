package com.lang.commentsystem.epoxy.view

import com.lang.commentsystem.R
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.databinding.LayoutNestedMoreBinding
import com.lang.commentsystem.epoxy.LoadNestedCommentListener
import com.lang.commentsystem.epoxy.model.UiModel
import timber.log.Timber

class ExpandNestedCommentHolder(
    val commentData: CommentData,
    val nextPage: Int,
    val loadNestedCommentListener: LoadNestedCommentListener?
) : ViewBindingKotlinModel<LayoutNestedMoreBinding>(R.layout.layout_nested_more){
    override fun LayoutNestedMoreBinding.bind() {
        val commentId = commentData.commentId
        root.setOnClickListener {
        Timber.d("loadNestedCommentListener nextPage $nextPage")
            loadNestedCommentListener?.onLoad(commentId, nextPage)
        }
    }
}