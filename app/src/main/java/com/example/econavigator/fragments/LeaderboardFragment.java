package com.example.econavigator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.econavigator.R;
import com.example.econavigator.adapters.LeaderboardAdapter;
import com.example.econavigator.models.Student;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private RecyclerView rvLeaderboard;
    private LeaderboardAdapter adapter;
    private List<Student> studentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        initViews(view);
        loadLeaderboardData();

        return view;
    }

    private void initViews(View view) {
        rvLeaderboard = view.findViewById(R.id.rv_leaderboard);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(requireContext()));

        studentList = new ArrayList<>();
        adapter = new LeaderboardAdapter(requireContext(), studentList);
        rvLeaderboard.setAdapter(adapter);
    }

    private void loadLeaderboardData() {
        // TODO: Load from database
        // Demo data
        studentList.add(new Student(1, "Айдан", "5А", 850, 4));
        studentList.add(new Student(2, "Нурай", "5А", 720, 4));
        studentList.add(new Student(3, "Бекзат", "5Б", 650, 4));
        studentList.add(new Student(4, "Айгуль", "5А", 580, 3));
        studentList.add(new Student(5, "Эрлан", "5В", 490, 3));

        adapter.notifyDataSetChanged();
    }
}