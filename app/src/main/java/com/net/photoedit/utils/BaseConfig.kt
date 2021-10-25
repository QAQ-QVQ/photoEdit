package com.net.photoedit.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.blankj.utilcode.util.ObjectUtils

class BaseConfig {

    companion object {
        var bitmap:Bitmap?=null
        private var instance: BaseConfig? = null
            get() {
                field ?: run {
                    field = BaseConfig()
                }
                return field
            }

        @Synchronized
        fun instance(): BaseConfig {
            return instance!!
        }
    }

     fun staractivity(context: Context, clazz: Class<*>){
        context.startActivity(Intent(context, clazz))
    }


}