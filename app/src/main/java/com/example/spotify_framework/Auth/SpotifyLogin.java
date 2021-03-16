package com.example.spotify_framework.Auth;

import android.app.Activity;
import android.content.Context;

import com.example.cfm.MainActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class SpotifyLogin {
    // Initializing the client login vars
    public static final String clientId = "9db9499ad1554b70b6942e9e3f3495e3";
    public static final String redirectUri = "https://example.com/callback/";
    public static final String tag = "Spotify " + MainActivity.class.getSimpleName();
    private static final int reqCode = 1337;
    public static final String AuthToken = "AUTH_TOKEN";

    //Begin authentication
    public static void openLoginWindow(Context context) {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                clientId,
                AuthorizationResponse.Type.TOKEN,
                redirectUri);
        builder.setScopes(new String[]{});
        AuthorizationRequest req = builder.build();

        AuthorizationClient.openLoginActivity((Activity) context, reqCode, req);
    }
}
