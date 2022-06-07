package com.hindbyte.redfun.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private static final String TAG = SessionManager.class.getSimpleName();
    private final SharedPreferences pref;

    private final SharedPreferences.Editor editor;
    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        pref = context.getSharedPreferences("MY_PREF", 0);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
