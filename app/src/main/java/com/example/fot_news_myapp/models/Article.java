package com.example.fot_news_myapp.models;

public class Article {
    private String title;
    private String summary;
    private String date;
    private String imageResId; // Stores the URL of the image (e.g., gs://bucket/path/to/image.png)

    // Required no-argument constructor for Firebase deserialization
    public Article() {
        // Default constructor required for calls to DataSnapshot.getValue(Article.class)
    }

    public Article(String title, String summary, String date, String imageResId) {
        this.title = title;
        this.summary = summary;
        this.date = date;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getDate() {
        return date;
    }

    public String getImageResId() { // Getter for image URL
        return imageResId;
    }

    // Setters (optional, but good practice if you plan to modify articles)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }
}
