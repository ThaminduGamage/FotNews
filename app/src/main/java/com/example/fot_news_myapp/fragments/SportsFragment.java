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

public class SportsFragment extends Fragment implements ArticleAdapter.OnReadMoreClickListener {

    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<Article> articleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sports, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the event list without images
        articleList = new ArrayList<>();
        articleList.add(new Article(
                "Public Debt in Japan and Its Implications for Sri Lanka...",
                "The third lecture in the JICA Chair Programme series took place...",
                "Dec 25, 2024"
        ));
        articleList.add(new Article(
                "Towards a Green Horizon: Japan's Journey...",
                "Exploring Japan's strategy for carbon neutrality...",
                "Dec 20, 2024"
        ));

        adapter = new ArticleAdapter(articleList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onReadMoreClick(Article article) {
        // Handle the "Read More" click event
        ArticleDetailFragment detailFragment = ArticleDetailFragment.newInstance(
                article.getTitle(),
                article.getDate(),
                article.getSummary() // No image URL needed anymore
        );

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
