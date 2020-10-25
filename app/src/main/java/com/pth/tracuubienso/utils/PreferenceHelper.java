package com.pth.tracuubienso.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pth.tracuubienso.models.Province;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sonvx on 29/08/2019
 */
public class PreferenceHelper {
    public static final String MY_PREFS_NAME = "account_sharedpreferences";
    private static volatile PreferenceHelper mInstance = new PreferenceHelper();
    private final String KEY_ACCOUNT = "account_key";
    private final String KEY_HISTORY = "account_history";

    private Gson gson = new Gson();

    public static PreferenceHelper getIns() {
        return mInstance;
    }


    public void saveInfoAccount(Context context, String email, String pass) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, 0).edit();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("password", pass);
        editor.putString(KEY_ACCOUNT, gson.toJson(hashMap));
        editor.apply();
    }

    public HashMap<String, String> getInfoAccount(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFS_NAME, 0);
        String info = pref.getString(KEY_ACCOUNT, "");
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> hashMap = gson.fromJson(info, type);
        return hashMap;
    }


    public void setProvinceList(Context context, String key, List<Province> list) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, 0).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_HISTORY, json);
        editor.commit();
    }


    public List<Province> getProvinceList(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFS_NAME, 0);
        List<Province> arrayItems = null;
        String serializedObject = pref.getString(KEY_HISTORY, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Province>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

}
