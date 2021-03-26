package com.example.spotify_framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongService {
    private ArrayList<Song> playlist = new ArrayList<>();
    private SharedPreferences preferences;
    private RequestQueue queue;

    public SongService(Context context) {
        preferences = context.getSharedPreferences("SPOTIFY",0);
        queue = Volley.newRequestQueue(context);

    }

    public ArrayList<Song> getPlaylist() {
        return playlist;
    }

    public ArrayList<Song> getRecentlyPlayed(final VolleyCallBack callBack) {
        String endpoint = "https://api.spoify.com/v1/me/player/recently-played";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                endpoint,
                null,
                response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            object = object.optJSONObject("track");
                            Log.d("Song Response: ", object.toString());
                            Song song = gson.fromJson(object.toString(), Song.class);
                            playlist.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    //Handle error here
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = preferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
        return playlist;
    }
}
