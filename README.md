# 🎓 Campus Item Sharing - 智能校园物品共享平台

[English](#english) | [中文](#中文)

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Gemini](https://img.shields.io/badge/AI-Gemini%201.5-4285F4?style=for-the-badge&logo=google&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**一个集成 Google Gemini AI 的智能校园物品共享平台**  
**An intelligent campus item-sharing platform powered by Google Gemini AI**

[功能特性](#-功能特性--features) • [技术栈](#️-技术栈--tech-stack) • [快速开始](#-快速开始--quick-start) • [贡献](#-贡献--contributing)

</div>

---

## 🔗 相关仓库 / Related Repositories

> **本项目是 Campus Item Sharing Platform 的 Android 前端应用**  
> **This is the Android frontend for Campus Item Sharing Platform**

### 📦 完整项目组成 / Complete Project Components

| 组件 Component | 仓库 Repository | 技术栈 Tech Stack | 说明 Description |
|---------------|----------------|------------------|-----------------|
| **🎨 前端 Frontend** | [CampusShare-AI](https://github.com/psmarter/CampusShare-AI) **(当前仓库 Current)** | Android (Kotlin) + Gemini AI | Mobile Application |
| **⚙️ 后端 Backend** | [Campus_Spring_boot](https://github.com/psmarter/Campus_Spring_boot) | Spring Boot + MySQL | RESTful API Server |

### 🚀 完整部署指南 / Full Deployment Guide

#### 📱 仅前端开发 / Frontend Only

按照下方的 [快速开始](#-快速开始--quick-start) 指南即可。

#### 🔧 前后端联调 / Full-Stack Development

1. ✅ 部署后端服务：按照 [后端仓库说明](https://github.com/psmarter/Campus_Spring_boot) 启动 Spring Boot API
2. ✅ 配置前端 API 地址（见下方 [后端 API 配置](#-后端-api-配置--backend-api-configuration)）
3. ✅ 运行 Android 应用

> **💡 开发者提示 / Developer Tip**:  
> 查看详细的后端对接文档：[BACKEND_API.md](./docs/BACKEND_API.md)

---

## 中文

### 📱 项目简介

Campus Item Sharing 是一个专为大学生设计的智能物品共享平台。通过集成 Google Gemini 1.5 Flash AI，为用户提供智能推荐、物品分类和交易建议，让校园闲置物品交易更高效、更智能。

#### ✨ 核心亮点

- 🤖 **AI 智能助手**: 集成 Gemini 1.5 Flash，提供智能物品推荐和描述生成
- 💬 **实时聊天**: 买卖双方实时沟通，快速达成交易
- 📸 **图片展示**: 支持多图上传，清晰展示物品细节
- 🏷️ **智能分类**: AI 自动识别物品类型和标签
- 📊 **交易历史**: 完整记录发布和交易记录
- 👥 **好友系统**: 添加好友，私聊交流更方便

### 🌟 功能特性 / Features

#### 1. 智能物品发布

- 支持图片上传和预览
- AI 自动生成物品描述和标签
- 价格建议和市场分析
- 多类型物品支持（书籍、电子产品、文具等）

#### 2. Gemini AI 集成

- **智能推荐**: 根据用户需求推荐合适的物品
- **自动标签**: AI 自动识别物品类型并添加标签
- **描述生成**: 智能生成吸引人的物品描述
- **价格建议**: 基于物品类型提供合理价格范围

#### 3. 社交功能

- 用户注册和登录
- 好友添加和管理
- 实时消息通知
- 私聊和群聊支持

#### 4. 交易管理

- 发布历史记录
- 物品浏览和搜索
- 收藏和关注
- 交易状态跟踪

### 🛠️ 技术栈 / Tech Stack

#### 核心技术

- **语言**: Kotlin
- **最低 SDK**: Android 29 (Android 10)
- **目标 SDK**: Android 35
- **构建工具**: Gradle 8.x + Kotlin DSL

#### 主要依赖

| 技术 | 用途 | 版本 |
|------|------|------|
| **Google Gemini AI** | AI 智能推荐和内容生成 | 1.5 Flash |
| **Retrofit** | 网络请求框架 | Latest |
| **Gson** | JSON 解析 | Latest |
| **Jetpack Compose** | 现代化 UI 框架 | Latest |
| **Material 3** | Material Design UI 组件 | Latest |
| **Glide** | 图片加载和缓存 | Latest |
| **CameraX** | 相机功能 | Latest |
| **ML Kit** | 二维码扫描 | Latest |

### 🌐 后端 API 配置 / Backend API Configuration

本应用需要连接后端 API 服务器才能完整运行。后端提供用户认证、物品管理、聊天等核心功能。  
This app requires connection to the backend API server for full functionality. The backend provides user authentication, item management, chat, and other core features.

#### 后端仓库 / Backend Repository

**Spring Boot API**: [Campus_Spring_boot](https://github.com/psmarter/Campus_Spring_boot)

#### API 地址配置 / API URL Configuration

**开发环境 / Development**:

- Android 模拟器 / Emulator: `http://10.0.2.2:8080`
- 真机调试 / Physical Device: `http://YOUR_LOCAL_IP:8080` (替换为你电脑的IP)

**生产环境 / Production**:

- 部署后端到云服务器后的 HTTPS 地址

> **📖 详细配置指南**: 查看 [docs/BACKEND_API.md](./docs/BACKEND_API.md) 获取完整的后端对接文档

### 📥 快速开始 / Quick Start

#### 前置要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 11 或更高版本
- Android SDK 29-35
- Google Gemini API 密钥（[获取密钥](https://aistudio.google.com/app/apikey)）

#### 安装步骤

1. **克隆仓库**

   ```bash
   git clone https://github.com/psmarter/CampusShare-AI.git
   cd Campus
   ```

2. **配置 API 密钥** 🔑

   这是最重要的步骤！

   ```bash
   # 复制示例配置文件
   cp local.properties.example local.properties
   ```

   打开 `local.properties` 文件，填入您的 Gemini API 密钥：

   ```properties
   GEMINI_API_KEY=your_actual_api_key_here
   ```

   > ⚠️ **安全提示**: `local.properties` 文件已在 `.gitignore` 中，不会被提交到 Git，确保 API 密钥安全。

3. **打开项目**
   - 使用 Android Studio 打开项目文件夹
   - 等待 Gradle 同步完成

4. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 Run 按钮（或按 Shift+F10）

#### 获取 Gemini API 密钥

1. 访问 [Google AI Studio](https://aistudio.google.com/app/apikey)
2. 登录您的 Google 账户
3. 点击 "Create API Key"
4. 复制生成的 API 密钥
5. 粘贴到 `local.properties` 文件中

### 📂 项目结构

```
app/src/main/java/com/example/campus_item_sharing/
├── 📱 Activities & Fragments
│   ├── MainActivity.kt              # 主界面
│   ├── LoginActivity.kt             # 登录界面
│   ├── GeminiActivity.kt            # AI 助手界面
│   ├── ChatActivity.kt              # 聊天界面
│   ├── PostHistory.kt               # 发布历史
│   ├── HomeFragment.kt              # 首页 Fragment
│   ├── PostFragment.kt              # 发布 Fragment
│   ├── MessagesFragment.kt          # 消息 Fragment
│   └── SettingsFragment.kt          # 设置 Fragment
│
├── 📦 Models
│   ├── account/                     # 账户数据模型
│   ├── chatmodel/                   # 聊天数据模型
│   ├── friendmodel/                 # 好友数据模型
│   └── itemmodel/                   # 物品数据模型
│
├── 🌐 Network
│   └── retrofit/                    # Retrofit API 接口
│
├── 🛠️ Utilities
│   ├── tools/                       # 工具类
│   └── ui/                          # UI 组件
│
└── 📄 Resources
    ├── res/layout/                  # XML 布局文件
    ├── res/drawable/                # 图片资源
    └── res/values/                  # 字符串、颜色等
```

### 🎯 使用示例

#### 1. 使用 AI 助手发布物品

```kotlin
// 在 GeminiActivity 中使用 Gemini AI
val prompt = "我想卖一本《算法导论》，八成新，帮我生成描述和定价建议"
generateText(prompt)
```

AI 会返回：

- 物品类型标签：书籍
- 建议描述：经典算法教材，适合计算机专业学生...
- 建议价格：¥30-50

#### 2. 浏览和搜索物品

在 `HomeFragment` 中浏览物品列表，支持：

- 按类型筛选
- 按价格排序
- 关键词搜索
- AI 智能推荐

### 🔐 安全说明

本项目采用安全的 API 密钥管理：

- ✅ API 密钥存储在 `local.properties`（本地文件）
- ✅ `local.properties` 已添加到 `.gitignore`
- ✅ 使用 BuildConfig 安全读取密钥
- ❌ 绝不在代码中硬编码密钥
- ❌ 绝不将密钥提交到 GitHub

### 🚀 未来计划

- [ ] 添加物品状态管理（在售、已售、已预订）
- [ ] 集成支付功能
- [ ] 添加评分和评论系统
- [ ] 支持物品定位和地图展示
- [ ] 添加推送通知
- [ ] 优化 AI 推荐算法
- [ ] 支持多语言（English, 简体中文）

### 🤝 贡献 / Contributing

我们欢迎所有形式的贡献！请查看 [贡献指南](CONTRIBUTING.md) 了解详情。

#### 快速贡献步骤

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: 添加某个功能'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 📄 许可证 / License

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

### 📞 联系方式

- GitHub: [@psmarter](https://github.com/psmarter)
- Project Link: [https://github.com/psmarter/CampusShare-AI](https://github.com/psmarter/CampusShare-AI)

### 🙏 致谢

- [Google Gemini AI](https://ai.google.dev/) - 提供强大的 AI 能力
- [Material Design](https://material.io/) - UI 设计规范
- 所有贡献者和支持者

---

## English

### 📱 Project Overview

Campus Item Sharing is an intelligent item-sharing platform designed for college students. By integrating Google Gemini 1.5 Flash AI, it provides smart recommendations, item categorization, and trading suggestions, making campus item trading more efficient and intelligent.

#### ✨ Highlights

- 🤖 **AI Assistant**: Integrated with Gemini 1.5 Flash for smart item recommendations and description generation
- 💬 **Real-time Chat**: Instant communication between buyers and sellers
- 📸 **Image Display**: Support multiple image uploads with clear item details
- 🏷️ **Smart Classification**: AI auto-identifies item types and tags
- 📊 **Trading History**: Complete record of posts and transactions
- 👥 **Friend System**: Add friends for convenient private conversations

### 🌟 Features

#### 1. Smart Item Posting

- Image upload and preview support
- AI-generated item descriptions and tags
- Price suggestions and market analysis
- Multi-type item support (books, electronics, stationery, etc.)

#### 2. Gemini AI Integration

- **Smart Recommendations**: Recommend suitable items based on user needs
- **Auto Tagging**: AI automatically identifies item types and adds tags
- **Description Generation**: Intelligently generates attractive item descriptions
- **Price Suggestions**: Provides reasonable price ranges based on item types

#### 3. Social Features

- User registration and login
- Friend adding and management
- Real-time message notifications
- Private and group chat support

#### 4. Transaction Management

- Post history records
- Item browsing and search
- Favorites and follows
- Transaction status tracking

### 🛠️ Tech Stack

#### Core Technologies

- **Language**: Kotlin
- **Min SDK**: Android 29 (Android 10)
- **Target SDK**: Android 35
- **Build Tools**: Gradle 8.x + Kotlin DSL

#### Main Dependencies

| Technology | Purpose | Version |
|------------|---------|---------|
| **Google Gemini AI** | AI recommendations and content generation | 1.5 Flash |
| **Retrofit** | Network request framework | Latest |
| **Gson** | JSON parsing | Latest |
| **Jetpack Compose** | Modern UI framework | Latest |
| **Material 3** | Material Design UI components | Latest |
| **Glide** | Image loading and caching | Latest |
| **CameraX** | Camera functionality | Latest |
| **ML Kit** | QR code scanning | Latest |

### 📥 Quick Start

#### Prerequisites

- Android Studio Hedgehog (2023.1.1) or higher
- JDK 11 or higher
- Android SDK 29-35
- Google Gemini API Key ([Get Key](https://aistudio.google.com/app/apikey))

#### Installation Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/psmarter/CampusShare-AI.git
   cd Campus
   ```

2. **Configure API Key** 🔑

   This is the most important step!

   ```bash
   # Copy the example configuration file
   cp local.properties.example local.properties
   ```

   Open the `local.properties` file and fill in your Gemini API key:

   ```properties
   GEMINI_API_KEY=your_actual_api_key_here
   ```

   > ⚠️ **Security Note**: The `local.properties` file is in `.gitignore` and won't be committed to Git, ensuring API key security.

3. **Open Project**
   - Open the project folder with Android Studio
   - Wait for Gradle sync to complete

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the Run button (or press Shift+F10)

#### Getting a Gemini API Key

1. Visit [Google AI Studio](https://aistudio.google.com/app/apikey)
2. Log in to your Google account
3. Click "Create API Key"
4. Copy the generated API key
5. Paste it into the `local.properties` file

### 📂 Project Structure

```
app/src/main/java/com/example/campus_item_sharing/
├── 📱 Activities & Fragments
│   ├── MainActivity.kt              # Main activity
│   ├── LoginActivity.kt             # Login activity
│   ├── GeminiActivity.kt            # AI assistant activity
│   ├── ChatActivity.kt              # Chat activity
│   ├── PostHistory.kt               # Post history
│   ├── HomeFragment.kt              # Home fragment
│   ├── PostFragment.kt              # Post fragment
│   ├── MessagesFragment.kt          # Messages fragment
│   └── SettingsFragment.kt          # Settings fragment
│
├── 📦 Models
│   ├── account/                     # Account data models
│   ├── chatmodel/                   # Chat data models
│   ├── friendmodel/                 # Friend data models
│   └── itemmodel/                   # Item data models
│
├── 🌐 Network
│   └── retrofit/                    # Retrofit API interfaces
│
├── 🛠️ Utilities
│   ├── tools/                       # Utility classes
│   └── ui/                          # UI components
│
└── 📄 Resources
    ├── res/layout/                  # XML layout files
    ├── res/drawable/                # Image resources
    └── res/values/                  # Strings, colors, etc.
```

### 🎯 Usage Examples

#### 1. Using AI Assistant to Post Items

```kotlin
// Using Gemini AI in GeminiActivity
val prompt = "I want to sell 'Introduction to Algorithms', 80% new, help me generate description and pricing suggestion"
generateText(prompt)
```

AI will return:

- Item type tag: Books
- Suggested description: Classic algorithm textbook, suitable for CS students...
- Suggested price: $30-50

#### 2. Browse and Search Items

Browse item lists in `HomeFragment`, supporting:

- Filter by type
- Sort by price
- Keyword search
- AI smart recommendations

### 🔐 Security Notes

This project uses secure API key management:

- ✅ API keys stored in `local.properties` (local file)
- ✅ `local.properties` added to `.gitignore`
- ✅ Uses BuildConfig to securely read keys
- ❌ Never hardcode keys in code
- ❌ Never commit keys to GitHub

### 🚀 Future Plans

- [ ] Add item status management (on sale, sold, reserved)
- [ ] Integrate payment functionality
- [ ] Add rating and review system
- [ ] Support item location and map display
- [ ] Add push notifications
- [ ] Optimize AI recommendation algorithm
- [ ] Multi-language support (English, 简体中文)

### 🤝 Contributing

We welcome all forms of contribution! Please check the [Contributing Guidelines](CONTRIBUTING.md) for details.

#### Quick Contribution Steps

1. Fork this repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'feat: Add some feature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 📞 Contact

- GitHub: [@psmarter](https://github.com/psmarter)
- Project Link: [https://github.com/psmarter/CampusShare-AI](https://github.com/psmarter/CampusShare-AI)

### 🙏 Acknowledgments

- [Google Gemini AI](https://ai.google.dev/) - Providing powerful AI capabilities
- [Material Design](https://material.io/) - UI design specifications
- All contributors and supporters

---

<div align="center">

**如果这个项目对你有帮助，请给一个 ⭐ Star！**  
**If this project helps you, please give it a ⭐ Star!**

Made with ❤️ by [psmarter](https://github.com/psmarter)

</div>
