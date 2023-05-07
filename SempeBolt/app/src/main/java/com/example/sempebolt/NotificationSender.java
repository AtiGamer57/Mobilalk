package com.example.sempebolt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationSender {

    private static final String CHANNEL_ID = "noti"; // Értesítési csatorna azonosítója

    public void sendNotification(Context context, String title, String message) {
        // Értesítési csatorna létrehozása (csak Android 8.0 Oreo vagy újabb verziókhoz szükséges)
        createNotificationChannel(context);

        // Értesítés létrehozása
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.circle) // Kicsi ikon a status sávban
                .setContentTitle(title) // Értesítés címe
                .setContentText(message) // Értesítés szövege
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Értesítés prioritása
                .setAutoCancel(true); // Az értesítés eltűnik, amikor rákattintasz

        // Értesítés megjelenítése
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    // Értesítési csatorna létrehozása
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel"; // Értesítési csatorna neve
            String channelDescription = "My Channel Description"; // Értesítési csatorna leírása
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
