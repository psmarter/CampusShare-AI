# 贡献指南 / Contributing Guidelines

[English](#english) | [中文](#中文)

---

## 中文

感谢您对 Campus Item Sharing 项目的关注！我们欢迎任何形式的贡献。

### 如何贡献

#### 🐛 报告 Bug

1. 确保 Bug 未被报告过，检查 [Issues](https://github.com/psmarter/Campus/issues)
2. 创建新 Issue，使用 Bug Report 模板
3. 详细描述问题，包括：
   - 设备型号和 Android 版本
   - 复现步骤
   - 预期行为 vs 实际行为
   - 截图（如果适用）

#### 💡 提出新功能

1. 检查 [Issues](https://github.com/psmarter/Campus/issues) 确保功能未被提出
2. 创建 Feature Request Issue
3. 详细描述：
   - 功能的用途和价值
   - 可能的实现方式
   - 示例或参考

#### 🔧 提交代码

1. **Fork 仓库**

   ```bash
   git clone https://github.com/your-username/Campus.git
   cd Campus
   ```

2. **创建分支**

   ```bash
   git checkout -b feature/your-feature-name
   # 或
   git checkout -b fix/bug-description
   ```

3. **配置 API 密钥**
   - 复制 `local.properties.example` 为 `local.properties`
   - 在 [Google AI Studio](https://aistudio.google.com/app/apikey) 获取 API 密钥
   - 填入 `local.properties` 中的 `GEMINI_API_KEY`

4. **编写代码**
   - 遵循项目现有代码风格
   - 添加必要的注释（中英双语优先）
   - 确保代码通过编译

5. **提交更改**

   ```bash
   git add .
   git commit -m "feat: 添加新功能描述"
   # 或
   git commit -m "fix: 修复Bug描述"
   ```

6. **推送并创建 Pull Request**

   ```bash
   git push origin feature/your-feature-name
   ```

   然后在 GitHub 上创建 Pull Request

### 代码规范

#### Commit Message 格式

```
<type>: <description>

[optional body]
```

类型 (type):

- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式（不影响功能）
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具链相关

#### 代码风格

- 使用 Kotlin 官方代码风格
- 类名使用 PascalCase
- 函数和变量使用 camelCase
- 常量使用 UPPER_SNAKE_CASE
- 重要代码添加注释，优先使用中英双语

#### 文件组织

```
app/src/main/java/com/example/campus_item_sharing/
├── account/          # 账户相关
├── chatmodel/        # 聊天模型
├── friendmodel/      # 好友模型
├── itemmodel/        # 物品模型
├── post/             # 发布相关
├── retrofit/         # 网络请求
├── tools/            # 工具类
└── ui/               # UI组件
```

### 开发环境要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 11
- Android SDK 29-35
- Kotlin 1.9+

### 测试

在提交 PR 前，请确保：

- [ ] 应用可以成功编译
- [ ] 核心功能正常工作
- [ ] 没有明显的 UI 问题
- [ ] 新功能已经过测试

### 行为准则

- 尊重所有贡献者
- 友好、建设性地讨论
- 专注于代码和技术问题
- 欢迎新手提问和学习

### 需要帮助？

- 📧 创建 [Issue](https://github.com/psmarter/Campus/issues) 提问
- 💬 在 Pull Request 中讨论
- 📖 查看项目 [README](README.md)

---

## English

Thank you for your interest in the Campus Item Sharing project! We welcome contributions of all kinds.

### How to Contribute

#### 🐛 Report Bugs

1. Check if the bug has been reported in [Issues](https://github.com/psmarter/Campus/issues)
2. Create a new Issue using the Bug Report template
3. Provide detailed information:
   - Device model and Android version
   - Steps to reproduce
   - Expected vs actual behavior
   - Screenshots (if applicable)

#### 💡 Suggest Features

1. Check [Issues](https://github.com/psmarter/Campus/issues) to avoid duplicates
2. Create a Feature Request Issue
3. Describe in detail:
   - Purpose and value of the feature
   - Possible implementation approaches
   - Examples or references

#### 🔧 Submit Code

1. **Fork the Repository**

   ```bash
   git clone https://github.com/your-username/Campus.git
   cd Campus
   ```

2. **Create a Branch**

   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/bug-description
   ```

3. **Configure API Keys**
   - Copy `local.properties.example` to `local.properties`
   - Get your API key from [Google AI Studio](https://aistudio.google.com/app/apikey)
   - Fill in `GEMINI_API_KEY` in `local.properties`

4. **Write Code**
   - Follow existing code style
   - Add necessary comments (bilingual preferred)
   - Ensure code compiles

5. **Commit Changes**

   ```bash
   git add .
   git commit -m "feat: add new feature description"
   # or
   git commit -m "fix: bug description"
   ```

6. **Push and Create Pull Request**

   ```bash
   git push origin feature/your-feature-name
   ```

   Then create a Pull Request on GitHub

### Code Standards

#### Commit Message Format

```
<type>: <description>

[optional body]
```

Types:

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation updates
- `style`: Code formatting (no functional changes)
- `refactor`: Code refactoring
- `test`: Testing related
- `chore`: Build/toolchain related

#### Code Style

- Use official Kotlin code style
- Class names: PascalCase
- Functions and variables: camelCase
- Constants: UPPER_SNAKE_CASE
- Add comments for important code, bilingual preferred

#### File Organization

```
app/src/main/java/com/example/campus_item_sharing/
├── account/          # Account related
├── chatmodel/        # Chat models
├── friendmodel/      # Friend models
├── itemmodel/        # Item models
├── post/             # Post related
├── retrofit/         # Network requests
├── tools/            # Utilities
└── ui/               # UI components
```

### Development Environment

- Android Studio Hedgehog (2023.1.1) or higher
- JDK 11
- Android SDK 29-35
- Kotlin 1.9+

### Testing

Before submitting a PR, ensure:

- [ ] App compiles successfully
- [ ] Core features work properly
- [ ] No obvious UI issues
- [ ] New features are tested

### Code of Conduct

- Respect all contributors
- Discuss in a friendly and constructive manner
- Focus on code and technical issues
- Welcome newcomers and learners

### Need Help?

- 📧 Create an [Issue](https://github.com/psmarter/Campus/issues) to ask questions
- 💬 Discuss in Pull Requests
- 📖 Check the project [README](README.md)

---

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
