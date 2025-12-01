package com.example.econavigator.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.econavigator.R;
import com.example.econavigator.models.TrashItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingGameActivity extends AppCompatActivity {

    private TextView tvScore, tvLevel;
    private LinearLayout trashContainer;
    private LinearLayout binPlastic, binPaper, binGlass, binMetal, binOrganic;

    private List<TrashItem> trashItems;
    private int score = 0;
    private int level = 1;
    private int correctSorts = 0;
    private int levelStartScore = 0; // очки в начале уровня

    private final int MAX_LEVEL = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_game);

        initViews();
        initTrashItems();
        setupDragAndDrop();
        startLevel();
    }

    private void initViews() {
        tvScore = findViewById(R.id.tv_score);
        tvLevel = findViewById(R.id.tv_level);
        trashContainer = findViewById(R.id.trash_container);
        binPlastic = findViewById(R.id.bin_plastic);
        binPaper = findViewById(R.id.bin_paper);
        binGlass = findViewById(R.id.bin_glass);
        binMetal = findViewById(R.id.bin_metal);
        binOrganic = findViewById(R.id.bin_organic);

        tvScore.setText("Очки: " + score);
        tvLevel.setText("Уровень: " + level);
    }

    private void initTrashItems() {
        trashItems = new ArrayList<>();

        // Пластик
        trashItems.add(new TrashItem("Бутылка", TrashItem.TrashType.PLASTIC, "🧴"));
        trashItems.add(new TrashItem("Пакет", TrashItem.TrashType.PLASTIC, "🛍️"));
        trashItems.add(new TrashItem("Контейнер", TrashItem.TrashType.PLASTIC, "📦"));

        // Бумага
        trashItems.add(new TrashItem("Газета", TrashItem.TrashType.PAPER, "📰"));
        trashItems.add(new TrashItem("Коробка", TrashItem.TrashType.PAPER, "📦"));
        trashItems.add(new TrashItem("Журнал", TrashItem.TrashType.PAPER, "📖"));

        // Стекло
        trashItems.add(new TrashItem("Банка", TrashItem.TrashType.GLASS, "🫙"));
        trashItems.add(new TrashItem("Бутылка", TrashItem.TrashType.GLASS, "🍾"));

        // Металл
        trashItems.add(new TrashItem("Банка", TrashItem.TrashType.METAL, "🥫"));
        trashItems.add(new TrashItem("Крышка", TrashItem.TrashType.METAL, "⚙️"));

        // Органика
        trashItems.add(new TrashItem("Яблоко", TrashItem.TrashType.ORGANIC, "🍎"));
        trashItems.add(new TrashItem("Банан", TrashItem.TrashType.ORGANIC, "🍌"));
        trashItems.add(new TrashItem("Листья", TrashItem.TrashType.ORGANIC, "🍂"));
    }

    private void startLevel() {
        // Запоминаем очки в начале уровня
        levelStartScore = score;
        displayTrashItems();
    }

    private void displayTrashItems() {
        trashContainer.removeAllViews();

        List<TrashItem> levelItems = new ArrayList<>(trashItems);
        Collections.shuffle(levelItems);

        int itemsToShow = Math.min(5 + level, levelItems.size());

        for (int i = 0; i < itemsToShow; i++) {
            TrashItem item = levelItems.get(i);
            TextView trashView = createTrashView(item);
            trashContainer.addView(trashView);
        }
    }

    private TextView createTrashView(TrashItem item) {
        TextView textView = new TextView(this);
        textView.setText(item.getEmoji() + "\n" + item.getName());
        textView.setTextSize(20);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setPadding(24, 24, 24, 24);
        textView.setBackgroundColor(Color.parseColor("#E8F5E9"));
        textView.setTag(item);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        textView.setLayoutParams(params);

        textView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(null, shadowBuilder, v, 0);
                return true;
            }
            return false;
        });

        return textView;
    }

    private void setupDragAndDrop() {
        View.OnDragListener dragListener = (v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(Color.parseColor("#BBDEFB"));
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    resetBinColor(v);
                    return true;

                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    TrashItem item = (TrashItem) draggedView.getTag();
                    TrashItem.TrashType binType = (TrashItem.TrashType) v.getTag();

                    if (item.getType() == binType) {
                        // Правильный ответ
                        score += 10;
                        tvScore.setText("Очки: " + score);
                        trashContainer.removeView(draggedView);
                        Toast.makeText(this, "✅ Правильно!", Toast.LENGTH_SHORT).show();

                        if (trashContainer.getChildCount() == 0) {
                            levelComplete();
                        }
                    } else {
                        // Неправильный ответ
                        score = Math.max(0, score - 5);
                        tvScore.setText("Очки: " + score);
                        Toast.makeText(this, "❌ Неправильный контейнер! " +
                                        item.getName() + " → " + item.getType().getRussianName(),
                                Toast.LENGTH_SHORT).show();

                        // Проверяем, если очки = 0
                        if (score == 0) {
                            new AlertDialog.Builder(this)
                                    .setTitle("Игра окончена")
                                    .setMessage("Ваши очки закончились! Хотите повторить уровень?")
                                    .setPositiveButton("Повторить", (dialog, which) -> {
                                        score = levelStartScore; // восстанавливаем очки на уровне
                                        tvScore.setText("Очки: " + score);
                                        displayTrashItems();
                                    })
                                    .setNegativeButton("Выйти в меню", (dialog, which) -> finish())
                                    .setCancelable(false)
                                    .show();
                        }
                    }

                    resetBinColor(v);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    resetBinColor(v);
                    return true;

                default:
                    return false;
            }
        };

        binPlastic.setOnDragListener(dragListener);
        binPlastic.setTag(TrashItem.TrashType.PLASTIC);

        binPaper.setOnDragListener(dragListener);
        binPaper.setTag(TrashItem.TrashType.PAPER);

        binGlass.setOnDragListener(dragListener);
        binGlass.setTag(TrashItem.TrashType.GLASS);

        binMetal.setOnDragListener(dragListener);
        binMetal.setTag(TrashItem.TrashType.METAL);

        binOrganic.setOnDragListener(dragListener);
        binOrganic.setTag(TrashItem.TrashType.ORGANIC);
    }

    private void resetBinColor(View bin) {
        bin.setBackgroundColor(Color.TRANSPARENT);
    }

    private void levelComplete() {
        if (level < MAX_LEVEL) {
            level++;
            tvLevel.setText("Уровень: " + level);
            startLevel();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("🏆 Поздравляем!")
                    .setMessage("Вы прошли все 8 уровней!")
                    .setPositiveButton("Выйти в меню", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
        }
    }
}
