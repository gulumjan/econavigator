package com.example.econavigator.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPrefsManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // ==================== STUDENT DATA ====================

    public void saveStudentData(int id, String name, String className, int points, int level) {
        editor.putInt(Constants.KEY_STUDENT_ID, id);
        editor.putString(Constants.KEY_STUDENT_NAME, name);
        editor.putString(Constants.KEY_STUDENT_CLASS, className);
        editor.putInt(Constants.KEY_STUDENT_POINTS, points);
        editor.putInt(Constants.KEY_STUDENT_LEVEL, level);
        editor.apply();
    }

    public int getStudentId() {
        return sharedPreferences.getInt(Constants.KEY_STUDENT_ID, -1);
    }

    public String getStudentName() {
        return sharedPreferences.getString(Constants.KEY_STUDENT_NAME, "");
    }

    public String getStudentClass() {
        return sharedPreferences.getString(Constants.KEY_STUDENT_CLASS, "");
    }

    public int getStudentPoints() {
        return sharedPreferences.getInt(Constants.KEY_STUDENT_POINTS, 0);
    }

    public int getStudentLevel() {
        return sharedPreferences.getInt(Constants.KEY_STUDENT_LEVEL, 1);
    }

    public void updatePoints(int points) {
        editor.putInt(Constants.KEY_STUDENT_POINTS, points);
        editor.apply();
    }

    public void updateLevel(int level) {
        editor.putInt(Constants.KEY_STUDENT_LEVEL, level);
        editor.apply();
    }

    // ==================== FIREBASE DATA (НОВОЕ) ====================

    /**
     * Сохранить Firebase UID
     */
    public void saveFirebaseUid(String uid) {
        editor.putString(Constants.KEY_FIREBASE_UID, uid);
        editor.apply();
    }

    /**
     * Получить Firebase UID
     */
    public String getFirebaseUid() {
        return sharedPreferences.getString(Constants.KEY_FIREBASE_UID, "");
    }

    /**
     * Сохранить email
     */
    public void saveEmail(String email) {
        editor.putString(Constants.KEY_EMAIL, email);
        editor.apply();
    }

    /**
     * Получить email
     */
    public String getEmail() {
        return sharedPreferences.getString(Constants.KEY_EMAIL, "");
    }

    /**
     * Сохранить роль (student или admin)
     */
    public void saveRole(String role) {
        editor.putString(Constants.KEY_ROLE, role);
        editor.apply();
    }

    /**
     * Получить роль
     */
    public String getRole() {
        return sharedPreferences.getString(Constants.KEY_ROLE, "student");
    }

    /**
     * Проверить, является ли пользователь админом
     */
    public boolean isAdmin() {
        return "admin".equals(getRole());
    }

    /**
     * Проверить, авторизован ли пользователь
     */
    public boolean isLoggedIn() {
        return !getFirebaseUid().isEmpty();
    }

    // ==================== FIRST LAUNCH ====================

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(Constants.KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunchComplete() {
        editor.putBoolean(Constants.KEY_FIRST_LAUNCH, false);
        editor.apply();
    }

    // ==================== LAST LOGIN ====================

    public void saveLastLoginDate() {
        editor.putLong(Constants.KEY_LAST_LOGIN_DATE, System.currentTimeMillis());
        editor.apply();
    }

    public long getLastLoginDate() {
        return sharedPreferences.getLong(Constants.KEY_LAST_LOGIN_DATE, 0);
    }

    // ==================== CLEAR DATA ====================

    /**
     * Очистить все данные
     */
    public void clearAll() {
        editor.clear();
        editor.apply();
    }

    /**
     * Очистить только данные пользователя (для выхода)
     */
    public void clearUserData() {
        editor.remove(Constants.KEY_FIREBASE_UID);
        editor.remove(Constants.KEY_EMAIL);
        editor.remove(Constants.KEY_ROLE);
        editor.remove(Constants.KEY_STUDENT_NAME);
        editor.remove(Constants.KEY_STUDENT_CLASS);
        editor.remove(Constants.KEY_STUDENT_POINTS);
        editor.remove(Constants.KEY_STUDENT_LEVEL);
        editor.apply();
    }
}