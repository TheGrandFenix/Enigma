package com.fenix.enigma;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fenix.enigma.activities.ChatActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class EnigmaNotification {
    public static void sendNotification(Context context) {
        Intent resultIntent = new Intent(context, ChatActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification.Builder notifBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.enigma)
                        .setContentTitle("New messages")
                        .setContentText("You have new Enigma messages...")
                        .setContentIntent(resultPendingIntent);

        NotificationManager notifMngr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notifMngr.notify(001, notifBuilder.build());
    }
}
