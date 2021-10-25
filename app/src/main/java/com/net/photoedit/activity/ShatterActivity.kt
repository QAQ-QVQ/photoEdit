package com.net.photoedit.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.view.View
import android.widget.*

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.net.photoedit.GalleryConfigSet
import com.net.photoedit.R
import com.net.photoedit.View.DrawableSticker
import com.net.photoedit.View.Filter
import com.net.photoedit.View.Shatter
import com.net.photoedit.View.StickerView
import com.net.photoedit.adapter.SetAdapter
import com.net.photoedit.utils.BaseConfig
import com.net.photoedit.utils.ResourceUtils
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import net.entity.ResourceEntity
import net.entity.StickerModel


class ShatterActivity : BaseActivity() {
    var funRotate = true
    var funDRotate = true
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var frameData: ArrayList<ResourceEntity> = ArrayList()
    var optionAdapter: SetAdapter? = null
    var completeBitmap: Bitmap? = null
    var bitmap: Bitmap? = BaseConfig.bitmap
    private var processOfCount = 0
    private var processOfX = 0
    lateinit var leftTv: TextView
    lateinit var titleTv: TextView
    lateinit var rightTv: TextView
    lateinit var img_main: ImageView
    lateinit var img_frame: ImageView
    lateinit var img_rotate: ImageView
    lateinit var img_threerotate: ImageView
    lateinit var recycler_res: RecyclerView
    lateinit var main: RelativeLayout
    lateinit var tools: LinearLayout
    lateinit var rotateLayout: LinearLayout
    lateinit var threed_rotateLayout: LinearLayout
    lateinit var stickerView: StickerView
    lateinit var clear: TextView
    lateinit var colorTv: TextView
    lateinit var rgShatter: RadioGroup
    companion object {
        val filter = Shatter()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shatter
    }

    override fun init() {
        BusUtils.register(this)
        img_main = findViewById(R.id.img_main)
        leftTv = findViewById(R.id.leftTv)
        titleTv = findViewById(R.id.titleTv)
        rightTv = findViewById(R.id.rightTv)
        recycler_res = findViewById(R.id.recycler_res)
        stickerView = findViewById(R.id.stickerView)
        main = findViewById(R.id.main)
        tools = findViewById(R.id.tools)
        img_rotate = findViewById(R.id.img_rotate)
        img_threerotate = findViewById(R.id.img_threerotate)
        rotateLayout = findViewById(R.id.rotateLayout)
        clear = findViewById(R.id.clear)
        rgShatter = findViewById(R.id.rgShatter)
        colorTv = findViewById(R.id.colorTv)
        img_frame = findViewById(R.id.img_frame)
        threed_rotateLayout = findViewById(R.id.threed_rotateLayout)
        Glide.with(this).load(BaseConfig.bitmap).into(img_main)
        initTitleBar()
        initCloseBtn()
        initStickerData()
        initFrameData()
        initResAdapter()
        initRadio()
        initTools()
    }

    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "shatter"
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


    private fun initRadio() {
        rgShatter.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbt -> {
                    recycler_res.visibility = View.INVISIBLE
                    tools.visibility = View.VISIBLE
                }
                R.id.rbf -> {
                    recycler_res.visibility = View.VISIBLE
                    tools.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(frameData)
                    optionAdapter!!.setOnItemClickListener { adapter, view, position ->
                        val entity = adapter.data[position] as ResourceEntity
                        Glide.with(this).load(entity.id).into(img_frame)
                    }
                }
                R.id.rbtxt -> {
                    rgShatter.clearCheck()
                    startActivity(Intent(this, TextActivity::class.java))
                }
                R.id.rbs -> {
                    recycler_res.visibility = View.VISIBLE
                    tools.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(stickerData)
                    optionAdapter!!.setOnItemClickListener { adapter, view, position ->
                        val entity = adapter.data[position] as ResourceEntity
                        val s = StickerModel(this.resources.getDrawable(entity.id))
                        val d = DrawableSticker(s.drawable)
                        stickerView.addSticker(d)

                    }
                }
            }
        }
    }

    private fun initStickerData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceUtils
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
    }

    private fun initFrameData(): ArrayList<ResourceEntity> {
        if (frameData.size == 0) {
            frameData = ResourceUtils
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "frameshatter_")
        }
        return frameData
    }

    private fun initResAdapter() {
        optionAdapter = SetAdapter(this, R.layout.layout_item_pip, null)

        recycler_res.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_res.adapter = optionAdapter
    }

    private fun initCloseBtn() {
        clear.setOnClickListener {
            rgShatter.clearCheck()
            recycler_res.visibility = View.GONE
            tools.visibility = View.GONE
        }
    }

    private fun initTools() {
        rotateLayout.setOnClickListener {
            if (funRotate) {
                img_rotate.setImageResource(R.mipmap.box)
                funRotate = false
                filter.boolPar[0] =
                    Filter.BoolParameter("Rotate Blocks", java.lang.Boolean.FALSE)
                changeStyleAsyncTask()
            } else {
                funRotate = true
                img_rotate.setImageResource(R.mipmap.rotate)
                filter.boolPar[0] =
                    Filter.BoolParameter("Rotate Blocks", java.lang.Boolean.TRUE)
                changeStyleAsyncTask()
            }
        }
        threed_rotateLayout.setOnClickListener {
            if (funDRotate) {
                img_threerotate.setImageResource(R.mipmap.box)
                funDRotate = false
                filter.boolPar[1] =
                    Filter.BoolParameter("Shattered Blocks", java.lang.Boolean.FALSE)
                changeStyleAsyncTask()
            } else {
                funDRotate = true
                img_threerotate.setImageResource(R.mipmap.rotate)
                filter.boolPar[1] = Filter.BoolParameter("Shattered Blocks", java.lang.Boolean.TRUE)
                changeStyleAsyncTask()
            }
        }
        findViewById<IndicatorSeekBar>(R.id.seekbarcount).onSeekChangeListener =
            object : OnSeekChangeListener {
                override fun onSeeking(seekParams: SeekParams) {
                    processOfCount = seekParams.progress
                }

                override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
                override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
                    filter.intPar[0] = Filter.IntParameter("Count", processOfCount, 2, 100)
                    changeStyleAsyncTask()
                }
            }
        findViewById<IndicatorSeekBar>(R.id.seekbarx).onSeekChangeListener =
            object : OnSeekChangeListener {
                override fun onSeeking(seekParams: SeekParams) {
                    processOfX = seekParams.progress
                }

                override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
                override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
                    filter.intPar[1] = Filter.IntParameter("X", "%", processOfX, 0, 100)
                    changeStyleAsyncTask()
                }
            }
    }

    private fun changeStyleAsyncTask() {
        Thread(Runnable {
            runOnUiThread {
                completeBitmap = filter.Apply(bitmap)

                img_main.setImageBitmap(completeBitmap)
            }

        }).start()
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