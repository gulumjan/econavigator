package com.example.econavigator.helpers;

import com.example.econavigator.utils.Constants;

/**
 * Калькулятор баллов для различных действий
 */
public class PointsCalculator {

    /**
     * Рассчитать баллы за сдачу бутылки
     */
    public static int calculateBottlePoints(int quantity) {
        return quantity * Constants.POINTS_BOTTLE;
    }

    /**
     * Рассчитать баллы за сдачу макулатуры
     */
    public static int calculatePaperPoints(double weightKg) {
        return (int) (weightKg * Constants.POINTS_PAPER_KG);
    }

    /**
     * Рассчитать баллы за сдачу стекла
     */
    public static int calculateGlassPoints(int quantity) {
        return quantity * Constants.POINTS_GLASS;
    }

    /**
     * Рассчитать общие баллы
     */
    public static int calculateTotalPoints(int bottles, double paperKg, int glass) {
        return calculateBottlePoints(bottles) +
                calculatePaperPoints(paperKg) +
                calculateGlassPoints(glass);
    }

    /**
     * Получить уровень по баллам
     */
    public static int getLevelByPoints(int points) {
        if (points >= Constants.LEVEL_5_THRESHOLD) return 5;
        if (points >= Constants.LEVEL_4_THRESHOLD) return 4;
        if (points >= Constants.LEVEL_3_THRESHOLD) return 3;
        if (points >= Constants.LEVEL_2_THRESHOLD) return 2;
        return 1;
    }

    /**
     * Получить название уровня
     */
    public static String getLevelName(int level) {
        switch (level) {
            case 5: return Constants.LEVEL_5_NAME;
            case 4: return Constants.LEVEL_4_NAME;
            case 3: return Constants.LEVEL_3_NAME;
            case 2: return Constants.LEVEL_2_NAME;
            default: return Constants.LEVEL_1_NAME;
        }
    }

    /**
     * Получить порог для уровня
     */
    public static int getLevelThreshold(int level) {
        switch (level) {
            case 5: return Constants.LEVEL_5_THRESHOLD;
            case 4: return Constants.LEVEL_4_THRESHOLD;
            case 3: return Constants.LEVEL_3_THRESHOLD;
            case 2: return Constants.LEVEL_2_THRESHOLD;
            default: return Constants.LEVEL_1_THRESHOLD;
        }
    }

    /**
     * Рассчитать прогресс до следующего уровня (0-100)
     */
    public static int calculateLevelProgress(int currentPoints, int currentLevel) {
        int currentThreshold = getLevelThreshold(currentLevel);
        int nextThreshold = getLevelThreshold(currentLevel + 1);

        if (currentLevel >= 5) return 100;

        int pointsInLevel = currentPoints - currentThreshold;
        int pointsNeeded = nextThreshold - currentThreshold;

        return (int) (((float) pointsInLevel / pointsNeeded) * 100);
    }

    /**
     * Рассчитать баллы за игру "Сортировка"
     */
    public static int calculateSortingGamePoints(int level, int correctSorts) {
        return level * Constants.SORTING_GAME_POINTS_PER_LEVEL +
                (correctSorts * 2);
    }

    /**
     * Рассчитать баллы за игру "Викторина"
     */
    public static int calculateQuizGamePoints(int correctAnswers) {
        return correctAnswers * Constants.QUIZ_GAME_POINTS_PER_QUESTION;
    }

    /**
     * Рассчитать баллы за игру "Поиск мусора"
     */
    public static int calculateSearchGamePoints(int foundItems, int timeBonus) {
        return (foundItems * Constants.SEARCH_GAME_POINTS_PER_ITEM) + timeBonus;
    }
}