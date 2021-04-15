package com.lang.commentsystem.epoxy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.lang.commentsystem.R
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.databinding.FragmentContentBinding
import com.lang.commentsystem.epoxy.repo.CommentCache
import com.lang.commentsystem.utils.collectIn
import com.lang.commentsystem.utils.setDuration
import com.lang.commentsystem.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class EpoxyFragment : Fragment(R.layout.fragment_content), CommentListener {
    private val viewModel by viewModels<EpoxyViewModel>()
    private val binding by viewBinding(FragmentContentBinding::bind)
    @Inject
    lateinit var commentCache: CommentCache
    lateinit var controller: NoteController

    lateinit var layoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManagerSmoothly(requireContext(), viewLifecycleOwner.lifecycleScope)

        controller =  NoteController(commentCache, viewLifecycleOwner.lifecycleScope)
        controller.commentListener = this
        binding.rvContent.itemAnimator?.setDuration(150)
        binding.rvContent.layoutManager = layoutManager
        binding.rvContent.adapter = controller.adapter
        viewModel.comments.collectIn(viewLifecycleOwner) {
            Timber.d("collectIn")
            controller.submitData(it)
        }

        viewModel.commentChange.receiveAsFlow().collectIn(viewLifecycleOwner) {
            controller.userComments = viewModel.userComments.value.toList()
            controller.requestModelBuild()
        }

        binding.mainComment.setOnClickListener {
            Timber.d("mainComment")
            viewModel.mainComment("note1")
        }

        binding.nestedComment.setOnClickListener {
            Timber.d("nestedComment")
            viewModel.nestedComment("id3")
        }

        controller.adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.rvContent.smoothScrollToPosition(0)
                }
            }
        })
    }

    override fun onLoadNested(commentId: String, page: Int) {
        viewModel.loadNestedComment(commentId, page)
    }

    override fun onCommentClick(commentData: CommentData, position: Int) {
        Timber.d("onCommentClick position $position")
        // layoutManager.scrollToPositionWithOffset(position, 0)
        binding.rvContent.smoothScrollToPosition(position)
    }
}