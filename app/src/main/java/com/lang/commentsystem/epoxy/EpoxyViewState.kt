package com.lang.commentsystem.epoxy

import com.lang.commentsystem.epoxy.model.UiModel

data class EpoxyViewState(
    val uiModels: List<UiModel> = emptyList()
)