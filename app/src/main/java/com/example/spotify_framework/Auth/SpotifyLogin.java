package com.example.spotify_framework.Auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.cfm.MainActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class SpotifyLogin {
    // Initializing the client login vars
    private static final String clientId = "9db9499ad1554b70b6942e9e3f3495e3";
    private static final String redirectUri = "https://example.com/callback/";
    private static final String tag = "Spotify " + MainActivity.class.getSimpleName();
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

    public static String getClientId() {
        return clientId;
    }

    public static int getReqCode() {
        return reqCode;
    }

    public static String getRedirectUri() {
        return redirectUri;
    }

    public static String getTag() {
        return tag;
    }

    public void reqCode(View view, Context context) {
        final AuthorizationRequest request = getAuthorizationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity((Activity) context, reqCode, request);
    }

    public void reqToken(View view, Context context) {
        final AuthorizationRequest request = getAuthorizationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity((Activity) context, reqCode, request);
    }
    private AuthorizationRequest getAuthorizationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(clientId, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email"})
                .setCampaign("your-campaign-token")
                .build();
    }
}
