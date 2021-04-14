package com.lang.commentsystem.groupie

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lang.commentsystem.R
import com.lang.commentsystem.databinding.FragmentContentBinding
import com.lang.commentsystem.epoxy.model.CommentCacheData
import com.lang.commentsystem.groupie.view.CommentItem
import com.lang.commentsystem.groupie.view.LoadingFooterItem
import com.lang.commentsystem.groupie.view.MoreCommentItem
import com.lang.commentsystem.groupie.view.NestedCommentItem
import com.lang.commentsystem.utils.collectIn
import com.lang.commentsystem.utils.viewBinding
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class GroupieFragment : Fragment(R.layout.fragment_content) {
    private val viewModel by viewModels<GroupieViewModel>()
    private val binding by viewBinding(FragmentContentBinding::bind)
    private val adapter by lazy { GroupieAdapter() }

    private val footer by lazy { LoadingFooterItem() }
    private val sectionWithFooter by lazy {
        Section().apply {
            setFooter(footer)
        }
    }

    private val scrollListener by lazy {
        val layoutManager = binding.rvContent.layoutManager as LinearLayoutManager
        object : InfiniteScrollListener(layoutManager) {
            override fun onLoadMore(currentPage: Int) {
                Timber.d("currentPage $currentPage")
                viewModel.getComment(currentPage)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContent.adapter = adapter.apply {
            setOnItemClickListener(onItemClickListener)
        }
        binding.rvContent.itemAnimator?.changeDuration = 0
        adapter.add(sectionWithFooter)

        binding.rvContent.addOnScrollListener(scrollListener)
        binding.mainComment.setOnClickListener {
            viewModel.mainComment("note0")
        }

        binding.nestedComment.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val data = viewModel.nestedComment(commentId = "id3")

            }
        }

        viewModel.events.collectIn(viewLifecycleOwner) {

            binding.rvContent.smoothScrollToPosition(0)
        }

        viewModel.state.collectIn(viewLifecycleOwner) {
            Timber.d("state list size it.comments ${it.comments.size}")
            renderList(it.comments)
        }
    }

    private fun renderList(list: List<CommentCacheData>?) {
        Timber.d("state list size: ${list?.size}")
        val groups = list?.map { comment ->
            // set root comment as header
            Section(CommentItem(comment.commentData)).apply {
                comment.nestedComments.forEach {
                    add(NestedCommentItem(it))
                }

                // add more action item
                if (comment.hasMore) {
                    addMoreCommentItem(this, comment)
                }
            }
        } ?: emptyList<Group>()
        sectionWithFooter.replaceAll(groups)
    }

    private fun addMoreCommentItem(section: Section, comment: CommentCacheData) {
        section.add(MoreCommentItem { moreItem ->
            // when click on more item, load nested comment remove self
            moreItem.setLoading()
            moreItem.notifyChanged()

            Timber.d("load more on $comment")
            viewLifecycleOwner.lifecycleScope.launch {
                val data =
                    viewModel.loadNestedComment(comment.commentData.commentId, comment.nextPage)
                        ?: return@launch
                val newNestedComment = data.nestedComments.map { NestedCommentItem(it) }
                section.update(newNestedComment)

                if (data.hasMore) {
                    addMoreCommentItem(section, data)
                }
                scrollListener.updated()
            }
        })
    }

    private val onItemClickListener = OnItemClickListener { item, _ ->
        Timber.d("OnItemClickListener $item")
        if (item is CommentItem && item.commentData.content.isNotEmpty()) {
            Toast.makeText(requireContext(), item.commentData.content, Toast.LENGTH_SHORT).show()
        }
    }
}