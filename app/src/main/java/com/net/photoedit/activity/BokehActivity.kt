package com.net.photoedit.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
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


class BokehActivity : BaseActivity() {

    var bokehData: ArrayList<ResourceEntity> = ArrayList()
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var effectData: ArrayList<Bitmap> = ArrayList()
    var gpuImage: GPUImage? = null
    var optionAdapter: SetAdapter? = null
    var bitmapAdapter: StickerAdapter? = null
    lateinit var leftTv: TextView
    lateinit var titleTv: TextView
    lateinit var rightTv: TextView
    lateinit var img_main: ImageView
    lateinit var recycler_res: RecyclerView
    lateinit var recycler_effect: RecyclerView
    lateinit var main: RelativeLayout
    lateinit var stickerView: StickerView
    lateinit var clear: TextView
    lateinit var colorTv: TextView
    lateinit var rgBokeh: RadioGroup
    override fun getLayoutId(): Int {
        return R.layout.activity_bokeh
    }

    override fun init() {
        BusUtils.register(this)
        img_main = findViewById(R.id.img_main)
        leftTv = findViewById(R.id.leftTv)
        titleTv = findViewById(R.id.titleTv)
        rightTv = findViewById(R.id.rightTv)
        recycler_res = findViewById(R.id.recycler_res)
        recycler_effect = findViewById(R.id.recycler_effect)
        stickerView = findViewById(R.id.stickerView)
        main = findViewById(R.id.main)
        clear = findViewById(R.id.clear)
        rgBokeh = findViewById(R.id.rgBokeh)
        colorTv = findViewById(R.id.colorTv)

        gpuImage = GPUImage(this)
        Glide.with(this).load(BaseConfig.bitmap).into(img_main)
        initTitleBar()
        initBokehData()
        initStickerData()
        initEffectData()
        initResAdapter()
        initBitmapAdapter()
        initRadio()
        initCloseBtn()
    }

    private fun initResAdapter() {
        optionAdapter = SetAdapter(this, R.layout.layout_item_pip, null)
        optionAdapter!!.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.data[position] as ResourceEntity
            val s = StickerModel(this.resources.getDrawable(entity.id))
            val d = DrawableSticker(s.drawable)
            stickerView.addSticker(d)
        }
        recycler_res.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_res.adapter = optionAdapter
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

    private fun initRadio() {
        rgBokeh.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbb -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(bokehData)
                }
                R.id.rbe -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    bitmapAdapter!!.setNewInstance(effectData)
                }
                R.id.rbt -> {
                    rgBokeh.clearCheck()
                    startActivity(Intent(this, TextActivity::class.java))
                }
                R.id.rbs -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(stickerData)
                }
            }
        }
    }

    private fun initCloseBtn() {
        clear.setOnClickListener {
            rgBokeh.clearCheck()
            recycler_res.visibility = View.GONE
            recycler_effect.visibility = View.GONE

        }
    }

    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "Bokeh"
        rightTv.text = "ok"
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            ImageUtils.save(
                ImageUtils.view2Bitmap(main),
                Environment.getExternalStorageDirectory().getPath()+ GalleryConfigSet.galleryConfig.filePath+"/"+System.currentTimeMillis().toString()+".png",
                Bitmap.CompressFormat.PNG
            )
            BaseConfig.instance().staractivity(this,ShareActivity::class.java)
        }
    }

    private fun initBokehData(): ArrayList<ResourceEntity> {
        if (bokehData.size == 0) {
            bokehData = ResourceUtils
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "blend")
        }
        return bokehData
    }

    private fun initStickerData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceUtils
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
    }

    private fun initEffectData(): ArrayList<Bitmap> {
        if (effectData.size == 0) {
            effectData.add(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_no_effect
                )
            )
            for (index in 1 until 19) {
                gpuImage!!.setImage(BaseConfig.bitmap)
                gpuImage!!.setFilter(GUPUtil.createFilterForType(GUPUtil.getFilters().filters[index]))
                effectData.add(gpuImage!!.bitmapWithFilterApplied)
            }
        }
        return effectData
    }

    @BusUtils.Bus(tag = "text")
    fun busUtilsFun(string: String) {
        colorTv.text = string
    }

    override fun onDestroy() {
        super.onDestroy()
        BusUtils.unregister(this)
    }
}