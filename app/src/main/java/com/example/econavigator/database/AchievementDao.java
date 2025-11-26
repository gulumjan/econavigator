package com.example.econavigator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.econavigator.models.Achievement;

import java.util.List;

/**
 * DAO для работы с достижениями
 */
@Dao
public interface AchievementDao {

    @Insert
    long insert(Achievement achievement);

    @Update
    void update(Achievement achievement);

    @Delete
    void delete(Achievement achievement);

    @Query("SELECT * FROM achievements WHERE id = :id")
    LiveData<Achievement> getAchievementById(int id);

    @Query("SELECT * FROM achievements")
    LiveData<List<Achievement>> getAllAchievements();

    @Query("SELECT * FROM achievements WHERE unlocked = 1")
    LiveData<List<Achievement>> getUnlockedAchievements();

    @Query("UPDATE achievements SET unlocked = 1, unlockedDate = :date WHERE id = :id")
    void unlockAchievement(int id, long date);

    @Query("SELECT COUNT(*) FROM achievements WHERE unlocked = 1")
    LiveData<Integer> getUnlockedCount();

    @Query("DELETE FROM achievements")
    void deleteAll();
}