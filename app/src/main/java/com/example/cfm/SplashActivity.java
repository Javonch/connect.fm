package com.example.cfm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.spotify_framework.User;
import com.example.spotify_framework.UserService;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    //Spotify Authorization fields
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    private RequestQueue queue;

    private static final String clientId = "9db9499ad1554b70b6942e9e3f3495e3";
    private static final String redirectUri = "https://example.com/callback/";
    private static final String[] scopes = new String[]{"user-read-email", "user-library-modify" , "user-read-email" , "user-read-private"};
    private static final int reqCode = 0x10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserService.authenticateSpotify(clientId, redirectUri,reqCode,scopes,this);
        preferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);

        if(requestCode == reqCode){
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode,intent);

            switch(response.getType()) {
                case TOKEN : {
                    editor = getSharedPreferences("SPOTIFY",0).edit();
                    editor.putString("token", response.getAccessToken());
                    Map prefs = getSharedPreferences("SPOTIFY",0).getAll();
                    for(Object x : prefs.keySet().toArray()) {
                        Log.d("Prefs",x + ": " + prefs.get(x));
                    }
                    Log.d("STARTING", "AUTH TOKEN "+ response.getAccessToken());
                    editor.apply();
                    waitForUserInfo();
                    break;
                }

                case ERROR : {
                    Log.e("ERROR:", response.getError());
                }

                default :
                    Log.d(response.getType() + ": ", response.toString());
                    onActivityResult(requestCode,resultCode,intent);
            }
        }
    }

    private void waitForUserInfo() {
        UserService userService = new UserService(queue, preferences);
        userService.get(() -> {
            User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            editor.putString("displayName", user.display_name);
            editor.putString("country", user.country);
            Log.d("STARTING", "GOT USER INFORMATION");
            // We use commit instead of apply because we need the information stored immediately
            editor.commit();
            startMainActivity();
        });
    }
    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}