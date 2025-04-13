package com.example.campus_item_sharing.function

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.campus_item_sharing.R

class WebActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled", "MissingInflatedId") // 允许 JS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val webView: WebView = findViewById(R.id.web_view)

        // 启用 JavaScript
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        // 获取 URL
        val url = intent.getStringExtra("web_url") ?: "https://www.uestc.edu.cn/"
        webView.loadUrl(url)
    }
}
