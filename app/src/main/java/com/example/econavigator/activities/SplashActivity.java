package com.example.econavigator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.econavigator.R;
import com.example.econavigator.firebase.FirebaseAuthManager;
import com.example.econavigator.utils.SharedPrefsManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 секунды

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Скрываем ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Проверяем авторизацию после задержки
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkAuthAndNavigate();
        }, SPLASH_DURATION);
    }

    private void checkAuthAndNavigate() {
        FirebaseAuthManager authManager = new FirebaseAuthManager(this);
        SharedPrefsManager prefsManager = new SharedPrefsManager(this);

        // Проверяем, авторизован ли пользователь
        if (authManager.isUserLoggedIn() && prefsManager.isLoggedIn()) {
            // Сохраняем дату последнего входа
            prefsManager.saveLastLoginDate();

            // Проверяем роль и переходим на соответствующий экран
            String role = prefsManager.getRole();

            if ("admin".equals(role)) {
                goToAdminDashboard();
            } else {
                goToMainActivity();
            }
        } else {
            // Пользователь не авторизован - переходим на экран входа
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToAdminDashboard() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}