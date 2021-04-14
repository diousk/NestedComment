package com.lang.commentsystem.utils

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ItemAnimator.setDuration(millis: Long) {
    changeDuration = millis
    moveDuration = millis
    addDuration = millis
    removeDuration = millis
}