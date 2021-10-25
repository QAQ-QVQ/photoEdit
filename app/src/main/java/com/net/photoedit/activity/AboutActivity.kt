package com.net.photoedit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import com.net.photoedit.R

class AboutActivity : BaseActivity() {
    lateinit var leftTv: TextView
    lateinit var titleTv: TextView
    lateinit var rightTv: TextView
    lateinit var web: WebView


    override fun init() {
        leftTv = findViewById(R.id.leftTv)
        titleTv = findViewById(R.id.titleTv)
        rightTv = findViewById(R.id.rightTv)
        web = findViewById(R.id.web)
        leftTv.text = "back"
        titleTv.text = "privacy policy"
        rightTv.visibility = View.INVISIBLE
        leftTv.setOnClickListener {
            finish()
        }
        web.loadUrl("file:///android_asset/privacyPolicy.html")
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }
}