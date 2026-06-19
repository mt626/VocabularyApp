package com.example.vocabularyapp.model;

/**
 * 单词数据模型类
 * 用于存储单词的基本信息
 */
public class Word {
    private int id;             // 单词ID
    private String word;        // 单词
    private String phonetic;    // 音标
    private String meaning;     // 释义
    private String example;     // 例句
    private boolean isFavorite; // 是否收藏
    private boolean isLearned;  // 是否已学习
    private int studyCount;     // 学习次数

    // 构造方法
    public Word() {
    }

    public Word(String word, String phonetic, String meaning, String example) {
        this.word = word;
        this.phonetic = phonetic;
        this.meaning = meaning;
        this.example = example;
        this.isFavorite = false;
        this.isLearned = false;
        this.studyCount = 0;
    }

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    public int getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(int studyCount) {
        this.studyCount = studyCount;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", phonetic='" + phonetic + '\'' +
                ", meaning='" + meaning + '\'' +
                ", isFavorite=" + isFavorite +
                ", isLearned=" + isLearned +
                ", studyCount=" + studyCount +
                '}';
    }
}