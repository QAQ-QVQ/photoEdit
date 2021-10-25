package com.net.photoedit.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.library.YLCircleImageView
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.net.photoedit.GalleryConfigSet
import com.net.photoedit.R
import com.net.photoedit.View.DrawableSticker
import com.net.photoedit.View.StickerView
import com.net.photoedit.adapter.SetAdapter
import com.net.photoedit.utils.BaseConfig
import com.net.photoedit.utils.ResourceUtils
import jp.co.cyberagent.android.gpuimage.GPUImage

import net.entity.ResourceEntity
import net.entity.StickerModel


class PixelActivity : BaseActivity() {

    var effectData: ArrayList<ResourceEntity> = ArrayList()
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var colorData:ArrayList<String> = ArrayList()
    var gpuImage: GPUImage? = null
    var optionAdapter: SetAdapter? = null
    var colorAdapter:ColorAdapter?=null
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
    lateinit var rgPix: RadioGroup
    override fun getLayoutId(): Int {
        return R.layout.activity_pixel
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
        rgPix = findViewById(R.id.rgPix)
        colorTv = findViewById(R.id.colorTv)
        gpuImage = GPUImage(this)
        Glide.with(this).load(BaseConfig.bitmap).into(img_main)
        initTitleBar()
        initEffectData()
        initColorData()
        initStickerData()
        initResAdapter()
        initColorAdapter()
        initCloseBtn()
        initRadio()
    }


    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "Pixel"
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

    private fun initEffectData(): ArrayList<ResourceEntity> {
        if (effectData.size == 0) {
            effectData = ResourceUtils
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "pixel_")
        }
        return effectData
    }

    private fun initColorData():ArrayList<String> {
        if (colorData.size == 0){
            colorData = this.resources.getStringArray(R.array.color).toList() as ArrayList<String>
        }
        return colorData
    }

    private fun initStickerData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceUtils
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
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

    private fun initColorAdapter(){
        colorAdapter = ColorAdapter(this,R.layout.layout_item_pip,null)
        colorAdapter!!.setOnItemClickListener { adapter, view, position ->
            stickerView.changeBackgroundColor(adapter.data[position] as String?)
        }
        recycler_effect.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_effect.adapter = colorAdapter
    }

    private fun initRadio() {
        rgPix.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbe -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(effectData)
                }
                R.id.rbc -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    colorAdapter!!.setNewInstance(colorData)
                }
                R.id.rbt -> {
                    rgPix.clearCheck()
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
            rgPix.clearCheck()
            recycler_res.visibility = View.GONE
            recycler_effect.visibility = View.GONE

        }
    }

    @BusUtils.Bus(tag = "text")
    fun busUtilsFun(string: String) {
        colorTv.text = string
    }

    override fun onDestroy() {
        super.onDestroy()
        BusUtils.unregister(this)
    }
    class ColorAdapter(val activity: Activity, layoutResId: Int, data: ArrayList<String>?) :
        BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.getView<YLCircleImageView>(R.id.itemPip).let {
                it.layoutParams = it.layoutParams.apply {
                    width = ResourceUtils.getScreenSize(activity)[1] / 6
                    height = ResourceUtils.getScreenSize(activity)[1] / 6
                }
                it.setBorderColor(Color.parseColor(item))
                it.setBorderWidth(50f)
            }
        }
    }
}