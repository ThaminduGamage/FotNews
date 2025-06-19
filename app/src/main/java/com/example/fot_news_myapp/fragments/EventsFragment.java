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

public class EventsFragment extends Fragment {

    private static final String TAG = "EventsFragment";
    private RecyclerView recyclerViewEvents;
    private ArticleAdapter articleAdapter;
    private List<Article> eventList; // Renamed for clarity in this fragment

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList = new ArrayList<>();

        // Initialize adapter with a click listener
        articleAdapter = new ArticleAdapter(eventList, article -> {
            if (getActivity() != null) {
                // Navigate to ArticleDetailFragment when "Read More" is clicked
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, // Assuming R.id.fragmentContainer is where your fragments are displayed
                                // Pass the imageResId (URL) to the detail fragment
                                ArticleDetailFragment.newInstance(article.getTitle(), article.getDate(), article.getSummary(), article.getImageResId()))
                        .addToBackStack(null) // Allows going back to the list fragment
                        .commit();
            }
        });
        recyclerViewEvents.setAdapter(articleAdapter);
        fetchEventNewsFromFirebase();

        return view;
    }

    private void fetchEventNewsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("categories/events");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear(); // Clear existing data to avoid duplicates
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Article event = eventSnapshot.getValue(Article.class);
                    if (event != null) {
                        eventList.add(event);
                    } else {
                        Log.e(TAG, "Failed to parse event article: " + eventSnapshot.getKey());
                    }
                }
                articleAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read event news.", databaseError.toException());
            }
        });
    }
}
