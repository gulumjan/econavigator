package com.example.econavigator.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.econavigator.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SearchGameActivity extends AppCompatActivity {

    private TextView tvScore, tvTimer, tvFound;
    private GridLayout gameGrid;
    private Button btnReset;

    private int score = 0;
    private int foundItems = 0;
    private int totalTrashItems = 10;
    private CountDownTimer timer;
    private long timeLeft = 60000; // 60 seconds

    private List<Integer> trashPositions;
    private String[] emojis = {"🌳", "🌲", "🌿", "🍃", "🌾", "🌱", "🌵", "🌴", "🌺", "🌸"};
    private String[] trashEmojis = {"🗑️", "🧃", "🥤", "🍔", "🍟", "🧴", "📦", "🛍️", "🥫", "🍾"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_game);

        initViews();
        setupGame();
        startTimer();
    }

    private void initViews() {
        tvScore = findViewById(R.id.tv_score);
        tvTimer = findViewById(R.id.tv_timer);
        tvFound = findViewById(R.id.tv_found);
        gameGrid = findViewById(R.id.game_grid);
        btnReset = findViewById(R.id.btn_reset);

        btnReset.setOnClickListener(v -> resetGame());
    }

    private void setupGame() {
        gameGrid.removeAllViews();
        foundItems = 0;

        tvFound.setText(String.format("Найдено: %d/%d", foundItems, totalTrashItems));
        tvScore.setText("Очки: " + score);

        // Generate random positions for trash
        trashPositions = new ArrayList<>();
        Random random = new Random();

        while (trashPositions.size() < totalTrashItems) {
            int pos = random.nextInt(48); // 6x8 grid = 48 cells
            if (!trashPositions.contains(pos)) {
                trashPositions.add(pos);
            }
        }

        // Create grid
        for (int i = 0; i < 48; i++) {
            Button cell = new Button(this);

            if (trashPositions.contains(i)) {
                cell.setText(trashEmojis[random.nextInt(trashEmojis.length)]);
                cell.setTag("trash");
            } else {
                cell.setText(emojis[random.nextInt(emojis.length)]);
                cell.setTag("clean");
            }

            cell.setTextSize(24);
            cell.setPadding(4, 4, 4, 4);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(2, 2, 2, 2);
            cell.setLayoutParams(params);

            final int position = i;
            cell.setOnClickListener(v -> onCellClick(cell, position));

            gameGrid.addView(cell);
        }
    }

    private void onCellClick(Button cell, int position) {
        if (cell.isEnabled() == false) return;

        if ("trash".equals(cell.getTag())) {
            // Found trash
            cell.setBackgroundColor(Color.parseColor("#4CAF50"));
            cell.setEnabled(false);
            foundItems++;
            score += 10;

            tvFound.setText(String.format("Найдено: %d/%d", foundItems, totalTrashItems));
            tvScore.setText("Очки: " + score);

            Toast.makeText(this, "✅ Нашёл мусор! +10", Toast.LENGTH_SHORT).show();

            if (foundItems == totalTrashItems) {
                gameWon();
            }
        } else {
            // Wrong click
            cell.setBackgroundColor(Color.parseColor("#F44336"));
            score = Math.max(0, score - 5);
            tvScore.setText("Очки: " + score);
            Toast.makeText(this, "❌ Это не мусор! -5", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                int seconds = (int) (millisUntilFinished / 1000);
                tvTimer.setText(String.format("⏱️ %d сек", seconds));

                if (seconds <= 10) {
                    tvTimer.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                tvTimer.setText("⏱️ 0 сек");
                gameOver();
            }
        }.start();
    }

    private void gameWon() {
        if (timer != null) timer.cancel();

        int bonus = (int) (timeLeft / 1000) * 2;
        score += bonus;

        new AlertDialog.Builder(this)
                .setTitle("🎉 Победа!")
                .setMessage(String.format("Ты нашёл весь мусор!\n\nОчки: %d\nБонус за время: %d\nИтого: %d",
                        score - bonus, bonus, score))
                .setPositiveButton("Играть снова", (dialog, which) -> resetGame())
                .setNegativeButton("Выход", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void gameOver() {
        for (int i = 0; i < gameGrid.getChildCount(); i++) {
            gameGrid.getChildAt(i).setEnabled(false);
        }

        new AlertDialog.Builder(this)
                .setTitle("⏰ Время вышло!")
                .setMessage(String.format("Найдено: %d/%d\nОчки: %d", foundItems, totalTrashItems, score))
                .setPositiveButton("Попробовать снова", (dialog, which) -> resetGame())
                .setNegativeButton("Выход", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void resetGame() {
        if (timer != null) timer.cancel();

        score = 0;
        foundItems = 0;
        timeLeft = 60000;

        tvTimer.setTextColor(Color.BLACK);

        setupGame();
        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}