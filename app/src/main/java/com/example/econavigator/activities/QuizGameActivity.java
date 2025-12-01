package com.example.econavigator.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.econavigator.R;
import com.example.econavigator.models.QuizQuestion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizGameActivity extends AppCompatActivity {

    private TextView tvQuestion, tvScore, tvQuestionNumber;
    private LinearLayout optionsContainer;
    private ProgressBar progressBar;
    private Button btnNext;

    private List<QuizQuestion> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int selectedAnswerIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);

        initViews();
        initQuestions();
        displayQuestion();
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tv_question);
        tvScore = findViewById(R.id.tv_score);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        optionsContainer = findViewById(R.id.options_container);
        progressBar = findViewById(R.id.progress_bar);
        btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> nextQuestion());
    }

    private void initQuestions() {
        questions = new ArrayList<>();

        questions.add(new QuizQuestion(
                "Сколько лет разлагается пластиковая бутылка?",
                Arrays.asList("1 год", "10 лет", "100 лет", "450 лет"),
                3,
                "Пластиковая бутылка разлагается около 450 лет!"
        ));

        questions.add(new QuizQuestion(
                "Какой процент мусора можно переработать?",
                Arrays.asList("10%", "30%", "50%", "75%"),
                3,
                "До 75% отходов можно переработать при правильной сортировке!"
        ));

        questions.add(new QuizQuestion(
                "Сколько энергии экономит переработка одной алюминиевой банки?",
                Arrays.asList("Хватит на 1 час работы лампы", "Хватит на 3 часа работы ТВ", "Хватит на 2 часа зарядки телефона", "Не экономит энергию"),
                1,
                "Переработка одной банки экономит энергию на 3 часа работы телевизора!"
        ));

        questions.add(new QuizQuestion(
                "Что НЕ относится к опасным отходам?",
                Arrays.asList("Батарейки", "Градусники", "Пищевые отходы", "Лампы"),
                2,
                "Пищевые отходы - это органика, не опасные отходы!"
        ));

        questions.add(new QuizQuestion(
                "Сколько раз можно переработать стекло?",
                Arrays.asList("1 раз", "5 раз", "10 раз", "Бесконечно"),
                3,
                "Стекло можно перерабатывать бесконечное количество раз без потери качества!"
        ));
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showResults();
            return;
        }

        QuizQuestion question = questions.get(currentQuestionIndex);

        tvQuestion.setText(question.getQuestion());
        tvQuestionNumber.setText(String.format("Вопрос %d/%d", currentQuestionIndex + 1, questions.size()));
        tvScore.setText(String.format("Очки: %d", score));

        int progress = (int) (((float) currentQuestionIndex / questions.size()) * 100);
        ObjectAnimator.ofInt(progressBar, "progress", progress).setDuration(300).start();

        optionsContainer.removeAllViews();
        selectedAnswerIndex = -1;
        btnNext.setEnabled(false);

        for (int i = 0; i < question.getOptions().size(); i++) {
            final int index = i;
            Button optionButton = new Button(this);
            optionButton.setText(question.getOptions().get(i));
            optionButton.setTextSize(16);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            optionButton.setLayoutParams(params);

            optionButton.setOnClickListener(v -> selectAnswer(index, optionButton));

            optionsContainer.addView(optionButton);
        }
    }

    private void selectAnswer(int index, Button selectedButton) {
        if (selectedAnswerIndex != -1) return;

        selectedAnswerIndex = index;
        QuizQuestion question = questions.get(currentQuestionIndex);

        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            Button btn = (Button) optionsContainer.getChildAt(i);
            btn.setEnabled(false);

            if (i == question.getCorrectAnswerIndex()) {
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            } else if (i == index) {
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }

        if (question.isCorrect(index)) {
            score += 10;
            tvScore.setText(String.format("Очки: %d", score));
            Toast.makeText(this, "✅ Правильно!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ " + question.getExplanation(), Toast.LENGTH_LONG).show();
        }

        btnNext.setEnabled(true);
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        displayQuestion();
    }

    private void showResults() {
        int maxScore = questions.size() * 10;
        int percentage = (score * 100) / maxScore;

        String message;
        if (percentage >= 80) {
            message = "🌟 Отлично! Ты эко-эксперт!";
        } else if (percentage >= 60) {
            message = "👍 Хорошо! Продолжай учиться!";
        } else if (percentage >= 40) {
            message = "📚 Неплохо, но есть куда расти!";
        } else {
            message = "💪 Попробуй ещё раз!";
        }

        new AlertDialog.Builder(this)
                .setTitle("Результаты викторины")
                .setMessage(String.format("%s\n\nТвой результат: %d/%d (%d%%)",
                        message, score, maxScore, percentage))
                .setPositiveButton("Играть снова", (dialog, which) -> {
                    currentQuestionIndex = 0;
                    score = 0;
                    displayQuestion();
                })
                .setNegativeButton("Выход", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}