package com.lang.commentsystem.utils

import android.content.res.Resources
import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import timber.log.Timber

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

object ImageResizeOptions {
    private val tinyIconImgOptions = ResizeOptions(16.toPx, 16.toPx)
    private val iconImgOptions = ResizeOptions(40.toPx, 40.toPx)
    private val mediumIconImgOptions = ResizeOptions(60.toPx, 60.toPx)
    private val bannerImgOptions = ResizeOptions(1600, 900)
    private val smallBannerImgOptions = ResizeOptions(100.toPx, 60.toPx)
    private val coverImgOptions = ResizeOptions(650, 350)
    private val largeCoverImgOptions = ResizeOptions(1300, 700)
    private val headImgOptions = ResizeOptions(36.toPx, 36.toPx)
    private val largeIconImgOptions = ResizeOptions(90.toPx, 90.toPx)
    private val avatarOverlayImgOptions = ResizeOptions(180.toPx, 180.toPx)
    private val previewImgOptions = ResizeOptions(220.toPx, 220.toPx)
    private val skillLicenseImgOptions = ResizeOptions(335.toPx, 180.toPx)

    const val DEFAULT_IMG = 2000
    const val ICON_IMG = 2001
    const val BANNER_IMG = 2002
    const val COVER_IMG = 2003
    const val HEAD_IMG = 2004
    const val LARGE_COVER_IMG = 2005
    const val LARGE_ICON_IMG = 2006
    const val AVATAR_OVERLAY_IMG = 2007
    const val SMALL_BANNER = 2008
    const val MEDIUM_ICON_IMG = 2009
    const val PREVIEW_IMAGE = 2010
    const val LICENSE_IMAGE = 2011
    const val TINY_IMG = 2012

    fun getResizeOption(type: Int): ResizeOptions? {
        return when (type) {
            TINY_IMG -> tinyIconImgOptions
            ICON_IMG -> iconImgOptions
            MEDIUM_ICON_IMG -> mediumIconImgOptions
            BANNER_IMG -> bannerImgOptions
            COVER_IMG -> coverImgOptions
            HEAD_IMG -> headImgOptions
            LARGE_COVER_IMG -> largeCoverImgOptions
            LARGE_ICON_IMG -> largeIconImgOptions
            AVATAR_OVERLAY_IMG -> avatarOverlayImgOptions
            SMALL_BANNER -> smallBannerImgOptions
            PREVIEW_IMAGE -> previewImgOptions
            LICENSE_IMAGE -> skillLicenseImgOptions
            else -> null
        }
    }
}

fun SimpleDraweeView.loadImageUrl(imgUrl: String?, viewType: Int = ImageResizeOptions.DEFAULT_IMG) {
    if (imgUrl == null) {
        Timber.w("imgUrl is null")
        return
    }
    val resizeOptions = ImageResizeOptions.getResizeOption(viewType)

    val cacheChoice = when (viewType) {
        ImageResizeOptions.ICON_IMG -> ImageRequest.CacheChoice.SMALL
        ImageResizeOptions.HEAD_IMG -> ImageRequest.CacheChoice.SMALL
        else -> ImageRequest.CacheChoice.DEFAULT
    }

    val request = ImageRequestBuilder
        .newBuilderWithSource(Uri.parse(imgUrl))
        .setResizeOptions(resizeOptions)
        .setCacheChoice(cacheChoice)
        .build()

    // Load image url.
    controller = Fresco.newDraweeControllerBuilder().apply {
        autoPlayAnimations = true
        oldController = controller
        imageRequest = request
    }.build()
}