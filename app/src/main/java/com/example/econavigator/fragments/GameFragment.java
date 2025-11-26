package com.example.econavigator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.econavigator.R;

public class GameFragment extends Fragment {

    private Button btnSortingGame, btnQuizGame, btnSearchGame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        initViews(view);
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        btnSortingGame = view.findViewById(R.id.btn_sorting_game);
        btnQuizGame = view.findViewById(R.id.btn_quiz_game);
        btnSearchGame = view.findViewById(R.id.btn_search_game);
    }

    private void setupClickListeners() {
        btnSortingGame.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Игра Сортировка - скоро!", Toast.LENGTH_SHORT).show();
            // TODO: Launch sorting game
        });

        btnQuizGame.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Эко-викторина - скоро!", Toast.LENGTH_SHORT).show();
            // TODO: Launch quiz game
        });

        btnSearchGame.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Найди мусор - скоро!", Toast.LENGTH_SHORT).show();
            // TODO: Launch search game
        });
    }
}