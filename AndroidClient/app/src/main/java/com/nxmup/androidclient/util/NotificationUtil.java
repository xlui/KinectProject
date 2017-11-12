package com.nxmup.androidclient.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.activity.DangerHandStateInfoActivity;

public class NotificationUtil {
    private static int notificationId = 1;

    public static void createDangerNotification(Context context, String title, String message) {
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, DangerHandStateInfoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentText(message).build();
        notifyManager.notify(notificationId++, notification);
    }
}
