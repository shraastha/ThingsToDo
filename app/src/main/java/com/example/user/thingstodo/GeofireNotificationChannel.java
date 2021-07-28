package com.example.user.thingstodo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.provider.Settings;


/**
 * Created by user on 5/27/2019.
 */

public class GeofireNotificationChannel extends Application {
    public static final String CHANNEL_1_ID = "Channel1";
    public static final String CHANNEL_Daily_ID = "dailyChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel Channel1 = new NotificationChannel(CHANNEL_1_ID,
                    "GeoFire Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            Channel1.setDescription("This notification channel notifies the user when he/she enters/exits the dangerous area.");
            Channel1.setVibrationPattern(new long[] {1000});
            Channel1.enableVibration(true);
            Channel1.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(Channel1);
        }
    }

}
