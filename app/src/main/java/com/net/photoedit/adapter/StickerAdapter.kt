package com.net.photoedit.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.net.photoedit.R
import com.net.photoedit.utils.ResourceUtils


class StickerAdapter(val activity: Activity, layoutResId: Int, data: ArrayList<Bitmap>?) :
    BaseQuickAdapter<Bitmap, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: Bitmap) {
        holder.getView<ImageView>(R.id.itemPip).let {
            it.layoutParams = it.layoutParams.apply {
                width = ResourceUtils.getScreenSize(activity)[1] / 6
                height = ResourceUtils.getScreenSize(activity)[1] / 6
            }
            Glide.with(activity).load(item).into(it)
        }
    }

}