package com.example.vocabularyapp.ui.study;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.vocabularyapp.R;
import com.example.vocabularyapp.db.DatabaseHelper;
import com.example.vocabularyapp.model.Word;
import com.example.vocabularyapp.model.StudyRecord;
import com.example.vocabularyapp.utils.ExcelImporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 学习Activity - 莫兰迪色系版本
 * 用于单词学习和背诵
 */
public class StudyActivity extends AppCompatActivity implements View.OnClickListener {

    // UI组件 - 侧边栏
    private ImageView ivAvatar;
    private TextView tvCheckIn;
    private TextView tvProgressPercent;
    private TextView tvNewWordCount;
    private TextView tvReviewCount;
    private TextView tvFavoriteCount;

    // 主区域组件
    private CardView cardWord;
    private TextView tvProgress;
    private TextView tvWordProgress;
    private ProgressBar progressBar;
    private TextView tvWord;
    private TextView tvPhonetic;
    private TextView tvPartOfSpeech;
    private TextView tvMeaning;
    private TextView tvMemory;
    private TextView tvExample;
    private Button btnForget;
    private Button btnNormal;
    private Button btnRemember;
    private Button btnPrevious;
    private Button btnFavorite;
    private Button btnNext;
    private TextView btnImport;

    // 单词信息显示状态
    private boolean isWordInfoVisible = false;

    // 底部导航栏组件
    private View tabStudy;
    private View tabTest;
    private View tabList;
    private View tabStats;
    private View tabFavorite;

    // 数据组件
    private DatabaseHelper databaseHelper;
    private List<Word> wordList;
    private int currentIndex = 0;
    private int totalWords;
    private int learnedWords;

    // 文件选择请求码
    private static final int REQUEST_CODE_PICK_EXCEL = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_new);

        // 初始化数据库帮助类
        databaseHelper = new DatabaseHelper(this);

        // 初始化UI组件
        initViews();

        // 设置点击监听
        setClickListeners();

        // 加载单词数据
        loadWords();

        // 更新统计数据
        updateStatistics();

        // 显示第一个单词
        showCurrentWord();
    }

    /**
     * 初始化UI组件
     */
    private void initViews() {
        // 侧边栏组件
        ivAvatar = findViewById(R.id.ivAvatar);
        tvCheckIn = findViewById(R.id.tvCheckIn);
        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        tvNewWordCount = findViewById(R.id.tvNewWordCount);
        tvReviewCount = findViewById(R.id.tvReviewCount);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);

        // 主区域组件
        cardWord = findViewById(R.id.cardWord);
        tvProgress = findViewById(R.id.tvProgress);
        tvWordProgress = findViewById(R.id.tvWordProgress);
        progressBar = findViewById(R.id.progressBar);
        tvWord = findViewById(R.id.tvWord);
        tvPhonetic = findViewById(R.id.tvPhonetic);
        tvPartOfSpeech = findViewById(R.id.tvPartOfSpeech);
        tvMeaning = findViewById(R.id.tvMeaning);
        tvMemory = findViewById(R.id.tvMemory);
        tvExample = findViewById(R.id.tvExample);
        btnForget = findViewById(R.id.btnForget);
        btnNormal = findViewById(R.id.btnNormal);
        btnRemember = findViewById(R.id.btnRemember);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnNext = findViewById(R.id.btnNext);
        btnImport = findViewById(R.id.btnImport);

        // 底部导航栏组件
        tabStudy = findViewById(R.id.tabStudy);
        tabTest = findViewById(R.id.tabTest);
        tabList = findViewById(R.id.tabList);
        tabStats = findViewById(R.id.tabStats);
        tabFavorite = findViewById(R.id.tabFavorite);
    }

    /**
     * 设置点击监听
     */
    private void setClickListeners() {
        // 卡片点击事件
        cardWord.setOnClickListener(this);
        
        btnForget.setOnClickListener(this);
        btnNormal.setOnClickListener(this);
        btnRemember.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnImport.setOnClickListener(this);

        // 底部导航栏点击监听
        tabTest.setOnClickListener(this);
        tabList.setOnClickListener(this);
        tabStats.setOnClickListener(this);
        tabFavorite.setOnClickListener(this);
    }

    /**
     * 加载单词数据
     */
    private void loadWords() {
        // 从数据库获取所有单词
        wordList = databaseHelper.getAllWords();
        totalWords = wordList.size();

        // 如果没有单词，提示用户
        if (wordList.isEmpty()) {
            Toast.makeText(this, "请先导入单词", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 加载上次学习的进度
        loadLastStudyProgress();
    }

    /**
     * 加载上次学习的进度
     */
    private void loadLastStudyProgress() {
        int lastWordId = databaseHelper.getLastStudyWordId();
        if (lastWordId > 0) {
            // 找到对应的单词索引
            for (int i = 0; i < wordList.size(); i++) {
                if (wordList.get(i).getId() == lastWordId) {
                    currentIndex = i;
                    break;
                }
            }
        }
        // 如果没找到或者lastWordId为0，就从0开始
    }

    /**
     * 更新统计数据
     */
    private void updateStatistics() {
        // 新词数（未学习的）
        int newWords = 0;
        // 复习数（已学习但需要复习的）
        int reviewWords = 0;
        // 收藏数
        int favoriteWords = 0;

        for (Word word : wordList) {
            if (word.isFavorite()) {
                favoriteWords++;
            }
            if (word.isLearned()) {
                reviewWords++;
            } else {
                newWords++;
            }
        }

        // 新词数限制为999
        if (newWords > 999) {
            newWords = 999;
        }
        if (reviewWords > 999) {
            reviewWords = 999;
        }
        if (favoriteWords > 999) {
            favoriteWords = 999;
        }

        // 更新UI
        tvNewWordCount.setText(String.valueOf(newWords));
        tvReviewCount.setText(String.valueOf(reviewWords));
        tvFavoriteCount.setText(String.valueOf(favoriteWords));

        // 更新学习进度
        learnedWords = reviewWords;
        int progressPercent = totalWords > 0 ? (learnedWords * 100 / totalWords) : 0;
        tvProgress.setText("进度 " + progressPercent + "%");
        progressBar.setProgress(progressPercent);
        tvProgressPercent.setText(progressPercent + "%");
    }

    /**
     * 显示当前单词
     */
    private void showCurrentWord() {
        if (wordList == null || wordList.isEmpty()) {
            return;
        }

        // 获取当前单词
        Word word = wordList.get(currentIndex);

        // 重置显示状态
        isWordInfoVisible = false;

        // 只显示单词
        tvWord.setText(word.getWord());

        // 隐藏其他信息
        tvPhonetic.setVisibility(View.INVISIBLE);
        tvPartOfSpeech.setVisibility(View.INVISIBLE);
        tvMeaning.setVisibility(View.INVISIBLE);
        tvMemory.setVisibility(View.INVISIBLE);
        tvExample.setVisibility(View.INVISIBLE);

        // 更新进度显示
        updateProgressDisplay();

        // 更新收藏按钮状态
        updateFavoriteButton(word);
    }

    /**
     * 切换单词信息显示状态
     */
    private void toggleWordInfo() {
        if (wordList == null || wordList.isEmpty()) {
            return;
        }

        isWordInfoVisible = !isWordInfoVisible;
        Word word = wordList.get(currentIndex);

        if (isWordInfoVisible) {
            // 显示所有信息
            tvPhonetic.setText(word.getPhonetic());
            tvPhonetic.setVisibility(View.VISIBLE);

            tvMeaning.setText(word.getMeaning());
            tvMeaning.setVisibility(View.VISIBLE);

            // 显示例句（如果有的话）
            String example = word.getExample();
            if (example != null && !example.isEmpty()) {
                tvExample.setText(example);
                tvExample.setVisibility(View.VISIBLE);
            } else {
                tvExample.setVisibility(View.GONE);
            }

            // 显示助记（如果有的话）
            String memory = extractMemory(word.getWord(), word.getMeaning());
            if (memory != null && !memory.isEmpty()) {
                tvMemory.setText(memory);
                tvMemory.setVisibility(View.VISIBLE);
            } else {
                tvMemory.setVisibility(View.GONE);
            }
        } else {
            // 隐藏所有信息
            tvPhonetic.setVisibility(View.INVISIBLE);
            tvPartOfSpeech.setVisibility(View.INVISIBLE);
            tvMeaning.setVisibility(View.INVISIBLE);
            tvMemory.setVisibility(View.INVISIBLE);
            tvExample.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 提取助记信息
     */
    private String extractMemory(String word, String meaning) {
        // 简单的助记提取逻辑
        // 如果有词根词缀信息就显示
        if (meaning.contains("词根") || meaning.contains("前缀") || meaning.contains("后缀")) {
            return meaning;
        }
        return "";
    }

    /**
     * 更新进度显示
     */
    private void updateProgressDisplay() {
        int current = currentIndex + 1;
        int remaining = totalWords - current;
        tvProgress.setText("进度 " + (current * 100 / totalWords) + "%");
        progressBar.setProgress(current * 100 / totalWords);
        tvProgressPercent.setText((current * 100 / totalWords) + "%");
        // 更新右上角单词进度显示
        tvWordProgress.setText(current + "/" + totalWords);
    }

    /**
     * 更新收藏按钮状态
     */
    private void updateFavoriteButton(Word word) {
        if (word.isFavorite()) {
            btnFavorite.setText("已收藏");
        } else {
            btnFavorite.setText("收藏");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cardWord) {
            // 点击卡片显示/隐藏单词信息
            toggleWordInfo();
        } else if (v.getId() == R.id.btnForget) {
            // 不熟按钮
            handleWordResponse(0);
        } else if (v.getId() == R.id.btnNormal) {
            // 一般按钮
            handleWordResponse(1);
        } else if (v.getId() == R.id.btnRemember) {
            // 熟记按钮
            handleWordResponse(2);
        } else if (v.getId() == R.id.btnPrevious) {
            // 上一个按钮
            if (currentIndex > 0) {
                currentIndex--;
                saveStudyProgress();
                showCurrentWord();
            } else {
                Toast.makeText(this, "已经是第一个单词", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnFavorite) {
            // 收藏按钮
            toggleFavorite();
        } else if (v.getId() == R.id.btnNext) {
            // 下一个按钮
            if (currentIndex < wordList.size() - 1) {
                currentIndex++;
                saveStudyProgress();
                showCurrentWord();
            } else {
                Toast.makeText(this, "恭喜！已完成所有单词的学习！", Toast.LENGTH_SHORT).show();
                saveStudyProgress();
            }
        } else if (v.getId() == R.id.btnImport) {
            // 导入单词
            openFileSelector();
        } else if (v.getId() == R.id.tabTest) {
            // 单词测试
            saveStudyProgress();
            Intent intent = new Intent(this, com.example.vocabularyapp.ui.test.TestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabList) {
            // 单词列表
            saveStudyProgress();
            Intent intent = new Intent(this, com.example.vocabularyapp.ui.main.WordListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabStats) {
            // 学习统计
            saveStudyProgress();
            Intent intent = new Intent(this, com.example.vocabularyapp.ui.statistics.StatisticsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabFavorite) {
            // 生词收藏
            saveStudyProgress();
            Intent intent = new Intent(this, com.example.vocabularyapp.ui.main.FavoriteActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 处理单词学习响应
     */
    private void handleWordResponse(int response) {
        if (wordList == null || wordList.isEmpty()) {
            return;
        }

        Word word = wordList.get(currentIndex);

        // 根据响应更新学习状态
        if (response == 2) { // 熟记
            databaseHelper.updateLearned(word.getId(), true);
            Toast.makeText(this, "太棒了！掌握了这个单词！", Toast.LENGTH_SHORT).show();
        } else if (response == 1) { // 一般
            databaseHelper.updateLearned(word.getId(), true);
            databaseHelper.incrementStudyCount(word.getId());
            Toast.makeText(this, "继续加油！", Toast.LENGTH_SHORT).show();
        } else { // 不熟
            databaseHelper.incrementStudyCount(word.getId());
            Toast.makeText(this, "这个单词还需要多复习哦~", Toast.LENGTH_SHORT).show();
        }

        // 自动跳转到下一个单词
        if (currentIndex < wordList.size() - 1) {
            currentIndex++;
            saveStudyProgress();
            showCurrentWord();
            updateStatistics();
        } else {
            Toast.makeText(this, "恭喜！已完成所有单词的学习！", Toast.LENGTH_SHORT).show();
            saveStudyProgress();
            finish();
        }
    }

    /**
     * 切换收藏状态
     */
    private void toggleFavorite() {
        if (wordList == null || wordList.isEmpty()) {
            return;
        }

        Word word = wordList.get(currentIndex);
        boolean newFavoriteStatus = !word.isFavorite();

        // 更新数据库
        databaseHelper.updateFavorite(word.getId(), newFavoriteStatus);

        // 更新单词对象
        word.setFavorite(newFavoriteStatus);

        // 更新按钮状态
        updateFavoriteButton(word);

        // 更新统计数据
        updateStatistics();

        // 提示用户
        if (newFavoriteStatus) {
            Toast.makeText(this, "已添加到生词本", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "已从生词本移除", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存当前学习进度
     */
    private void saveStudyProgress() {
        if (wordList != null && !wordList.isEmpty()) {
            int currentWordId = wordList.get(currentIndex).getId();
            databaseHelper.saveStudyProgress(currentWordId);
        }
    }

    /**
     * 打开文件选择器
     */
    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "选择Excel文件"),
                    REQUEST_CODE_PICK_EXCEL
            );
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请安装文件管理器应用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_EXCEL && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                Toast.makeText(this, "正在导入单词，请稍候...", Toast.LENGTH_SHORT).show();
                importWordsFromExcel(uri);
            }
        }
    }

    /**
     * 从Excel文件导入单词
     */
    private void importWordsFromExcel(Uri uri) {
        try {
            String filePath = getFilePathFromUri(uri);

            if (filePath != null) {
                ExcelImporter importer = new ExcelImporter(this);

                importer.importFromExcelAsync(filePath, new ExcelImporter.ImportCallback() {
                    @Override
                    public void onSuccess(int importCount) {
                        Toast.makeText(StudyActivity.this,
                                "导入成功！共导入 " + importCount + " 个单词",
                                Toast.LENGTH_LONG).show();
                        // 重新加载单词数据
                        loadWords();
                        updateStatistics();
                        showCurrentWord();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(StudyActivity.this,
                                "导入失败: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "无法读取文件路径", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "导入失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 从URI获取文件路径
     */
    private String getFilePathFromUri(Uri uri) {
        try {
            String fileName = getFileName(uri);

            File tempFile = new File(getExternalCacheDir(), fileName);

            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件名
     */
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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
