package com.lang.commentsystem.groupie

import android.view.View
import com.airbnb.epoxy.IdUtils
import com.lang.commentsystem.R
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.databinding.LayoutNestedMoreBinding
import com.lang.commentsystem.databinding.LayoutRootCommentBinding
import com.xwray.groupie.viewbinding.BindableItem

class MoreCommentItem(
    val onLoadMore: (MoreCommentItem) -> Unit
) : BindableItem<LayoutNestedMoreBinding>() {
    private var loading = false
    override fun bind(viewBinding: LayoutNestedMoreBinding, position: Int) {
        viewBinding.root.setOnClickListener { onLoadMore.invoke(this) }
        if (loading) {
            viewBinding.content.text = "Loading..."
        } else {
            viewBinding.content.text = "Click to load more"
        }
    }

    fun setLoading() {
        loading = true
    }

    override fun getLayout(): Int = R.layout.layout_nested_more

    override fun initializeViewBinding(view: View): LayoutNestedMoreBinding {
        return LayoutNestedMoreBinding.bind(view)
    }
}