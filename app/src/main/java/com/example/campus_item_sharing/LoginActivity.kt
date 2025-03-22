package com.example.campus_item_sharing


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campus_item_sharing.retrofit.LoginRequest
import com.example.campus_item_sharing.retrofit.RegisterRequest
import com.example.campus_item_sharing.retrofit.ResponseModel
import com.example.campus_item_sharing.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var studentNumberEdit: EditText    // 学号编辑框
    private lateinit var accountEdit: EditText      // 账号编辑框
    private lateinit var btnLogin: Button       // 登录或注册按钮
    private lateinit var passwordEdit: EditText     // 密码编辑框

    private var passwordHideOrShow = true       // 密码显示状态
    private var loginState = false      // 登录状态

    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPreferencesFile = "user_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        sharedPreferences = getSharedPreferences(sharedPreferencesFile, MODE_PRIVATE) // 获取共享偏好
    }

    private fun initView() {

        findViewById<TextView>(R.id.login_text_create_account).visibility = View.GONE
        findViewById<LinearLayout>(R.id.login_input_number_layout).visibility = View.GONE

        studentNumberEdit = findViewById(R.id.login_input_input_number)
        accountEdit = findViewById(R.id.login_input_create_account)
        passwordEdit = findViewById(R.id.login_input_create_password)
        btnLogin = findViewById(R.id.login_btn)

        btnLogin.setOnClickListener(clickListenerLoginOrRegister)       // 登录或注册按钮
        findViewById<ImageView>(R.id.btn_login_back).setOnClickListener { finish() }        // 返回
        findViewById<View>(R.id.password_display_icon).setOnClickListener(clickListenerHideOrShow)      // 更改密码显示状态
        findViewById<View>(R.id.exchange_text_login_or_sign).setOnClickListener(clickListenerExchangeState)     // 右下角注册或登录文本
    }

    // 点击右下角注册或登录文本
    private val clickListenerExchangeState = View.OnClickListener { v: View? ->
            if (loginState) {
                findViewById<View>(R.id.login_text_create_account).visibility = View.GONE
                findViewById<View>(R.id.login_input_number_layout).visibility = View.GONE

                accountEdit.setHint(R.string.user_center_login_input_text)
                btnLogin.setText(R.string.sr_login)

                val textView = findViewById<TextView>(R.id.exchange_text_account)
                textView.setText(R.string.user_center_login_exchange_sign_text)
                val textViewBtn = findViewById<TextView>(R.id.exchange_text_login_or_sign)
                textViewBtn.setText(R.string.sr_sign_up)
                loginState = false
            } else {
                findViewById<View>(R.id.login_text_create_account).visibility = View.VISIBLE
                findViewById<View>(R.id.login_input_number_layout).visibility = View.VISIBLE

                accountEdit.setHint(R.string.user_center_login_create_text)
                btnLogin.setText(R.string.sr_sign_up)

                val textView = findViewById<TextView>(R.id.exchange_text_account)
                textView.setText(R.string.user_center_login_exchange_login_text)
                val textViewBtn = findViewById<TextView>(R.id.exchange_text_login_or_sign)
                textViewBtn.setText(R.string.sr_login)
                loginState = true
            }
        }

    // 点击密码显示或隐藏图标
    private val clickListenerHideOrShow = View.OnClickListener { v: View? ->
        val imageView = findViewById<ImageView>(R.id.password_display_icon)
        val pos: Int = passwordEdit.selectionStart
        if (passwordHideOrShow) {
            imageView.setImageResource(R.drawable.ic_icon_password_show)
            passwordEdit.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            imageView.setImageResource(R.drawable.ic_icon_password_hide)
            passwordEdit.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        passwordEdit.setSelection(pos)
        passwordHideOrShow = !passwordHideOrShow
    }

    // 点击登录或注册按钮
    private val clickListenerLoginOrRegister = View.OnClickListener clickListenerLoginOrRegister@{ v: View? ->
        if(!loginState){
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()

            if (account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show()
                return@clickListenerLoginOrRegister
            }

            login(account, password)
        } else {
            val studentNumber = studentNumberEdit.text.toString()
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            //val email = emailEdit.text.toString()

            if (studentNumber.isEmpty() || account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show()
                return@clickListenerLoginOrRegister
            }

            register(studentNumber, account, password)
        }

    }

    // 登录代码
    private fun login(account: String, password: String) {
        val request = LoginRequest(account, password)
        RetrofitClient.apiService.login(request).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.status == "success") {
                            // 显示登录成功的提示
                            Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()

                            // 保存用户数据和登录状态
                            saveUserData(responseBody)

                            // 准备结果 Intent
                            val intent = Intent()
                            intent.putExtra("login_status", true) // 设置登录状态
                            setResult(Activity.RESULT_OK, intent) // 设置结果
                            finish() // 结束登录 Activity
                        } else {
                            // 显示服务器返回的错误信息
                            Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // 处理响应体为空的情况
                        Toast.makeText(this@LoginActivity, "登录失败：响应为空", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 处理请求失败的情况
                    Toast.makeText(this@LoginActivity, "登录失败：${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                // 处理网络请求失败的情况
                Toast.makeText(this@LoginActivity, "网络错误：${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 注册代码
    private fun register(studentNumber: String, account: String, password: String, email: String = "111") {
        val request = RegisterRequest(studentNumber, account, password, email)
        RetrofitClient.apiService.register(request).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.status == "success") {
                            Toast.makeText(this@LoginActivity, "注册成功", Toast.LENGTH_SHORT).show()
                            // 返回到登录页面
                            loginState = true
                            clickListenerExchangeState.onClick(findViewById(R.id.exchange_text_login_or_sign))
                        } else {
                            Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "注册失败：响应为空", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "注册失败：${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "网络错误：${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 保存用户数据
    private fun saveUserData(userData: ResponseModel) {
        with(sharedPreferences.edit()) {
            putString("studentNumber", userData.userDetails?.studentNumber)
            putString("account", userData.userDetails?.account)
            putString("passwordHash", userData.userDetails?.passwordHash)
            putString("email", userData.userDetails?.email) // 这里假设有邮箱字段
            putBoolean("isLoggedIn", true) // 保存登录状态
            apply()
        }
    }

}