package com.example.econavigator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.econavigator.R;
import com.example.econavigator.activities.SortingGameActivity;
import com.example.econavigator.activities.QuizGameActivity;
import com.example.econavigator.activities.SearchGameActivity;

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
            Intent intent = new Intent(requireContext(), SortingGameActivity.class);
            startActivity(intent);
        });

        btnQuizGame.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuizGameActivity.class);
            startActivity(intent);
        });

        btnSearchGame.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchGameActivity.class);
            startActivity(intent);
        });
    }
}