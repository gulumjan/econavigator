package com.example.econavigator.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FirebaseMission {
    public String missionId;
    public String studentUid;
    public String title;
    public String description;
    public int targetCount;
    public int currentCount;
    public int rewardPoints;
    public boolean completed;
    public String type; // daily, weekly, special
    public long createdDate;
    public long completedDate;

    public FirebaseMission() {}

    public FirebaseMission(String missionId, String studentUid, String title,
                           String description, int targetCount, int rewardPoints, String type) {
        this.missionId = missionId;
        this.studentUid = studentUid;
        this.title = title;
        this.description = description;
        this.targetCount = targetCount;
        this.currentCount = 0;
        this.rewardPoints = rewardPoints;
        this.completed = false;
        this.type = type;
        this.createdDate = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }

    public String getStudentUid() { return studentUid; }
    public void setStudentUid(String studentUid) { this.studentUid = studentUid; }

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

    public long getCreatedDate() { return createdDate; }
    public void setCreatedDate(long createdDate) { this.createdDate = createdDate; }

    public long getCompletedDate() { return completedDate; }
    public void setCompletedDate(long completedDate) { this.completedDate = completedDate; }

    // Helper methods
    public void incrementProgress() {
        if (!completed && currentCount < targetCount) {
            currentCount++;
            if (currentCount >= targetCount) {
                completed = true;
                completedDate = System.currentTimeMillis();
            }
        }
    }

    public int getProgressPercentage() {
        if (targetCount == 0) return 0;
        return (int) ((float) currentCount / targetCount * 100);
    }
}