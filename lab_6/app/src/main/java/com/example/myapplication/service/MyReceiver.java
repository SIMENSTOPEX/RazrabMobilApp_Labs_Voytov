package com.example.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent mIntent = new Intent(context, Service.class);
            Service.enqueueWork(context, mIntent);
        }
    }
}