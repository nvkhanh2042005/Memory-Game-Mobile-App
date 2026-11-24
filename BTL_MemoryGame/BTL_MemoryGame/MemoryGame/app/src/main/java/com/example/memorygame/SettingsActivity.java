package com.example.memorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME =
            "game_settings";

    private static final String KEY_MUSIC_ENABLED =
            "music_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // =========================
        // GIAO DIỆN
        // =========================

        ImageButton btnBack =
                findViewById(R.id.btnBack);

        MaterialSwitch switchMusic =
                findViewById(R.id.switchMusic);

        // =========================
        // SHARED PREFERENCES
        // =========================

        SharedPreferences preferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );

        // Nếu chưa từng lưu thì mặc định bật nhạc
        boolean musicEnabled =
                preferences.getBoolean(
                        KEY_MUSIC_ENABLED,
                        true
                );

        switchMusic.setChecked(
                musicEnabled
        );

        // =========================
        // NÚT QUAY LẠI
        // =========================

        btnBack.setOnClickListener(
                view -> finish()
        );

        // =========================
        // BẬT / TẮT NHẠC
        // =========================

        switchMusic.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        preferences
                                .edit()
                                .putBoolean(
                                        KEY_MUSIC_ENABLED,
                                        isChecked
                                )
                                .apply()
        );
    }
}