package com.example.campus_item_sharing.config

/**
 * API 配置类 / API Configuration Class
 * 
 * 配置后端服务器地址和 API 端点
 * Configure backend server URL and API endpoints
 * 
 * @author psmarter
 * @version 1.0
 * @since 2025-12-30
 */
object ApiConfig {
    
    // ==================== 服务器配置 / Server Configuration ====================
    
    /**
     * 开发环境基础 URL / Development Environment Base URL
     * 
     * Android 模拟器访问本机：http://10.0.2.2:8080
     * Android emulator accessing host machine: http://10.0.2.2:8080
     * 
     * 真机调试：替换为你电脑的局域网IP，例如 http://192.168.1.100:8080
     * Physical device: Replace with your computer's local IP, e.g., http://192.168.1.100:8080
     */
    private const val DEV_BASE_URL = "http://10.0.2.2:8080"
    
    /**
     * 生产环境基础 URL / Production Environment Base URL
     * 
     * 替换为实际部署的后端服务器地址（HTTPS）
     * Replace with your actual deployed backend server URL (HTTPS)
     */
    private const val PROD_BASE_URL = "https://your-backend-server.com"
    
    /**
     * 当前使用的基础 URL / Currently Active Base URL
     * 
     * 开发时使用 DEV_BASE_URL，发布时改为 PROD_BASE_URL
     * Use DEV_BASE_URL for development, change to PROD_BASE_URL for release
     */
    val BASE_URL: String = DEV_BASE_URL
    
    /**
     * 是否启用日志 / Enable Logging
     */
    const val ENABLE_LOGGING = true
    
    // ==================== API 端点 / API Endpoints ====================
    
    /**
     * API 端点常量类 / API Endpoints Constants
     */
    object Endpoints {
        
        // 用户管理相关 / User Management Related
        
        /**
         * 用户注册 / User Registration
         * POST /api/users/register
         */
        const val USER_REGISTER = "/api/users/register"
        
        /**
         * 用户登录 / User Login
         * POST /api/users/login
         */
        const val USER_LOGIN = "/api/users/login"
        
        /**
         * 更新用户信息 / Update User Information
         * PUT /api/users/update
         */
        const val USER_UPDATE = "/api/users/update"
        
        /**
         * 删除用户 / Delete User
         * DELETE /api/users/delete/{account}
         */
        const val USER_DELETE = "/api/users/delete"
        
        /**
         * 获取所有用户 / Get All Users
         * GET /api/users/all
         */
        const val USER_ALL = "/api/users/all"
        
        // 物品管理相关 / Item Management Related
        
        /**
         * 添加物品 / Add Item
         * POST /api/items/add
         */
        const val ITEM_ADD = "/api/items/add"
        
        /**
         * 获取所有物品 / Get All Items
         * GET /api/items/all
         */
        const val ITEM_ALL = "/api/items/all"
        
        // 好友管理相关 / Friend Management Related
        
        /**
         * 添加好友 / Add Friend
         * POST /api/friends/add
         */
        const val FRIEND_ADD = "/api/friends/add"
        
        /**
         * 获取所有好友 / Get All Friends
         * GET /api/friends/all
         */
        const val FRIEND_ALL = "/api/friends/all"
        
        // 聊天相关 / Chat Related
        
        /**
         * 发送消息 / Send Message
         * POST /api/chat/send
         */
        const val CHAT_SEND = "/api/chat/send"
        
        /**
         * 获取消息历史 / Get Message History
         * GET /api/chat/messages
         */
        const val CHAT_MESSAGES = "/api/chat/messages"
    }
    
    // ==================== 工具方法 / Utility Methods ====================
    
    /**
     * 获取完整的 API URL / Get Full API URL
     * 
     * @param endpoint API 端点路径 / API endpoint path
     * @return 完整的 URL / Complete URL
     * 
     * 使用示例 / Usage Example:
     * ```kotlin
     * val loginUrl = ApiConfig.getFullUrl(ApiConfig.Endpoints.USER_LOGIN)
     * // Returns: "http://10.0.2.2:8080/api/users/login"
     * ```
     */
    fun getFullUrl(endpoint: String): String {
        return "$BASE_URL$endpoint"
    }
    
    /**
     * 检查是否为生产环境 / Check if in Production Environment
     * 
     * @return true if production, false if development
     */
    fun isProduction(): Boolean {
        return BASE_URL == PROD_BASE_URL
    }
    
    /**
     * 获取环境名称 / Get Environment Name
     * 
     * @return "生产环境" or "开发环境"
     */
    fun getEnvironmentName(): String {
        return if (isProduction()) {
            "生产环境 / Production"
        } else {
            "开发环境 / Development"
        }
    }
    
    // ==================== 请求超时配置 / Request Timeout Configuration ====================
    
    /**
     * 连接超时时间（秒）/ Connection Timeout (seconds)
     */
    const val CONNECT_TIMEOUT = 30L
    
    /**
     * 读取超时时间（秒）/ Read Timeout (seconds)
     */
    const val READ_TIMEOUT = 30L
    
    /**
     * 写入超时时间（秒）/ Write Timeout (seconds)
     */
    const val WRITE_TIMEOUT = 30L
}
