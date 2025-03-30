package com.example.campus_item_sharing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 获取 SharedPreferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val lastFragment = sharedPreferences.getString("last_fragment", "home") // 默认家

        // 根据保存的状态加载相应的 Fragment
        val initialFragment = when (lastFragment) {
            "messages" -> {
                bottomNavigationView.selectedItemId = R.id.nav_messages // 更新导航选项为设置
                MessagesFragment()
            }
            "settings" -> {
                bottomNavigationView.selectedItemId = R.id.nav_settings // 更新导航选项为设置
                SettingsFragment()
            }
            else -> {
                bottomNavigationView.selectedItemId = R.id.nav_home // 更新导航选项为主页
                HomeFragment()
            }
        }

        loadFragment(initialFragment)

        // 设置导航栏点击事件
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    saveLastFragment("home") // 保存选中的 Fragment
                }
                R.id.nav_messages -> {
                    loadFragment(MessagesFragment())
                    saveLastFragment("messages") // 保存选中的 Fragment
                }
                R.id.nav_settings -> {
                    loadFragment(SettingsFragment())
                    saveLastFragment("settings") // 保存选中的 Fragment
                }
            }
            true
        }
    }

    // 加载 Fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // 保存上次访问的 Fragment 到 SharedPreferences
    private fun saveLastFragment(fragmentName: String) {
        val editor = getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
        editor.putString("last_fragment", fragmentName)
        editor.apply()
    }
}