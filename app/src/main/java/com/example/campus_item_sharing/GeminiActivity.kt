package com.example.campus_item_sharing

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Gemini AI 助手界面 - 智能物品推荐和描述生成
 * Gemini AI Assistant Activity - Smart item recommendations and description generation
 * 
 * 功能 / Features:
 * - 集成 Google Gemini 1.5 Flash AI / Integrate Google Gemini 1.5 Flash AI
 * - 智能物品推荐 / Smart item recommendations
 * - 自动生成物品描述和标签 / Auto-generate item descriptions and tags
 * - 价格建议 / Price suggestions
 * - 支持隐含提示词组合 / Support hidden prompt combination
 */
class GeminiActivity : AppCompatActivity() {

    // UI 组件 / UI Components
    private lateinit var promptEditText: EditText      // 用户输入框 / User input field
    private lateinit var generateButton: Button        // 生成按钮 / Generate button
    private lateinit var resultTextView: TextView      // 结果显示区域 / Result display area
    private lateinit var btnBack: ImageView            // 返回按钮 / Back button


    // Gemini AI 模型配置 / Gemini AI Model Configuration
    // 隐含提示，不在界面上展示 / Hidden prompt, not displayed in UI
    private var hiddenAIPrompt: String? = null

    // 使用 BuildConfig 安全地读取 API 密钥 / Securely read API key from BuildConfig
    // API 密钥存储在 local.properties 中，不会被提交到 GitHub
    // API key is stored in local.properties and won't be committed to GitHub
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    /**
     * 初始化界面和设置事件监听器
     * Initialize UI and set up event listeners
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gemini)

        // 绑定 UI 组件 / Bind UI components
        promptEditText = findViewById(R.id.promptEditText)
        generateButton = findViewById(R.id.generateButton)
        resultTextView = findViewById(R.id.resultTextView)
        btnBack = findViewById(R.id.user_message_home_back)

        // 返回按钮点击事件 / Back button click listener
        btnBack.setOnClickListener {
            finish()
        }

        // 从 Intent 中获取隐含提示，但不显示在编辑框中
        // Get hidden prompt from Intent, but don't display in edit field
        // 用途：从其他Activity传递物品信息，让AI基于这些信息生成推荐
        // Purpose: Pass item info from other Activities for AI to generate recommendations
        hiddenAIPrompt = intent.getStringExtra("ai_prompt")

        // 生成按钮点击事件 / Generate button click listener
        generateButton.setOnClickListener {
            val userInput = promptEditText.text.toString().trim()
            if (userInput.isNotEmpty()) {
                resultTextView.text = getString(R.string.loading_text)
                
                // 组合隐含提示和用户输入，作为最终的提示
                // Combine hidden prompt and user input as final prompt
                val finalPrompt = if (hiddenAIPrompt.isNullOrEmpty()) {
                    userInput
                } else {
                    // 构建包含上下文信息的完整提示词
                    // Build complete prompt with context information
                    "$hiddenAIPrompt\n对你的要求：请根据上述内容，进行你认为合理的推荐，并给出accountName、itemType、price、tags、description等信息，注意用户提问的标签，如果他想要一本书籍，其推荐的物品tags必须为书籍，采用中文回答和沟通。请求：$userInput"
                }
                generateText(finalPrompt)
            }
        }
    }


    /**
     * 使用 Gemini AI 生成文本内容
     * Generate text content using Gemini AI
     * 
     * @param prompt 提示词（用户输入 + 可选的隐含提示）/ Prompt (user input + optional hidden prompt)
     * 
     * 工作流程 / Workflow:
     * 1. 在 IO 线程调用 Gemini API / Call Gemini API on IO thread
     * 2. 解析 API 返回的内容 / Parse API response
     * 3. 在主线程更新 UI / Update UI on main thread
     * 4. 处理可能的异常 / Handle potential exceptions
     */
    private fun generateText(prompt: String) {
        // 使用协程在后台线程执行网络请求
        // Use coroutine to execute network request in background thread
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 调用 Gemini API 生成内容
                // Call Gemini API to generate content
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                
                // 获取生成的文本或错误信息
                // Get generated text or error message
                val outputText = response.text ?: getString(R.string.no_result)
                
                // 切换到主线程更新 UI
                // Switch to main thread to update UI
                withContext(Dispatchers.Main) {
                    resultTextView.text = outputText
                }
            } catch (e: Exception) {
                // 捕获异常并显示错误信息
                // Catch exception and display error message
                withContext(Dispatchers.Main) {
                    resultTextView.text = e.localizedMessage ?: getString(R.string.error_message)
                }
            }
        }
    }
}
