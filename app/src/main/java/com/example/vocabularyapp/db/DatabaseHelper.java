package com.example.vocabularyapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vocabularyapp.model.Word;
import com.example.vocabularyapp.model.StudyRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮助类
 * 用于管理SQLite数据库的创建、升级和基本操作
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库信息
    private static final String DATABASE_NAME = "VocabularyDB";
    private static final int DATABASE_VERSION = 2;

    // 单词表
    private static final String TABLE_WORDS = "words";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_PHONETIC = "phonetic";
    private static final String COLUMN_MEANING = "meaning";
    private static final String COLUMN_EXAMPLE = "example";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";
    private static final String COLUMN_IS_LEARNED = "is_learned";
    private static final String COLUMN_STUDY_COUNT = "study_count";

    // 学习记录表
    private static final String TABLE_RECORDS = "study_records";
    private static final String COLUMN_RECORD_ID = "id";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_WORD_COUNT = "word_count";
    private static final String COLUMN_TEST_SCORE = "test_score";
    private static final String COLUMN_TEST_TOTAL = "test_total";
    private static final String COLUMN_TYPE = "type";

    // 创建单词表的SQL语句
    private static final String CREATE_TABLE_WORDS =
            "CREATE TABLE " + TABLE_WORDS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WORD + " TEXT NOT NULL, " +
            COLUMN_PHONETIC + " TEXT, " +
            COLUMN_MEANING + " TEXT NOT NULL, " +
            COLUMN_EXAMPLE + " TEXT, " +
            COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0, " +
            COLUMN_IS_LEARNED + " INTEGER DEFAULT 0, " +
            COLUMN_STUDY_COUNT + " INTEGER DEFAULT 0)";

    // 创建学习记录表的SQL语句
    private static final String CREATE_TABLE_RECORDS =
            "CREATE TABLE " + TABLE_RECORDS + " (" +
            COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TIMESTAMP + " INTEGER, " +
            COLUMN_WORD_COUNT + " INTEGER, " +
            COLUMN_TEST_SCORE + " INTEGER, " +
            COLUMN_TEST_TOTAL + " INTEGER, " +
            COLUMN_TYPE + " TEXT)";

    // 创建进度表的SQL语句
    private static final String CREATE_TABLE_PROGRESS =
            "CREATE TABLE progress (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "key TEXT UNIQUE, " +
            "value TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_WORDS);
        db.execSQL(CREATE_TABLE_RECORDS);
        db.execSQL(CREATE_TABLE_PROGRESS);
        
        // 插入预置单词数据
        insertDefaultWords(db);
    }
    
    /**
     * 插入预置单词数据
     * 当数据库首次创建时自动插入一些示例单词
     */
    private void insertDefaultWords(SQLiteDatabase db) {
        // 预置单词数据（包含常用单词）
        String[][] wordsData = {
            {"the", "[ðə;ði:]", "art. 这；那；adv. 更加（用于比较级，最高级前）", ""},
            {"be", "[bi:]", "prep. 在，存在；是", ""},
            {"and", "[ənd; (ə)n; ænd]", "conj. 和，与；而且；然后；就；但是", ""},
            {"of", "[ɒv; (ə)v]", "prep. 属于；关于；...的；由...组成的", ""},
            {"a", "[ə; eɪ]", "art. 一；任一；每一", ""},
            {"in", "[ɪn]", "prep. 在...之内；从事于；按照（表示方式）", ""},
            {"to", "[tə;tu;tu:]", "prep. 向；到；趋于；达到", ""},
            {"have", "[hæv;həv]", "vt. 有；让；从事；允许；拿", ""},
            {"it", "[ɪt]", "pron. 它；这；那", ""},
            {"that", "[ðæt;ðət]", "adj. 那；那个；pron. 那；那个", ""},
            {"for", "[fɔː;fə]", "prep. 为了；给；对；供", ""},
            {"not", "[nɒt]", "adv. 不；没有", ""},
            {"on", "[ɒn;ɔːn]", "prep. 在...上；关于；一...就", ""},
            {"with", "[wɪð;wɪθ]", "prep. 和...一起；用；具有", ""},
            {"he", "[hi:]", "pron. 他", ""},
            {"as", "[æz;əz]", "adv. 和...一样；prep. 作为", ""},
            {"you", "[ju:;jʊ]", "pron. 你；你们", ""},
            {"do", "[du:;dʊ]", "v. 做；干；从事", ""},
            {"at", "[æt;ət]", "prep. 在；向；以", ""},
            {"this", "[ðɪs]", "adj. 这；这个；pron. 这；这个", ""},
            {"but", "[bʌt;bət]", "conj. 但是；然而；prep. 除...以外", ""},
            {"his", "[hɪz]", "adj. 他的；pron. 他的", ""},
            {"by", "[baɪ]", "prep. 由；被；通过；用", ""},
            {"from", "[frɒm;frəm]", "prep. 从；来自；由于", ""},
            {"they", "[ðeɪ]", "pron. 他们；她们；它们", ""},
            {"we", "[wi:;wi]", "pron. 我们", ""},
            {"say", "[seɪ]", "v. 说；讲；表明", ""},
            {"her", "[hɜː;hə]", "pron. 她；她的", ""},
            {"she", "[ʃi:]", "pron. 她", ""},
            {"or", "[ɔː;ə]", "conj. 或者；否则", ""},
            {"an", "[æn;ən]", "art. 一（用于元音音素前）", ""},
            {"will", "[wɪl]", "v. 将要；愿意；aux. 将", ""},
            {"my", "[maɪ]", "adj. 我的", ""},
            {"one", "[wʌn]", "num. 一；一个", ""},
            {"all", "[ɔːl]", "adj. 所有的；adv. 完全", ""},
            {"would", "[wʊd;wəd]", "v. 将；会；愿意", ""},
            {"there", "[ðeə;ðɛə]", "adv. 在那里；存在", ""},
            {"their", "[ðeə;ðɛə]", "adj. 他们的；她们的；它们的", ""},
            {"what", "[wɒt]", "pron. 什么；adj. 什么", ""},
            {"so", "[səʊ]", "adv. 如此；这么；conj. 因此", ""},
            {"up", "[ʌp]", "adv. 向上；prep. 沿着...向上", ""},
            {"out", "[aʊt]", "adv. 出去；向外；prep. 超过", ""},
            {"if", "[ɪf]", "conj. 如果；是否", ""},
            {"about", "[əˈbaʊt]", "prep. 关于；大约", ""},
            {"who", "[huː;hu]", "pron. 谁", ""},
            {"get", "[get]", "v. 得到；获得；变得", ""},
            {"which", "[wɪtʃ]", "pron. 哪一个；adj. 哪一个", ""},
            {"go", "[gəʊ]", "v. 去；走；进行", ""},
            {"me", "[mi:;mi]", "pron. 我（宾格）", ""},
            {"when", "[wen]", "adv. 什么时候；conj. 当...时候", ""},
            {"make", "[meɪk]", "v. 做；制造；使", ""},
            {"can", "[kæn;kən]", "v. 能够；可以", ""},
            {"like", "[laɪk]", "v. 喜欢；prep. 像", ""},
            {"time", "[taɪm]", "n. 时间；次；时期", ""},
            {"no", "[nəʊ]", "adv. 不；否定", ""},
            {"just", "[dʒʌst]", "adv. 仅仅；刚刚；正好", ""},
            {"him", "[hɪm]", "pron. 他（宾格）", ""},
            {"know", "[nəʊ]", "v. 知道；了解", ""},
            {"take", "[teɪk]", "v. 拿；取；接受", ""},
            {"people", "[ˈpiːpl]", "n. 人们；人民", ""},
            {"into", "[ˈɪntu;ˈɪntə]", "prep. 进入；到...里面", ""},
            {"year", "[jɪə;jɜː]", "n. 年；年度", ""},
            {"your", "[jɔː;jʊə]", "adj. 你的；你们的", ""},
            {"good", "[gʊd]", "adj. 好的；优秀的", ""},
            {"some", "[sʌm;səm]", "adj. 一些；某些", ""},
            {"could", "[kʊd;kəd]", "v. 能够（can的过去式）", ""},
            {"them", "[ðem;ðəm]", "pron. 他们（宾格）", ""},
            {"than", "[ðæn;ðən]", "conj. 比；prep. 比", ""},
            {"then", "[ðen]", "adv. 然后；那时", ""},
            {"now", "[naʊ]", "adv. 现在；此刻", ""},
            {"look", "[lʊk]", "v. 看；看起来；n. 看", ""},
            {"only", "[ˈəʊnli]", "adv. 仅仅；只", ""},
            {"come", "[kʌm]", "v. 来；来到", ""},
            {"its", "[ɪts]", "adj. 它的", ""},
            {"over", "[ˈəʊvə]", "prep. 在...上方；adv. 结束", ""},
            {"think", "[θɪŋk]", "v. 想；认为；思考", ""},
            {"also", "[ˈɔːlsəʊ]", "adv. 也；同样", ""},
            {"back", "[bæk]", "adv. 回来；向后", ""},
            {"after", "[ˈɑːftə;ˈæftə]", "prep. 在...之后", ""},
            {"use", "[juːz;jʊs]", "v. 使用；利用", ""},
            {"two", "[tu:]", "num. 二；两个", ""},
            {"how", "[haʊ]", "adv. 如何；怎样", ""},
            {"our", "[ˈaʊə]", "adj. 我们的", ""},
            {"work", "[wɜːk]", "n. 工作；v. 工作", ""},
            {"first", "[fɜːst]", "adj. 第一；adv. 首先", ""},
            {"well", "[wel]", "adv. 好；adj. 健康的", ""},
            {"way", "[weɪ]", "n. 路；方法；方式", ""},
            {"even", "[ˈiːvn]", "adv. 甚至；即使", ""},
            {"new", "[njuː;nuː]", "adj. 新的", ""},
            {"want", "[wɒnt;wɑːnt]", "v. 想要；需要", ""},
            {"because", "[bɪˈkɒz;bɪˈkʌz]", "conj. 因为", ""},
            {"any", "[ˈeni]", "adj. 任何；一些", ""},
            {"these", "[ðiːz]", "adj. 这些；pron. 这些", ""},
            {"give", "[ɡɪv]", "v. 给；给予", ""},
            {"day", "[deɪ]", "n. 天；日子；白天", ""},
            {"most", "[məʊst]", "adj. 最多的；adv. 最", ""},
            {"us", "[ʌs;əs]", "pron. 我们（宾格）", ""},
        };
        
        // 插入数据
        for (String[] data : wordsData) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_WORD, data[0]);
            values.put(COLUMN_PHONETIC, data[1]);
            values.put(COLUMN_MEANING, data[2]);
            values.put(COLUMN_EXAMPLE, data[3]);
            values.put(COLUMN_IS_FAVORITE, 0);
            values.put(COLUMN_IS_LEARNED, 0);
            values.put(COLUMN_STUDY_COUNT, 0);
            db.insert(TABLE_WORDS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果是从旧版本升级，添加新表
        if (oldVersion < 2) {
            db.execSQL(CREATE_TABLE_PROGRESS);
        }
    }

    // ==================== 单词相关操作 ====================

    /**
     * 添加单词
     */
    public long addWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, word.getWord());
        values.put(COLUMN_PHONETIC, word.getPhonetic());
        values.put(COLUMN_MEANING, word.getMeaning());
        values.put(COLUMN_EXAMPLE, word.getExample());
        values.put(COLUMN_IS_FAVORITE, word.isFavorite() ? 1 : 0);
        values.put(COLUMN_IS_LEARNED, word.isLearned() ? 1 : 0);
        values.put(COLUMN_STUDY_COUNT, word.getStudyCount());

        long id = db.insert(TABLE_WORDS, null, values);
        db.close();
        return id;
    }

    /**
     * 获取所有单词
     */
    public List<Word> getAllWords() {
        List<Word> wordList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WORDS + " ORDER BY " + COLUMN_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                word.setWord(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD)));
                word.setPhonetic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONETIC)));
                word.setMeaning(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEANING)));
                word.setExample(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAMPLE)));
                word.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1);
                word.setLearned(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_LEARNED)) == 1);
                word.setStudyCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDY_COUNT)));

                wordList.add(word);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return wordList;
    }

    /**
     * 搜索单词
     */
    public List<Word> searchWords(String keyword) {
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_WORDS +
                " WHERE " + COLUMN_WORD + " LIKE ? OR " + COLUMN_MEANING + " LIKE ?" +
                " ORDER BY " + COLUMN_WORD;
        String[] selectionArgs = new String[]{"%" + keyword + "%", "%" + keyword + "%"};

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                word.setWord(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD)));
                word.setPhonetic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONETIC)));
                word.setMeaning(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEANING)));
                word.setExample(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAMPLE)));
                word.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1);
                word.setLearned(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_LEARNED)) == 1);
                word.setStudyCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDY_COUNT)));

                wordList.add(word);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return wordList;
    }

    /**
     * 获取收藏的单词
     */
    public List<Word> getFavoriteWords() {
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_WORDS +
                " WHERE " + COLUMN_IS_FAVORITE + " = 1" +
                " ORDER BY " + COLUMN_ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                word.setWord(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD)));
                word.setPhonetic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONETIC)));
                word.setMeaning(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEANING)));
                word.setExample(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAMPLE)));
                word.setFavorite(true);
                word.setLearned(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_LEARNED)) == 1);
                word.setStudyCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDY_COUNT)));

                wordList.add(word);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return wordList;
    }

    /**
     * 更新单词收藏状态
     */
    public void updateFavorite(int wordId, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);

        db.update(TABLE_WORDS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(wordId)});
        db.close();
    }

    /**
     * 更新单词学习状态
     */
    public void updateLearned(int wordId, boolean isLearned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_LEARNED, isLearned ? 1 : 0);

        db.update(TABLE_WORDS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(wordId)});
        db.close();
    }

    /**
     * 增加学习次数
     */
    public void incrementStudyCount(int wordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_WORDS +
                " SET " + COLUMN_STUDY_COUNT + " = " + COLUMN_STUDY_COUNT + " + 1" +
                " WHERE " + COLUMN_ID + " = " + wordId;

        db.execSQL(updateQuery);
        db.close();
    }

    /**
     * 获取单词总数
     */
    public int getTotalWordCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_WORDS;
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    /**
     * 获取已学单词数
     */
    public int getLearnedWordCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_WORDS +
                " WHERE " + COLUMN_IS_LEARNED + " = 1";
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    /**
     * 获取收藏单词数
     */
    public int getFavoriteWordCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_WORDS +
                " WHERE " + COLUMN_IS_FAVORITE + " = 1";
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    /**
     * 删除单词
     */
    public void deleteWord(int wordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORDS, COLUMN_ID + " = ?", new String[]{String.valueOf(wordId)});
        db.close();
    }

    /**
     * 清空所有单词
     */
    public void clearAllWords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORDS, null, null);
        db.close();
    }

    // ==================== 学习记录相关操作 ====================

    /**
     * 添加学习记录
     */
    public long addStudyRecord(StudyRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, record.getTimestamp());
        values.put(COLUMN_WORD_COUNT, record.getWordCount());
        values.put(COLUMN_TEST_SCORE, record.getTestScore());
        values.put(COLUMN_TEST_TOTAL, record.getTestTotal());
        values.put(COLUMN_TYPE, record.getType());

        long id = db.insert(TABLE_RECORDS, null, values);
        db.close();
        return id;
    }

    /**
     * 获取测试次数
     */
    public int getTestCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_RECORDS +
                " WHERE " + COLUMN_TYPE + " = 'test'";
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    /**
     * 获取平均测试得分
     */
    public double getAverageTestScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        String avgQuery = "SELECT AVG(" + COLUMN_TEST_SCORE + ") FROM " + TABLE_RECORDS +
                " WHERE " + COLUMN_TYPE + " = 'test'";
        Cursor cursor = db.rawQuery(avgQuery, null);

        double avg = 0;
        if (cursor.moveToFirst()) {
            avg = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return avg;
    }

    /**
     * 获取所有学习记录
     */
    public List<StudyRecord> getAllStudyRecords() {
        List<StudyRecord> recordList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECORDS + " ORDER BY " + COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                StudyRecord record = new StudyRecord();
                record.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECORD_ID)));
                record.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)));
                record.setWordCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WORD_COUNT)));
                record.setTestScore(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEST_SCORE)));
                record.setTestTotal(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEST_TOTAL)));
                record.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));

                recordList.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recordList;
    }

    // ==================== 学习进度相关操作 ====================

    /**
     * 保存学习进度
     */
    public void saveStudyProgress(int wordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("key", "last_study_word_id");
        values.put("value", wordId);

        // 先删除旧记录
        db.delete("progress", "key = ?", new String[]{"last_study_word_id"});
        // 插入新记录
        db.insert("progress", null, values);
        db.close();
    }

    /**
     * 获取上次学习的单词ID
     */
    public int getLastStudyWordId() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT value FROM progress WHERE key = 'last_study_word_id'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        int wordId = 0;
        if (cursor.moveToFirst()) {
            wordId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return wordId;
    }

    /**
     * 检查单词是否已收藏
     */
    public boolean isWordFavorite(int wordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT " + COLUMN_IS_FAVORITE + " FROM " + TABLE_WORDS +
                " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(wordId)});

        boolean isFavorite = false;
        if (cursor.moveToFirst()) {
            isFavorite = cursor.getInt(0) == 1;
        }

        cursor.close();
        db.close();
        return isFavorite;
    }

    /**
     * 添加单词到收藏
     */
    public void addFavorite(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, 1);

        db.update(TABLE_WORDS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(word.getId())});
        db.close();
    }

    /**
     * 更新测试次数统计
     */
    public void updateTestCount(int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("key", "test_count");
        values.put("value", count);

        // 先删除旧记录
        db.delete("progress", "key = ?", new String[]{"test_count"});
        // 插入新记录
        db.insert("progress", null, values);
        db.close();
    }
}