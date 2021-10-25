package com.net.photoedit.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.net.photoedit.GalleryConfigSet
import com.net.photoedit.R
import com.net.photoedit.View.DrawableSticker
import com.net.photoedit.View.StickerView
import com.net.photoedit.adapter.SetAdapter
import com.net.photoedit.adapter.StickerAdapter
import com.net.photoedit.utils.BaseConfig
import com.net.photoedit.utils.GUPUtil
import com.net.photoedit.utils.ResourceUtils
import jp.co.cyberagent.android.gpuimage.GPUImage

import net.entity.ResourceEntity
import net.entity.StickerModel
import java.io.File
import java.text.Format


class PipActivity : BaseActivity() {
    var pipData: ArrayList<ResourceEntity> = ArrayList()
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var backgroundData: ArrayList<Bitmap> = ArrayList()
    var forcegoundData: ArrayList<Bitmap> = ArrayList()
    var setAdapter: SetAdapter? = null
    var bitmapAdapter: StickerAdapter? = null
    var gpuImage: GPUImage? = null
    lateinit var leftTv: TextView
    lateinit var titleTv: TextView
    lateinit var rightTv: TextView
    lateinit var img_main: ImageView
    lateinit var recycler_res: RecyclerView
    lateinit var recycler_effect: RecyclerView
    lateinit var main: RelativeLayout
    lateinit var stickerView: StickerView
    lateinit var clear: TextView
    lateinit var rgPip: RadioGroup

    override fun getLayoutId(): Int {
        return R.layout.activity_pip
    }

    override fun init() {
        img_main = findViewById(R.id.img_main)
        leftTv = findViewById(R.id.leftTv)
        titleTv = findViewById(R.id.titleTv)
        rightTv = findViewById(R.id.rightTv)
        recycler_res = findViewById(R.id.recycler_res)
        recycler_effect = findViewById(R.id.recycler_effect)
        stickerView = findViewById(R.id.stickerView)
        main = findViewById(R.id.main)
        gpuImage = GPUImage(this)
        Glide.with(this).load(BaseConfig.bitmap).into(img_main)
        initTitleBar()
        initCloseBtn()
        initResAdapter()
        initBitmapAdapter()
        initPipData()
        initBackgroundData()
        initForcegoundData()
        initStickerData()
        initRadio()
    }

    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "pip"
        rightTv.text = "ok"
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
         ImageUtils.save(
             ImageUtils.view2Bitmap(main),
             Environment.getExternalStorageDirectory().getPath()+GalleryConfigSet.galleryConfig.filePath+"/"+System.currentTimeMillis().toString()+".png",
             Bitmap.CompressFormat.PNG
         )
            BaseConfig.instance().staractivity(this,ShareActivity::class.java)
        }
    }

    private fun initCloseBtn() {
        clear = findViewById(R.id.clear)
        rgPip = findViewById(R.id.rgPip)


        clear.setOnClickListener {
            rgPip.clearCheck()
            recycler_res.visibility = View.GONE
            recycler_effect.visibility = View.GONE
        }
    }

    private fun initResAdapter() {
        setAdapter = SetAdapter(this, R.layout.layout_item_pip, null)
        setAdapter!!.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.data[position] as ResourceEntity
            val s = StickerModel(this.resources.getDrawable(entity.id))
            val d = DrawableSticker(s.drawable)
            stickerView.addSticker(d)
        }
        recycler_res.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_res.adapter = setAdapter
    }

    private fun initBitmapAdapter() {
        bitmapAdapter = StickerAdapter(this, R.layout.layout_item_pip, null)
        recycler_effect.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_effect.adapter = bitmapAdapter
        bitmapAdapter!!.setOnItemClickListener { adapter, view, position ->
            img_main.invalidate()
            val b = adapter.data[position] as Bitmap
            img_main.setImageBitmap(b)
        }
    }

    private fun initPipData(): ArrayList<ResourceEntity> {
        if (pipData.size == 0) {
            pipData = ResourceUtils
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "framepip_")
        }
        return pipData
    }

    private fun initBackgroundData(): ArrayList<Bitmap> {
        if (backgroundData.size == 0) {
            backgroundData.add(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_no_effect
                )
            )
            for (index in 1 until 19) {
                gpuImage!!.setImage(BaseConfig.bitmap)
                gpuImage!!.setFilter(GUPUtil.createFilterForType(GUPUtil.getFilters().filters[index]))
                backgroundData.add(gpuImage!!.bitmapWithFilterApplied)
            }
        }
        return backgroundData
    }

    private fun initForcegoundData(): ArrayList<Bitmap> {
        if (forcegoundData.size == 0) {
            forcegoundData.add(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_no_effect
                )
            )
            for (index in 1 until 19) {
                gpuImage!!.setImage(BaseConfig.bitmap)
                gpuImage!!.setFilter(GUPUtil.createFilterForType(GUPUtil.getFilters().filters[index]))
                forcegoundData.add(gpuImage!!.bitmapWithFilterApplied)
            }
        }
        return forcegoundData
    }

    private fun initStickerData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceUtils
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
    }

    private fun initRadio() {
        rgPip.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbp -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    setAdapter!!.setNewInstance(pipData)
                }
                R.id.rbb -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    bitmapAdapter!!.setNewInstance(backgroundData)
                }
                R.id.rbf -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    bitmapAdapter!!.setNewInstance(forcegoundData)
                }
                R.id.rbs -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    setAdapter!!.setNewInstance(stickerData)
                }
            }
        }
    }
}
