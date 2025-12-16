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
import com.example.econavigator.firebase.FirebaseDataManager;
import com.example.econavigator.models.GameResult;
import com.example.econavigator.utils.SharedPrefsManager;
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

    // Firebase
    private FirebaseDataManager dataManager;
    private SharedPrefsManager prefsManager;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);

        // Initialize Firebase
        dataManager = new FirebaseDataManager();
        prefsManager = new SharedPrefsManager(this);
        currentUid = prefsManager.getFirebaseUid();

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
                Arrays.asList(
                        "Хватит на 1 час работы лампы",
                        "Хватит на 3 часа работы ТВ",
                        "Хватит на 2 часа зарядки телефона",
                        "Не экономит энергию"
                ),
                1,
                "Переработка одной банки экономит энергию на 3 часа работы телевизора!"
        ));

        questions.add(new QuizQuestion(
                "Что НЕ относится к опасным отходам?",
                Arrays.asList("Батарейки", "Градусники", "Пищевые отходы", "Лампы"),
                2,
                "Пищевые отходы — это органика, не опасные отходы!"
        ));

        questions.add(new QuizQuestion(
                "Сколько раз можно переработать стекло?",
                Arrays.asList("1 раз", "5 раз", "10 раз", "Бесконечно"),
                3,
                "Стекло можно перерабатывать бесконечное количество раз без потери качества!"
        ));

        questions.add(new QuizQuestion(
                "Какой вид мусора разлагается быстрее всего?",
                Arrays.asList("Пластик", "Стекло", "Бумага", "Металл"),
                2,
                "Бумага разлагается быстрее остальных — за несколько месяцев."
        ));

        questions.add(new QuizQuestion(
                "Какой цвет контейнера обычно используется для пластика?",
                Arrays.asList("Зелёный", "Синий", "Жёлтый", "Чёрный"),
                2,
                "Жёлтые контейнеры чаще всего предназначены для пластика."
        ));

        questions.add(new QuizQuestion(
                "Что можно выбрасывать в контейнер для бумаги?",
                Arrays.asList(
                        "Грязные салфетки",
                        "Картон и газеты",
                        "Ламинированную бумагу",
                        "Обои"
                ),
                1,
                "Чистый картон и газеты подходят для переработки."
        ));

        questions.add(new QuizQuestion(
                "Какой мусор запрещено выбрасывать в обычный контейнер?",
                Arrays.asList("Пищевые отходы", "Пластиковые бутылки", "Батарейки", "Бумагу"),
                2,
                "Батарейки относятся к опасным отходам и требуют специальной утилизации."
        ));

        questions.add(new QuizQuestion(
                "Что нужно сделать с пластиковой бутылкой перед переработкой?",
                Arrays.asList(
                        "Оставить с крышкой",
                        "Раздавить и снять крышку",
                        "Помыть и наполнить водой",
                        "Ничего делать не нужно"
                ),
                1,
                "Бутылку лучше раздавить и снять крышку для эффективной переработки."
        ));

        questions.add(new QuizQuestion(
                "Какой материал разлагается дольше всего?",
                Arrays.asList("Бумага", "Органика", "Стекло", "Дерево"),
                2,
                "Стекло может разлагаться тысячи лет."
        ));

        questions.add(new QuizQuestion(
                "Почему важно сортировать мусор?",
                Arrays.asList(
                        "Для красоты",
                        "Чтобы уменьшить количество отходов",
                        "Для увеличения свалок",
                        "Это не имеет значения"
                ),
                1,
                "Сортировка помогает сократить объём отходов и сохранить ресурсы."
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

        // Save to Firebase
        saveGameResultToFirebase();

        new AlertDialog.Builder(this)
                .setTitle("Результаты викторины")
                .setMessage(String.format("%s\n\nТвой результат: %d/%d (%d%%)\n\nБаллы добавлены в профиль!",
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

    private void saveGameResultToFirebase() {
        if (currentUid == null || currentUid.isEmpty()) {
            Toast.makeText(this, "Ошибка: пользователь не авторизован", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create game result
        GameResult gameResult = new GameResult(
                currentUid,
                "quiz",
                score,
                score,
                0
        );

        // Save game result
        dataManager.saveGameResult(gameResult, new FirebaseDataManager.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                // Update student points
                dataManager.updateStudentPoints(currentUid, score, new FirebaseDataManager.DataCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        // Update local SharedPreferences
                        int currentPoints = prefsManager.getStudentPoints();
                        prefsManager.updatePoints(currentPoints + score);

                        Toast.makeText(QuizGameActivity.this,
                                "✅ Баллы сохранены!",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(QuizGameActivity.this,
                                "Ошибка сохранения баллов: " + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(QuizGameActivity.this,
                        "Ошибка сохранения результата: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}