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
import com.example.econavigator.models.FirebaseStudent;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private RecyclerView rvLeaderboard;
    private LeaderboardAdapter adapter;
    private List<FirebaseStudent> studentList;

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
        // Demo data - ИСПРАВЛЕНО: используем правильный конструктор
        studentList.add(new FirebaseStudent("uid1", "student1@example.com", "Айдан", "5А", "student"));
        studentList.get(0).setPoints(850);
        studentList.get(0).setLevel(4);

        studentList.add(new FirebaseStudent("uid2", "student2@example.com", "Нурай", "5А", "student"));
        studentList.get(1).setPoints(720);
        studentList.get(1).setLevel(4);

        studentList.add(new FirebaseStudent("uid3", "student3@example.com", "Бекзат", "5Б", "student"));
        studentList.get(2).setPoints(650);
        studentList.get(2).setLevel(4);

        studentList.add(new FirebaseStudent("uid4", "student4@example.com", "Айгуль", "5А", "student"));
        studentList.get(3).setPoints(580);
        studentList.get(3).setLevel(3);

        studentList.add(new FirebaseStudent("uid5", "student5@example.com", "Эрлан", "5В", "student"));
        studentList.get(4).setPoints(490);
        studentList.get(4).setLevel(3);

        adapter.notifyDataSetChanged();
    }
}