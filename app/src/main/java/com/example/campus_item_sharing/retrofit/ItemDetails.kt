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
) {
    fun deepCopy(): ItemDetails {
        return ItemDetails(
            accountName = this.accountName,
            itemType = this.itemType,
            price = this.price,
            contactName = this.contactName,
            contactNumber = this.contactNumber,
            tags = this.tags,  // String 是不可变的，直接赋值即可
            imageData = this.imageData,
            imageUniqueId = this.imageUniqueId,
            description = this.description
        )
    }
}