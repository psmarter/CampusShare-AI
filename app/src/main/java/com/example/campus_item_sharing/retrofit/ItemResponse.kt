package com.example.campus_item_sharing.retrofit

data class ItemResponse(
    val status: String,
    val message: String,
    val itemDetails: ItemDetails? // 获取物品的详细信息（如果有）
)