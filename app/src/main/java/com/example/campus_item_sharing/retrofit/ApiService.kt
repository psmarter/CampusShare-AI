package com.example.campus_item_sharing.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/users/login")
    fun login(@Body request: LoginRequest): Call<ResponseModel>

    @POST("api/users/register")
    fun register(@Body request: RegisterRequest): Call<ResponseModel>

    @GET("api/users/details/{account}") // 请求的 URL 根据 API 端点进行调整
    fun getUserDetails(@Path("account") account: String): Call<ResponseModel>
}