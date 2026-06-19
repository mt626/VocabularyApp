package com.example.vocabularyapp.ui.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocabularyapp.R;
import com.example.vocabularyapp.db.DatabaseHelper;
import com.example.vocabularyapp.model.Word;
import com.example.vocabularyapp.model.StudyRecord;
import com.example.vocabularyapp.ui.main.FavoriteActivity;
import com.example.vocabularyapp.ui.main.WordListActivity;
import com.example.vocabularyapp.ui.statistics.StatisticsActivity;
import com.example.vocabularyapp.ui.study.StudyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 测试Activity
 * 用于单词测试和测验
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    // UI组件
    private TextView tvTitle;
    private TextView tvScore;
    private TextView tvProgress;
    private TextView tvQuestionWord;
    private RadioGroup radioGroupOptions;
    private RadioButton rbOption1;
    private RadioButton rbOption2;
    private RadioButton rbOption3;
    private RadioButton rbOption4;
    private TextView tvResult;
    private Button btnSubmit;
    private Button btnNext;
    private Button btnStartTest;
    private Button btnRestart;
    private Button btnBackToStart;

    // 界面布局
    private View layoutStart;
    private View cardTest;
    private View layoutResult;
    private View bottomLayout;

    // 底部导航栏组件
    private View tabStudy;
    private View tabTest;
    private View tabList;
    private View tabStats;
    private View tabFavorite;

    // 数据组件
    private DatabaseHelper databaseHelper;
    private List<Word> wordList;
    private List<Word> testWords;
    private List<Word> wrongWords;
    private int currentIndex = 0;
    private int score = 0;
    private static final int TOTAL_QUESTIONS = 20;
    private static final int SCORE_PER_QUESTION = 5;
    private Word currentWord;
    private boolean hasSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // 初始化数据库帮助类
        databaseHelper = new DatabaseHelper(this);

        // 初始化UI组件
        initViews();

        // 设置点击监听
        setClickListeners();

        // 加载单词数据
        loadWords();

        // 显示开始界面
        showStartScreen();
    }

    /**
     * 初始化UI组件
     */
    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvScore = findViewById(R.id.tvScore);
        tvProgress = findViewById(R.id.tvProgress);
        tvQuestionWord = findViewById(R.id.tvQuestionWord);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        rbOption4 = findViewById(R.id.rbOption4);
        tvResult = findViewById(R.id.tvResult);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnNext = findViewById(R.id.btnNext);
        btnStartTest = findViewById(R.id.btnStartTest);
        btnRestart = findViewById(R.id.btnRestart);
        btnBackToStart = findViewById(R.id.btnBackToStart);

        // 界面布局
        layoutStart = findViewById(R.id.layoutStart);
        cardTest = findViewById(R.id.cardTest);
        layoutResult = findViewById(R.id.layoutResult);
        bottomLayout = findViewById(R.id.bottomLayout);

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
        btnStartTest.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        btnBackToStart.setOnClickListener(this);

        // 底部导航栏点击监听
        tabStudy.setOnClickListener(this);
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

        // 如果单词数量不足，提示用户
        if (wordList.size() < 4) {
            Toast.makeText(this, "单词数量不足，请先导入更多单词", Toast.LENGTH_SHORT).show();
            btnStartTest.setEnabled(false);
        }
    }

    /**
     * 显示开始界面
     */
    private void showStartScreen() {
        layoutStart.setVisibility(View.VISIBLE);
        cardTest.setVisibility(View.GONE);
        layoutResult.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        tvScore.setText("");
    }

    /**
     * 显示测试界面
     */
    private void showTestScreen() {
        layoutStart.setVisibility(View.GONE);
        cardTest.setVisibility(View.VISIBLE);
        layoutResult.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示结果界面
     */
    private void showResultScreen() {
        layoutStart.setVisibility(View.GONE);
        cardTest.setVisibility(View.GONE);
        layoutResult.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.GONE);

        // 更新最终得分显示
        int finalScore = score * SCORE_PER_QUESTION;
        TextView tvFinalScore = findViewById(R.id.tvFinalScore);
        TextView tvResultMessage = findViewById(R.id.tvResultMessage);

        tvFinalScore.setText(String.format("得分: %d/%d", finalScore, TOTAL_QUESTIONS * SCORE_PER_QUESTION));

        // 根据得分显示不同的消息
        if (finalScore >= 90) {
            tvResultMessage.setText("太棒了！完美表现！");
            tvResultMessage.setTextColor(Color.GREEN);
        } else if (finalScore >= 70) {
            tvResultMessage.setText("表现不错！继续加油！");
            tvResultMessage.setTextColor(Color.BLUE);
        } else if (finalScore >= 60) {
            tvResultMessage.setText("及格了！还需努力！");
            tvResultMessage.setTextColor(Color.YELLOW);
        } else {
            tvResultMessage.setText("需要多加练习！");
            tvResultMessage.setTextColor(Color.RED);
        }
    }

    /**
     * 开始测试
     */
    private void startTest() {
        // 重置测试状态
        currentIndex = 0;
        score = 0;
        wrongWords = new ArrayList<>();

        // 随机选择测试单词（最多20个）
        int actualCount = Math.min(TOTAL_QUESTIONS, wordList.size());
        testWords = selectRandomWords(actualCount);

        // 显示测试界面
        showTestScreen();

        // 显示第一题
        showQuestion();
    }

    /**
     * 随机选择测试单词
     */
    private List<Word> selectRandomWords(int count) {
        List<Word> selectedWords = new ArrayList<>();
        List<Word> allWords = new ArrayList<>(wordList);

        // 随机打乱顺序
        Collections.shuffle(allWords);

        // 选择指定数量的单词
        for (int i = 0; i < count && i < allWords.size(); i++) {
            selectedWords.add(allWords.get(i));
        }

        return selectedWords;
    }

    /**
     * 显示题目
     */
    private void showQuestion() {
        if (currentIndex >= testWords.size()) {
            // 测试结束
            finishTest();
            return;
        }

        // 获取当前测试单词
        currentWord = testWords.get(currentIndex);

        // 显示问题单词
        tvQuestionWord.setText(currentWord.getWord());

        // 生成选项
        generateOptions();

        // 清空选择
        radioGroupOptions.clearCheck();

        // 隐藏结果
        tvResult.setVisibility(View.GONE);

        // 更新得分显示
        updateScore();

        // 更新进度显示
        updateProgress();

        // 重置提交状态
        hasSubmitted = false;
        btnSubmit.setEnabled(true);
        btnNext.setEnabled(false);
    }

    /**
     * 生成选项
     */
    private void generateOptions() {
        // 正确答案
        String correctAnswer = currentWord.getMeaning();

        // 随机选择其他3个单词的释义作为错误选项
        List<String> wrongAnswers = new ArrayList<>();
        Random random = new Random();

        while (wrongAnswers.size() < 3) {
            Word randomWord = wordList.get(random.nextInt(wordList.size()));
            if (randomWord.getId() != currentWord.getId() &&
                    !wrongAnswers.contains(randomWord.getMeaning())) {
                wrongAnswers.add(randomWord.getMeaning());
            }
        }

        // 将正确答案和错误答案混合
        List<String> allOptions = new ArrayList<>();
        allOptions.add(correctAnswer);
        allOptions.addAll(wrongAnswers);

        // 随机打乱顺序
        Collections.shuffle(allOptions);

        // 设置选项
        rbOption1.setText(allOptions.get(0));
        rbOption2.setText(allOptions.get(1));
        rbOption3.setText(allOptions.get(2));
        rbOption4.setText(allOptions.get(3));
    }

    /**
     * 提交答案
     */
    private void submitAnswer() {
        // 检查是否选择了答案
        int selectedId = radioGroupOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "请选择一个答案", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取选择的答案
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedAnswer = selectedRadioButton.getText().toString();

        // 判断答案是否正确
        boolean isCorrect = selectedAnswer.equals(currentWord.getMeaning());

        // 显示结果
        showResult(isCorrect);

        // 更新得分
        if (isCorrect) {
            score++;
        } else {
            // 将错误的单词添加到收藏
            addWordToFavorite(currentWord);
            wrongWords.add(currentWord);
        }

        // 更新提交状态
        hasSubmitted = true;
        btnSubmit.setEnabled(false);
        btnNext.setEnabled(true);
    }

    /**
     * 将单词添加到收藏
     */
    private void addWordToFavorite(Word word) {
        // 检查是否已在收藏中
        if (!databaseHelper.isWordFavorite(word.getId())) {
            databaseHelper.addFavorite(word);
        }
    }

    /**
     * 显示结果
     */
    private void showResult(boolean isCorrect) {
        tvResult.setVisibility(View.VISIBLE);

        if (isCorrect) {
            tvResult.setText(R.string.test_correct);
            tvResult.setTextColor(Color.GREEN);
        } else {
            tvResult.setText(R.string.test_wrong);
            tvResult.setTextColor(Color.RED);
        }
    }

    /**
     * 下一题
     */
    private void nextQuestion() {
        currentIndex++;
        showQuestion();
    }

    /**
     * 更新得分显示
     */
    private void updateScore() {
        int currentScore = score * SCORE_PER_QUESTION;
        tvScore.setText(String.format("得分: %d", currentScore));
    }

    /**
     * 更新进度显示
     */
    private void updateProgress() {
        tvProgress.setText(String.format("%d/%d", currentIndex + 1, testWords.size()));
    }

    /**
     * 完成测试
     */
    private void finishTest() {
        // 保存测试记录
        saveTestRecord();

        // 显示结果界面
        showResultScreen();
    }

    /**
     * 保存测试记录
     */
    private void saveTestRecord() {
        int finalScore = score * SCORE_PER_QUESTION;

        // 创建测试记录
        StudyRecord record = new StudyRecord(
                System.currentTimeMillis(),
                finalScore,
                testWords.size(),
                "test"
        );

        // 保存到数据库
        databaseHelper.addStudyRecord(record);

        // 更新测试次数统计
        updateTestCount();
    }

    /**
     * 更新测试次数统计
     */
    private void updateTestCount() {
        // 获取当前测试次数
        int testCount = databaseHelper.getTestCount();

        // 增加测试次数
        databaseHelper.updateTestCount(testCount + 1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStartTest) {
            // 开始测试按钮
            startTest();
        } else if (v.getId() == R.id.btnSubmit) {
            // 提交答案按钮
            submitAnswer();
        } else if (v.getId() == R.id.btnNext) {
            // 下一题按钮
            nextQuestion();
        } else if (v.getId() == R.id.btnRestart) {
            // 再测一次按钮
            startTest();
        } else if (v.getId() == R.id.btnBackToStart) {
            // 返回开始界面
            showStartScreen();
        } else if (v.getId() == R.id.tabStudy) {
            // 学习界面
            Intent intent = new Intent(this, StudyActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tabTest) {
            // 单词测试（当前界面）
            // 不做任何操作
        } else if (v.getId() == R.id.tabList) {
            // 单词列表
            Intent intent = new Intent(this, WordListActivity.class);
            startActivity(intent);
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