package com.net.photoedit.utils

import android.content.Context
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.ItemListener
import com.afollestad.materialdialogs.list.listItems

object DialogUtils {
    fun showDialog(
        context: Context,
        items: List<String>,
        listener: ItemListener
    ): MaterialDialog {
        return MaterialDialog(context, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            listItems(items = items, selection = listener)
        }
    }

}