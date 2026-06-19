package com.example.vocabularyapp.ui.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocabularyapp.R;
import com.example.vocabularyapp.db.DatabaseHelper;
import com.example.vocabularyapp.ui.main.FavoriteActivity;
import com.example.vocabularyapp.ui.main.WordListActivity;
import com.example.vocabularyapp.ui.study.StudyActivity;
import com.example.vocabularyapp.ui.test.TestActivity;

/**
 * 统计Activity
 * 展示用户的学习统计数据
 */
public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    // UI组件
    private ImageButton btnBack;
    private TextView tvTotalWords;
    private TextView tvLearnedWords;
    private TextView tvFavoriteWords;
    private TextView tvTestCount;
    private TextView tvAverageScore;

    // 底部导航栏组件
    private View tabStudy;
    private View tabTest;
    private View tabList;
    private View tabStats;
    private View tabFavorite;

    // 数据组件
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // 初始化数据库帮助类
        databaseHelper = new DatabaseHelper(this);

        // 初始化UI组件
        initViews();

        // 加载统计数据
        loadStatistics();
    }

    /**
     * 初始化UI组件
     */
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTotalWords = findViewById(R.id.tvTotalWords);
        tvLearnedWords = findViewById(R.id.tvLearnedWords);
        tvFavoriteWords = findViewById(R.id.tvFavoriteWords);
        tvTestCount = findViewById(R.id.tvTestCount);
        tvAverageScore = findViewById(R.id.tvAverageScore);

        // 底部导航栏组件
        tabStudy = findViewById(R.id.tabStudy);
        tabTest = findViewById(R.id.tabTest);
        tabList = findViewById(R.id.tabList);
        tabStats = findViewById(R.id.tabStats);
        tabFavorite = findViewById(R.id.tabFavorite);

        // 返回按钮点击事件
        btnBack.setOnClickListener(this);

        // 底部导航栏点击监听
        tabStudy.setOnClickListener(this);
        tabTest.setOnClickListener(this);
        tabList.setOnClickListener(this);
        tabStats.setOnClickListener(this);
        tabFavorite.setOnClickListener(this);
    }

    /**
     * 加载统计数据
     */
    private void loadStatistics() {
        // 获取总单词数
        int totalWords = databaseHelper.getTotalWordCount();
        tvTotalWords.setText(String.valueOf(totalWords));

        // 获取已学单词数
        int learnedWords = databaseHelper.getLearnedWordCount();
        tvLearnedWords.setText(String.valueOf(learnedWords));

        // 获取收藏单词数
        int favoriteWords = databaseHelper.getFavoriteWordCount();
        tvFavoriteWords.setText(String.valueOf(favoriteWords));

        // 获取测试次数
        int testCount = databaseHelper.getTestCount();
        tvTestCount.setText(String.valueOf(testCount));

        // 获取平均测试得分
        double averageScore = databaseHelper.getAverageTestScore();
        if (averageScore > 0) {
            tvAverageScore.setText(String.format("%.1f", averageScore));
        } else {
            tvAverageScore.setText("0");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            // 返回按钮，关闭Activity
            finish();
        } else if (v.getId() == R.id.tabStudy) {
            // 学习界面
            Intent intent = new Intent(this, StudyActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabTest) {
            // 单词测试
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabList) {
            // 单词列表
            Intent intent = new Intent(this, WordListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabStats) {
            // 学习统计（当前界面）
            // 不做任何操作
        } else if (v.getId() == R.id.tabFavorite) {
            // 生词收藏
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭数据库连接
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}