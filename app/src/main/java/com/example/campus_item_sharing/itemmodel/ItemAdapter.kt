package com.example.campus_item_sharing.itemmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_item_sharing.R

class ItemAdapter(private val context: Context, private val itemList: List<ItemObject>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemAccountName: TextView = view.findViewById(R.id.itemAccountName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_object, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemName.text = item.name
        holder.itemAccountName.text = item.accountName

        // 将 Base64 字符串解码为 Bitmap
        if (item.imageData.isNotEmpty()) {
            val bitmap = decodeBase64ToBitmap(item.imageData)
            if (bitmap != null) {
                holder.itemImage.setImageBitmap(bitmap) // 将 Bitmap 设置到 ImageView
            } else {
                holder.itemImage.setImageResource(R.drawable.ic_avatar) // 使用默认图片
            }
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_avatar) // 使用默认图片
        }
    }

    override fun getItemCount() = itemList.size

    // 将 Base64 字符串解码为 Bitmap 的辅助方法
    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}