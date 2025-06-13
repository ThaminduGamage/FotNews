package com.example.fot_news_myapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.example.fot_news_myapp.R;
import com.example.fot_news_myapp.adapters.ArticleAdapter;
import com.example.fot_news_myapp.models.Article;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SportsFragment extends Fragment {

    private static final String TAG = "SportsFragment";
    private RecyclerView recyclerViewSports;
    private ArticleAdapter articleAdapter;
    private List<Article> articleList;

    public SportsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sports, container, false);

        recyclerViewSports = view.findViewById(R.id.recyclerViewSports);
        recyclerViewSports.setLayoutManager(new LinearLayoutManager(getContext()));

        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(articleList, article -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,
                                // CORRECTED: Pass article.getImageResId() (String) instead of 0 (int)
                                ArticleDetailFragment.newInstance(article.getTitle(), article.getDate(), article.getSummary(), article.getImageResId()))
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerViewSports.setAdapter(articleAdapter);
        fetchSportsNewsFromFirebase();

        return view;
    }

    private void fetchSportsNewsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("categories/sports");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                articleList.clear();
                for (DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
                    Article article = articleSnapshot.getValue(Article.class);
                    if (article != null) {
                        articleList.add(article);
                    } else {
                        Log.e(TAG, "Failed to parse article: " + articleSnapshot.getKey());
                    }
                }
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
