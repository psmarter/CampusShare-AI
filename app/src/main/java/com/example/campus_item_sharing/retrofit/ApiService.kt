package com.example.campus_item_sharing.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/users/login")
    fun login(@Body request: LoginRequest): Call<ResponseModel>

    @POST("api/users/register")
    fun register(@Body request: RegisterRequest): Call<ResponseModel>

    @GET("api/users/details/{account}") // 请求的 URL 根据 API 端点进行调整
    fun getUserDetails(@Path("account") account: String): Call<ResponseModel>

    @PUT("api/users/update") // 端点为 "api/users/update"
    fun updateUser(@Body request: RegisterRequest): Call<ResponseModel> // 使用新的请求体

    @DELETE("api/users/delete/{account}") // 添加删除账号的请求
    fun deleteUser(@Path("account") account: String): Call<ResponseModel> // 使用 account 作为参数


    // 物品相关的请求
    @POST("api/items/add") // 添加物品
    fun addItem(@Body request: ItemRequest): Call<ItemResponse>

    // 获取所有物品的请求
    @GET("api/items/all")
    fun getAllItems(): Call<List<ItemDetails>> // 获取物品详情列表

    @POST("api/friends/add")
    fun addFriend(
        @Query("account") account: String,
        @Query("friendAccount") friendAccount: String
    ): Call<ResponseModel>

    // 获取好友列表接口，返回 ResponseModel，其中 data 部分存放 List<FriendItem>
    @GET("api/friends/list")
    fun getFriends(@Query("account") account: String): Call<ResponseModel>

    @POST("api/messages/send")
    fun sendMessage(
        @Query("senderAccount") senderAccount: String,
        @Query("receiverAccount") receiverAccount: String,
        @Query("messageText") messageText: String
    ): Call<ResponseModel>

    @GET("api/messages/history")
    fun getChatHistory(
        @Query("account1") account1: String,
        @Query("account2") account2: String
    ): Call<ResponseModel>
}