package com.example.campus_item_sharing.retrofit

data class LoginRequest(
    val account: String,
    val passwordHash: String
)