package com.example.campus_item_sharing.retrofit

data class ResponseModel(
    val message: String,
    val status: String,
    val data: Any? = null,
    val userDetails: UserDetails?
)
