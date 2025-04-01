package com.example.campus_item_sharing.chatmodel


data class ChatMessage(
    val messageText: String,
    val isSent: Boolean  // true：当前用户发送；false：对方发送
)
