package com.example.campus_item_sharing.account

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campus_item_sharing.R
import com.example.campus_item_sharing.retrofit.RegisterRequest
import com.example.campus_item_sharing.retrofit.ResponseModel
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.example.campus_item_sharing.retrofit.UserDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalInfoActivity : AppCompatActivity() {

    private lateinit var textStudentNumber: TextView
    private lateinit var textAccountName: TextView
    private lateinit var editTextEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var backButton: ImageView // Back button

    private var initialEmail: String? = null // 存储初始邮箱

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_modify_info)

        // 初始化视图
        textStudentNumber = findViewById(R.id.text_student_number)
        textAccountName = findViewById(R.id.text_account_name)
        editTextEmail = findViewById(R.id.edit_text_email)
        btnSave = findViewById(R.id.btn_save)
        backButton = findViewById(R.id.user_message_home_back) // 初始化back button

        btnSave.isEnabled = false  // 默认不可用

        // 加载用户数据
        loadUserData()

        // 返回按钮点击事件
        backButton.setOnClickListener {
            finish() // 结束当前活动并返回
        }

        // 监听邮箱输入变化
        editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 用户修改了邮箱，检查是否与初始邮箱不同
                btnSave.isEnabled = s.toString() != initialEmail
            }
        })

        // 保存按钮点击事件
        btnSave.setOnClickListener {
            updateUserInformation() // 处理邮箱保存逻辑
        }
    }

    // 获取 SharedPreferences
    private fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
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

    private fun loadUserData() {
        // 读取用户数据并设置到对应的视图
        val userData = getUserData()
        textStudentNumber.text = userData.studentNumber
        textAccountName.text = userData.account
        initialEmail = userData.email // 存储初始邮箱
        editTextEmail.setText(initialEmail) // 设置当前邮箱
    }

    private fun updateUserInformation() {
        val newEmail = editTextEmail.text.toString()
        val studentNumber = textStudentNumber.text.toString()
        val accountName = textAccountName.text.toString()

        // 创建更新请求
        val userUpdateRequest = RegisterRequest(
            studentNumber = studentNumber,
            account = accountName,
            passwordHash = getUserData().passwordHash, // 原密码
            email = newEmail
        )

        RetrofitClient.apiService.updateUser(userUpdateRequest).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.status == "success") {
                        //showToast("用户信息更新成功")
                        saveEmail() // 仅在更新成功后保存新邮箱
                    } else {
                        showToast("更新失败: ${responseBody?.message ?: "未知错误"}")
                    }
                } else {
                    showToast("更新失败: ${response.errorBody()?.string() ?: "无法获取错误信息"}")
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                showToast("网络错误: ${t.message}")
            }
        })
    }

    private fun saveEmail() {
        val newEmail = editTextEmail.text.toString()

        // 保存到 SharedPreferences
        val editor = getSharedPreferences().edit()
        editor.putString("email", newEmail)
        editor.apply() // 提交修改
        showToast("邮箱已更改为: $newEmail")
        btnSave.isEnabled = false // 存储后将按钮设置为不可用
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
