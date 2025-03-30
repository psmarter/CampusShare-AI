package com.example.campus_item_sharing


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.campus_item_sharing.itemmodel.ItemAdapter
import com.example.campus_item_sharing.post.PostActivity
import com.example.campus_item_sharing.post.PostInfoActivity
import com.example.campus_item_sharing.retrofit.ItemDetails
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private var itemList: MutableList<ItemDetails> = mutableListOf()
    private var itemList1: MutableList<ItemDetails> = mutableListOf() // 使用 mutableListOf
    private lateinit var categorySpinner: Spinner
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout // 添加 SwipeRefreshLayout
    private var currentCategory: String = "全部" // 当前选中的分类
    private var isDataLoaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 初始化 FloatingActionButton，并设置点击事件
        val fab: FloatingActionButton = view.findViewById(R.id.fab) // 获取FAB
        categorySpinner = view.findViewById(R.id.top_school_selector)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout) // 初始化 SwipeRefreshLayout

        // 创建一个字符串数组
        val categories = arrayOf("电子科技大学", "清华大学", "北京大学")

        // 创建 ArrayAdapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // 设置下拉列表样式
        categorySpinner.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 设置为两列

        // 创建适配器并给 RecyclerView 设置适配器
        //itemAdapter = ItemAdapter(requireContext(), itemList)
        itemAdapter = ItemAdapter(requireContext(), itemList) { selectedItem ->
            // 启动 PostInfoActivity
            val intent = Intent(requireContext(), PostInfoActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = itemAdapter // 设置适配器

        // 获取所有物品数据
        //fetchAllItems()

        // 只在第一次创建视图时加载数据
        if (!isDataLoaded) {
            fetchAllItems()
            isDataLoaded = true // 更新状态以防再次加载
        } else {
            selectAllCategory() // 如果数据已经加载，直接选择全部类别
        }

        // 设置下拉刷新监听器
        swipeRefreshLayout.setOnRefreshListener {
            fetchAllItems() // 刷新数据
        }

        fab.setOnClickListener {
            // 创建 Intent 启动 PostActivity
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent) // 启动活动
        }

        // 点击事件监听器为各个分类
        setupCategoryClickListeners(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 单独调用一次“全部”
        selectAllCategory()
    }

    private fun setupCategoryClickListeners(view: View) {
        val viewAll = view.findViewById<LinearLayout>(R.id.view_all)
        val viewBook = view.findViewById<LinearLayout>(R.id.view_book)
        val viewDigital = view.findViewById<LinearLayout>(R.id.view_digital)
        val viewSport = view.findViewById<LinearLayout>(R.id.view_sport)

        viewAll.setOnClickListener {
            selectAllCategory()
        }

        viewBook.setOnClickListener {
            currentCategory = "书籍"
            filterItems() // 过滤书籍
            updateCategoryColors(viewBook)
        }

        viewDigital.setOnClickListener {
            currentCategory = "电子设备"
            filterItems() // 过滤电子设备
            updateCategoryColors(viewDigital)
        }

        viewSport.setOnClickListener {
            currentCategory = "体育"
            filterItems() // 过滤体育
            updateCategoryColors(viewSport)
        }
    }

    private fun selectAllCategory() {
        currentCategory = "全部"
        filterItems() // 过滤全部
        updateCategoryColors(view?.findViewById(R.id.view_all)!!) // 传递选中的视图
    }

    private fun filterItems() {
        // 根据当前的分类过滤项目
        val filteredItems = itemList.filter { item ->
            when (currentCategory) {
                "全部" -> true
                "书籍" -> item.tags.contains("书籍", ignoreCase = true)
                "电子设备" -> item.tags.contains("电子设备", ignoreCase = true)
                "体育" -> item.tags.contains("体育", ignoreCase = true)
                else -> false
            }
        }
        itemAdapter.updateData(filteredItems) // 更新适配器数据
    }

    private fun updateCategoryColors(selectedCategory: LinearLayout) {
        // 重置所有类别的背景颜色
        listOf(R.id.view_all, R.id.view_book, R.id.view_digital, R.id.view_sport).forEach { id ->
            view?.findViewById<LinearLayout>(id)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), android.R.color.transparent)
            )
        }

        // 设置所选的类别的背景颜色
        selectedCategory.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_blue1)) // 选中时的背景颜色
    }

    private fun fetchAllItems() {
        swipeRefreshLayout.isRefreshing = true // 开始刷新动画
        RetrofitClient.apiService.getAllItems().enqueue(object : Callback<List<ItemDetails>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<ItemDetails>>, response: Response<List<ItemDetails>>) {
                swipeRefreshLayout.isRefreshing = false // 停止刷新动画

                if (response.isSuccessful) {
                    response.body()?.let { items ->
                        itemList1.clear()
                        itemList1.addAll(items) // 添加所有返回的 ItemDetails

                        itemList = itemList1.map { it.deepCopy() }.toMutableList()
                        itemAdapter.notifyDataSetChanged() // 不使用 DiffUtil，直接更新 RecyclerView
                        filterItems()
                    } ?: showToast("无法获取物品数据：响应为空")
                } else {
                    showToast("无法获取物品数据：${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemDetails>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false // 停止刷新动画
                showToast("网络错误：${t.message}")
            }
        })
    }

    // 可以添加方法来动态添加或删除项目
    fun addItem(item: ItemDetails) {
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