package com.lang.commentsystem.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.lang.commentsystem.epoxy.model.UiModel
import com.lang.commentsystem.epoxy.repo.CommentCache
import com.lang.commentsystem.epoxy.view.CommentHolder
import com.lang.commentsystem.epoxy.view.EmptyPlaceholder
import com.lang.commentsystem.epoxy.view.ExpandNestedCommentHolder
import com.lang.commentsystem.epoxy.view.NestedCommentHolder
import timber.log.Timber
import javax.inject.Inject

// callback with comment id and page index
fun interface LoadNestedCommentListener {
    fun onLoad(commentId: String, page: Int)
}

class NoteController @Inject constructor(
    val commentCache: CommentCache
) : PageController<UiModel>() {

    var onLoadNestedComment: LoadNestedCommentListener? = null

    // TODO: add user self's comments at top

    override fun buildItemModel(currentPosition: Int, item: UiModel?): EpoxyModel<*> {
        Timber.d("buildItemModel")
        return when (item) {
            is UiModel.Comment -> {
                CommentHolder(item).id(item.commentData.commentId)
            }
            else -> EmptyPlaceholder().apply { id(-currentPosition) }
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        Timber.d("addModels models size ${models.size}")
        val modelList = models.toMutableList()
        val iterator = modelList.listIterator()
        for (model in iterator) {
            Timber.d("model $model")
            // append nested comment & nested load more
            if (model is CommentHolder) {
                val commentId = model.commendModel.commentData.commentId
                val cachedComment = commentCache.getCachedComment(commentId)
                Timber.d("cachedComment $cachedComment")
                cachedComment?.nestedComments?.forEach {
                    iterator.add(NestedCommentHolder(it)
                        .id(it.commentId))
                }
                if (cachedComment?.hasMore == true) {
                    iterator.add(
                        ExpandNestedCommentHolder(cachedComment.commentData, cachedComment.nextPage, onLoadNestedComment)
                            .id(cachedComment.commentData.commentId)
                    )
                }
            }
        }
        super.addModels(modelList)
    }
}