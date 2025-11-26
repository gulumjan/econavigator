package com.example.econavigator.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.econavigator.activities.MainActivity;
import com.example.econavigator.R;

/**
 * Helper для создания уведомлений
 */
public class NotificationHelper {

    private static final String CHANNEL_ID = "eco_navigator_channel";
    private static final String CHANNEL_NAME = "Эко-Навигатор";
    private static final int NOTIFICATION_ID = 1001;

    private Context context;
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();
    }

    /**
     * Создать канал уведомлений (для Android 8.0+)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Уведомления об эко-достижениях");
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Показать уведомление о повышении уровня
     */
    public void showLevelUpNotification(int newLevel) {
        String title = "🎉 Поздравляем!";
        String message = "Ты достиг " + PointsCalculator.getLevelName(newLevel) + "!";

        showNotification(title, message);
    }

    /**
     * Показать уведомление о разблокировке достижения
     */
    public void showAchievementUnlockedNotification(String achievementName) {
        String title = "🏅 Новое достижение!";
        String message = "Получено: " + achievementName;

        showNotification(title, message);
    }

    /**
     * Показать уведомление о выполнении миссии
     */
    public void showMissionCompletedNotification(String missionName, int reward) {
        String title = "✅ Миссия выполнена!";
        String message = missionName + " (+" + reward + " баллов)";

        showNotification(title, message);
    }

    /**
     * Показать уведомление о начислении баллов
     */
    public void showPointsEarnedNotification(int points) {
        String title = "💰 Баллы получены!";
        String message = "+" + points + " баллов за сдачу отходов";

        showNotification(title, message);
    }

    /**
     * Показать произвольное уведомление
     */
    private void showNotification(String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bottle)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}