package com.net.photoedit.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.*
import com.net.photoedit.GalleryConfigSet
import com.net.photoedit.R
import com.net.photoedit.utils.AppStoreUtils
import com.net.photoedit.utils.BaseConfig
import java.io.File

class ShareActivity : BaseActivity(),ViewPager.OnPageChangeListener{
    lateinit var leftTv: TextView
    lateinit var titleTv: TextView
    lateinit var rightTv: TextView
    lateinit var countTv: TextView
    lateinit var pager: ViewPager
    lateinit var iv_share_image: ImageView
    lateinit var iv_whatsup_share: ImageView
    lateinit var iv_email_share: ImageView
    lateinit var iv_facebook_share: ImageView
    lateinit var iv_instagram_share: ImageView
    var path: String = ""
    lateinit var mpagerAdapter: mPagerAdapter
    lateinit var imageList: MutableList<String>
    var mposition:Int = 0
    override fun init() {
        leftTv = findViewById(R.id.leftTv)
        titleTv = findViewById(R.id.titleTv)
        rightTv = findViewById(R.id.rightTv)
        countTv = findViewById(R.id.countTv)
        pager = findViewById(R.id.pager)
        iv_share_image = findViewById(R.id.iv_share_image)
        iv_whatsup_share = findViewById(R.id.iv_whatsup_share)
        iv_email_share = findViewById(R.id.iv_email_share)
        iv_facebook_share = findViewById(R.id.iv_facebook_share)
        iv_instagram_share = findViewById(R.id.iv_instagram_share)
        getImageList()
        if(imageList.size > 0){
            path = imageList[0]
        }
        initViewClick()
        if (imageList.size != 0){
            countTv.setText((mposition+1).toString()+"/"+imageList.size.toString())
        }else{
            countTv.setText("0/0")
        }
        leftTv.setText("back")
        rightTv.setText("delete")
        titleTv.setText("all")
        mpagerAdapter = mPagerAdapter(this)

        pager.adapter = mpagerAdapter
        pager.addOnPageChangeListener(this)
    }
    fun getImageList(){
        var tempList:MutableList<File> = FileUtils.listFilesInDir(GalleryConfigSet.path)
        imageList = mutableListOf()
        for (file:File in tempList){
            if (ImageUtils.isImage(file)){
                imageList.add(file.path)
            }
        }
    }
    override fun getLayoutId(): Int {
       return R.layout.activity_share
    }

    private fun initViewClick() {
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            FileUtils.delete(path)
            imageList.clear()
            getImageList()
            mpagerAdapter.notifyDataSetChanged()
            if (imageList.size != 0){
                countTv.setText((mposition+1).toString()+"/"+imageList.size.toString())
            }else{
                countTv.setText("0/0")
            }
        }

//        iv_share_image.setOnClickListener {
//            setClick("com.facebook.katana")
//        }
        iv_whatsup_share.setOnClickListener {
            setClick("com.whatsapp")
        }
        iv_email_share.setOnClickListener {
            path = imageList[mposition]
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.fromFile(File(path))
                )

                intent.putExtra(
                    Intent.EXTRA_TEXT, """Make more pics with app link 
     https://play.google.com/store/apps/details?id=$packageName"""
                )
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(Intent.createChooser(intent, "Share Picture"))
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Mail app have not been installed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        iv_instagram_share.setOnClickListener {
            setClick("com.instagram.android")
        }
        iv_facebook_share.setOnClickListener {
            setClick("com.facebook.katana")
        }
    }

    fun setClick(packge:String){
        path = imageList[mposition]
        if (!AppUtils.isAppInstalled(packge)){
            AppStoreUtils.getAppStoreIntent(packge,true)?.apply {
                ActivityUtils.startActivity(this)
            }
        }else{
            IntentUtils.getShareImageIntent(path).setPackage(packge)?.apply {
                ActivityUtils.startActivity(this)
            }
        }
    }

   inner class mPagerAdapter(context: Context) : PagerAdapter() {
        var mcontext:Context = context
        var list:MutableList<ImageView> = mutableListOf()
        override fun getCount(): Int {
          return  imageList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
           return  view == `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var imageView:ImageView = ImageView(mcontext)
            list.add(imageView)
            imageView.setImageURI(imageList[position].toUri())
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(list[position])
        }

       override fun getItemPosition(`object`: Any): Int {
           return POSITION_NONE
       }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        this.mposition = position
        if (imageList.size != 0){
            countTv.setText((position+1).toString()+"/"+imageList.size.toString())
        }else{
            countTv.setText("0/0")
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}

