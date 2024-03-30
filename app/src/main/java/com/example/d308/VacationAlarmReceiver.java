package com.example.d308;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class VacationAlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TYPE = "type";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(EXTRA_TITLE);
        String type = intent.getStringExtra(EXTRA_TYPE);

        Intent localIntent = new Intent("VacationAlarm");
        localIntent.putExtra("message", type + ": " + title);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }
}
