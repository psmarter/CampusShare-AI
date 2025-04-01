package com.example.campus_item_sharing.friendmodel

data class FriendItem(
    val account: String,       // 好友账户名
    val lastMessage: String = ""  // 最近一条消息，默认为空
)