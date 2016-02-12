package fr.sio.ecp.federatedbirds.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Michaël on 25/11/2015.
 */
public class TokenManager {

    private static final String AUTH_PREFERENCES = "auth";
    private static final String TOKEN_KEY = "token";

    public static String getUserToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(TOKEN_KEY, null);
    }

    public static void setUserToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString(TOKEN_KEY, token).apply();
        Log.i(TokenManager.class.getSimpleName(), "User token saved: " + token);
    }

    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
        Log.i(TokenManager.class.getSimpleName(), "Auth preferences cleared");
    }

    public static void registerUser(Context context, String login) {
        // this function permits to add the current user login to the sharedpreferences to make getauthenticateduser work
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString("login", login).apply();
        return;
    }

    public static String getAuthenticatedUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString("login", null);
    }
}
