package com.example.vocabularyapp.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.vocabularyapp.R;
import com.example.vocabularyapp.ui.study.StudyActivity;
import com.example.vocabularyapp.ui.test.TestActivity;
import com.example.vocabularyapp.ui.statistics.StatisticsActivity;
import com.example.vocabularyapp.utils.ExcelImporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 主界面Activity
 * 应用启动后的主界面，包含各个功能模块的入口
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // UI组件
    private CardView cardStudy;
    private CardView cardTest;
    private CardView cardWordList;
    private CardView cardStatistics;
    private CardView cardFavorite;
    private Button btnImport;

    // 文件选择请求码
    private static final int REQUEST_CODE_PICK_EXCEL = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化UI组件
        initViews();

        // 设置点击监听
        setClickListeners();
    }

    /**
     * 初始化UI组件
     */
    private void initViews() {
        cardStudy = findViewById(R.id.cardStudy);
        cardTest = findViewById(R.id.cardTest);
        cardWordList = findViewById(R.id.cardWordList);
        cardStatistics = findViewById(R.id.cardStatistics);
        cardFavorite = findViewById(R.id.cardFavorite);
        btnImport = findViewById(R.id.btnImport);
    }

    /**
     * 设置点击监听
     */
    private void setClickListeners() {
        cardStudy.setOnClickListener(this);
        cardTest.setOnClickListener(this);
        cardWordList.setOnClickListener(this);
        cardStatistics.setOnClickListener(this);
        cardFavorite.setOnClickListener(this);
        btnImport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        // 根据点击的组件跳转到相应的界面
        if (v.getId() == R.id.cardStudy) {
            // 跳转到学习界面
            intent = new Intent(MainActivity.this, StudyActivity.class);
        } else if (v.getId() == R.id.cardTest) {
            // 跳转到测试界面
            intent = new Intent(MainActivity.this, TestActivity.class);
        } else if (v.getId() == R.id.cardWordList) {
            // 跳转到单词列表界面
            intent = new Intent(MainActivity.this, WordListActivity.class);
        } else if (v.getId() == R.id.cardStatistics) {
            // 跳转到统计界面
            intent = new Intent(MainActivity.this, StatisticsActivity.class);
        } else if (v.getId() == R.id.cardFavorite) {
            // 跳转到生词本界面
            intent = new Intent(MainActivity.this, FavoriteActivity.class);
        } else if (v.getId() == R.id.btnImport) {
            // 导入单词 - 打开文件选择器
            openFileSelector();
        }

        // 启动相应的Activity
        if (intent != null) {
            startActivity(intent);
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
            // 如果没有文件管理器，提示用户
            Toast.makeText(this, "请安装文件管理器应用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_EXCEL && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                // 获取选中的文件URI
                Uri uri = data.getData();
                
                // 显示加载提示
                Toast.makeText(this, "正在导入单词，请稍候...", Toast.LENGTH_SHORT).show();
                
                // 异步导入单词
                importWordsFromExcel(uri);
            }
        }
    }

    /**
     * 从Excel文件导入单词
     */
    private void importWordsFromExcel(Uri uri) {
        try {
            // 将URI转换为文件路径
            String filePath = getFilePathFromUri(uri);
            
            if (filePath != null) {
                // 创建Excel导入器
                ExcelImporter importer = new ExcelImporter(this);
                
                // 异步导入
                importer.importFromExcelAsync(filePath, new ExcelImporter.ImportCallback() {
                    @Override
                    public void onSuccess(int importCount) {
                        Toast.makeText(MainActivity.this, 
                                "导入成功！共导入 " + importCount + " 个单词", 
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(MainActivity.this, 
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
            // 获取文件名
            String fileName = getFileName(uri);
            
            // 创建临时文件
            File tempFile = new File(getExternalCacheDir(), fileName);
            
            // 将内容复制到临时文件
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
}