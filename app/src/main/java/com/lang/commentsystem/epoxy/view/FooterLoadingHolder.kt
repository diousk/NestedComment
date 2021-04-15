package com.lang.commentsystem.epoxy.view

import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.lang.commentsystem.R
import com.lang.commentsystem.databinding.LayoutFooterMoreBinding

data class FooterLoadingHolder(
    val loadState: LoadState
) : ViewBindingKotlinModel<LayoutFooterMoreBinding>(R.layout.layout_footer_more) {
    override fun LayoutFooterMoreBinding.bind() {
        content.isVisible = loadState == LoadState.Loading
    }
}