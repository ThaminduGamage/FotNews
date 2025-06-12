package com.example.fot_news_myapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fot_news_myapp.R;  // Corrected the import
import com.example.fot_news_myapp.adapters.ArticleAdapter;  // Corrected the import
import com.example.fot_news_myapp.models.Article;  // Corrected the import

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment implements ArticleAdapter.OnReadMoreClickListener {

    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<Article> eventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the event list and add events without image reference
        eventList = new ArrayList<>();
        eventList.add(new Article("Annual Science Fair",
                "The annual university science fair showcasing innovative projects.",
                "June 5, 2025"));
        eventList.add(new Article("Tech Conference 2025",
                "Conference focusing on emerging technology trends and research.",
                "July 12, 2025"));

        adapter = new ArticleAdapter(eventList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onReadMoreClick(Article article) {
        ArticleDetailFragment detailFragment = ArticleDetailFragment.newInstance(
                article.getTitle(),
                article.getDate(),
                article.getSummary() // No need to pass image resource anymore
        );

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
