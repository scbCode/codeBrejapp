package brejapp.com.brejapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

    import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

    /**
     * Created by fabio on 24/01/2016.
     */
    public class Broadcastreceiverservice
       extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

                   Intent intentserv = new Intent(context, Chat_service.class);
                   intentserv.putExtra("ntfrepeat",true);
                   context.startService(intentserv);;

        }

    }
