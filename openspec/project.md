# Project Context

## Purpose
AndroidDemo：基于《Android Studio 开发实战：从零基础到 App 上线（第二版）》配套源码改造的综合性 Android 学习/演示工程，包含 21 个业务模块、400+ Activity、100+ 自定义 View，覆盖 Android 开发从基础控件到高级特性的方方面面；`app` 主模块已二次开发为股票历史数据分析工具。

## Tech Stack

### 核心技术
- **Android SDK**: compileSdk 34 / minSdk 23 / targetSdk 34（例外：`device` 为 33/23/33，`hezi` 为 34/26/34）
- **编程语言**: Java 为主；`app`、`customview` 两个模块含 Kotlin（Kotlin 1.8.22）
- **构建工具**: Gradle 8.0 + Android Gradle Plugin 7.4.2
- **JDK**: Java 8 源码兼容（`sourceCompatibility=1.8`、`jvmTarget=1.8`）；本机以 JDK 17 构建
- **支持库**: AndroidX（`android.useAndroidX=true`、`android.enableJetifier=true`），已完成从 support 库的迁移
- **UI 绑定**: ViewBinding（`app`、`device`、`customview`、`hezi`、`group` 五个模块启用）
- **仓库源**: 阿里云 Maven 镜像优先，Google / MavenCentral / JitPack 兜底

### 模块结构
21 个模块参与构建（19 个 application + 2 个 library），`thirdsdk` 在 settings.gradle 中已注释。按章节划分：

| 模块 | 定位 | 模块 | 定位 |
| --- | --- | --- | --- |
| app | 主模块：股票分析工具（入口 AlarmActivity） | group | 第7章 组合控件 + 三方库 |
| junior | 第2章 初级控件 | test | 第8章 调试与上线 / 加密 |
| middle | 第3章 中级控件 | device | 第9章 设备操作 |
| storage | 第4章 数据存储 | network | 第10章 网络通信 |
| senior | 第5章 高级控件 | event | 第11章 事件处理 |
| custom | 第6章 自定义控件 | animation | 第12章 动画 |
| media | 第13章 多媒体 | mixture | 第14章 融合技术（JNI/WebView/PDF） |
| weixin | 微信 SDK（分享/支付/授权） | performance | 第16章 性能优化 |
| util | 60+ 通用工具类 | customview | 高级自定义 View 合集 |
| hezi | 独立 App（Lottie + 本地 AAR） | CustomToast | library：自定义 Toast |
| filedialog | library：文件对话框 | thirdsdk | 第15章 三方 SDK（默认不构建） |

`app` 主模块核心文件：
```
app/src/main/java/com/example/helloworld/
├── AlarmActivity.java    — 启动入口，5 分钟循环闹钟 + 股票分析按钮
├── JDK.java              — 股票回测算法核心（约 438 行）
├── Constants.kt          — 硬编码股票价格数据（Kotlin object）
├── TimeChangeReceiver.java — 时间变更广播接收器
├── CircleCornerView.java — 自定义圆角 View
├── GenerateValueFiles.java — 生成资源值文件工具
└── MainActivity.java     — 演示页（非启动页）
app/src/main/assets/      — 7 个股票历史数据 txt 文件
```

## Project Conventions

### Code Style
- Java 为主，Kotlin 仅用于 app / customview
- Activity 命名: `*Activity.java`
- 5 个模块使用 ViewBinding 替代 findViewById
- 模块入口页面多为按钮目录式 MainActivity，点击跳转各功能演示 Activity

### Architecture Patterns
- 简单 Activity 直接处理逻辑，无 MVVM 分层
- 耗时操作通过 `new Thread()` 执行
- library 模块：`filedialog`（被 event/network/media/mixture 依赖）、`CustomToast`（被 group 依赖）

### Git Workflow
- 主分支: master
- 提交格式: `AndroidDemo: 描述` 或 `feat: 描述`

### Build Notes
- 全量构建并非全绿；历史日志显示 33 成功 / 9 失败，建议按模块构建
- 详细的构建状态与已知问题见仓库根目录 `快速上手文档.md`
- 规范驱动的变更流程见 `openspec/config.yaml`
