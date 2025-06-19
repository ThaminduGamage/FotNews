package com.example.fot_news_myapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AcademicFragment extends Fragment {

    private static final String TAG = "AcademicFragment";
    private RecyclerView recyclerViewAcademic;
    private ArticleAdapter articleAdapter;
    private List<Article> articleList;

    public AcademicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academic, container, false);

        recyclerViewAcademic = view.findViewById(R.id.recyclerViewAcademic);
        recyclerViewAcademic.setLayoutManager(new LinearLayoutManager(getContext()));
        articleList = new ArrayList<>();

        // Initialize adapter with a click listener
        articleAdapter = new ArticleAdapter(articleList, article -> {
            if (getActivity() != null) {
                // Navigate to ArticleDetailFragment when "Read More" is clicked
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, // Assuming R.id.fragmentContainer is where your fragments are displayed
                                ArticleDetailFragment.newInstance(article.getTitle(), article.getDate(), article.getSummary(), article.getImageResId()))
                        .addToBackStack(null) // Allows going back to the list fragment
                        .commit();
            }
        });

        recyclerViewAcademic.setAdapter(articleAdapter);
        fetchAcademicNewsFromFirebase();

        return view;
    }

    private void fetchAcademicNewsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("categories/academic");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                articleList.clear(); // Clear existing data to avoid duplicates
                for (DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
                    Article article = articleSnapshot.getValue(Article.class);
                    if (article != null) {
                        articleList.add(article);
                    } else {
                        Log.e(TAG, "Failed to parse academic article: " + articleSnapshot.getKey());
                    }
                }
                articleAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read academic news.", databaseError.toException());
            }
        });
    }
}
