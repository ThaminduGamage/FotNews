package com.example.fot_news_myapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fot_news_myapp.R;
import com.example.fot_news_myapp.adapters.ArticleAdapter;
import com.example.fot_news_myapp.models.Article;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AcademicFragment extends Fragment implements ArticleAdapter.OnReadMoreClickListener {

    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<Article> articleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_academic, container, false);

        // Initialize RecyclerView and set its layout manager
        recyclerView = view.findViewById(R.id.recyclerViewAcademic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase Firestore and fetch articles data
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("articles")  // The collection name in Firestore
                .get()  // Get all documents in the "articles" collection
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        articleList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Ensure the document fields are present
                            String title = document.getString("title");
                            String date = document.getString("date");
                            String summary = document.getString("summary");

                            // Check if any field is missing or null, and handle accordingly
                            if (title != null && date != null) {
                                // Add articles to list if they contain necessary data
                                articleList.add(new Article(title, summary, date));
                            } else {
                                Log.w("Firestore", "Missing fields in document: " + document.getId());
                            }
                        }

                        // Set the adapter with the fetched articles
                        adapter = new ArticleAdapter(articleList, AcademicFragment.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Handle any errors
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });

        return view;
    }

    @Override
    public void onReadMoreClick(Article article) {
        // Handle the "Read More" click event
        ArticleDetailFragment detailFragment = ArticleDetailFragment.newInstance(
                article.getTitle(),
                article.getDate(),
                article.getSummary() // Only pass title, date, and summary
        );

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
