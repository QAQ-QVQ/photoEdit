package com.net.photoedit.utils

import android.content.Context
import android.content.Intent

object shareUtils {
    fun share(context: Context, extra:String, shareMessage:String){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, extra)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(Intent.createChooser(shareIntent, "choose one"))
    }
}