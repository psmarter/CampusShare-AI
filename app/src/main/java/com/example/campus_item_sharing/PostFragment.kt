package com.example.campus_item_sharing


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.campus_item_sharing.itemmodel.ItemAdapter
import com.example.campus_item_sharing.post.PostInfoActivity
import com.example.campus_item_sharing.retrofit.ItemDetails
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.example.campus_item_sharing.retrofit.UserDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private var itemList: MutableList<ItemDetails> = mutableListOf()
    private var itemList1: MutableList<ItemDetails> = mutableListOf() // 使用 mutableListOf
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout // 添加 SwipeRefreshLayout
    private var isDataLoaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)


        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout) // 初始化 SwipeRefreshLayout

        recyclerView.layoutManager = GridLayoutManager(context, 2) // 设置为两列

        // 创建适配器并给 RecyclerView 设置适配器
//        itemAdapter = ItemAdapter(requireContext(), itemList)
        itemAdapter = ItemAdapter(requireContext(), itemList) { selectedItem ->
            // 启动 PostInfoActivity
            val intent = Intent(requireContext(), PostInfoActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = itemAdapter // 设置适配器


        // 只在第一次创建视图时加载数据
        if (!isDataLoaded) {
            fetchAllItems()
            isDataLoaded = true // 更新状态以防再次加载
        } else {

        }

        // 设置下拉刷新监听器
        swipeRefreshLayout.setOnRefreshListener {
            fetchAllItems() // 刷新数据
        }


        return view
    }


    private fun filterItems(accountName: String) {
        // 仅根据账户名过滤项目
        val filteredItems = itemList.filter { item ->
            item.accountName.equals(accountName, ignoreCase = true) // 检查账户名
        }
        itemAdapter.updateData(filteredItems) // 更新适配器数据
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


                        filterItems(getUserData().account)
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // 获取 SharedPreferences
    private fun getSharedPreferences(): SharedPreferences {
        return requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    // 读取用户信息的方法
    private fun getUserData(): UserDetails {
        val sharedPreferences = getSharedPreferences()
        val studentNumber = sharedPreferences.getString("studentNumber", null)
        val account = sharedPreferences.getString("account", null)
        val passwordHash = sharedPreferences.getString("passwordHash", null)
        val email = sharedPreferences.getString("email", null)

        // 处理空值的情况
        return UserDetails(
            studentNumber ?: "未设置",
            account ?: "未设置",
            passwordHash ?: "未设置",
            email ?: "未设置"
        )
    }



}