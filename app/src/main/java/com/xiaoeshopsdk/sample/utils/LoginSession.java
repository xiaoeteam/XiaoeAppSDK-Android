package com.xiaoeshopsdk.sample.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xiaoe.shop.webcore.XEToken;

public class LoginSession {
    private static final String KEY_TOKEN_KEY = "token_key";
    private static final String KEY_TOKEN_VALUE = "token_value";
    private SharedPreferences mPreferences;

    public LoginSession(Context context) {
        mPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        clear();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void setToken(XEToken token) {
        mPreferences.edit()
                .putString(KEY_TOKEN_KEY, token.getTokenKey())
                .putString(KEY_TOKEN_VALUE, token.getTokenValue())
                .apply();
    }

    public XEToken getToken() {
        final String tokenKey = mPreferences.getString(KEY_TOKEN_KEY, null);
        final String tokenValue = mPreferences.getString(KEY_TOKEN_VALUE, null);
        if (TextUtils.isEmpty(tokenKey) || TextUtils.isEmpty(tokenValue)) {
            return null;
        }
        return new XEToken(tokenKey, tokenValue);
    }

    public void clear() {
        mPreferences.edit().clear().apply();
    }
}