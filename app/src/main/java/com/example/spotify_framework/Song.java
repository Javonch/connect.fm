package com.example.spotify_framework;

import com.example.spotify_framework.Genre;

public class Song {
    private String uri;
    private int danceability;
    private int bpm;
    private Genre genre;

    public void Song() {

    }

    public int getDanceability() {
        return danceability;
    }

    public int getBpm() {
        return bpm;
    }

    public Genre getGenre() {
        return genre;
    }

}
