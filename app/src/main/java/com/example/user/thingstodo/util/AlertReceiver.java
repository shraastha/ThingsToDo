package com.example.user.thingstodo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

/**
 * Created by user on 5/5/2019.
 */

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("Notification_title");
        String content = intent.getStringExtra("Notification_message");
        Log.e("TITLE NOTIFiCATION", title);
        Log.e("CONTENT NOTIFiCATION", content);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(title, content);
        notificationHelper.getManager().notify(new Random().nextInt(), nb.build());
    }
}
