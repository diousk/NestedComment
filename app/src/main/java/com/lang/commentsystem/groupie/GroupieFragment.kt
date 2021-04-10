package com.lang.commentsystem.groupie

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lang.commentsystem.R
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.data.DataProvider
import com.lang.commentsystem.databinding.FragmentContentBinding
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
    private val sectionWithFooter by lazy { Section().apply {
        setFooter(footer)
    } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContent.adapter = adapter.apply {
            setOnItemClickListener(onItemClickListener)
        }
        adapter.add(sectionWithFooter)

        val layoutManager = binding.rvContent.layoutManager as LinearLayoutManager
        binding.rvContent.addOnScrollListener(object : InfiniteScrollListener(layoutManager) {
            override fun onLoadMore(currentPage: Int) {
                Timber.d("currentPage $currentPage")
                viewModel.getComment(currentPage)
            }
        })
        viewModel.selectObserve(GroupieViewState::comments)
            .collectIn(viewLifecycleOwner, action = ::renderList)
    }

    private fun renderList(list: List<CommentData>?) {
        Timber.d("state: $list")
        val groups = list?.map { comment ->
            // set root comment as header
            Section(CommentItem(comment)).apply {

                // add nested first reply comment
                comment.replyComment?.let { reply ->
                    add(NestedCommentItem(reply))
                }

                // add more action item
                if (comment.replyCount > 1) {
                    val lastItem = groups.last { it is NestedCommentItem } as NestedCommentItem
                    addMoreCommentItem(this, lastItem.commentData.commentId)
                }
            }
        } ?: emptyList<Group>()
        sectionWithFooter.replaceAll(groups)
    }

    private fun addMoreCommentItem(section: Section, commentId: String) {
        section.add(MoreCommentItem { moreItem ->
            // when click on more item, load nested comment remove self
            moreItem.setLoading()
            moreItem.notifyChanged()

            Timber.d("load more on $commentId")

            viewLifecycleOwner.lifecycleScope.launch {
                val comments = DataProvider.getNestedComment2_1(commentId)
                section.remove(moreItem)
                Timber.d("getNestedComment2_1 done")
                val newCommentItems = comments.map { NestedCommentItem(it) }
                section.addAll(newCommentItems)
                addMoreCommentItem(section, comments.last().commentId)
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