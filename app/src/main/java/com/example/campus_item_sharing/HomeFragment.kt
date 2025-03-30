package com.example.campus_item_sharing


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_item_sharing.itemmodel.ItemAdapter
import com.example.campus_item_sharing.itemmodel.ItemObject
import com.example.campus_item_sharing.post.PostActivity
import com.example.campus_item_sharing.retrofit.ItemDetails
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private var itemList: MutableList<ItemObject> = mutableListOf() // 使用 mutableListOf
    private lateinit var categorySpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 初始化 FloatingActionButton，并设置点击事件
        val fab: FloatingActionButton = view.findViewById(R.id.fab) // 获取FAB
        categorySpinner = view.findViewById(R.id.top_school_selector)
        recyclerView = view.findViewById(R.id.recyclerView)

        // 创建一个字符串数组
        val categories = arrayOf("电子科技大学", "清华大学", "北京大学")

        // 创建 ArrayAdapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // 设置下拉列表样式

        // 将适配器设置给 Spinner
        categorySpinner.adapter = adapter

        // 修改为 GridLayoutManager，设置列数为 2
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 设置为两列

        // 获取所有物品数据
        fetchAllItems()

        // 创建适配器并给 RecyclerView 设置适配器
        itemAdapter = ItemAdapter(requireContext(), itemList)
        recyclerView.adapter = itemAdapter // 设置适配器


        fab.setOnClickListener {
            // 创建 Intent 启动 PostActivity
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent) // 启动活动
        }

        return view
    }

    private fun fetchAllItems() {
        RetrofitClient.apiService.getAllItems().enqueue(object : Callback<List<ItemDetails>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<ItemDetails>>, response: Response<List<ItemDetails>>) {
                if (response.isSuccessful) {
                    response.body()?.let { items ->
                        itemList.clear() // 清空旧的数据
                        items.forEach { itemDetails ->
                            itemList.add(
                                ItemObject(
                                    itemDetails.description, // 使用物品类型作为展示数据
                                    itemDetails.accountName,
                                    itemDetails.imageData, // 将图像数据作为 Base64 字符串存储在 ItemObject 中
                                    //itemDetails.description
                                )
                            )
                        }
                        itemAdapter.notifyDataSetChanged() // 更新 RecyclerView
                    } ?: showToast("无法获取物品数据：响应为空")
                } else {
                    showToast("无法获取物品数据：${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemDetails>>, t: Throwable) {
                showToast("网络错误：${t.message}")
            }
        })
    }

    // 可以添加方法来动态添加或删除项目
    fun addItem(item: ItemObject) {
        itemList.add(item)
        itemAdapter.notifyItemInserted(itemList.size - 1) // 通知适配器数据已更改
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < itemList.size) {
            itemList.removeAt(position) // 从列表中删除项
            itemAdapter.notifyItemRemoved(position) // 通知适配器数据已更改
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}