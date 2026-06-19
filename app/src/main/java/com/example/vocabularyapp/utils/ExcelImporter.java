package com.example.vocabularyapp.utils;

import android.content.Context;

import com.example.vocabularyapp.db.DatabaseHelper;
import com.example.vocabularyapp.model.Word;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel导入工具类
 * 用于从Excel文件中读取单词数据并导入数据库
 */
public class ExcelImporter {

    private Context context;
    private DatabaseHelper databaseHelper;

    public ExcelImporter(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
    }

    /**
     * 从Excel文件导入单词
     * 
     * 文件格式要求：
     * A列: 单词
     * B列: 音标
     * C列: 释义
     * D列: 词频排序（可选，会被忽略）
     *
     * @param filePath Excel文件路径
     * @return 导入的单词数量
     * @throws IOException 文件读取异常
     */
    public int importFromExcel(String filePath) throws IOException {
        List<Word> wordList = new ArrayList<>();

        // 打开Excel文件
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        // 获取第一个工作表
        Sheet sheet = workbook.getSheetAt(0);

        // 遍历每一行（跳过标题行）
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                continue;
            }

            // 读取单词数据（适配用户的文件格式）
            // A列: 单词, B列: 音标, C列: 释义, D列: 词频排序(忽略)
            String word = getCellValue(row.getCell(0));
            String phonetic = getCellValue(row.getCell(1));
            String meaning = getCellValue(row.getCell(2));
            // D列是词频排序，这里忽略，例句字段留空
            String example = "";

            // 创建Word对象
            if (word != null && !word.isEmpty() && meaning != null && !meaning.isEmpty()) {
                Word wordObj = new Word(word, phonetic, meaning, example);
                wordList.add(wordObj);
            }
        }

        // 关闭文件
        workbook.close();
        fis.close();

        // 导入到数据库
        int importCount = 0;
        for (Word word : wordList) {
            long id = databaseHelper.addWord(word);
            if (id > 0) {
                importCount++;
            }
        }

        return importCount;
    }

    /**
     * 获取单元格的值
     *
     * @param cell 单元格
     * @return 单元格的字符串值
     */
    private String getCellValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getCellFormula();

            default:
                return "";
        }
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    /**
     * 导入结果回调接口
     */
    public interface ImportCallback {
        void onSuccess(int importCount);
        void onFailure(String errorMessage);
    }

    /**
     * 异步导入单词
     *
     * @param filePath  Excel文件路径
     * @param callback  导入结果回调
     */
    public void importFromExcelAsync(final String filePath, final ImportCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int importCount = importFromExcel(filePath);

                    // 成功回调
                    if (callback != null) {
                        // 在主线程中回调
                        new android.os.Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(importCount);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    // 失败回调
                    if (callback != null) {
                        // 在主线程中回调
                        new android.os.Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(e.getMessage());
                            }
                        });
                    }
                }
            }
        }).start();
    }
}