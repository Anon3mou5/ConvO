package com.example.convo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class breceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        waakelocker.acquire(context);
        // do something
        context.startService(new Intent(context,myservice.class));
      //  waakelocker.release();
    }
}

