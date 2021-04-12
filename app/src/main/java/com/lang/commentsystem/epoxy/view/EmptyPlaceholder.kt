package com.lang.commentsystem.epoxy.view

import com.lang.commentsystem.R
import com.lang.commentsystem.databinding.ViewHolderPlaceholderBinding

data class EmptyPlaceholder(
    val text: String = ""
): ViewBindingKotlinModel<ViewHolderPlaceholderBinding>(R.layout.view_holder_placeholder) {
    override fun ViewHolderPlaceholderBinding.bind() {
    }
}