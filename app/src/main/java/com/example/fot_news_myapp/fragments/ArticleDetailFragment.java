package com.example.fot_news_myapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fot_news_myapp.R;

public class ArticleDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DATE = "date";
    private static final String ARG_SUMMARY = "summary";

    private String title;
    private String date;
    private String summary;

    public static ArticleDetailFragment newInstance(String title, String date, String summary) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DATE, date);
        args.putString(ARG_SUMMARY, summary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);

        // Retrieve the arguments
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            date = getArguments().getString(ARG_DATE);
            summary = getArguments().getString(ARG_SUMMARY);
        }

        // Set the article data to the views
        TextView titleView = view.findViewById(R.id.articleTitle);
        TextView dateView = view.findViewById(R.id.articleDate);
        TextView summaryView = view.findViewById(R.id.articleSummary);

        titleView.setText(title);
        dateView.setText(date);
        summaryView.setText(summary);

        return view;
    }
}
