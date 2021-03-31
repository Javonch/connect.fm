package com.example.spotify_framework;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private SharedPreferences preferences;
    private RequestQueue queue;
    private User user;

    public UserService(RequestQueue queue, SharedPreferences preferences) {
        this.queue = queue;
        this.preferences = preferences;
    }

    public User getUser() {
        return user;
    }

    public void get(final VolleyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, response -> {
            Gson gson = new Gson();
            Log.d("User Response",response.toString());
            user = gson.fromJson(response.toString(), User.class);
            callBack.onSuccess();
        }, error -> get(() -> {

        })) {
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
    }

    public static void authenticateSpotify(String clientId, String redirectUri, int reqCode, String[] scopes, Context context) {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                clientId,
                AuthorizationResponse.Type.TOKEN,
                redirectUri);

        builder.setScopes(scopes);
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity((Activity) context,reqCode,request);
    }

}
