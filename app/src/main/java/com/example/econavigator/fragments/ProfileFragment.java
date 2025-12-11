package com.example.econavigator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.econavigator.R;
import com.example.econavigator.activities.LoginActivity;
import com.example.econavigator.adapters.AchievementAdapter;
import com.example.econavigator.firebase.FirebaseAuthManager;
import com.example.econavigator.models.FirebaseAchievement;
import com.example.econavigator.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName, tvProfileClass, tvProfileLevel, tvProfilePoints;
    private Button btnLogout;
    private RecyclerView rvAchievements;
    private AchievementAdapter adapter;
    private List<FirebaseAchievement> achievementList;
    private SharedPrefsManager prefsManager;
    private FirebaseAuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        loadProfileData();
        loadAchievements();

        return view;
    }

    private void initViews(View view) {
        tvProfileName = view.findViewById(R.id.tv_profile_name);
        tvProfileClass = view.findViewById(R.id.tv_profile_class);
        tvProfileLevel = view.findViewById(R.id.tv_profile_level);
        tvProfilePoints = view.findViewById(R.id.tv_profile_points);
        rvAchievements = view.findViewById(R.id.rv_achievements);

        // Добавляем кнопку выхода (если её нет в layout, можно не использовать)
        // btnLogout = view.findViewById(R.id.btn_logout);

        prefsManager = new SharedPrefsManager(requireContext());
        authManager = new FirebaseAuthManager(requireContext());

        rvAchievements.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        achievementList = new ArrayList<>();
        adapter = new AchievementAdapter(requireContext(), achievementList);
        rvAchievements.setAdapter(adapter);

        // Если есть кнопка выхода
        // btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Обновляем данные при возврате на экран (после игр)
        loadProfileData();
    }

    private void loadProfileData() {
        String name = prefsManager.getStudentName();
        String className = prefsManager.getStudentClass();
        int points = prefsManager.getStudentPoints();
        int level = prefsManager.getStudentLevel();

        tvProfileName.setText(name.isEmpty() ? "Эко-герой" : name);
        tvProfileClass.setText(className.isEmpty() ? "5А" : className);
        tvProfilePoints.setText(points + " баллов");

        // Показываем уровень с эмодзи и названием
        String levelText = getLevelName(level);
        tvProfileLevel.setText(levelText);
    }

    private String getLevelName(int level) {
        switch (level) {
            case 5: return "⭐ Эко-герой (Уровень 5)";
            case 4: return "🌳 Эко-воин (Уровень 4)";
            case 3: return "🍀 Защитник природы (Уровень 3)";
            case 2: return "🌿 Юный эколог (Уровень 2)";
            default: return "🌱 Новичок (Уровень 1)";
        }
    }

    private void loadAchievements() {
        // TODO: Load from Firebase
        // Demo data
        String uid = prefsManager.getFirebaseUid();

        if (uid.isEmpty()) {
            uid = "demo_uid";
        }

        achievementList.add(new FirebaseAchievement("ach1", uid, "Первый шаг",
                "Первая сдача отходов", "ic_bottle"));
        achievementList.add(new FirebaseAchievement("ach2", uid, "Эко-новичок",
                "10 сдач отходов", "ic_paper"));
        achievementList.add(new FirebaseAchievement("ach3", uid, "Эко-профи",
                "50 сдач отходов", "ic_plastic"));
        achievementList.add(new FirebaseAchievement("ach4", uid, "Викторина",
                "Прошёл викторину", "ic_game"));
        achievementList.add(new FirebaseAchievement("ach5", uid, "Сортировка",
                "Прошёл сортировку", "ic_bottle"));
        achievementList.add(new FirebaseAchievement("ach6", uid, "Поиск",
                "Нашёл весь мусор", "ic_glass"));

        adapter.notifyDataSetChanged();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Выход")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> logout())
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void logout() {
        // Выход из Firebase
        authManager.logout();

        // Переход на экран входа
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}