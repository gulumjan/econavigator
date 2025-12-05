package com.example.econavigator.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FirebaseAchievement {
    public String achievementId;
    public String studentUid;
    public String title;
    public String description;
    public String iconName;
    public boolean unlocked;
    public long unlockedDate;

    public FirebaseAchievement() {}

    public FirebaseAchievement(String achievementId, String studentUid, String title,
                               String description, String iconName) {
        this.achievementId = achievementId;
        this.studentUid = studentUid;
        this.title = title;
        this.description = description;
        this.iconName = iconName;
        this.unlocked = false;
    }

    // Getters and Setters
    public String getAchievementId() { return achievementId; }
    public void setAchievementId(String achievementId) { this.achievementId = achievementId; }

    public String getStudentUid() { return studentUid; }
    public void setStudentUid(String studentUid) { this.studentUid = studentUid; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }

    public long getUnlockedDate() { return unlockedDate; }
    public void setUnlockedDate(long unlockedDate) { this.unlockedDate = unlockedDate; }

    public void unlock() {
        this.unlocked = true;
        this.unlockedDate = System.currentTimeMillis();
    }
}