package com.example.fot_news_myapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fot_news_myapp.R;
import com.example.fot_news_myapp.models.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    // Interface for handling click events on the "Read More" button
    public interface OnReadMoreClickListener {
        void onReadMoreClick(Article article);
    }

    private List<Article> articleList;
    private OnReadMoreClickListener readMoreClickListener;

    public ArticleAdapter(List<Article> articleList, OnReadMoreClickListener listener) {
        this.articleList = articleList;
        this.readMoreClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the card layout for each article item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articleList.get(position);

        // Set the text for title, summary, and date
        holder.articleTitle.setText(article.getTitle());
        holder.articleSummary.setText(article.getSummary());
        holder.articleDate.setText(article.getDate());

        // Set an OnClickListener for the "Read More" TextView
        holder.readMore.setOnClickListener(v -> {
            if (readMoreClickListener != null) {
                readMoreClickListener.onReadMoreClick(article);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle, articleSummary, articleDate, readMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views for the title, summary, date, and read more text
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleSummary = itemView.findViewById(R.id.articleSummary);
            articleDate = itemView.findViewById(R.id.articleDate);
            readMore = itemView.findViewById(R.id.readMore);
        }
    }
}
