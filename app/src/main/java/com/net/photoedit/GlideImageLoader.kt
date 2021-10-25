package com.net.photoedit

import android.app.Activity
import android.content.Context
import com.bumptech.glide.Glide
import com.yancy.gallerypick.inter.ImageLoader
import com.yancy.gallerypick.widget.GalleryImageView


class GlideImageLoader :ImageLoader {
    override fun displayImage(
        activity: Activity?,
        context: Context?,
        path: String?,
        galleryImageView: GalleryImageView?,
        width: Int,
        height: Int
    ) {
        context?.let {
            galleryImageView?.let { it1 ->
                Glide.with(it)
                    .load(path)
                    .placeholder(R.mipmap.gallery_pick_photo)
                    .centerCrop()
                    .into(it1)
            }
        };
    }

    override fun clearMemoryCache() {
        TODO("Not yet implemented")
    }
}