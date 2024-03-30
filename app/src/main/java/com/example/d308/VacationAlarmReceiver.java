package com.example.d308;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VacationAlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TYPE = "type";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(EXTRA_TITLE);
        String type = intent.getStringExtra(EXTRA_TYPE);

        // Get the current date
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String message = "Vacation " + type + ": " + title + " on " + date;

        Intent localIntent = new Intent("VacationAlarm");
        localIntent.putExtra(EXTRA_TITLE, title);
        localIntent.putExtra(EXTRA_TYPE, type);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }
}
