package com.example.econavigator.utils;

public class Constants {

    // School location (ISIT "Erudite")
    public static final double SCHOOL_LATITUDE = 42.8746;
    public static final double SCHOOL_LONGITUDE = 74.6122;
    public static final String SCHOOL_NAME = "ISIT Эрудит";
    public static final String SCHOOL_ADDRESS = "ул. 7 Апреля, 1/1, Бишкек";

    // Points system
    public static final int POINTS_BOTTLE = 10;
    public static final int POINTS_PAPER_KG = 20;
    public static final int POINTS_GLASS = 15;
    public static final int POINTS_DAILY_LOGIN = 5;
    public static final int POINTS_GAME_WIN = 5;

    // Levels
    public static final int LEVEL_1_THRESHOLD = 0;
    public static final int LEVEL_2_THRESHOLD = 100;
    public static final int LEVEL_3_THRESHOLD = 300;
    public static final int LEVEL_4_THRESHOLD = 600;
    public static final int LEVEL_5_THRESHOLD = 1000;

    // Level names
    public static final String LEVEL_1_NAME = "🌱 Новичок";
    public static final String LEVEL_2_NAME = "🌿 Юный эколог";
    public static final String LEVEL_3_NAME = "🍀 Защитник природы";
    public static final String LEVEL_4_NAME = "🌳 Эко-воин";
    public static final String LEVEL_5_NAME = "⭐ Эко-герой";

    // Waste types
    public static final String WASTE_PLASTIC = "plastic";
    public static final String WASTE_PAPER = "paper";
    public static final String WASTE_GLASS = "glass";

    // SharedPreferences keys
    public static final String PREFS_NAME = "EcoNavigatorPrefs";
    public static final String KEY_STUDENT_ID = "student_id";
    public static final String KEY_STUDENT_NAME = "student_name";
    public static final String KEY_STUDENT_CLASS = "student_class";
    public static final String KEY_STUDENT_POINTS = "student_points";
    public static final String KEY_STUDENT_LEVEL = "student_level";
    public static final String KEY_FIRST_LAUNCH = "first_launch";
    public static final String KEY_LAST_LOGIN_DATE = "last_login_date";

    // Animation durations
    public static final int ANIMATION_DURATION_SHORT = 300;
    public static final int ANIMATION_DURATION_MEDIUM = 500;
    public static final int ANIMATION_DURATION_LONG = 800;

    // Map settings
    public static final float DEFAULT_ZOOM = 15f;
    public static final int MAP_PADDING = 100;
}