package com.example.spotify_framework;

import android.app.LauncherActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import com.example.connectfm.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String clientID = "9db9499ad1554b70b6942e9e3f3495e3";
    private static final String redirectURI = "https://example.com/callback/";
    private SpotifyAppRemote spotifyAppRemote_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        
        if(!isSpotifyInstalled()){
            final String appPackageName = "com.spotify.music";
            final String referrer = "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";

            try {
                Uri uri = Uri.parse("market://details")
                        .buildUpon()
                        .appendQueryParameter("id", appPackageName)
                        .appendQueryParameter("referrer", referrer)
                        .build();
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (android.content.ActivityNotFoundException ignored) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                        .buildUpon()
                        .appendQueryParameter("id", appPackageName)
                        .appendQueryParameter("referrer", referrer)
                        .build();
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        }
        connect();
    }

    protected void onStop() {
        super.onStop();

    }

    public void connect() {
        ConnectionParams connectionParams = new ConnectionParams.Builder(clientID)
                .setRedirectUri(redirectURI)
                .showAuthView(true)
                .build();

        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                spotifyAppRemote_ = spotifyAppRemote;
                Log.d("MainActivity" , "Connection Successful!");
                connected();
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity" , throwable.getMessage(), throwable);
            }
        });
    }

    protected void connected() {
        spotifyAppRemote_.getPlayerApi(). play("spotify:playlist:37i9dQZF1DX7K31D69s4M1");
    }

    public boolean isSpotifyInstalled() {
        PackageManager pm = getPackageManager();
        boolean installed = true;
        try{
            pm.getPackageInfo("com.spotify.music", 0);
        } catch(PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    private void getAppList() {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("MainActivity", "Installed Package: " + packageInfo.packageName);
        }
    }
}