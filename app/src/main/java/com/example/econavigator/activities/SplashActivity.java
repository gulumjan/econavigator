package com.example.econavigator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.econavigator.R;
import com.example.econavigator.utils.SharedPrefsManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 секунды

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Скрываем ActionBar/Toolbar, если есть
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Сохраняем дату последнего входа
        new SharedPrefsManager(this).saveLastLoginDate();

        // Переходим в MainActivity через 2 секунды
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // закрываем Splash, чтобы при нажатии Назад пользователь не вернулся сюда
        }, SPLASH_DURATION);
    }
}