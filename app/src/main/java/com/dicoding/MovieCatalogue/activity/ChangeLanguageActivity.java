package com.dicoding.MovieCatalogue.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;

import com.dicoding.MovieCatalogue.R;

import java.util.Locale;

public class ChangeLanguageActivity extends AppCompatActivity {
    private RadioButton mRbEnglish, mRbIndonesian;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

        RadioGroup mRgLanguage = findViewById(R.id.rg_language);
        mRbEnglish = findViewById(R.id.rb_english);
        mRbIndonesian = findViewById(R.id.rb_indonesian);
        Toolbar mToolbar = findViewById(R.id.tb_change_language_details);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        mToolbar.setNavigationOnClickListener(v -> {
            v.startAnimation(buttonClick);
            finish();
        });

        Locale mCurrent = getResources().getConfiguration().locale;
        if (mCurrent.toString().equals("en_US")) {
            mRbEnglish.setChecked(true);
            mRbEnglish.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selected_state));
            mRbEnglish.setTextColor(Color.parseColor("#f8b500"));

            mRbIndonesian.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regular_state));
            mRbIndonesian.setTextColor(Color.parseColor("#ffffff"));
        } else if (mCurrent.toString().equals("in")) {
            mRbIndonesian.setChecked(true);
            mRbEnglish.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regular_state));
            mRbEnglish.setTextColor(Color.parseColor("#ffffff"));

            mRbIndonesian.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selected_state));
            mRbIndonesian.setTextColor(Color.parseColor("#f8b500"));
        }

        mRgLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            if (mRbEnglish.isChecked()) {
                setLocale("en_US");
                mRbEnglish.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selected_state));
                mRbEnglish.setTextColor(Color.parseColor("#f8b500"));

                mRbIndonesian.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regular_state));
                mRbIndonesian.setTextColor(Color.parseColor("#ffffff"));
            } else if (mRbIndonesian.isChecked()) {
                setLocale("in");
                mRbEnglish.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regular_state));
                mRbEnglish.setTextColor(Color.parseColor("#ffffff"));

                mRbIndonesian.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selected_state));
                mRbIndonesian.setTextColor(Color.parseColor("#f8b500"));
            }
        });
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent intent = new Intent(this, MainActivity.class);
        SettingsActivity.instance.finish();
        MainActivity.instance.finish();
        startActivity(intent);
        finish();
    }
}
