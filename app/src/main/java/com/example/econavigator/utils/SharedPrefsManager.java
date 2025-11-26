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

    // Student data
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

    // First launch
    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(Constants.KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunchComplete() {
        editor.putBoolean(Constants.KEY_FIRST_LAUNCH, false);
        editor.apply();
    }

    // Last login
    public void saveLastLoginDate() {
        editor.putLong(Constants.KEY_LAST_LOGIN_DATE, System.currentTimeMillis());
        editor.apply();
    }

    public long getLastLoginDate() {
        return sharedPreferences.getLong(Constants.KEY_LAST_LOGIN_DATE, 0);
    }

    // Clear all data
    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}