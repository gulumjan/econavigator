package com.example.econavigator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.econavigator.models.Mission;

import java.util.List;

/**
 * DAO для работы с миссиями
 */
@Dao
public interface MissionDao {

    @Insert
    long insert(Mission mission);

    @Update
    void update(Mission mission);

    @Delete
    void delete(Mission mission);

    @Query("SELECT * FROM missions WHERE id = :id")
    LiveData<Mission> getMissionById(int id);

    @Query("SELECT * FROM missions")
    LiveData<List<Mission>> getAllMissions();

    @Query("SELECT * FROM missions WHERE completed = 0")
    LiveData<List<Mission>> getActiveMissions();

    @Query("SELECT * FROM missions WHERE completed = 1")
    LiveData<List<Mission>> getCompletedMissions();

    @Query("SELECT * FROM missions WHERE type = :type")
    LiveData<List<Mission>> getMissionsByType(String type);

    @Query("UPDATE missions SET currentCount = :count WHERE id = :id")
    void updateProgress(int id, int count);

    @Query("UPDATE missions SET completed = 1 WHERE id = :id")
    void completeMission(int id);

    @Query("DELETE FROM missions")
    void deleteAll();
}