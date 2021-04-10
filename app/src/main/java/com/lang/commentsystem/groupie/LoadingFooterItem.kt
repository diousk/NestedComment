package com.lang.commentsystem.groupie

import android.view.View
import com.lang.commentsystem.R
import com.lang.commentsystem.databinding.LayoutFooterMoreBinding
import com.xwray.groupie.viewbinding.BindableItem

class LoadingFooterItem : BindableItem<LayoutFooterMoreBinding>() {
    private var loading = false
    override fun bind(viewBinding: LayoutFooterMoreBinding, position: Int) {
    }

    fun setLoading() {
        loading = true
    }

    override fun getLayout(): Int = R.layout.layout_footer_more

    override fun initializeViewBinding(view: View): LayoutFooterMoreBinding {
        return LayoutFooterMoreBinding.bind(view)
    }
}