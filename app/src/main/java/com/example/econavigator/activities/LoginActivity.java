package com.example.econavigator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.econavigator.R;
import com.example.econavigator.firebase.FirebaseAuthManager;
import com.example.econavigator.models.FirebaseStudent;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private ProgressBar progressBar;

    private FirebaseAuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Скрыть ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        authManager = new FirebaseAuthManager(this);

        // Проверяем, авторизован ли уже пользователь
        if (authManager.isUserLoggedIn()) {
            goToMainActivity();
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        progressBar = findViewById(R.id.progress_bar);

        btnLogin.setOnClickListener(v -> loginUser());
        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Валидация
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Введите email");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Неверный формат email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Введите пароль");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Пароль должен быть минимум 6 символов");
            etPassword.requestFocus();
            return;
        }

        // Показываем прогресс
        showLoading(true);

        // Вход через Firebase
        authManager.login(email, password, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseStudent student) {
                showLoading(false);
                Toast.makeText(LoginActivity.this,
                        "Добро пожаловать, " + student.getName() + "!",
                        Toast.LENGTH_SHORT).show();

                // Проверяем роль и переходим на соответствующий экран
                if (student.isAdmin()) {
                    goToAdminDashboard();
                } else {
                    goToMainActivity();
                }
            }

            @Override
            public void onError(String error) {
                showLoading(false);

                // Пользовательские сообщения об ошибках
                String errorMessage;
                if (error.contains("no user record")) {
                    errorMessage = "Пользователь не найден";
                } else if (error.contains("password is invalid")) {
                    errorMessage = "Неверный пароль";
                } else if (error.contains("network error")) {
                    errorMessage = "Проверьте подключение к интернету";
                } else {
                    errorMessage = "Ошибка входа: " + error;
                }

                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showForgotPasswordDialog() {
        // Создаём диалог для ввода email
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Сброс пароля");
        builder.setMessage("Введите ваш email:");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        builder.setPositiveButton("Отправить", (dialog, which) -> {
            String email = input.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();
                return;
            }

            showLoading(true);

            authManager.resetPassword(email, new FirebaseAuthManager.AuthCallback() {
                @Override
                public void onSuccess(FirebaseStudent student) {
                    showLoading(false);
                    Toast.makeText(LoginActivity.this,
                            "Письмо для сброса пароля отправлено на " + email,
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String error) {
                    showLoading(false);
                    Toast.makeText(LoginActivity.this,
                            "Ошибка: " + error,
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        etEmail.setEnabled(!show);
        etPassword.setEnabled(!show);
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