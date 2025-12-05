package com.example.econavigator.firebase;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.econavigator.models.FirebaseStudent;
import com.example.econavigator.models.FirebaseAchievement;
import com.example.econavigator.models.FirebaseMission;
import com.example.econavigator.models.GameResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseDataManager {

    private static final String TAG = "FirebaseDataManager";
    private DatabaseReference mDatabase;

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    public interface ListCallback<T> {
        void onSuccess(List<T> dataList);
        void onError(String error);
    }

    public FirebaseDataManager() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // ==================== STUDENT OPERATIONS ====================

    /**
     * Получить данные студента
     */
    public void getStudent(String uid, DataCallback<FirebaseStudent> callback) {
        mDatabase.child("students").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseStudent student = snapshot.getValue(FirebaseStudent.class);
                        if (student != null) {
                            callback.onSuccess(student);
                        } else {
                            callback.onError("Студент не найден");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    /**
     * Получить данные студента в реальном времени
     */
    public void getStudentRealtime(String uid, DataCallback<FirebaseStudent> callback) {
        mDatabase.child("students").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseStudent student = snapshot.getValue(FirebaseStudent.class);
                        if (student != null) {
                            callback.onSuccess(student);
                        } else {
                            callback.onError("Студент не найден");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    /**
     * Обновить баллы студента
     */
    public void updateStudentPoints(String uid, int pointsToAdd, DataCallback<Boolean> callback) {
        // Получаем текущие данные
        getStudent(uid, new DataCallback<FirebaseStudent>() {
            @Override
            public void onSuccess(FirebaseStudent student) {
                int oldLevel = student.getLevel();
                student.addPoints(pointsToAdd); // автоматически обновит уровень
                int newLevel = student.getLevel();

                // Сохраняем обратно
                mDatabase.child("students").child(uid)
                        .setValue(student)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Обновляем leaderboard
                                updateLeaderboard(uid, student.getPoints());

                                // Проверяем повышение уровня
                                if (newLevel > oldLevel) {
                                    Log.d(TAG, "Level up! New level: " + newLevel);
                                }

                                callback.onSuccess(true);
                            } else {
                                callback.onError("Ошибка обновления баллов");
                            }
                        });
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    /**
     * Обновить leaderboard
     */
    private void updateLeaderboard(String uid, int points) {
        mDatabase.child("leaderboard").child(uid).setValue(points);
    }

    /**
     * Получить топ студентов
     */
    public void getTopStudents(int limit, ListCallback<FirebaseStudent> callback) {
        Query query = mDatabase.child("students")
                .orderByChild("points")
                .limitToLast(limit);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FirebaseStudent> students = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    FirebaseStudent student = child.getValue(FirebaseStudent.class);
                    if (student != null && "student".equals(student.getRole())) {
                        students.add(student);
                    }
                }
                // Сортируем по убыванию баллов
                Collections.reverse(students);
                callback.onSuccess(students);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    /**
     * Получить топ студентов в реальном времени
     */
    public void getTopStudentsRealtime(int limit, ListCallback<FirebaseStudent> callback) {
        Query query = mDatabase.child("students")
                .orderByChild("points")
                .limitToLast(limit);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FirebaseStudent> students = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    FirebaseStudent student = child.getValue(FirebaseStudent.class);
                    if (student != null && "student".equals(student.getRole())) {
                        students.add(student);
                    }
                }
                Collections.reverse(students);
                callback.onSuccess(students);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    /**
     * Получить студентов по классу
     */
    public void getStudentsByClass(String className, ListCallback<FirebaseStudent> callback) {
        Query query = mDatabase.child("students")
                .orderByChild("className")
                .equalTo(className);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FirebaseStudent> students = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    FirebaseStudent student = child.getValue(FirebaseStudent.class);
                    if (student != null) {
                        students.add(student);
                    }
                }
                // Сортируем по баллам
                students.sort((s1, s2) -> Integer.compare(s2.getPoints(), s1.getPoints()));
                callback.onSuccess(students);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    // ==================== ACHIEVEMENT OPERATIONS ====================

    /**
     * Получить достижения студента
     */
    public void getStudentAchievements(String uid, ListCallback<FirebaseAchievement> callback) {
        mDatabase.child("achievements").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<FirebaseAchievement> achievements = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            FirebaseAchievement achievement = child.getValue(FirebaseAchievement.class);
                            if (achievement != null) {
                                achievements.add(achievement);
                            }
                        }
                        callback.onSuccess(achievements);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    /**
     * Добавить достижение
     */
    public void addAchievement(FirebaseAchievement achievement, DataCallback<Boolean> callback) {
        mDatabase.child("achievements")
                .child(achievement.getStudentUid())
                .child(achievement.getAchievementId())
                .setValue(achievement)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onError("Ошибка добавления достижения");
                    }
                });
    }

    /**
     * Разблокировать достижение
     */
    public void unlockAchievement(String uid, String achievementId, DataCallback<Boolean> callback) {
        mDatabase.child("achievements").child(uid).child(achievementId)
                .child("unlocked").setValue(true);

        mDatabase.child("achievements").child(uid).child(achievementId)
                .child("unlockedDate").setValue(System.currentTimeMillis())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onError("Ошибка разблокировки");
                    }
                });
    }

    // ==================== MISSION OPERATIONS ====================

    /**
     * Получить миссии студента
     */
    public void getStudentMissions(String uid, ListCallback<FirebaseMission> callback) {
        mDatabase.child("missions").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<FirebaseMission> missions = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            FirebaseMission mission = child.getValue(FirebaseMission.class);
                            if (mission != null) {
                                missions.add(mission);
                            }
                        }
                        callback.onSuccess(missions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    /**
     * Получить активные миссии
     */
    public void getActiveMissions(String uid, ListCallback<FirebaseMission> callback) {
        mDatabase.child("missions").child(uid)
                .orderByChild("completed")
                .equalTo(false)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<FirebaseMission> missions = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            FirebaseMission mission = child.getValue(FirebaseMission.class);
                            if (mission != null) {
                                missions.add(mission);
                            }
                        }
                        callback.onSuccess(missions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    /**
     * Добавить миссию
     */
    public void addMission(FirebaseMission mission, DataCallback<Boolean> callback) {
        mDatabase.child("missions")
                .child(mission.getStudentUid())
                .child(mission.getMissionId())
                .setValue(mission)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onError("Ошибка добавления миссии");
                    }
                });
    }

    /**
     * Обновить прогресс миссии
     */
    public void updateMissionProgress(String uid, String missionId, int newCount, DataCallback<Boolean> callback) {
        mDatabase.child("missions").child(uid).child(missionId)
                .child("currentCount").setValue(newCount)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onError("Ошибка обновления прогресса");
                    }
                });
    }

    /**
     * Завершить миссию
     */
    public void completeMission(String uid, String missionId, DataCallback<Boolean> callback) {
        mDatabase.child("missions").child(uid).child(missionId)
                .child("completed").setValue(true);

        mDatabase.child("missions").child(uid).child(missionId)
                .child("completedDate").setValue(System.currentTimeMillis())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onError("Ошибка завершения миссии");
                    }
                });
    }

    // ==================== GAME RESULTS OPERATIONS ====================

    /**
     * Сохранить результат игры
     */
    public void saveGameResult(GameResult result, DataCallback<Boolean> callback) {
        mDatabase.child("game_results")
                .child(result.getStudentUid())
                .child(result.getResultId())
                .setValue(result)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onError("Ошибка сохранения результата");
                    }
                });
    }

    /**
     * Получить историю игр студента
     */
    public void getGameHistory(String uid, ListCallback<GameResult> callback) {
        mDatabase.child("game_results").child(uid)
                .orderByChild("playedDate")
                .limitToLast(20)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<GameResult> results = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            GameResult result = child.getValue(GameResult.class);
                            if (result != null) {
                                results.add(result);
                            }
                        }
                        Collections.reverse(results);
                        callback.onSuccess(results);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    // ==================== ADMIN OPERATIONS ====================

    /**
     * Получить всех студентов (для админа)
     */
    public void getAllStudents(ListCallback<FirebaseStudent> callback) {
        mDatabase.child("students")
                .orderByChild("role")
                .equalTo("student")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<FirebaseStudent> students = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            FirebaseStudent student = child.getValue(FirebaseStudent.class);
                            if (student != null) {
                                students.add(student);
                            }
                        }
                        callback.onSuccess(students);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    /**
     * Удалить студента (для админа)
     */
    public void deleteStudent(String uid, DataCallback<Boolean> callback) {
        mDatabase.child("students").child(uid).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Удаляем из leaderboard
                        mDatabase.child("leaderboard").child(uid).removeValue();
                        callback.onSuccess(true);
                    } else {
                        callback.onError("Ошибка удаления студента");
                    }
                });
    }
}