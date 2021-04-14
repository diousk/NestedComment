package com.lang.commentsystem.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.lang.commentsystem.data.CommentData
import com.lang.commentsystem.epoxy.model.CommentCacheData
import com.lang.commentsystem.epoxy.model.UiModel
import com.lang.commentsystem.epoxy.repo.CommentCache
import com.lang.commentsystem.epoxy.view.CommentHolder
import com.lang.commentsystem.epoxy.view.EmptyPlaceholder
import com.lang.commentsystem.epoxy.view.ExpandNestedCommentHolder
import com.lang.commentsystem.epoxy.view.NestedCommentHolder
import timber.log.Timber
import javax.inject.Inject

// callback with comment id and page index
interface CommentListener {
    fun onLoadNested(commentId: String, page: Int)
    fun onCommentClick(commentData: CommentData, position: Int)
}

class NoteController @Inject constructor(
    val commentCache: CommentCache
) : PageController<UiModel>() {

    var userComments: List<CommentCacheData> = emptyList()
    var commentListener: CommentListener? = null

    // TODO: add user self's comments at top

    override fun buildItemModel(currentPosition: Int, item: UiModel?): EpoxyModel<*> {
        Timber.d("buildItemModel")
        return when (item) {
            is UiModel.Comment -> {
                CommentHolder(item, commentListener).id(item.commentData.commentId)
            }
            else -> EmptyPlaceholder().apply { id(-currentPosition) }
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        Timber.d("addModels userComments size ${userComments.size}")

        var offset = 0
        userComments.forEach { item ->
            CommentHolder(UiModel.Comment(item.commentData), commentListener).apply {
                id(item.commentData.commentId)
                position = offset
            }.addTo(this)
            offset ++
            item.nestedComments.forEachIndexed { index, commentData ->
                add(NestedCommentHolder(commentData, commentListener, offset).id(commentData.commentId))
                offset++
            }
        }

        Timber.d("addModels models size ${models.size}")
        val modelList = models.toMutableList()
        val iterator = modelList.listIterator()
        for (model in iterator) {
            Timber.d("model $model, offset = $offset")
            // append nested comment & nested load more
            if (model is CommentHolder) {
                model.position = offset
                val commentId = model.commendModel.commentData.commentId
                val cachedComment = commentCache.getCachedComment(commentId)
                Timber.d("cachedComment $cachedComment")

                cachedComment?.nestedComments?.forEach {
                    offset++
                    Timber.d("NestedCommentHolder ${it.commentId} offset $offset")
                    iterator.add(NestedCommentHolder(it, commentListener, offset)
                        .id(it.commentId))
                }

                if (cachedComment?.hasMore == true) {
                    Timber.d("cachedComment.nextPage ${cachedComment.nextPage} ")
                    offset++
                    iterator.add(
                        ExpandNestedCommentHolder(cachedComment.commentData, cachedComment.nextPage, commentListener)
                            .id(cachedComment.commentData.commentId + cachedComment.nextPage)
                    )
                }
            }
            offset++
        }

        super.addModels(modelList)
    }
}