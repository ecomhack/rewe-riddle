package com.ecomhack.riddle.estimote;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class EstimoteService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("riddle", "Starting estimote service.");
        try {
            EstimoteManager.Create((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE), this, intent);
        } catch (Exception e) {
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("riddle", "Stopping estimote service.");
        super.onDestroy();
        EstimoteManager.stop();
    }
}