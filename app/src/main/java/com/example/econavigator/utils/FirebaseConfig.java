package com.example.econavigator.utils;

/**
 * Firebase Configuration
 * Centralized configuration for Firebase services
 */
public class FirebaseConfig {

    /**
     * Firebase Realtime Database URL
     * This is your database instance URL from Firebase Console
     */
    public static final String DATABASE_URL = "https://inst-81b86-default-rtdb.europe-west1.firebasedatabase.app/";

    /**
     * Database node names
     */
    public static final String NODE_STUDENTS = "students";
    public static final String NODE_LEADERBOARD = "leaderboard";
    public static final String NODE_ACHIEVEMENTS = "achievements";
    public static final String NODE_MISSIONS = "missions";
    public static final String NODE_GAME_RESULTS = "game_results";

    /**
     * Student roles
     */
    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_ADMIN = "admin";
}