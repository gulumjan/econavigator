package com.example.econavigator.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GameResult {
    public String resultId;
    public String studentUid;
    public String gameName; // "sorting", "quiz", "search"
    public int score;
    public int pointsEarned;
    public long playedDate;
    public int level; // для sorting game

    public GameResult() {}

    public GameResult(String studentUid, String gameName, int score, int pointsEarned, int level) {
        this.resultId = String.valueOf(System.currentTimeMillis());
        this.studentUid = studentUid;
        this.gameName = gameName;
        this.score = score;
        this.pointsEarned = pointsEarned;
        this.playedDate = System.currentTimeMillis();
        this.level = level;
    }

    // Getters and Setters
    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }

    public String getStudentUid() { return studentUid; }
    public void setStudentUid(String studentUid) { this.studentUid = studentUid; }

    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }

    public long getPlayedDate() { return playedDate; }
    public void setPlayedDate(long playedDate) { this.playedDate = playedDate; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}