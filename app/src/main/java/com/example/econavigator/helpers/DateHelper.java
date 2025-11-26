package com.example.econavigator.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Helper для работы с датами
 */
public class DateHelper {

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd.MM.yyyy", new Locale("ru"));

    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", new Locale("ru"));

    private static final SimpleDateFormat DATETIME_FORMAT =
            new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale("ru"));

    /**
     * Форматировать дату
     */
    public static String formatDate(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }

    /**
     * Форматировать время
     */
    public static String formatTime(long timestamp) {
        return TIME_FORMAT.format(new Date(timestamp));
    }

    /**
     * Форматировать дату и время
     */
    public static String formatDateTime(long timestamp) {
        return DATETIME_FORMAT.format(new Date(timestamp));
    }

    /**
     * Получить текущую дату
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Проверить, сегодня ли была дата
     */
    public static boolean isToday(long timestamp) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String today = dayFormat.format(new Date());
        String checkDate = dayFormat.format(new Date(timestamp));
        return today.equals(checkDate);
    }

    /**
     * Получить количество дней назад
     */
    public static int getDaysAgo(long timestamp) {
        long diff = System.currentTimeMillis() - timestamp;
        return (int) (diff / (24 * 60 * 60 * 1000));
    }
}