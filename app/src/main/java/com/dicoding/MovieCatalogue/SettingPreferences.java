package com.dicoding.MovieCatalogue;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingPreferences {
    private static final String PREFS_NAME = "setting_pref";
    private static final String RELEASE_REMINDER = "release_reminder";
    private static final String DAILY_REMINDER = "daily_reminder";
    private final SharedPreferences preferences;

    public SettingPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setSetting(String releaseReminder, String dailyReminder) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(RELEASE_REMINDER, releaseReminder);
        editor.putString(DAILY_REMINDER, dailyReminder);
        editor.apply();
    }

    public Map<String, String> getSetting() {
        Map<String, String> setting = new HashMap<>();
        setting.put(RELEASE_REMINDER, Objects.requireNonNull(preferences.getString(RELEASE_REMINDER, "")));
        setting.put(DAILY_REMINDER, Objects.requireNonNull(preferences.getString(DAILY_REMINDER, "")));
        return setting;
    }

}
