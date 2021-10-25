package com.net.photoedit.activity

import android.util.Log
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.BusUtils
import com.net.photoedit.R



class TextActivity:BaseActivity() {
    lateinit var leftTv: TextView
    lateinit var titleTv: TextView
    lateinit var rightTv: TextView
    lateinit var editText: TextView
    override fun getLayoutId(): Int {
        return R.layout.activity_text
    }

    override fun init() {
        leftTv = findViewById(R.id.leftTv)
        titleTv = findViewById(R.id.titleTv)
        rightTv = findViewById(R.id.rightTv)
        editText = findViewById(R.id.editText)
        leftTv.visibility = View.INVISIBLE
        titleTv.visibility = View.INVISIBLE
        rightTv.text = "ok"
        rightTv.setOnClickListener {
            BusUtils.post("text",editText.text.toString())
            Log.i("ww", "init: "+BusUtils.toString_())
            finish()
        }
    }
}