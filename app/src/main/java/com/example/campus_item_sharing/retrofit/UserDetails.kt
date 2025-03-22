package com.example.campus_item_sharing.retrofit

data class UserDetails(
    val studentNumber: String,
    val account: String,
    val passwordHash: String,
    val email: String

)