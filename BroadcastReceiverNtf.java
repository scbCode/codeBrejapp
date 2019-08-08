package brejapp.com.brejapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import brejapp.com.brejapp.ui.camera.CameraSource;

public class BroadcastReceiverNtf extends BroadcastReceiver {
    public static final String ALARM_ALERT_ACTION ="com.android.alarmclock.ALARM_ALERT";
    public static final String ALARM_INTENT_EXTRA = "intent.extra.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        String act = "brejapp.com.brejapp";


        if (intent.getAction().equals("0")){
            Log.i("service","extraclick ");

            Intent servicechat = new Intent(context, Chat_service.class);
            servicechat.putExtra("offloja",true);
            context.startService(servicechat);

//            Chat_service c = new Chat_service();
//            c.sendOffline();
        }else
            {
                Log.i("service","extraremoveservice ");
                Intent servicechat = new Intent(context, Chat_service.class);
                context.stopService(servicechat);
            }


    }
}