package com.example.campus_item_sharing.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.campus_item_sharing.R
import com.example.campus_item_sharing.retrofit.RegisterRequest
import com.example.campus_item_sharing.retrofit.ResponseModel
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.example.campus_item_sharing.retrofit.UserDetails
import com.example.campus_item_sharing.tools.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordEditActivity : AppCompatActivity() {

    private lateinit var editTextOriginPassword: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var editTextNewPasswordAgain: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_user_center_edit_psw) // 布局文件名

        // 初始化视图
        editTextOriginPassword = findViewById(R.id.edit_psw_origin)
        editTextNewPassword = findViewById(R.id.edit_psw_new)
        editTextNewPasswordAgain = findViewById(R.id.edit_psw_new_again)
        btnSave = findViewById(R.id.btn_user_center_psw__edit)
        btnBack = findViewById(R.id.btn_login_back)

        // 按钮点击事件
        btnSave.setOnClickListener {
            if (validateInput()) {
                changePassword()
            }
        }

        // 返回按钮点击事件
        btnBack.setOnClickListener {
            finish() // 结束当前活动并返回
        }
    }

    // 验证用户输入
    private fun validateInput(): Boolean {
        val originPassword = editTextOriginPassword.text.toString().trim()
        val newPassword = editTextNewPassword.text.toString().trim()
        val newPasswordAgain = editTextNewPasswordAgain.text.toString().trim()

        return when {
            originPassword.isEmpty() -> {
                ToastUtils.showToast(this, "请输入原始密码") // 使用全局 Toast 方法
                false
            }
            newPassword.isEmpty() -> {
                ToastUtils.showToast(this, "请输入新密码")
                false
            }
            newPasswordAgain.isEmpty() -> {
                ToastUtils.showToast(this, "请再次输入新密码")
                false
            }
            newPassword != newPasswordAgain -> {
                ToastUtils.showToast(this, "密碼不一致")
                false
            }
            else -> true
        }
    }

    // 发送更改密码请求
    private fun changePassword() {
        val originPassword = editTextOriginPassword.text.toString().trim() // 获取原密码
        val newPassword = editTextNewPassword.text.toString().trim() // 获取新密码

        // 获取现有用户信息（包括学生号、账户名和邮件等）
        val userData = getUserData()

        // 创建更新请求，只更新密码
        val userUpdateRequest = RegisterRequest(
            studentNumber = userData.studentNumber, // 保留原学生号
            account = userData.account,               // 保留原账户名
            passwordHash = newPassword,               // 使用新密码
            email = userData.email                    // 保留原电子邮件
        )

        // 调用 API 更新用户信息
        RetrofitClient.apiService.updateUser(userUpdateRequest).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.status == "success") {
                        ToastUtils.showToast(this@PasswordEditActivity, "密码修改成功")

                        val intent = Intent()
                        intent.putExtra("logout", true) // 设置退出登录标志
                        setResult(Activity.RESULT_OK, intent) // 返回结果到上一个 Activity

                        finish() // 完成后返回上一个活动
                    } else {
                        ToastUtils.showToast(this@PasswordEditActivity, "修改失败: ${responseBody?.message ?: "未知错误"}")
                    }
                } else {
                    ToastUtils.showToast(this@PasswordEditActivity, "修改失败: ${response.errorBody()?.string() ?: "无法获取错误信息"}")
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                ToastUtils.showToast(this@PasswordEditActivity, "网络错误: ${t.message}")
            }
        })
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
}