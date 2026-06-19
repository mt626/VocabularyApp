package com.example.vocabularyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocabularyapp.R;
import com.example.vocabularyapp.model.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * 单词列表适配器
 * 用于RecyclerView展示单词列表
 */
public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<Word> wordList;
    private OnWordClickListener listener;

    /**
     * 单词点击监听接口
     */
    public interface OnWordClickListener {
        void onWordClick(Word word, int position);
        void onFavoriteClick(Word word, int position);
    }

    public WordAdapter() {
        this.wordList = new ArrayList<>();
    }

    public WordAdapter(List<Word> wordList) {
        this.wordList = wordList;
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
        notifyDataSetChanged();
    }

    public void setOnWordClickListener(OnWordClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = wordList.get(position);

        // 设置单词数据
        holder.tvWord.setText(word.getWord());
        holder.tvPhonetic.setText(word.getPhonetic());
        holder.tvMeaning.setText(word.getMeaning());

        // 设置收藏图标
        if (word.isFavorite()) {
            holder.ivFavorite.setVisibility(View.VISIBLE);
        } else {
            holder.ivFavorite.setVisibility(View.GONE);
        }

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onWordClick(word, position);
            }
        });

        holder.ivFavorite.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteClick(word, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    /**
     * ViewHolder类
     */
    static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord;
        TextView tvPhonetic;
        TextView tvMeaning;
        ImageView ivFavorite;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvPhonetic = itemView.findViewById(R.id.tvPhonetic);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }
    }
}