package com.example.campus_item_sharing.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://3038-218-194-35-10.ngrok-free.app/" // 本地服务器地址，10.0.2.2 是 Android 模拟器的本地地址

    // 日志拦截器，用于调试网络请求
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient，增加超时设置
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // 连接超时
        .readTimeout(30, TimeUnit.SECONDS)    // 读取超时
        .writeTimeout(30, TimeUnit.SECONDS)   // 写入超时
        .build()

    // Retrofit 实例
    private var retrofit = createRetrofit(BASE_URL)

    // 创建 Retrofit 实例的方法
    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // 提供 ApiService 实例
    val apiService: ApiService
        get() = retrofit.create(ApiService::class.java)

}