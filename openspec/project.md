# Project Context

## Purpose
AndroidDemo：Android 开发学习项目，包含常用工具类、自定义 View、以及股票数据分析算法。

## Tech Stack

### 核心技术
- **Android SDK**: compileSdk 34, minSdk 23, targetSdk 34
- **编程语言**: Java
- **构建工具**: Gradle (build-tools 30.0.3)
- **支持库**: android.support.v7 (AppCompat)
- **UI 绑定**: ViewBinding

### 模块结构
app/
├── src/main/java/com/example/helloworld/
│   ├── AlarmActivity.java    — 股票分析入口页面，定时任务
│   ├── JDK.java              — 股票买卖策略算法核心
│   ├── Constants.java        — 股票价格数据
│   ├── MainActivity.java     — 主页面
│   ├── TimeChangeReceiver.java — 广播接收器
│   ├── CircleCornerView.java — 自定义圆角 View
│   └── GenerateValueFiles.java — 生成资源值文件工具
├── src/main/res/layout/
└── src/main/assets/          — 股票历史数据 txt 文件

## Project Conventions

### Code Style
- Java 代码，无 Kotlin
- Activity 命名: `*Activity.java`
- 使用 ViewBinding 替代 findViewById

### Architecture Patterns
- 简单 Activity 直接处理逻辑，无 MVVM 分层
- 耗时操作通过 `new Thread()` 执行

### Git Workflow
- 主分支: master
- 提交格式: `AndroidDemo: 描述` 或 `feat: 描述`
