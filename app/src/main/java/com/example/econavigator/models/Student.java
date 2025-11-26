package com.example.econavigator.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String className;
    private int points;
    private int level;
    private String avatarUrl;

    public Student() {}

    public Student(int id, String name, String className, int points, int level) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.points = points;
        this.level = level;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    // Helper methods
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
}