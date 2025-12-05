package com.example.econavigator.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.econavigator.R;
import com.example.econavigator.firebase.FirebaseAuthManager;
import com.example.econavigator.models.FirebaseStudent;
import com.example.econavigator.utils.PasswordGenerator;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private Spinner spinnerClass;
    private TextView tvGeneratedPassword;
    private Button btnGeneratePassword, btnRegister, btnBack;
    private ProgressBar progressBar;

    private FirebaseAuthManager authManager;
    private String generatedPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Регистрация ученика");
        }

        initViews();
        setupClassSpinner();
        authManager = new FirebaseAuthManager(this);
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        spinnerClass = findViewById(R.id.spinner_class);
        tvGeneratedPassword = findViewById(R.id.tv_generated_password);
        btnGeneratePassword = findViewById(R.id.btn_generate_password);
        btnRegister = findViewById(R.id.btn_register);
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progress_bar);

        btnGeneratePassword.setOnClickListener(v -> generatePassword());
        btnRegister.setOnClickListener(v -> registerStudent());
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupClassSpinner() {
        String[] classes = {
                "5А", "5Б", "5В", "5Г",
                "6А", "6Б", "6В", "6Г",
                "7А", "7Б", "7В", "7Г",
                "8А", "8Б", "8В", "8Г",
                "9А", "9Б", "9В", "9Г",
                "10А", "10Б", "10В",
                "11А", "11Б", "11В"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                classes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(adapter);
    }

    private void generatePassword() {
        String studentName = etName.getText().toString().trim();

        if (TextUtils.isEmpty(studentName)) {
            // Генерируем простой пароль
            generatedPassword = PasswordGenerator.generateSimplePassword(8);
        } else {
            // Генерируем на основе имени
            generatedPassword = PasswordGenerator.generateStudentPassword(studentName);
        }

        tvGeneratedPassword.setText("Пароль: " + generatedPassword);
        tvGeneratedPassword.setVisibility(View.VISIBLE);

        // Показываем силу пароля
        int strength = PasswordGenerator.checkPasswordStrength(generatedPassword);
        String strengthText = PasswordGenerator.getPasswordStrengthText(strength);
        Toast.makeText(this, "Надёжность: " + strengthText, Toast.LENGTH_SHORT).show();
    }

    private void registerStudent() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String className = spinnerClass.getSelectedItem().toString();

        // Валидация
        if (TextUtils.isEmpty(name)) {
            etName.setError("Введите имя ученика");
            etName.requestFocus();
            return;
        }

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

        if (TextUtils.isEmpty(generatedPassword)) {
            Toast.makeText(this, "Сгенерируйте пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        // Показываем прогресс
        showLoading(true);

        // Регистрация через Firebase
        authManager.registerStudent(email, generatedPassword, name, className,
                new FirebaseAuthManager.AuthCallback() {
                    @Override
                    public void onSuccess(FirebaseStudent student) {
                        showLoading(false);
                        showSuccessDialog(student);
                    }

                    @Override
                    public void onError(String error) {
                        showLoading(false);

                        String errorMessage;
                        if (error.contains("email address is already in use")) {
                            errorMessage = "Этот email уже зарегистрирован";
                        } else if (error.contains("network error")) {
                            errorMessage = "Проверьте подключение к интернету";
                        } else {
                            errorMessage = "Ошибка регистрации: " + error;
                        }

                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showSuccessDialog(FirebaseStudent student) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("✅ Ученик зарегистрирован!");

        String message = String.format(
                "Имя: %s\n" +
                        "Класс: %s\n" +
                        "Email: %s\n" +
                        "Пароль: %s\n\n" +
                        "⚠️ ВАЖНО: Сохраните эти данные и отправьте ученику!",
                student.getName(),
                student.getClassName(),
                student.getEmail(),
                generatedPassword
        );

        builder.setMessage(message);

        builder.setPositiveButton("Скопировать данные", (dialog, which) -> {
            // Копируем в буфер обмена
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(
                    "Данные ученика",
                    String.format("Email: %s\nПароль: %s", student.getEmail(), generatedPassword)
            );
            clipboard.setPrimaryClip(clip);

            Toast.makeText(RegisterActivity.this,
                    "Данные скопированы в буфер обмена",
                    Toast.LENGTH_SHORT).show();

            // Очищаем форму для нового ученика
            clearForm();
        });

        builder.setNegativeButton("Закрыть", (dialog, which) -> {
            clearForm();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void clearForm() {
        etName.setText("");
        etEmail.setText("");
        tvGeneratedPassword.setVisibility(View.GONE);
        generatedPassword = "";
        spinnerClass.setSelection(0);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
        btnGeneratePassword.setEnabled(!show);
        etName.setEnabled(!show);
        etEmail.setEnabled(!show);
        spinnerClass.setEnabled(!show);
    }
}