package com.example.fot_news_myapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log; // Import Log for error logging

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fot_news_myapp.R;
import com.example.fot_news_myapp.models.Article;
import com.google.firebase.storage.FirebaseStorage; // Import FirebaseStorage
import com.google.firebase.storage.StorageReference; // Import StorageReference

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articleList.get(position);

        holder.articleTitle.setText(article.getTitle());
        holder.articleSummary.setText(article.getSummary());
        holder.articleDate.setText(article.getDate());

        String imageUri = article.getImageResId();

        if (imageUri != null && !imageUri.isEmpty()) {
            // Check if the URI is a Firebase Storage gs:// URI
            if (imageUri.startsWith("gs://")) {
                try {
                    // Create a StorageReference from the gs:// URI
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri);

                    // Get the download URL asynchronously
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Load the image using Glide with the obtained download URL
                        Glide.with(holder.articleImage.getContext())
                                .load(uri) // Load the direct download URL
                                .placeholder(R.drawable.placrholder_image) // Placeholder while loading
                                .error(R.drawable.error_images)      // Image to show if loading fails
                                .into(holder.articleImage);        // Target ImageView
                    }).addOnFailureListener(exception -> {
                        // Handle any errors
                        Log.e("ArticleAdapter", "Failed to get download URL: " + exception.getMessage());
                        holder.articleImage.setImageResource(R.drawable.error_images); // Show error image
                    });
                } catch (IllegalArgumentException e) {
                    Log.e("ArticleAdapter", "Invalid Firebase Storage URL: " + imageUri + " - " + e.getMessage());
                    holder.articleImage.setImageResource(R.drawable.error_images); // Show error image for invalid URL
                }
            } else {
                // If it's a regular HTTP/HTTPS URL (or any other type not gs://)
                Glide.with(holder.articleImage.getContext())
                        .load(imageUri)
                        .placeholder(R.drawable.placrholder_image)
                        .error(R.drawable.error_images)
                        .into(holder.articleImage);
            }
        } else {
            // If imageUri is null or empty, set the error image or placeholder
            holder.articleImage.setImageResource(R.drawable.placrholder_image); // Or error_images
        }

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
        ImageView articleImage;
        TextView articleTitle, articleSummary, articleDate, readMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.articleImage);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleSummary = itemView.findViewById(R.id.articleSummary);
            articleDate = itemView.findViewById(R.id.articleDate);
            readMore = itemView.findViewById(R.id.readMore);
        }
    }
}
