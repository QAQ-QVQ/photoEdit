package com.net.photoedit.activity

import android.R.attr.path
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.net.photoedit.GalleryConfigSet
import com.net.photoedit.R
import com.net.photoedit.utils.BaseConfig
import com.net.photoedit.utils.DialogUtils
import com.net.photoedit.utils.ResourceUtils
import com.yancy.gallerypick.config.GalleryPick
import com.yancy.gallerypick.inter.IHandlerCallBack


class EditorActivity : BaseActivity() {
    var data: ArrayList<String>? = null
    val dialogData = arrayListOf("TAKE PHOTO", "CHOOSE PHOTO")
    var type: String? = null
    override fun init() {
        data = ResourceUtils.getResource(this)
        var listAdapter:ListAdapter = ListAdapter(data!!, this)
        var recycler:ListView = findViewById(R.id.recycler)
        recycler.adapter = listAdapter
        recycler.setOnItemClickListener { parent, view, position, id ->
            type = listAdapter.data[position]

            DialogUtils.showDialog(this, dialogData) { dialog, index, text ->
                kotlin.run {
                    when (index) {
                        0 -> getPhoto()
                        1 -> choosePhoto()
                    }
                }
            }
        }
    }

    private fun choosePhoto() {
        GalleryPick.getInstance().setGalleryConfig(
            GalleryConfigSet.getConfig(
                false, iHandlerCallBack(
                    this,
                    type
                )
            )
        ).open(this);
    }

    private fun getPhoto() {
        GalleryPick.getInstance().setGalleryConfig(
            GalleryConfigSet.getConfig(
                true, iHandlerCallBack(
                    this,
                    type
                )
            )
        ).open(this);

    }

    override fun getLayoutId(): Int {
       return R.layout.activity_editor;
    }
    private class ListAdapter(val data: ArrayList<String>, val context: Context): BaseAdapter() {
        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = LayoutInflater.from(context).inflate(
                R.layout.layout_item_editor,
                parent,
                false
            )
            var image: ImageView = view.findViewById(R.id.itemEditor)
            Glide.with(context).load(data.get(position)).into(image)
            return view
        }
    }
    class iHandlerCallBack(context: Context, type: String?): IHandlerCallBack {
        var mContext:Context = context
        var mtype:String?= type
        override fun onStart() {

        }

        override fun onSuccess(photoList: MutableList<String>?) {
            for (s in photoList!!) {
                var type:String? = null
                mtype?.apply {
                   type = this.substring(this.lastIndexOf('/') + 1)
                }
                BaseConfig.bitmap = ImageUtils.getBitmap(s)
                when (type) {
                    "pip" -> {
                        BaseConfig.instance().staractivity(mContext,PipActivity::class.java)
                    }
                    "color" ->{
                        BaseConfig.instance().staractivity(mContext,ColorActivity::class.java)
                    }
                    "bokeh" -> {
                        BaseConfig.instance().staractivity(mContext,BokehActivity::class.java)
                    }
                    "pixel" -> {
                       BaseConfig.instance().staractivity(mContext,PixelActivity::class.java)
                    }
                    "shattering" -> {
                       BaseConfig.instance().staractivity(mContext,ShatterActivity::class.java)
                    }
                }
            }
        }

        override fun onCancel() {

        }

        override fun onFinish() {

        }

        override fun onError() {

        }

    }
}