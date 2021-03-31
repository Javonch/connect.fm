package com.example.cfm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.spotify_framework.User;
import com.example.spotify_framework.UserService;
import com.fonfon.geohash.GeoHash;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

        System.out.println("poo poo poo");
        locationTest();

        setContentView(R.layout.activity_splash);

        UserService.authenticateSpotify(clientId, redirectUri,reqCode,scopes,this);
        preferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("resumed the thing");
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

    private void locationTest() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                System.out.println("lemonade cosplay");
                System.out.println("Location: \t" + location);
                System.out.println("Latitude: \t" + location.getLatitude());
                System.out.println("Longitude:\t" + location.getLongitude());
                GeoHash hash = GeoHash.fromLocation(location, 5);
                System.out.println("Geohash:  \t" + hash);
            }
        });
    }
}