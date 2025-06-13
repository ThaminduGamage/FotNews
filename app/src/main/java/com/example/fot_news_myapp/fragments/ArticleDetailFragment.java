package com.example.fot_news_myapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fot_news_myapp.R;

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
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placrholder_image)
                    .error(R.drawable.error_images)
                    .into(detailImageView);
        }

        return view;
    }
}
