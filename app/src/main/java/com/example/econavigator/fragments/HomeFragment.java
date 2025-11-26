package com.example.econavigator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.econavigator.R;
import com.example.econavigator.utils.SharedPrefsManager;
import com.example.econavigator.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvStudentName, tvPoints, tvLevel;
    private ProgressBar progressLevel;
    private RecyclerView rvMissions;
    private SharedPrefsManager prefsManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        loadStudentData();

        return view;
    }

    private void initViews(View view) {
        tvStudentName = view.findViewById(R.id.tv_student_name);
        tvPoints = view.findViewById(R.id.tv_points);
        tvLevel = view.findViewById(R.id.tv_level);
        progressLevel = view.findViewById(R.id.progress_level);
        rvMissions = view.findViewById(R.id.rv_missions);

        prefsManager = new SharedPrefsManager(requireContext());

        rvMissions.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void loadStudentData() {
        String name = prefsManager.getStudentName();
        int points = prefsManager.getStudentPoints();
        int level = prefsManager.getStudentLevel();

        if (name.isEmpty()) {
            name = "Эко-герой";
            prefsManager.saveStudentData(1, name, "5А", 0, 1);
        }

        tvStudentName.setText("Привет, " + name + "!");
        tvPoints.setText(String.valueOf(points));
        tvLevel.setText(getLevelName(level));

        // Calculate progress to next level
        int currentLevelThreshold = getLevelThreshold(level);
        int nextLevelThreshold = getLevelThreshold(level + 1);
        int progress = (int) (((float) (points - currentLevelThreshold) /
                (nextLevelThreshold - currentLevelThreshold)) * 100);
        progressLevel.setProgress(Math.max(0, Math.min(100, progress)));
    }

    private String getLevelName(int level) {
        switch (level) {
            case 5: return Constants.LEVEL_5_NAME;
            case 4: return Constants.LEVEL_4_NAME;
            case 3: return Constants.LEVEL_3_NAME;
            case 2: return Constants.LEVEL_2_NAME;
            default: return Constants.LEVEL_1_NAME;
        }
    }

    private int getLevelThreshold(int level) {
        switch (level) {
            case 1: return Constants.LEVEL_1_THRESHOLD;
            case 2: return Constants.LEVEL_2_THRESHOLD;
            case 3: return Constants.LEVEL_3_THRESHOLD;
            case 4: return Constants.LEVEL_4_THRESHOLD;
            case 5: return Constants.LEVEL_5_THRESHOLD;
            default: return 10000;
        }
    }
}