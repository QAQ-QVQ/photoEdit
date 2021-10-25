package com.net.photoedit.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.net.photoedit.BuildConfig
import com.net.photoedit.GalleryConfigSet
import com.net.photoedit.R
import com.net.photoedit.utils.BaseConfig
import com.net.photoedit.utils.shareUtils
import com.yancy.gallerypick.config.GalleryPick
import com.yancy.gallerypick.inter.IHandlerCallBack
import com.zinc.libpermission.utils.JPermissionUtil
import com.zinc.libpermission.callback.IPermission as IPermission


class MainActivity : BaseActivity() {

    override fun init() {
        initPermission()
        initView()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private fun initView() {

       var edit:ImageView = findViewById(R.id.edit)
       var myphoto:ImageView = findViewById(R.id.myphoto)
       var options:ImageView = findViewById(R.id.options)
       var share:ImageView = findViewById(R.id.share)
       var camera:ImageView = findViewById(R.id.camera)

        edit.setOnClickListener {
            BaseConfig.instance().staractivity(this,EditorActivity::class.java)
        }
        myphoto.setOnClickListener {
            BaseConfig.instance().staractivity(this,ShareActivity::class.java)
        }
        options.setOnClickListener {
            BaseConfig.instance().staractivity(this,AboutActivity::class.java)
        }
        share.setOnClickListener {
            share()
        }
        camera.setOnClickListener {
            takePhoto()
        }
        GalleryConfigSet.getConfig(true,iHandlerCallBack())
    }

    private fun takePhoto() {
        GalleryPick.getInstance().setGalleryConfig(GalleryConfigSet.galleryConfig).open(this);
    }

    fun share() {
        var shareMessage = "Photo Editor" + "" + "\n\nLet me recommend you this application\n\n"
        shareMessage =
                "${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareUtils.share(this, getString(R.string.app_name), shareMessage)
    }

    fun initPermission(){
        JPermissionUtil.requestAllPermission(this, permission(this))
    }

    class permission(context: Context):IPermission{
        var mcontext:Context = context

        override fun ganted() {

        }

        override fun denied(p0: Int, p1: MutableList<String>?) {

        }

        override fun canceled(p0: Int) {
            JPermissionUtil.requestAllPermission(mcontext, permission(mcontext))
            Toast.makeText(mcontext, "请在 设置-应用管理 中开启此应用的授权。", Toast.LENGTH_SHORT).show();
        }

    }

    class iHandlerCallBack: IHandlerCallBack {

        override fun onStart() {

        }

        override fun onSuccess(photoList: MutableList<String>?) {

        }

        override fun onCancel() {

        }

        override fun onFinish() {

        }

        override fun onError() {

        }

    }
}
