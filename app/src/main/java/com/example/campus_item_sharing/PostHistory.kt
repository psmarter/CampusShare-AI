package com.example.campus_item_sharing

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.campus_item_sharing.itemmodel.ItemAdapter
import com.example.campus_item_sharing.post.PostInfoActivity
import com.example.campus_item_sharing.retrofit.ItemDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostHistory : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var messageLayout: TextView
    private var itemList: MutableList<ItemDetails> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout // 添加 SwipeRefreshLayout
    private val gson = Gson() // 添加 Gson 实例

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        messageLayout = view.findViewById(R.id.message_layout)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout) // 初始化 SwipeRefreshLayout

        recyclerView.layoutManager = GridLayoutManager(context, 2) // 设置为两列
        messageLayout.text = "浏览历史"

        // 创建适配器并给 RecyclerView 设置适配器
        itemAdapter = ItemAdapter(requireContext(), itemList) { selectedItem ->
            // 启动 PostInfoActivity
            val intent = Intent(requireContext(), PostInfoActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = itemAdapter // 设置适配器

        fetchAllItems()

        // 设置下拉刷新监听器
        swipeRefreshLayout.setOnRefreshListener {
            fetchAllItems() // 刷新数据
        }


        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchAllItems() {
        swipeRefreshLayout.isRefreshing = true // 开始刷新动画
        val sharedPreferences = getSharedPreferences("MyPreferences")
        val existingItemsJson = sharedPreferences.getString("items", "[]") ?: "[]"
        val itemListType = object : TypeToken<MutableList<ItemDetails>>() {}.type
        val items: MutableList<ItemDetails> = gson.fromJson(existingItemsJson, itemListType)

        // 清空 itemList 并添加新获取到的项目
        itemList.clear()
        itemList.addAll(items)

        // 更新适配器数据
        itemAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false // 停止刷新动画

        if (itemList.isEmpty()) {
            showToast("没有历史记录")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // 获取 SharedPreferences
    private fun getSharedPreferences(name: String): SharedPreferences {
        return requireActivity().getSharedPreferences(name, Context.MODE_PRIVATE)
    }

}