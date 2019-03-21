package com.example.rafiulislamrafi.locationalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class myReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context c, Intent i) {
        Intent myService1 = new Intent(c, myAlarmService.class);
        c.startService(myService1);
    }
}
