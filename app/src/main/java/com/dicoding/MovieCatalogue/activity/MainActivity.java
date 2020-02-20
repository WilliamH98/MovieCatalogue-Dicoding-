package com.dicoding.MovieCatalogue.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.SettingPreferences;
import com.dicoding.MovieCatalogue.fragment.FavoriteTabFragment;
import com.dicoding.MovieCatalogue.fragment.MovieTabFragment;
import com.dicoding.MovieCatalogue.fragment.TVShowsTabFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static MainActivity instance;
    public static int onFavorite = 0;
    private SettingPreferences mSettingPreferences;
    public static Map<String, String> mSetting = new HashMap<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_movies:
                onFavorite = 0;
                selectedFragment = new MovieTabFragment();
                break;
            case R.id.navigation_tv_shows:
                onFavorite = 0;
                selectedFragment = new TVShowsTabFragment();
                break;

            case R.id.navigation_favorite:
                onFavorite = 1;
                selectedFragment = new FavoriteTabFragment();
                break;
        }

        if (selectedFragment != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettingPreferences = new SettingPreferences(this);
        setPreference();
        instance = this;

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null)
            navView.setSelectedItemId(R.id.navigation_movies);
    }

    private void setPreference() {
        mSetting = mSettingPreferences.getSetting();

        if (Objects.equals(mSetting.get(getString(R.string.release_reminder_pref)), "")) {
            mSetting.put(getString(R.string.release_reminder_pref), "null");
            mSetting.put(getString(R.string.daily_reminder_pref), "null");
            mSetting.put(getString(R.string.locale_pref), "null");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPreference();
    }
}
