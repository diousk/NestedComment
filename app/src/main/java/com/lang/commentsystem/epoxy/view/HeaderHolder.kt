package com.lang.commentsystem.epoxy.view

import com.lang.commentsystem.R
import com.lang.commentsystem.databinding.LayoutHeaderContentBinding
import com.lang.commentsystem.epoxy.model.UiModel
import com.lang.commentsystem.utils.loadImageUrl

data class HeaderHolder(
    val content: UiModel.Content
) : ViewBindingKotlinModel<LayoutHeaderContentBinding>(R.layout.layout_header_content) {
    override fun LayoutHeaderContentBinding.bind() {
        image.loadImageUrl(content.contentData.image)
        description.text = content.contentData.description
    }
}