package com.example.econavigator.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.econavigator.models.Student;
import com.example.econavigator.models.Achievement;
import com.example.econavigator.models.Mission;

/**
 * Room Database для Эко-Навигатора
 */
@Database(entities = {Student.class, Achievement.class, Mission.class}, version = 1, exportSchema = false)
public abstract class EcoDatabase extends RoomDatabase {

    private static EcoDatabase instance;

    // DAOs
    public abstract StudentDao studentDao();
    public abstract AchievementDao achievementDao();
    public abstract MissionDao missionDao();

    /**
     * Singleton instance
     */
    public static synchronized EcoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            EcoDatabase.class,
                            "eco_navigator_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}