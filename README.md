# 📚 VocabularyApp - Android 英语单词学习应用

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![Database](https://img.shields.io/badge/Database-SQLite-blue.svg)](https://www.sqlite.org/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](https://opensource.org/licenses/MIT)

---

## 🌟 项目简介

**VocabularyApp** 是一款基于 Android 平台开发的英语单词学习应用，采用莫兰迪色系设计风格，界面清新舒适。应用提供完整的单词学习、智能测试、收藏管理和数据分析功能，帮助用户高效记忆英语单词。

### ✨ 核心特性

- **📖 卡片式学习**: 点击卡片切换显示/隐藏单词详情
- **🎯 智能测试**: 随机出题，即时反馈，自动收藏错题
- **📊 数据统计**: 学习进度、测试成绩一目了然
- **📥 批量导入**: 支持从 Excel 文件导入单词数据
- **💾 本地存储**: SQLite 数据库，数据安全可靠

---

## 🎯 功能模块

### 1. 单词学习模块

- 大字体展示单词、音标、释义及例句
- 点击卡片交互学习
- 实时显示学习进度（如 33/1405）
- "不熟"、"一般"、"熟记"三级难度标记

### 2. 智能测试系统

- 每次随机抽取 20 个单词
- 每题 5 分，满分 100 分
- 答题后立即显示正确答案
- 错误单词自动加入收藏

### 3. 单词管理功能

- 完整单词列表，支持实时搜索
- 便捷的收藏/取消收藏操作
- 支持从 Excel 文件批量导入

### 学习数据分析

- **学习统计**: 展示总单词数、已学单词数、学习进度
- **测试记录**: 记录测试次数、平均得分、最高分
- **可视化展示**: 直观呈现学习成果和进步趋势

## 技术架构

### 技术栈

- **开发框架**: Android Native (Java)
- **数据库**: SQLite
- **UI 组件**: Material Design、CardView、RecyclerView
- **Excel 解析**: Apache POI

### 项目结构

```
VocabularyApp/
├── app/src/main/
│   ├── java/com/example/vocabularyapp/
│   │   ├── model/          # 数据模型层
│   │   ├── db/             # 数据库操作层
│   │   ├── ui/             # 用户界面层
│   │   ├── adapter/        # 列表适配器
│   │   └── utils/          # 工具类
│   ├── res/                # 资源文件
│   │   ├── layout/         # 布局文件
│   │   ├── drawable/       # 图标和背景资源
│   │   └── values/         # 配置值
│   └── AndroidManifest.xml # 应用配置
└── build.gradle            # Gradle 构建配置
```

### 核心组件说明

| 组件               | 职责         | 关键文件                              |
| ------------------ | ------------ | ------------------------------------- |
| Word               | 单词数据模型 | model/Word.java                       |
| DatabaseHelper     | 数据库管理   | db/DatabaseHelper.java                |
| StudyActivity      | 学习界面     | ui/study/StudyActivity.java           |
| TestActivity       | 测试界面     | ui/test/TestActivity.java             |
| WordListActivity   | 单词列表     | ui/list/WordListActivity.java         |
| StatisticsActivity | 统计界面     | ui/statistics/StatisticsActivity.java |

## 安装与使用

### 环境要求

- Android Studio 4.0+
- Android SDK 24+ (Android 7.0 Nougat)
- Java Development Kit 8+

### 部署步骤

1. 克隆或下载项目到本地
2. 使用 Android Studio 打开项目
3. 等待 Gradle 同步完成
4. 连接 Android 设备或启动模拟器
5. 点击 Run 按钮运行应用

### 数据导入

1. 准备符合格式的 Excel 文件（参见 Excel 文件格式说明）
2. 在应用中点击"导入单词"按钮
3. 选择 Excel 文件完成导入

## Excel 文件格式说明

应用支持从 Excel 文件（.xlsx 格式）批量导入单词数据。详细格式规范请参考项目中的 **Excel文件格式说明.md** 文件。

## 功能流程图

```
学习流程:
首页 → 选择词本 → 学习模式 → 单词展示 → 标记难度 → 下一个单词

测试流程:
首页 → 开始测试 → 随机出题 → 选择答案 → 显示结果 → 统计得分

数据流程:
Excel导入 → 数据库存储 → 界面展示 → 用户操作 → 数据更新
```

## 特色亮点

1. **用户体验优先**: 采用莫兰迪色系设计，界面清新舒适，长时间学习不易疲劳
2. **智能学习算法**: 根据用户标记的难度自动调整复习策略
3. **数据安全**: 本地 SQLite 数据库存储，数据安全可靠
4. **灵活扩展**: 支持自定义词本导入，满足不同学习需求
5. **离线使用**: 无需网络连接，随时随地学习

**使用提示**: 首次使用请先导入单词数据。建议定期备份学习数据，以防数据丢失。
