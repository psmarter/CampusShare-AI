package com.example.campus_item_sharing

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeminiActivity : AppCompatActivity() {

    private lateinit var promptEditText: EditText
    private lateinit var generateButton: Button
    private lateinit var resultTextView: TextView

    // 隐含提示，不在界面上展示
    private var hiddenAIPrompt: String? = null

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyA62ZnVwnUAWcEKS1oB6bhHIGBVUBbRRTI"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gemini)

        promptEditText = findViewById(R.id.promptEditText)
        generateButton = findViewById(R.id.generateButton)
        resultTextView = findViewById(R.id.resultTextView)

        // 从 Intent 中获取隐含提示，但不显示在编辑框中
        hiddenAIPrompt = intent.getStringExtra("ai_prompt")

        generateButton.setOnClickListener {
            val userInput = promptEditText.text.toString().trim()
            if (userInput.isNotEmpty()) {
                resultTextView.text = getString(R.string.loading_text)
                // 组合隐含提示和用户输入，作为最终的提示
                val finalPrompt = if (hiddenAIPrompt.isNullOrEmpty()) {
                    userInput
                } else {
                    "$hiddenAIPrompt\n对你的要求：请根据上述内容，进行你认为合理的推荐，并给出accountName、itemType、price、tags、description等信息，注意用户提问的标签，如果他想要一本书籍，其推荐的物品tags必须为书籍。请求：$userInput"
                }
                generateText(finalPrompt)
            }
        }
    }

    private fun generateText(prompt: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                val outputText = response.text ?: getString(R.string.no_result)
                withContext(Dispatchers.Main) {
                    resultTextView.text = outputText
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    resultTextView.text = e.localizedMessage ?: getString(R.string.error_message)
                }
            }
        }
    }
}