package com.example.econavigator.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.econavigator.database.EcoDatabase;
import com.example.econavigator.database.StudentDao;
import com.example.econavigator.database.AchievementDao;
import com.example.econavigator.database.MissionDao;
import com.example.econavigator.models.Student;
import com.example.econavigator.models.Achievement;
import com.example.econavigator.models.Mission;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository для централизованного доступа к данным
 */
public class EcoRepository {

    private StudentDao studentDao;
    private AchievementDao achievementDao;
    private MissionDao missionDao;
    private ExecutorService executorService;

    public EcoRepository(Application application) {
        EcoDatabase database = EcoDatabase.getInstance(application);
        studentDao = database.studentDao();
        achievementDao = database.achievementDao();
        missionDao = database.missionDao();
        executorService = Executors.newFixedThreadPool(2);
    }

    // ==================== STUDENT OPERATIONS ====================

    public LiveData<List<Student>> getAllStudents() {
        return studentDao.getAllStudents();
    }

    public LiveData<List<Student>> getTopStudents(int limit) {
        return studentDao.getTopStudents(limit);
    }

    public void insertStudent(Student student) {
        executorService.execute(() -> studentDao.insert(student));
    }

    public void updateStudent(Student student) {
        executorService.execute(() -> studentDao.update(student));
    }

    // ==================== ACHIEVEMENT OPERATIONS ====================

    public LiveData<List<Achievement>> getAllAchievements() {
        return achievementDao.getAllAchievements();
    }

    public LiveData<List<Achievement>> getUnlockedAchievements() {
        return achievementDao.getUnlockedAchievements();
    }

    public void insertAchievement(Achievement achievement) {
        executorService.execute(() -> achievementDao.insert(achievement));
    }

    public void unlockAchievement(int id, long date) {
        executorService.execute(() -> achievementDao.unlockAchievement(id, date));
    }

    // ==================== MISSION OPERATIONS ====================

    public LiveData<List<Mission>> getAllMissions() {
        return missionDao.getAllMissions();
    }

    public LiveData<List<Mission>> getActiveMissions() {
        return missionDao.getActiveMissions();
    }

    public void insertMission(Mission mission) {
        executorService.execute(() -> missionDao.insert(mission));
    }

    public void updateMission(Mission mission) {
        executorService.execute(() -> missionDao.update(mission));
    }

    public void completeMission(int id) {
        executorService.execute(() -> missionDao.completeMission(id));
    }
}