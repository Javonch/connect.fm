package com.example.spotify_framework;

import android.os.Parcel;
import android.os.Parcelable;

import okhttp3.OkHttpClient;
import okhttp3.internal.http2.Header;

/**
 * <a href="https://developer.spotify.com/web-api/object-model/#track-object-full">Track object model</a>
 */

public class Song implements Parcelable {
    private OkHttpClient client = new OkHttpClient();
    private String accessToken;
    private Header authHeader_;
    private String id;
    private String uri;
    private String title;
    private String album;
    private String album_image;
    private long duration;
    private String artist;
    private String artist_id;
    private boolean playing;

    public Song(String uri, String accessToken) {
        this.uri = uri;



    }

    protected Song(Parcel in) {
        accessToken = in.readString();
        id = in.readString();
        uri = in.readString();
        title = in.readString();
        album = in.readString();
        album_image = in.readString();
        duration = in.readLong();
        artist = in.readString();
        artist_id = in.readString();
        playing = in.readByte() != 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeString(id);
        dest.writeString(uri);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(album_image);
        dest.writeLong(duration);
        dest.writeString(artist);
        dest.writeString(artist_id);
        dest.writeByte((byte) (playing ? 1 : 0));
    }
}
