package com.net.photoedit

import android.os.Environment
import com.yancy.gallerypick.config.GalleryConfig
import com.yancy.gallerypick.inter.IHandlerCallBack

object  GalleryConfigSet {
    lateinit var galleryConfig:GalleryConfig
       lateinit var path:String
        fun getConfig(config:Boolean,iHandlerCallBack: IHandlerCallBack):GalleryConfig{
          galleryConfig =  GalleryConfig.Builder()
                .imageLoader(GlideImageLoader()) // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack) // 监听接口（必填）
                .provider("com.net.photoedit.fileprovider") // provider (必填)
                .isOpenCamera(config)
                .crop(true)
                .build()
            path = Environment.getExternalStorageDirectory().getPath()+GalleryConfigSet.galleryConfig.filePath
        return galleryConfig
    }
}