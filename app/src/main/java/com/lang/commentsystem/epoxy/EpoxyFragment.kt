package com.lang.commentsystem.epoxy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lang.commentsystem.R
import com.lang.commentsystem.databinding.FragmentContentBinding
import com.lang.commentsystem.utils.collectIn
import com.lang.commentsystem.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class EpoxyFragment : Fragment(R.layout.fragment_content), LoadNestedCommentListener {
    private val viewModel by viewModels<EpoxyViewModel>()
    private val binding by viewBinding(FragmentContentBinding::bind)

    @Inject
    lateinit var controller: NoteController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller.onLoadNestedComment = this
        binding.rvContent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContent.adapter = controller.adapter
        viewModel.comments.collectIn(viewLifecycleOwner) {
            Timber.d("collectIn")
            controller.submitData(it)
        }

        viewModel.commentChange.receiveAsFlow().collectIn(viewLifecycleOwner) {
            controller.requestModelBuild()
        }
    }

    override fun onLoad(commentId: String, page: Int) {
        viewModel.loadNestedComment(commentId, page)
    }
}