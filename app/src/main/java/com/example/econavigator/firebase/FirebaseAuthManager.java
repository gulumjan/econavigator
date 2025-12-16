package com.example.econavigator.firebase;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.econavigator.models.FirebaseStudent;
import com.example.econavigator.utils.SharedPrefsManager;

public class FirebaseAuthManager {

    private static final String TAG = "FirebaseAuthManager";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context context;

    public interface AuthCallback {
        void onSuccess(FirebaseStudent student);
        void onError(String error);
    }

    public FirebaseAuthManager(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();

        // Configure Firebase Database with your database URL
        String databaseUrl = "https://inst-81b86-default-rtdb.europe-west1.firebasedatabase.app/";
        FirebaseDatabase database = FirebaseDatabase.getInstance(databaseUrl);
        this.mDatabase = database.getReference();
    }

    /**
     * Вход по email и паролю
     */
    public void login(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            loadStudentData(user.getUid(), callback);
                        }
                    } else {
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Ошибка входа";
                        callback.onError(error);
                    }
                });
    }

    /**
     * Регистрация нового ученика (только для админа)
     */
    public void registerStudent(String email, String password, String name,
                                String className, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Создаём объект студента
                            FirebaseStudent student = new FirebaseStudent(
                                    user.getUid(), email, name, className, "student"
                            );

                            // Сохраняем в базу
                            mDatabase.child("students").child(user.getUid())
                                    .setValue(student)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            // Инициализируем leaderboard для нового ученика
                                            mDatabase.child("leaderboard").child(user.getUid())
                                                    .setValue(0);

                                            callback.onSuccess(student);
                                        } else {
                                            callback.onError("Ошибка сохранения в базу");
                                        }
                                    });
                        }
                    } else {
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Ошибка регистрации";
                        callback.onError(error);
                    }
                });
    }

    /**
     * Загрузить данные студента
     */
    private void loadStudentData(String uid, AuthCallback callback) {
        mDatabase.child("students").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseStudent student = snapshot.getValue(FirebaseStudent.class);
                        if (student != null) {
                            // Сохраняем в SharedPrefs
                            saveToSharedPrefs(student);

                            // Обновляем время последнего входа
                            updateLastLogin(uid);

                            callback.onSuccess(student);
                        } else {
                            callback.onError("Пользователь не найден в базе");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    /**
     * Обновить время последнего входа
     */
    private void updateLastLogin(String uid) {
        mDatabase.child("students").child(uid).child("lastLoginDate")
                .setValue(System.currentTimeMillis());
    }

    /**
     * Сохранить в SharedPreferences
     */
    private void saveToSharedPrefs(FirebaseStudent student) {
        SharedPrefsManager prefsManager = new SharedPrefsManager(context);
        prefsManager.saveStudentData(
                0, // id не нужен для Firebase
                student.getName(),
                student.getClassName(),
                student.getPoints(),
                student.getLevel()
        );
        prefsManager.saveFirebaseUid(student.getUid());
        prefsManager.saveEmail(student.getEmail());
        prefsManager.saveRole(student.getRole());
    }

    /**
     * Выход
     */
    public void logout() {
        mAuth.signOut();
        SharedPrefsManager prefsManager = new SharedPrefsManager(context);
        prefsManager.clearAll();
    }

    /**
     * Получить текущего пользователя
     */
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    /**
     * Проверка авторизации
     */
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    /**
     * Получить UID текущего пользователя
     */
    public String getCurrentUid() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    /**
     * Сброс пароля
     */
    public void resetPassword(String email, AuthCallback callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Ошибка сброса пароля";
                        callback.onError(error);
                    }
                });
    }
}