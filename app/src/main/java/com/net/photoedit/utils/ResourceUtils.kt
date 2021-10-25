package com.net.photoedit.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import com.net.photoedit.R
import net.entity.ResourceEntity

object ResourceUtils {

    fun getResourceByFolder(
        context: Context,
        clazz: Class<*>,
        folderName: String,
        filter: String
    ): ArrayList<ResourceEntity> {
        val result = ArrayList<ResourceEntity>()
        for (field in clazz.fields) {
            val name = field.name
            if (name.startsWith(filter)) {
                val id = context.resources.getIdentifier(name, folderName, context.packageName)
                val entity = ResourceEntity(name, id)
                result.add(entity)
            }
        }
        return result
    }

    private fun getEditorItemRes(context: Context): ArrayList<String> {
        val result = ArrayList<String>()
        result.add(res2String(context, R.mipmap.pip))
        result.add(res2String(context, R.mipmap.color))
        result.add(res2String(context, R.mipmap.bokeh))
        result.add(res2String(context, R.mipmap.pixel))
        result.add(res2String(context, R.mipmap.shattering))
        return result
    }

    fun res2String(context: Context, id: Int): String {
        val r = context.resources
        val uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + r.getResourcePackageName(id) + "/"
                    + r.getResourceTypeName(id) + "/"
                    + r.getResourceEntryName(id)
        )
        return uri.toString()
    }
    fun getResource(context: Context): ArrayList<String> {
        return getEditorItemRes(context)
    }

    fun getScreenSize(activity: Activity):IntArray{
        val result = IntArray(2)
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        result[0] = height
        result[1] = width
        return result
    }

}