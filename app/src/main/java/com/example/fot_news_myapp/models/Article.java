package com.example.fot_news_myapp.models;

public class Article {
    private String title;
    private String summary;
    private String date;

    // Default constructor (required for Firebase or other uses)
    public Article() {}

    // Constructor to match your Firebase data structure
    public Article(String title, String summary, String date) {
        this.title = title;
        this.summary = summary;
        this.date = date;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getDate() {
        return date;
    }

    // Setter methods (optional, for Firebase deserialization or updating data)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
