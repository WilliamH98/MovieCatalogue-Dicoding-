package com.dicoding.MovieCatalogue.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import com.dicoding.MovieCatalogue.AlarmReceiver;
import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.SettingPreferences;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private LinearLayout mLLChangeLanguage;
    private TextView mTVLanguage;
    private Switch mSWReleaseReminder, mSWDailyReminder;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    @SuppressLint("StaticFieldLeak")
    public static SettingsActivity instance = null;
    private SettingPreferences mSettingPreferences;
    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        instance = this;
        mLLChangeLanguage = findViewById(R.id.ll_change_language);
        mTVLanguage = findViewById(R.id.tv_change_language_details);
        mSWReleaseReminder = findViewById(R.id.sw_release_reminder);
        mSWDailyReminder = findViewById(R.id.sw_daily_reminder);
        alarmReceiver = new AlarmReceiver();

        Toolbar mToolbar = findViewById(R.id.tb_settings);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        mToolbar.setNavigationOnClickListener(v -> {
            v.startAnimation(buttonClick);
            finish();
        });

        mLLChangeLanguage.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            Intent intent = new Intent(getApplicationContext(), ChangeLanguageActivity.class);
            startActivity(intent);
        });

        if (!Objects.equals(MainActivity.mSetting.get(getString(R.string.release_reminder_pref)), "null")) {
            if (Objects.equals(MainActivity.mSetting.get(getString(R.string.release_reminder_pref)), "true")) {
                mSWReleaseReminder.setChecked(true);
            } else {
                mSWReleaseReminder.setChecked(false);
            }

            if (Objects.equals(MainActivity.mSetting.get(getString(R.string.daily_reminder_pref)), "true")) {
                mSWDailyReminder.setChecked(true);
            } else {
                mSWDailyReminder.setChecked(false);
            }

            if (Objects.equals(MainActivity.mSetting.get(getString(R.string.release_reminder_pref)), "en_US")) {
                mTVLanguage.setText(getResources().getString(R.string.english));
            } else if (Objects.equals(MainActivity.mSetting.get(getString(R.string.release_reminder_pref)), "in")) {
                mTVLanguage.setText(getResources().getString(R.string.indonesian));
            }
        }

        mSWReleaseReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                setReleaseReminder();
            }else {
                setOffReleaseReminder();
            }
        });

        mSWDailyReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                setDailyReminder();
            }else {
                setOffDailyReminder();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSettingPreferences = new SettingPreferences(this);
        String mReleaseReminderStatus = "", mDailyReminderStatus = "";
        if (mSWReleaseReminder.isChecked()) {
            mReleaseReminderStatus = "true";
        } else if (!mSWReleaseReminder.isChecked()) {
            mReleaseReminderStatus = "false";
        }

        if (mSWDailyReminder.isChecked()) {
            mDailyReminderStatus = "true";
        } else if (!mSWDailyReminder.isChecked()) {
            mDailyReminderStatus = "false";
        }

        mSettingPreferences.setSetting(mReleaseReminderStatus, mDailyReminderStatus);
    }

    private void setDailyReminder() {
        alarmReceiver.setReminderDaily(this, AlarmReceiver.TYPE_DAILY);
    }

    private void setOffDailyReminder() {
        alarmReceiver.cancelReminderDaily(this);
    }

    private void setReleaseReminder() {
        alarmReceiver.setReminderRelease(this, AlarmReceiver.TYPE_RELEASE);
    }

    private void setOffReleaseReminder() {
        alarmReceiver.cancelReminderRelease(this);
    }
}
