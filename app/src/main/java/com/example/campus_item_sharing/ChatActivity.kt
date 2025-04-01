package com.example.campus_item_sharing

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_item_sharing.chatmodel.ChatAdapter
import com.example.campus_item_sharing.chatmodel.ChatMessage
import com.example.campus_item_sharing.retrofit.ResponseModel
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.example.campus_item_sharing.retrofit.UserDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    private lateinit var chatPartnerNameTextView: TextView
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var btnBack: ImageView

    // 模拟存储消息数据
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var receiverAccount: String  // 接收方账户
    private lateinit var senderAccount: String  // 发送方账户

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatPartnerNameTextView = findViewById(R.id.chat_partner_name)
        chatRecyclerView = findViewById(R.id.chat_recycler_view)
        chatInput = findViewById(R.id.chat_input)
        sendButton = findViewById(R.id.send_button)
        btnBack = findViewById(R.id.user_message_home_back)

        // 从 Intent 中获取聊天对象的账户名
        val partnerName = intent.getStringExtra("partnerName") ?: "Chat Partner"
        chatPartnerNameTextView.text = partnerName
        receiverAccount = partnerName
        senderAccount = getUserData().account

        // 初始化 RecyclerView
        chatAdapter = ChatAdapter(messages)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        // 加载聊天记录
        loadChatHistory()

        // 返回按钮点击事件
        btnBack.setOnClickListener {
            finish() // 结束当前活动并返回
        }

        sendButton.setOnClickListener {
            val text = chatInput.text.toString().trim()
            if (text.isNotEmpty()) {
                sendChatMessage(text)
            }
        }
    }

    /**
     * 调用后端 API 发送消息
     */
    private fun sendChatMessage(messageText: String) {
        RetrofitClient.apiService.sendMessage(senderAccount, receiverAccount, messageText)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        val newMessage = ChatMessage(messageText, true)
                        messages.add(newMessage)
                        chatAdapter.updateData(messages)
                        chatInput.text.clear()
                        chatRecyclerView.scrollToPosition(messages.size - 1)
                    } else {
                        Toast.makeText(this@ChatActivity, "消息发送失败", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@ChatActivity, "网络错误：" + t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    /**
     * 加载聊天记录
     */
    private fun loadChatHistory() {
        RetrofitClient.apiService.getChatHistory(senderAccount, receiverAccount)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        // 假设返回 data 为 List<Map<String, Object>>
                        val chatHistory = response.body()?.data as? List<*>
                        if (chatHistory != null) {
                            messages.clear()  // 先清空现有记录
                            chatHistory.forEach { item ->
                                val messageMap = item as? Map<*, *>
                                val text = messageMap?.get("messageText") as? String ?: ""
                                // 获取发送者账户，判断该消息是否由当前用户发送
                                val senderObj = messageMap?.get("sender") as? Map<*, *>
                                val senderAcc = senderObj?.get("account") as? String ?: ""
                                val isSent = (senderAcc == senderAccount)
                                messages.add(ChatMessage(text, isSent))
                            }
                            chatAdapter.updateData(messages)
                            chatRecyclerView.scrollToPosition(messages.size - 1)
                        }
                    } else {
                        Toast.makeText(this@ChatActivity, "获取聊天记录失败：" + response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@ChatActivity, "网络错误：" + t.message, Toast.LENGTH_SHORT).show()
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
        return getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }
}