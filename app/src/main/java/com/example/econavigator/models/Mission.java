package com.example.econavigator.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "missions")
public class Mission {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int targetCount;
    private int currentCount;
    private int rewardPoints;
    private boolean completed;
    private String type; // daily, weekly, special

    public Mission() {}

    public Mission(int id, String title, String description, int targetCount, int rewardPoints, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetCount = targetCount;
        this.currentCount = 0;
        this.rewardPoints = rewardPoints;
        this.completed = false;
        this.type = type;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getTargetCount() { return targetCount; }
    public void setTargetCount(int targetCount) { this.targetCount = targetCount; }

    public int getCurrentCount() { return currentCount; }
    public void setCurrentCount(int currentCount) { this.currentCount = currentCount; }

    public int getRewardPoints() { return rewardPoints; }
    public void setRewardPoints(int rewardPoints) { this.rewardPoints = rewardPoints; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // Helper methods
    public void incrementProgress() {
        if (!completed && currentCount < targetCount) {
            currentCount++;
            if (currentCount >= targetCount) {
                completed = true;
            }
        }
    }

    public int getProgressPercentage() {
        return (int) ((float) currentCount / targetCount * 100);
    }
}