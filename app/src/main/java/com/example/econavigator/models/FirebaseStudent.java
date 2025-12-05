package com.example.econavigator.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FirebaseStudent {
    public String uid;
    public String email;
    public String name;
    public String className;
    public int points;
    public int level;
    public String avatarUrl;
    public long lastLoginDate;
    public long registrationDate;
    public String role; // "student" или "admin"

    public FirebaseStudent() {
        // Default constructor required for Firebase
    }

    public FirebaseStudent(String uid, String email, String name, String className, String role) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.className = className;
        this.points = 0;
        this.level = 1;
        this.registrationDate = System.currentTimeMillis();
        this.role = role;
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public long getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(long lastLoginDate) { this.lastLoginDate = lastLoginDate; }

    public long getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(long registrationDate) { this.registrationDate = registrationDate; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Helper method
    public void addPoints(int points) {
        this.points += points;
        updateLevel();
    }

    private void updateLevel() {
        if (points >= 1000) level = 5;
        else if (points >= 600) level = 4;
        else if (points >= 300) level = 3;
        else if (points >= 100) level = 2;
        else level = 1;
    }

    public String getLevelTitle() {
        switch (level) {
            case 5: return "⭐ Эко-герой";
            case 4: return "🌳 Эко-воин";
            case 3: return "🍀 Защитник природы";
            case 2: return "🌿 Юный эколог";
            default: return "🌱 Новичок";
        }
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }
}