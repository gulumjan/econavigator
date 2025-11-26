package com.example.econavigator.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class Achievement {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int iconResource;
    private boolean unlocked;
    private long unlockedDate;

    public Achievement() {}

    public Achievement(int id, String title, String description, int iconResource) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconResource = iconResource;
        this.unlocked = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getIconResource() { return iconResource; }
    public void setIconResource(int iconResource) { this.iconResource = iconResource; }

    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }

    public long getUnlockedDate() { return unlockedDate; }
    public void setUnlockedDate(long unlockedDate) { this.unlockedDate = unlockedDate; }

    public void unlock() {
        this.unlocked = true;
        this.unlockedDate = System.currentTimeMillis();
    }
}