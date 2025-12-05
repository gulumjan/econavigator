package com.example.econavigator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.econavigator.R;
import com.example.econavigator.firebase.FirebaseAuthManager;
import com.example.econavigator.firebase.FirebaseDataManager;
import com.example.econavigator.models.FirebaseStudent;
import com.example.econavigator.adapters.LeaderboardAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvTotalStudents, tvTotalPoints, tvAveragePoints;
    private CardView cardAddStudent, cardViewAllStudents, cardStatistics;
    private RecyclerView rvTopStudents;

    private FirebaseDataManager dataManager;
    private FirebaseAuthManager authManager;
    private LeaderboardAdapter adapter;
    private List<FirebaseStudent> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Админ-панель");
        }

        initViews();
        dataManager = new FirebaseDataManager();
        authManager = new FirebaseAuthManager(this);

        loadStatistics();
        loadTopStudents();
    }

    private void initViews() {
        tvTotalStudents = findViewById(R.id.tv_total_students);
        tvTotalPoints = findViewById(R.id.tv_total_points);
        tvAveragePoints = findViewById(R.id.tv_average_points);

        cardAddStudent = findViewById(R.id.card_add_student);
        cardViewAllStudents = findViewById(R.id.card_view_all_students);
        cardStatistics = findViewById(R.id.card_statistics);

        rvTopStudents = findViewById(R.id.rv_top_students);
        rvTopStudents.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        adapter = new LeaderboardAdapter(this, studentList);
        rvTopStudents.setAdapter(adapter);

        // Listeners
        cardAddStudent.setOnClickListener(v -> openRegisterActivity());
        cardViewAllStudents.setOnClickListener(v -> showAllStudents());
        cardStatistics.setOnClickListener(v -> showDetailedStatistics());
    }

    private void loadStatistics() {
        dataManager.getAllStudents(new FirebaseDataManager.ListCallback<FirebaseStudent>() {
            @Override
            public void onSuccess(List<FirebaseStudent> dataList) {
                int totalStudents = dataList.size();
                int totalPoints = 0;

                for (FirebaseStudent student : dataList) {
                    totalPoints += student.getPoints();
                }

                int averagePoints = totalStudents > 0 ? totalPoints / totalStudents : 0;

                tvTotalStudents.setText(String.valueOf(totalStudents));
                tvTotalPoints.setText(String.valueOf(totalPoints));
                tvAveragePoints.setText(String.valueOf(averagePoints));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Ошибка загрузки статистики: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTopStudents() {
        dataManager.getTopStudents(10, new FirebaseDataManager.ListCallback<FirebaseStudent>() {
            @Override
            public void onSuccess(List<FirebaseStudent> dataList) {
                studentList.clear();
                studentList.addAll(dataList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Ошибка загрузки лидеров: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showAllStudents() {
        dataManager.getAllStudents(new FirebaseDataManager.ListCallback<FirebaseStudent>() {
            @Override
            public void onSuccess(List<FirebaseStudent> dataList) {
                // Создаём диалог со списком всех учеников
                String[] studentNames = new String[dataList.size()];
                for (int i = 0; i < dataList.size(); i++) {
                    FirebaseStudent student = dataList.get(i);
                    studentNames[i] = String.format("%s (%s) - %d баллов",
                            student.getName(),
                            student.getClassName(),
                            student.getPoints());
                }

                new AlertDialog.Builder(AdminDashboardActivity.this)
                        .setTitle("Все ученики (" + dataList.size() + ")")
                        .setItems(studentNames, (dialog, which) -> {
                            showStudentDetails(dataList.get(which));
                        })
                        .setPositiveButton("Закрыть", null)
                        .show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Ошибка: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStudentDetails(FirebaseStudent student) {
        String details = String.format(
                "Имя: %s\n" +
                        "Класс: %s\n" +
                        "Email: %s\n" +
                        "Баллы: %d\n" +
                        "Уровень: %d (%s)\n" +
                        "Дата регистрации: %s",
                student.getName(),
                student.getClassName(),
                student.getEmail(),
                student.getPoints(),
                student.getLevel(),
                student.getLevelTitle(),
                new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
                        .format(new java.util.Date(student.getRegistrationDate()))
        );

        new AlertDialog.Builder(this)
                .setTitle("Информация об ученике")
                .setMessage(details)
                .setPositiveButton("Закрыть", null)
                .setNegativeButton("Удалить", (dialog, which) -> {
                    confirmDeleteStudent(student);
                })
                .show();
    }

    private void confirmDeleteStudent(FirebaseStudent student) {
        new AlertDialog.Builder(this)
                .setTitle("Удаление ученика")
                .setMessage("Вы уверены, что хотите удалить " + student.getName() + "?")
                .setPositiveButton("Да, удалить", (dialog, which) -> {
                    deleteStudent(student.getUid());
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void deleteStudent(String uid) {
        dataManager.deleteStudent(uid, new FirebaseDataManager.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Ученик удалён",
                        Toast.LENGTH_SHORT).show();
                loadStatistics();
                loadTopStudents();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Ошибка удаления: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDetailedStatistics() {
        dataManager.getAllStudents(new FirebaseDataManager.ListCallback<FirebaseStudent>() {
            @Override
            public void onSuccess(List<FirebaseStudent> dataList) {
                // Подсчитываем статистику по уровням
                int[] levelCounts = new int[6]; // 0-5 уровни
                int[] classCounts = new int[7]; // 5-11 классы

                for (FirebaseStudent student : dataList) {
                    levelCounts[student.getLevel()]++;

                    // Подсчёт по классам (упрощенно)
                    String className = student.getClassName();
                    if (className.startsWith("5")) classCounts[0]++;
                    else if (className.startsWith("6")) classCounts[1]++;
                    else if (className.startsWith("7")) classCounts[2]++;
                    else if (className.startsWith("8")) classCounts[3]++;
                    else if (className.startsWith("9")) classCounts[4]++;
                    else if (className.startsWith("10")) classCounts[5]++;
                    else if (className.startsWith("11")) classCounts[6]++;
                }

                String stats = "📊 Статистика по уровням:\n\n" +
                        "🌱 Новичок: " + levelCounts[1] + "\n" +
                        "🌿 Юный эколог: " + levelCounts[2] + "\n" +
                        "🍀 Защитник природы: " + levelCounts[3] + "\n" +
                        "🌳 Эко-воин: " + levelCounts[4] + "\n" +
                        "⭐ Эко-герой: " + levelCounts[5] + "\n\n" +
                        "📚 По классам:\n\n" +
                        "5 класс: " + classCounts[0] + "\n" +
                        "6 класс: " + classCounts[1] + "\n" +
                        "7 класс: " + classCounts[2] + "\n" +
                        "8 класс: " + classCounts[3] + "\n" +
                        "9 класс: " + classCounts[4] + "\n" +
                        "10 класс: " + classCounts[5] + "\n" +
                        "11 класс: " + classCounts[6];

                new AlertDialog.Builder(AdminDashboardActivity.this)
                        .setTitle("Детальная статистика")
                        .setMessage(stats)
                        .setPositiveButton("Закрыть", null)
                        .show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Ошибка: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            loadStatistics();
            loadTopStudents();
            Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Выход")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> {
                    authManager.logout();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем данные при возврате на экран
        loadStatistics();
        loadTopStudents();
    }
}