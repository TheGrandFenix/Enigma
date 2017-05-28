package com.fenix.enigma;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.fenix.enigma.activities.ChatActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class EnigmaNotification {
    //Create a new notification
    public static void sendNotification(Context context, ChatActivity activity) {

        Notification.Builder notifBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.enigma)
                        .setContentTitle("New messages")
                        .setContentText("You have new Enigma messages...")
                        .setAutoCancel(true)
                        .setVibrate(new long[] {200})
                        .setLights(ContextCompat.getColor(context, R.color.enigma_purple), 1000, 500);

        NotificationManager notifMngr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notifMngr.notify(1, notifBuilder.build());
    }

    //Close an existing notification
    public static void closeNotification(Context context, int id){
        NotificationManager notifMngr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notifMngr.cancel(id);
    }
}
