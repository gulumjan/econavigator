package com.example.econavigator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.econavigator.R;
import com.example.econavigator.adapters.AchievementAdapter;
import com.example.econavigator.models.Achievement;
import com.example.econavigator.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName, tvProfileClass, tvProfileLevel, tvProfilePoints;
    private RecyclerView rvAchievements;
    private AchievementAdapter adapter;
    private List<Achievement> achievementList;
    private SharedPrefsManager prefsManager;

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

        prefsManager = new SharedPrefsManager(requireContext());

        rvAchievements.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        achievementList = new ArrayList<>();
        adapter = new AchievementAdapter(requireContext(), achievementList);
        rvAchievements.setAdapter(adapter);
    }

    private void loadProfileData() {
        String name = prefsManager.getStudentName();
        String className = prefsManager.getStudentClass();
        int points = prefsManager.getStudentPoints();
        int level = prefsManager.getStudentLevel();

        tvProfileName.setText(name.isEmpty() ? "Эко-герой" : name);
        tvProfileClass.setText(className.isEmpty() ? "5А" : className);
        tvProfilePoints.setText(points + " баллов");
        tvProfileLevel.setText("Уровень " + level);
    }

    private void loadAchievements() {
        // TODO: Load from database
        // Demo data
        achievementList.add(new Achievement(1, "Первый шаг", "Первая сдача отходов", R.drawable.ic_bottle));
        achievementList.add(new Achievement(2, "Эко-новичок", "10 сдач отходов", R.drawable.ic_paper));
        achievementList.add(new Achievement(3, "Эко-профи", "50 сдач отходов", R.drawable.ic_plastic));

        adapter.notifyDataSetChanged();
    }
}