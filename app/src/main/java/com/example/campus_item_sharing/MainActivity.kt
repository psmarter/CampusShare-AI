package com.example.campus_item_sharing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * 主界面 Activity - 管理底部导航和 Fragment 切换
 * Main Activity - Manages bottom navigation and fragment switching
 * 
 * 功能 / Features:
 * - 底部导航栏切换 / Bottom navigation switching
 * - Fragment 管理 / Fragment management
 * - 保存并恢复上次访问的页面 / Save and restore last visited page
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化底部导航视图 / Initialize bottom navigation view
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 获取 SharedPreferences，用于保存用户偏好
        // Get SharedPreferences for saving user preferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val lastFragment = sharedPreferences.getString("last_fragment", "home") // 默认主页 / Default to home

        // 根据保存的状态加载相应的 Fragment
        // Load corresponding fragment based on saved state
        val initialFragment = when (lastFragment) {
            "messages" -> {
                bottomNavigationView.selectedItemId = R.id.nav_messages // 更新导航选项为消息 / Update navigation to messages
                MessagesFragment()
            }
            "settings" -> {
                bottomNavigationView.selectedItemId = R.id.nav_settings // 更新导航选项为设置 / Update navigation to settings
                SettingsFragment()
            }
            else -> {
                bottomNavigationView.selectedItemId = R.id.nav_home // 更新导航选项为主页 / Update navigation to home
                HomeFragment()
            }
        }

        // 加载初始 Fragment / Load initial fragment
        loadFragment(initialFragment)

        // 设置导航栏点击事件 / Set navigation bar click listener
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    saveLastFragment("home") // 保存选中的 Fragment / Save selected fragment
                }
                R.id.nav_messages -> {
                    loadFragment(MessagesFragment())
                    saveLastFragment("messages") // 保存选中的 Fragment / Save selected fragment
                }
                R.id.nav_settings -> {
                    loadFragment(SettingsFragment())
                    saveLastFragment("settings") // 保存选中的 Fragment / Save selected fragment
                }
            }
            true
        }
    }

    /**
     * 加载 Fragment 到容器中
     * Load fragment into container
     * 
     * @param fragment 要加载的 Fragment / Fragment to load
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    /**
     * 保存上次访问的 Fragment 到 SharedPreferences
     * Save last visited fragment to SharedPreferences
     * 
     * @param fragmentName Fragment 名称 / Fragment name
     */
    private fun saveLastFragment(fragmentName: String) {
        val editor = getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
        editor.putString("last_fragment", fragmentName)
        editor.apply()
    }
}