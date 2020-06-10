package com.example.convo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class myservice extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new asynch().execute(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
