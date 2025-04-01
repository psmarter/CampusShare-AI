package com.example.campus_item_sharing


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.campus_item_sharing.friendmodel.FriendAdapter
import com.example.campus_item_sharing.friendmodel.FriendItem
import com.example.campus_item_sharing.retrofit.ResponseModel
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.example.campus_item_sharing.retrofit.UserDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessagesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val gson = Gson()

    // 好友列表数据
    private var friendList: MutableList<FriendItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        friendAdapter = FriendAdapter(friendList) { friendItem ->
            // 点击好友项时进入聊天界面，传递好友账户名
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("partnerName", friendItem.account)
            startActivity(intent)
        }
        recyclerView.adapter = friendAdapter

        // 下拉刷新监听
        swipeRefreshLayout.setOnRefreshListener {
            fetchFriendList()
        }

        // 调用接口获取好友列表
        fetchFriendList()

        return view
    }

    /**
     * 调用 Retrofit 接口获取好友列表，并更新 RecyclerView
     */
    private fun fetchFriendList() {
        val account = getUserData().account
        if (account.isEmpty() || account == "未设置") {
            Toast.makeText(requireContext(), "当前用户未登录", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
            return
        }
        RetrofitClient.apiService.getFriends(account).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success" && body.data != null) {
                        // 将 data 转换为 List<FriendItem>
                        val friendListType = object : TypeToken<List<FriendItem>>() {}.type
                        val friends: List<FriendItem> = gson.fromJson(gson.toJson(body.data), friendListType)
                        friendList.clear()
                        friendList.addAll(friends)
                        friendAdapter.updateData(friendList)
                    } else {
                        Toast.makeText(requireContext(), body?.message ?: "获取好友列表失败", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "获取好友列表失败：" + response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(requireContext(), "网络错误：" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
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

    // 获取 SharedPreferences
    private fun getSharedPreferences(): SharedPreferences {
        return requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }
}