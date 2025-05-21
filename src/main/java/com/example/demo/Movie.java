package com.example.demo;

public class Movie {
    private int id;
    private String title, genre, duration, date, posterPath,status;

    public Movie(int id, String title, String genre, String duration, String date, String posterPath) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.date = date;
        this.posterPath = posterPath;
    }
    public Movie(int id, String title, String genre, String duration, String date, String posterPath,String status) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.date = date;
        this.posterPath = posterPath;
        this.status = status;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
