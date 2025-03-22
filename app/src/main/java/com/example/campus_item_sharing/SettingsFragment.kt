package com.example.campus_item_sharing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.campus_item_sharing.retrofit.UserDetails

class SettingsFragment : Fragment() {

    private val sharedPreferencesFile = "user_avatar"


    private lateinit var loginResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageResultLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化 ActivityResultLauncher
        loginResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                updateLoginStatus(view) // 更新用户状态
            }
        }

        // 初始化用于选择图片的 ActivityResultLauncher
        imageResultLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                setAvatarUri(it) // 直接设置头像 URI
            }
        }


        // 检查登录状态并更新显示
        updateLoginStatus(view)

        // 点击头像
        view.findViewById<ImageView>(R.id.iv_avatar).setOnClickListener {
            checkLoginAndProceed {
                showAvatarSelectionDialog(view)
            }
        }

        // 点击未登录状态
        view.findViewById<TextView>(R.id.tv_user_status).setOnClickListener {
            checkLoginAndProceed {
                navigateToPersonalInfo()
            }
        }

        // 点击个人信息
        view.findViewById<LinearLayout>(R.id.ll_person_info).setOnClickListener {
            checkLoginAndProceed {
                navigateToPersonalInfo()
            }
        }

        // 点击我的发表
        view.findViewById<LinearLayout>(R.id.ll_my_publish).setOnClickListener {
            checkLoginAndProceed {
                navigateToMyPublish()
            }
        }

        // 点击浏览历史
        view.findViewById<LinearLayout>(R.id.ll_browsing_history).setOnClickListener {
            checkLoginAndProceed {
                navigateToBrowsingHistory()
            }
        }

        // 点击修改密码
        view.findViewById<LinearLayout>(R.id.ll_change_password).setOnClickListener {
            checkLoginAndProceed {
                navigateToChangePassword()
            }
        }

        // 点击退出登录
        view.findViewById<LinearLayout>(R.id.ll_log_out).setOnClickListener {
            if (isLoggedIn()) {
                logOut(view)
            } else {
                showToast("你还未登录，无法退出登录")
            }
        }

        // 点击注销账号
        view.findViewById<LinearLayout>(R.id.ll_deregister_account).setOnClickListener {
            if (isLoggedIn()) {
                showDeregisterConfirmationDialog()
            } else {
                showToast("你还未登录，无法注销账号")
            }
        }
    }

    // 检查是否已登录
    private fun isLoggedIn(): Boolean {
        val token = getSharedPreferences().getBoolean("isLoggedIn", false)
        return token
    }

    // 检查登录状态并执行操作
    private fun checkLoginAndProceed(action: () -> Unit) {
        if (isLoggedIn()) {
            action()
        } else {
            navigateToLogin()
        }
    }

    // 更新登录状态显示
    private fun updateLoginStatus(view: View) {
        val userStatusTextView = view.findViewById<TextView>(R.id.tv_user_status)
        val imageView = view.findViewById<ImageView>(R.id.iv_avatar)

        if (isLoggedIn()) {
            userStatusTextView.text = getUserData().account // 显示账户名

            // 尝试加载之前保存的头像 URI
            val avatarUri = getSharedPreferencesAvatar().getString("avatar_${getUserData().account}", null)
            if (avatarUri != null) {
                imageView.setImageURI(Uri.parse(avatarUri))
            } else {
                // 如果没有 URI，尝试加载保存的资源 ID
                val resId = getSharedPreferencesAvatar().getInt("avatar_res_id_${getUserData().account}", -1)
                if (resId != -1) {
                    imageView.setImageResource(resId) // 设定头像资源
                } else {
                    imageView.setImageResource(R.drawable.ic_avatar_no_image) // 默认头像
                }
            }
        } else {
            userStatusTextView.text = "未登录" // 用户未登录，显示未登录状态
            imageView.setImageResource(R.drawable.ic_avatar_no_image) // 设置无头像
        }
    }

    // 跳转到登录界面
    private fun navigateToLogin() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        loginResultLauncher.launch(intent)
    }

    // 跳转到个人信息页面
    private fun navigateToPersonalInfo() {
        showToast("跳转到个人信息页面")
        // 实际开发中可以替换为跳转到具体的 Activity 或 Fragment
    }

    // 跳转到我的发表页面
    private fun navigateToMyPublish() {
        showToast("跳转到我的发表页面")
    }

    // 跳转到浏览历史页面
    private fun navigateToBrowsingHistory() {
        showToast("跳转到浏览历史页面")
    }

    // 跳转到修改密码页面
    private fun navigateToChangePassword() {
        showToast("跳转到修改密码页面")
    }

    // 执行退出登录逻辑
    private fun logOut(view: View) {
        getSharedPreferences().edit().clear().apply()
        updateLoginStatus(view)
        showToast("已退出登录")
        navigateToLogin() // 退出登录后跳转到登录界面
    }

    // 显示注销账号确认对话框
    private fun showDeregisterConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("确认注销账号")
            .setMessage("注销账号将清空所有数据，是否继续？")
            .setPositiveButton("确认") { _, _ ->
                deregisterAccount()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    // 执行注销账号逻辑
    private fun deregisterAccount() {
        // 示例：注销账号逻辑
        getSharedPreferences().edit().clear().apply()
        getSharedPreferencesAvatar().edit().clear().apply()

        // 执行删除请求
        showToast("账号已注销")
        navigateToLogin() // 注销后跳转到登录界面
    }

    // 获取 SharedPreferences
    private fun getSharedPreferences(): SharedPreferences {
        return requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    private fun getSharedPreferencesAvatar(): SharedPreferences {
        return requireActivity().getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)
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

    // 显示提示信息
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // 显示头像选择对话框
    private fun showAvatarSelectionDialog(view: View) {
        val avatars = arrayOf("小丑头像", "头像男", "头像女", "从图库选择")
        AlertDialog.Builder(requireContext())
            .setTitle("选择头像")
            .setItems(avatars) { dialog, which ->
                when (which) {
                    0 -> setAvatar(R.drawable.ic_avatar) // 设置头像1
                    1 -> setAvatar(R.drawable.ic_avatar_boy) // 设置头像2
                    2 -> setAvatar(R.drawable.ic_avatar_girl) // 设置头像3
                    3 -> openGalleryForAvatar() // 从图库选择头像
                }
            }
            .show()
    }

    // 从图库选择头像
    private fun openGalleryForAvatar() {
        imageResultLauncher.launch("image/*") // 使用新的 launcher
    }

    // 更新头像的方法
    private fun setAvatar(resId: Int) {
        view?.findViewById<ImageView>(R.id.iv_avatar)?.setImageResource(resId)

        // 移除现有头像 URI，如果有的话
        getSharedPreferencesAvatar().edit().remove("avatar_${getUserData().account}").apply()

        // 使用资源 ID 保存头像
        getSharedPreferencesAvatar().edit().putInt("avatar_res_id_${getUserData().account}", resId).apply()
    }

    // 从图库选择后设置头像 URI
    private fun setAvatarUri(uri: Uri) {
        // 设置头像显示
        view?.findViewById<ImageView>(R.id.iv_avatar)?.setImageURI(uri)

        getSharedPreferencesAvatar().edit().remove("avatar_res_id_${getUserData().account}").apply()

        // 保存头像 URI 到 SharedPreferences
        getSharedPreferencesAvatar().edit().putString("avatar_${getUserData().account}", uri.toString()).apply()
    }
}