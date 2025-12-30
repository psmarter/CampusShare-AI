# 后端 API 对接指南 / Backend API Integration Guide

[English](#english) | [中文](#中文)

---

## 中文

### 🔗 后端仓库

**Spring Boot 后端仓库**: [Campus_Spring_boot](https://github.com/psmarter/Campus_Spring_boot)

该后端提供完整的 RESTful API，包括用户认证、物品管理、聊天等功能。  
请先按照后端仓库的 README 部署 Spring Boot 服务，然后再运行本 Android 应用。

### 🌐 API 基础地址配置

在 `app/src/main/java/com/example/campus_item_sharing/config/ApiConfig.kt` 中配置：

#### 开发环境

**Android 模拟器**:

```kotlin
private const val DEV_BASE_URL = "http://10.0.2.2:8080"
```

> **原理说明**: `10.0.2.2` 是 Android 模拟器访问主机 `localhost` 的特殊 IP 地址

**真机调试**:

```kotlin
private const val DEV_BASE_URL = "http://192.168.1.100:8080"
```

> **注意**: 替换为你电脑的实际局域网 IP，确保手机和电脑在同一 WiFi 网络

#### 生产环境

```kotlin
private const val PROD_BASE_URL = "https://your-backend-server.com"
val BASE_URL: String = PROD_BASE_URL  // 发布时切换到生产环境
```

### 📚 完整 API 文档

查看后端仓库的完整 API 文档：

- [API.md](https://github.com/psmarter/Campus_Spring_boot/blob/main/API.md)
- [移动端对接指南](https://github.com/psmarter/Campus_Spring_boot/blob/main/docs/MOBILE_INTEGRATION.md)

### 🔧 快速参考 - API 端点

#### 用户管理

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/users/register` | POST | 用户注册 |
| `/api/users/login` | POST | 用户登录 |
| `/api/users/update` | PUT | 更新用户信息 |
| `/api/users/delete/{account}` | DELETE | 删除用户 |

#### 物品管理

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/items/add` | POST | 发布物品 |
| `/api/items/all` | GET | 获取所有物品 |

#### 好友与聊天

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/friends/add` | POST | 添加好友 |
| `/api/friends/all` | GET | 获取好友列表 |
| `/api/chat/send` | POST | 发送消息 |
| `/api/chat/messages` | GET | 获取消息历史 |

### 💻 使用示例

#### 1. 在 Retrofit 接口中使用

```kotlin
import com.example.campus_item_sharing.config.ApiConfig

interface ApiService {
    @POST(ApiConfig.Endpoints.USER_LOGIN)
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>
    
    @GET(ApiConfig.Endpoints.ITEM_ALL)
    suspend fun getAllItems(): Response<List<Item>>
}
```

#### 2. 在 ViewModel 中调用

```kotlin
class UserViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitClient.apiService
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    // 登录成功
                } else {
                    // 处理错误
                }
            } catch (e: Exception) {
                // 网络错误
            }
        }
    }
}
```

### 📝 数据格式

#### 请求格式

所有请求使用 JSON 格式，Content-Type 为 `application/json`

**用户登录请求示例**:

```json
{
  "account": "test_user",
  "passwordHash": "password123"
}
```

**发布物品请求示例**:

```json
{
  "accountName": "test_user",
  "imageUniqueId": "img_001",
  "itemType": "Books",
  "price": 25.00,
  "contactName": "John Doe",
  "contactNumber": "1234567890",
  "tags": "textbook,mathematics",
  "description": "Advanced Mathematics Textbook",
  "imageData": "<base64_encoded_image>"
}
```

#### 响应格式

标准响应格式:

```json
{
  "status": "success" | "error",
  "message": "操作结果描述",
  "data": {
    // 返回的数据对象
  }
}
```

### 🔐 认证流程

```
1. 用户注册
   POST /api/users/register
   → 返回用户 ID

2. 用户登录
   POST /api/users/login
   → 返回用户信息 + Token (如有)

3. 保存认证信息
   在 SharedPreferences 中保存用户信息

4. 后续请求
   在请求头中携带认证信息（如需要）
```

### 📸 图片处理

后端接收 Base64 编码的图片数据：

```kotlin
// 将 Bitmap 转换为 Base64
fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
}
```

### 🧪 测试后端连接

#### 方法 1: 在 Android 应用中测试

在 `MainActivity.onCreate()` 中添加测试代码：

```kotlin
lifecycleScope.launch {
    try {
        val response = apiService.getAllItems()
        if (response.isSuccessful) {
            Log.d("API", "后端连接成功！items: ${response.body()?.size}")
        } else {
            Log.e("API", "后端连接失败: ${response.code()}")
        }
    } catch (e: Exception) {
        Log.e("API", "无法连接后端: ${e.message}")
    }
}
```

#### 方法 2: 使用浏览器测试

在手机浏览器或电脑浏览器中访问：

```
http://10.0.2.2:8080/api/items/all  (模拟器)
http://192.168.1.100:8080/api/items/all  (真机)
```

### 🔧 故障排查

#### 问题 1: 连接被拒绝（Connection refused）

**原因**:

- 后端服务未启动
- IP 地址配置错误
- 防火墙阻止连接

**解决方案**:

1. 确认后端服务正在运行：`mvn spring-boot:run`
2. 检查 `ApiConfig.kt` 中的 `BASE_URL` 配置
3. 真机调试时，确保手机和电脑在同一网络

#### 问题 2: NetworkOnMainThreadException

**原因**: 在主线程执行网络请求

**解决方案**: 使用协程或 RxJava：

```kotlin
viewModelScope.launch(Dispatchers.IO) {
    // 网络请求
}
```

#### 问题 3: UnknownHostException

**原因**: 无法解析主机名或 IP

**解决方案**:

1. 检查网络连接
2. 确认后端 IP 地址正确
3. AndroidManifest.xml 中添加网络权限：

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

#### 问题 4: 401 Unauthorized

**原因**: 认证失败或缺少认证信息

**解决方案**: 检查后端 SecurityConfig 配置，确认端点是否需要认证

### ✅ 开发检查清单

在开始开发前，确保：

- [ ] 后端服务已启动并运行在 `http://localhost:8080`
- [ ] `ApiConfig.kt` 中的 `BASE_URL` 配置正确
- [ ] AndroidManifest.xml 中已添加网络权限
- [ ] 手机/模拟器能访问后端服务（浏览器测试）
- [ ] Retrofit 配置正确
- [ ] 已查看后端 API 文档

### 📞 获取帮助

- 后端仓库 Issues: <https://github.com/psmarter/Campus_Spring_boot/issues>
- 前端仓库 Issues: <https://github.com/psmarter/CampusShare-AI/issues>
- 后端完整文档: <https://github.com/psmarter/Campus_Spring_boot/blob/main/docs/MOBILE_INTEGRATION.md>

---

## English

### 🔗 Backend Repository

**Spring Boot Backend Repository**: [Campus_Spring_boot](https://github.com/psmarter/Campus_Spring_boot)

The backend provides complete RESTful API including user authentication, item management, chat, etc.  
Please deploy the Spring Boot service following the backend repository's README before running this Android app.

### 🌐 API Base URL Configuration

Configure in `app/src/main/java/com/example/campus_item_sharing/config/ApiConfig.kt`:

#### Development Environment

**Android Emulator**:

```kotlin
private const val DEV_BASE_URL = "http://10.0.2.2:8080"
```

> **Note**: `10.0.2.2` is the special IP address for Android emulator to access host `localhost`

**Physical Device**:

```kotlin
private const val DEV_BASE_URL = "http://192.168.1.100:8080"
```

> **Note**: Replace with your computer's actual local IP, ensure phone and computer are on same WiFi

#### Production Environment

```kotlin
private const val PROD_BASE_URL = "https://your-backend-server.com"
val BASE_URL: String = PROD_BASE_URL  // Switch to production on release
```

### 📚 Complete API Documentation

See complete API documentation in backend repository:

- [API.md](https://github.com/psmarter/Campus_Spring_boot/blob/main/API.md)
- [Mobile Integration Guide](https://github.com/psmarter/Campus_Spring_boot/blob/main/docs/MOBILE_INTEGRATION.md)

### 🔧 Quick Reference - API Endpoints

#### User Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/users/register` | POST | User registration |
| `/api/users/login` | POST | User login |
| `/api/users/update` | PUT | Update user info |
| `/api/users/delete/{account}` | DELETE | Delete user |

#### Item Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/items/add` | POST | Publish item |
| `/api/items/all` | GET | Get all items |

#### Friends & Chat

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/friends/add` | POST | Add friend |
| `/api/friends/all` | GET | Get friends list |
| `/api/chat/send` | POST | Send message |
| `/api/chat/messages` | GET | Get message history |

### 💻 Usage Examples

#### 1. Using in Retrofit Interface

```kotlin
import com.example.campus_item_sharing.config.ApiConfig

interface ApiService {
    @POST(ApiConfig.Endpoints.USER_LOGIN)
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>
    
    @GET(ApiConfig.Endpoints.ITEM_ALL)
    suspend fun getAllItems(): Response<List<Item>>
}
```

#### 2. Calling in ViewModel

```kotlin
class UserViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitClient.apiService
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    // Login successful
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Network error
            }
        }
    }
}
```

For more details, please refer to the [backend integration guide](https://github.com/psmarter/Campus_Spring_boot/blob/main/docs/MOBILE_INTEGRATION.md).

---

<div align="center">

**完整的后端对接文档请查看后端仓库！**  
**For complete backend integration docs, see the backend repository!**

[查看后端仓库 / View Backend Repo](https://github.com/psmarter/Campus_Spring_boot)

</div>
