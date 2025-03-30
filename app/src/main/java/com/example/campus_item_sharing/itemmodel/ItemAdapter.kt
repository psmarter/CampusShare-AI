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
import com.example.campus_item_sharing.retrofit.ItemDetails

class ItemAdapter(
    private val context: Context,
    private val itemList: MutableList<ItemDetails>,
    private val onItemClick: (ItemDetails) -> Unit // 添加项点击事件的回调
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    //private var selectedPosition = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemAccountName: TextView = view.findViewById(R.id.itemAccountName)

        init {
            view.setOnClickListener {
                // Handle item click here (to change color, etc.)
                // Example: changeBackgroundColor(view)
                //view.setBackgroundColor(context.getColor(R.color.light_blue1)) // Add highlight color
                //selectedPosition = adapterPosition // 更新被选中的位置
                //notifyDataSetChanged()

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 获取点击的项目
                    val clickedItem = itemList[position]

                    // 将 ItemDetails 保存到 SharedPreferences
                    saveItemDetailsToSharedPreferences(clickedItem)

                    // 调用回调，处理点击事件
                    onItemClick(clickedItem)
                }
            }
        }
    }

    // 将 ItemDetails 保存到 SharedPreferences
    private fun saveItemDetailsToSharedPreferences(itemDetails: ItemDetails) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 保存所有字段
        editor.putString("description", itemDetails.description)
        editor.putString("accountName", itemDetails.accountName)
        editor.putString("itemType", itemDetails.itemType)  // 物品类型
        editor.putString("price", itemDetails.price.toString()) // 将价格转换为字符串保存
        editor.putString("contactName", itemDetails.contactName) // 联系人姓名
        editor.putString("contactNumber", itemDetails.contactNumber) // 联系人号码
        editor.putString("tags", itemDetails.tags)  // 保持标签原格式，但可以考虑将其保存为 Set 需自行适配
        editor.putString("imageData", itemDetails.imageData) // 原始图像数据
        editor.putString("imageUniqueId", itemDetails.imageUniqueId) // 物品唯一ID

        editor.apply() // 提交保存
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_object, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemName.text = item.description
        holder.itemAccountName.text = item.accountName

        // 设置背景颜色，根据 selectedPosition 来区分是否高亮
//        holder.itemView.setBackgroundColor(if (position == selectedPosition) {
//            context.getColor(R.color.light_blue1) // 选中状态
//        } else {
//            context.getColor(android.R.color.transparent) // 默认状态
//        })

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

    // 更新适配器数据的方法
    fun updateData(newList: List<ItemDetails>) {
        itemList.clear() // 清空当前列表
        itemList.addAll(newList) // 添加新的数据
        notifyDataSetChanged() // 通知适配器数据已更改
    }
}