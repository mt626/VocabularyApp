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

### 📊学习数据分析

- **学习统计**: 展示总单词数、已学单词数、学习进度
- **测试记录**: 记录测试次数、平均得分、最高分
- **可视化展示**: 直观呈现学习成果和进步趋势

## 技术架构

### 技术栈

- **开发框架**: Android Native (Java)
- **数据库**: SQLite
- **UI 组件**: Material Design、CardView、RecyclerView
- **Excel 解析**: Apache POI

### 📖项目结构

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

### 💾部署步骤

1. 克隆或下载项目到本地
2. 使用 Android Studio 打开项目
3. 等待 Gradle 同步完成
4. 连接 Android 设备或启动模拟器
5. 点击 Run 按钮运行应用

### 📥数据导入

1. 准备符合格式的 Excel 文件（参见 Excel 文件格式说明）
2. 将 Excel 文件上传到手机模拟器（参见下方"文件上传至模拟器"说明）
3. 在应用中点击"导入单词"按钮
4. 选择模拟器中的 Excel 文件完成导入

#### 文件上传至模拟器

若需要在模拟器中测试导入功能，可先将本地 Excel 文件上传至模拟器存储目录，常用方法如下：

**方法一：通过 Android Studio 的 Device File Explorer**
1. 点击 Android Studio 右下角的 **Device File Explorer**
2. 展开模拟器目录，进入 `sdcard/Download/` 或 `storage/emulated/0/Download/`
3. 右键目标文件夹，选择 **Upload...**，然后选择本地的 `.xlsx` 文件
4. 上传完成后，在模拟器的"文件管理"中即可看到该文件

**方法二：通过 adb 命令推送**
打开终端，执行以下命令（将 `path/to/words.xlsx` 替换为本地文件实际路径）：
```bash
adb push path/to/words.xlsx /sdcard/Download/
```
推送完成后，在模拟器文件管理器中进入 **Download** 文件夹即可找到该文件。

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

## 🔌 API 接口说明

### 数据库操作接口

#### Word 相关操作

| 方法名                                 | 功能描述       | 参数                           | 返回值           |
| -------------------------------------- | -------------- | ------------------------------ | ---------------- |
| `addWord(Word word)`                   | 添加单词       | `Word`: 单词对象               | `long`: 插入的ID |
| `getAllWords()`                        | 获取所有单词   | 无                             | `List<Word>`     |
| `searchWords(String keyword)`          | 搜索单词       | `keyword`: 搜索关键词          | `List<Word>`     |
| `getFavoriteWords()`                   | 获取收藏单词   | 无                             | `List<Word>`     |
| `updateFavorite(int id, boolean flag)` | 更新收藏状态   | `id`: 单词ID, `flag`: 是否收藏 | `int`: 影响行数  |
| `updateDifficulty(int id, int level)`  | 更新难度等级   | `id`: 单词ID, `level`: 难度    | `int`: 影响行数  |
| `getTotalWordCount()`                  | 获取单词总数   | 无                             | `int`: 数量      |
| `getLearnedWordCount()`                | 获取已学单词数 | 无                             | `int`: 数量      |

#### StudyRecord 相关操作

| 方法名                               | 功能描述     | 参数                    | 返回值           |
| ------------------------------------ | ------------ | ----------------------- | ---------------- |
| `addStudyRecord(StudyRecord record)` | 添加学习记录 | `StudyRecord`: 记录对象 | `long`: 插入的ID |
| `getTestCount()`                     | 获取测试次数 | 无                      | `int`: 次数      |
| `getAverageScore()`                  | 获取平均得分 | 无                      | `double`: 平均分 |
| `getHighestScore()`                  | 获取最高分   | 无                      | `int`: 最高分    |

### Excel 导入接口

| 方法名                        | 功能描述        | 参数           | 返回值              |
| ----------------------------- | --------------- | -------------- | ------------------- |
| `importFromExcel(Uri uri)`    | 从Excel导入单词 | `uri`: 文件URI | `int`: 导入数量     |
| `isValidExcelFormat(Uri uri)` | 验证Excel格式   | `uri`: 文件URI | `boolean`: 是否有效 |

---

## ✨ 特色亮点

1. **用户体验优先**: 采用莫兰迪色系设计，界面清新舒适，长时间学习不易疲劳
2. **智能学习算法**: 根据用户标记的难度自动调整复习策略
3. **数据安全**: 本地 SQLite 数据库存储，数据安全可靠
4. **灵活扩展**: 支持自定义词本导入，满足不同学习需求
5. **离线使用**: 无需网络连接，随时随地学习

---

**使用提示**: 首次使用请先导入单词数据。建议定期备份学习数据，以防数据丢失。
