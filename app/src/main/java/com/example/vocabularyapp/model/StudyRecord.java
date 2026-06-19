package com.example.vocabularyapp.model;

/**
 * 学习记录数据模型类
 * 用于记录用户的学习进度和测试成绩
 */
public class StudyRecord {
    private int id;             // 记录ID
    private long timestamp;     // 学习时间戳
    private int wordCount;      // 学习单词数量
    private int testScore;      // 测试得分
    private int testTotal;      // 测试总数
    private String type;        // 类型：study或test

    // 构造方法
    public StudyRecord() {
    }

    public StudyRecord(long timestamp, int wordCount, String type) {
        this.timestamp = timestamp;
        this.wordCount = wordCount;
        this.type = type;
    }

    public StudyRecord(long timestamp, int testScore, int testTotal, String type) {
        this.timestamp = timestamp;
        this.testScore = testScore;
        this.testTotal = testTotal;
        this.type = type;
    }

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getTestScore() {
        return testScore;
    }

    public void setTestScore(int testScore) {
        this.testScore = testScore;
    }

    public int getTestTotal() {
        return testTotal;
    }

    public void setTestTotal(int testTotal) {
        this.testTotal = testTotal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StudyRecord{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", wordCount=" + wordCount +
                ", testScore=" + testScore +
                ", testTotal=" + testTotal +
                ", type='" + type + '\'' +
                '}';
    }
}