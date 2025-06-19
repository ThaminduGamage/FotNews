package com.example.fot_news_myapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log; // Import Log for error logging

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fot_news_myapp.R;
import com.google.firebase.storage.FirebaseStorage; // Import FirebaseStorage
import com.google.firebase.storage.StorageReference; // Import StorageReference


public class ArticleDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DATE = "date";
    private static final String ARG_SUMMARY = "summary";
    private static final String ARG_IMAGE_URL = "imageUrl";

    private String title;
    private String date;
    private String summary;
    private String imageUrl;

    public static ArticleDetailFragment newInstance(String title, String date, String summary, String imageUrl) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DATE, date);
        args.putString(ARG_SUMMARY, summary);
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);

        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            date = getArguments().getString(ARG_DATE);
            summary = getArguments().getString(ARG_SUMMARY);
            imageUrl = getArguments().getString(ARG_IMAGE_URL);
        }

        TextView titleView = view.findViewById(R.id.articleTitle);
        TextView dateView = view.findViewById(R.id.articleDate);
        TextView summaryView = view.findViewById(R.id.articleSummary);
        ImageView detailImageView = view.findViewById(R.id.articleImage);

        titleView.setText(title);
        dateView.setText(date);
        summaryView.setText(summary);

        if (detailImageView != null && imageUrl != null && !imageUrl.isEmpty()) {
            // Check if the URI is a Firebase Storage gs:// URI
            if (imageUrl.startsWith("gs://")) {
                try {
                    // Create a StorageReference from the gs:// URI
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);

                    // Get the download URL asynchronously
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Load the image using Glide with the obtained download URL
                        Glide.with(this)
                                .load(uri) // Load the direct download URL
                                .placeholder(R.drawable.placrholder_image)
                                .error(R.drawable.error_images)
                                .into(detailImageView);
                    }).addOnFailureListener(exception -> {
                        // Handle any errors
                        Log.e("ArticleDetailFragment", "Failed to get download URL: " + exception.getMessage());
                        detailImageView.setImageResource(R.drawable.error_images); // Show error image
                    });
                } catch (IllegalArgumentException e) {
                    Log.e("ArticleDetailFragment", "Invalid Firebase Storage URL: " + imageUrl + " - " + e.getMessage());
                    detailImageView.setImageResource(R.drawable.error_images); // Show error image for invalid URL
                }
            } else {
                // If it's a regular HTTP/HTTPS URL (or any other type not gs://)
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placrholder_image)
                        .error(R.drawable.error_images)
                        .into(detailImageView);
            }
        } else {
            // If imageUrl is null or empty, set the error image or placeholder
            detailImageView.setImageResource(R.drawable.placrholder_image);
        }

        return view;
    }
}
