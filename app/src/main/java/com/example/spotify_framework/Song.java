package com.example.spotify_framework;

import android.os.Parcel;
import android.os.Parcelable;

public class Song {
    private String id;
    private String uri;
    private String title;
    private String album;
    private String album_image;
    private long duration;
    private String artist;
    private String artist_id;
    private boolean playing;

    public Song(String id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle() {
        this.title = title;
    }
}
