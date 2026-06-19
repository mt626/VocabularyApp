package com.example.vocabularyapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocabularyapp.R;
import com.example.vocabularyapp.adapter.WordAdapter;
import com.example.vocabularyapp.db.DatabaseHelper;
import com.example.vocabularyapp.model.Word;
import com.example.vocabularyapp.ui.statistics.StatisticsActivity;
import com.example.vocabularyapp.ui.study.StudyActivity;
import com.example.vocabularyapp.ui.test.TestActivity;

import java.util.List;

/**
 * 单词列表Activity
 * 展示所有单词，支持搜索功能
 */
public class WordListActivity extends AppCompatActivity implements View.OnClickListener {

    // UI组件
    private ImageButton btnBack;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private TextView tvEmpty;

    // 底部导航栏组件
    private View tabStudy;
    private View tabTest;
    private View tabList;
    private View tabStats;
    private View tabFavorite;

    // 数据组件
    private DatabaseHelper databaseHelper;
    private WordAdapter wordAdapter;
    private List<Word> wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        // 初始化数据库帮助类
        databaseHelper = new DatabaseHelper(this);

        // 初始化UI组件
        initViews();

        // 设置RecyclerView
        setupRecyclerView();

        // 加载单词数据
        loadWords();

        // 设置搜索监听
        setSearchListener();
    }

    /**
     * 初始化UI组件
     */
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);

        // 底部导航栏组件
        tabStudy = findViewById(R.id.tabStudy);
        tabTest = findViewById(R.id.tabTest);
        tabList = findViewById(R.id.tabList);
        tabStats = findViewById(R.id.tabStats);
        tabFavorite = findViewById(R.id.tabFavorite);

        // 返回按钮点击事件
        btnBack.setOnClickListener(v -> finish());

        // 底部导航栏点击监听
        tabStudy.setOnClickListener(this);
        tabTest.setOnClickListener(this);
        tabList.setOnClickListener(this);
        tabStats.setOnClickListener(this);
        tabFavorite.setOnClickListener(this);
    }

    /**
     * 设置RecyclerView
     */
    private void setupRecyclerView() {
        // 创建适配器
        wordAdapter = new WordAdapter();

        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 设置适配器
        recyclerView.setAdapter(wordAdapter);

        // 设置点击监听
        wordAdapter.setOnWordClickListener(new WordAdapter.OnWordClickListener() {
            @Override
            public void onWordClick(Word word, int position) {
                // 点击单词项，可以跳转到详情页或学习页
                // 这里简化处理
            }

            @Override
            public void onFavoriteClick(Word word, int position) {
                // 点击收藏图标，更新收藏状态
                boolean newFavoriteStatus = !word.isFavorite();
                databaseHelper.updateFavorite(word.getId(), newFavoriteStatus);
                word.setFavorite(newFavoriteStatus);
                wordAdapter.notifyItemChanged(position);
            }
        });
    }

    /**
     * 加载单词数据
     */
    private void loadWords() {
        // 从数据库获取所有单词
        wordList = databaseHelper.getAllWords();

        // 更新适配器数据
        wordAdapter.setWordList(wordList);

        // 显示或隐藏空状态提示
        updateEmptyView();
    }

    /**
     * 设置搜索监听
     */
    private void setSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 搜索单词
                searchWords(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 搜索单词
     */
    private void searchWords(String keyword) {
        if (keyword.isEmpty()) {
            // 如果搜索关键词为空，显示所有单词
            wordList = databaseHelper.getAllWords();
        } else {
            // 根据关键词搜索单词
            wordList = databaseHelper.searchWords(keyword);
        }

        // 更新适配器数据
        wordAdapter.setWordList(wordList);

        // 显示或隐藏空状态提示
        updateEmptyView();
    }

    /**
     * 更新空状态提示
     */
    private void updateEmptyView() {
        if (wordList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tabStudy) {
            // 学习界面
            Intent intent = new Intent(this, StudyActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabTest) {
            // 单词测试
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabList) {
            // 单词列表（当前界面）
            // 不做任何操作或刷新列表
        } else if (v.getId() == R.id.tabStats) {
            // 学习统计
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
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