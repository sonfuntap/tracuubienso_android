package com.pth.tracuubienso.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by sonvx on 29/08/2019
 */
public class AccountHelper {
    public static final String MY_PREFS_NAME = "account_sharedpreferences";
    private static volatile AccountHelper mInstance = new AccountHelper();
    private final String KEY_ACCOUNT = "account_key";
    private Gson gson= new Gson();

    public static AccountHelper getIns() {
        return mInstance;
    }

    public void saveInfoAccount(Context context, String email, String pass) {
        HashMap<String, String>  hashMap= new HashMap<>();
        hashMap.put("email", email );
        hashMap.put("password", pass);
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, 0).edit();
        editor.putString(KEY_ACCOUNT, gson.toJson(hashMap));
        editor.apply();
    }

    public HashMap<String, String> getInfoAccount(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFS_NAME, 0);
        String info= pref.getString(KEY_ACCOUNT, "");
        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> hashMap = gson.fromJson(info, type);
        return hashMap;
    }


}
