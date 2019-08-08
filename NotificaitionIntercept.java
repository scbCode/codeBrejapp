package brejapp.com.brejapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificaitionIntercept
        extends NotificationListenerService {

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        // Implement what you want here
        Log.i("NTF INTERC","NTF INTERCEPT "+sbn);

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        // Implement what you want here
        Log.i("NTF INTERC","NTF INTERCEPT r"+sbn);

    }
}
