package com.example.econavigator.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.econavigator.models.FirebaseStudent;
import com.example.econavigator.firebase.FirebaseDataManager;

import java.util.List;

/**
 * ViewModel для управления данными студентов
 */
public class StudentViewModel extends AndroidViewModel {

    private FirebaseDataManager dataManager;
    private MutableLiveData<FirebaseStudent> currentStudent;
    private MutableLiveData<List<FirebaseStudent>> topStudents;
    private MutableLiveData<String> errorMessage;

    public StudentViewModel(Application application) {
        super(application);
        dataManager = new FirebaseDataManager();
        currentStudent = new MutableLiveData<>();
        topStudents = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    // ==================== GETTERS ====================

    public LiveData<FirebaseStudent> getCurrentStudent() {
        return currentStudent;
    }

    public LiveData<List<FirebaseStudent>> getTopStudents() {
        return topStudents;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // ==================== OPERATIONS ====================

    /**
     * Загрузить данные студента
     */
    public void loadStudent(String uid) {
        dataManager.getStudent(uid, new FirebaseDataManager.DataCallback<FirebaseStudent>() {
            @Override
            public void onSuccess(FirebaseStudent student) {
                currentStudent.postValue(student);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    /**
     * Загрузить топ студентов
     */
    public void loadTopStudents(int limit) {
        dataManager.getTopStudents(limit, new FirebaseDataManager.ListCallback<FirebaseStudent>() {
            @Override
            public void onSuccess(List<FirebaseStudent> dataList) {
                topStudents.postValue(dataList);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    /**
     * Обновить баллы студента
     */
    public void updatePoints(String uid, int pointsToAdd) {
        dataManager.updateStudentPoints(uid, pointsToAdd, new FirebaseDataManager.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                // Перезагружаем данные студента
                loadStudent(uid);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }
}