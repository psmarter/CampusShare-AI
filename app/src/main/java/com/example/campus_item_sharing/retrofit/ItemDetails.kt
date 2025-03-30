package com.example.campus_item_sharing.retrofit

data class ItemDetails(
    val accountName: String,
    val itemType: String,
    val price: Double,
    val contactName: String,
    val contactNumber: String,
    val tags: String,
    val imageData: String,
    val imageUniqueId: String, // 物品唯一ID
    val description: String,
)