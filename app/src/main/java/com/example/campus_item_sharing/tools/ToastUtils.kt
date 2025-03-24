package com.example.campus_item_sharing.tools

import android.content.Context
import android.widget.Toast

object ToastUtils {
    // 显示 Toast 消息
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}