package com.example.econavigator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.econavigator.models.Student;

import java.util.List;

/**
 * DAO для работы с учениками
 */
@Dao
public interface StudentDao {

    @Insert
    long insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM students WHERE id = :id")
    LiveData<Student> getStudentById(int id);

    @Query("SELECT * FROM students ORDER BY points DESC")
    LiveData<List<Student>> getAllStudents();

    @Query("SELECT * FROM students WHERE className = :className ORDER BY points DESC")
    LiveData<List<Student>> getStudentsByClass(String className);

    @Query("SELECT * FROM students ORDER BY points DESC LIMIT :limit")
    LiveData<List<Student>> getTopStudents(int limit);

    @Query("UPDATE students SET points = :points WHERE id = :id")
    void updatePoints(int id, int points);

    @Query("UPDATE students SET level = :level WHERE id = :id")
    void updateLevel(int id, int level);

    @Query("DELETE FROM students")
    void deleteAll();
}