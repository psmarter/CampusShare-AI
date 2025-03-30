package com.example.campus_item_sharing.retrofit

data class ItemRequest(
    val accountName: String,
    val itemType: String,
    val price: Double,
    val contactName: String,
    val contactNumber: String,
    val tags: String,
    val imageData: String,// 需要是 Base64 编码字符串
    val description: String
)