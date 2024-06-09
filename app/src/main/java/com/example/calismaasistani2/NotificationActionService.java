package com.example.calismaasistani2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationActionService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "PLAY":
                    // Implement play action
                    break;
                case "PAUSE":
                    // Implement pause action
                    break;
                case "STOP":
                    // Implement stop action
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
