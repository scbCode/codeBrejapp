package brejapp.com.brejapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Chat_service extends Service {

    Character lets[]={'a','b','c','d','e','f','g','h','i','j','l','m','n','o','p','q','r','s','t','u','x','v','z','k','w','y'};
    Context c;
    static boolean ctrl=true;
    static boolean ctrlservice=false;
    static boolean thisctrlservice=false;
    static String l;
    static ArrayList<String> idsL = new ArrayList<String>();
    static ArrayList<Integer> idsN = new ArrayList<Integer>();
    static NotificationManager mNotificationManager;
    static boolean cancelntf=false;
    static boolean listenerChat=true;
    static   Intent intt;
    static Thread t;
    static ValueEventListener eventListener;
    static int flagsc;
    static Intent snoozeIntent;
    Empresa myEmpresa=null;
    static boolean myEmpresac=false;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        //TODO do something useful
        intt=intent;

        flagsc=flags;
        snoozeIntent = new Intent(this, BroadcastReceiverNtf.class);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        t = new Thread(){
            @Override
            public void run(){

                while (ctrl){

                    if (ctrlservice==true){

                        ctrl=false;
                        snoozeIntent.putExtra("Removeservice", "true");
                        snoozeIntent.setAction("1");
                        sendBroadcast(snoozeIntent);
                        t.interrupt();

                    }

                    try {

                        Thread.sleep(60000);
                        Log.i("Desctroy","Destroy SERVICE");
                        getuser();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


//                if (ctrl==false){
//                    stopSelf();
//                }

            }
        };
        t.start();

        for (int i = 0; i < 26; i++) {
            idsN.add(i);
        }

        if (intent!=null)
        if( intent.hasExtra("loja"))
            thisctrlservice=true;
        else
            thisctrlservice=false;

        if (intent!=null)
            if( intent.hasExtra("offloja"))
                myEmpresac=true;


        if (intent!=null)
            if( intent.hasExtra("ntfrepeat"))
                thisctrlservice=true;

        getuser();


        return Service.START_STICKY;

    }

    public void getuser(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null)
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersEmpresa")
                .child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                Empresa empresa = dataSnapshot.getValue(Empresa.class);
                Log.i("empresa","Empresa "+empresa.Nome);
                getMychatnewmsg(empresa.Nome);
                l=empresa.Nome;
                if (myEmpresac)
                    sendOffline();
                else
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals("ntfOn")){
                        if (thisctrlservice==false)
                        notificationStatus(empresa.Nome);
                    //    postSnapshot.getRef().removeValue();
                    }
                }
                }else {
                    getMychatnewmsguser();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getMychatnewmsg(String loja){

        String email="";

        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }

       FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                .child(loja).child("newmsgnotification").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

                            Log.i("ID", "ID NTF " +dataSnapshot.getKey());

                            for(DataSnapshot datab : dataSnapshot.getChildren())
                            {

                            }

                            if (dataSnapshot.exists()) {
                                newmsg nm = dataSnapshot.getValue(newmsg.class);

                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Chat_service.this, "2");
                                mBuilder.setSmallIcon(R.drawable.ntficonchat);
                                mBuilder.setContentTitle(nm.email);
                                mBuilder.setContentText(nm.msg);
                                mBuilder.setPriority(Notification.PRIORITY_MAX);

                                String id = "";
                                int idf = 0;
                                if (nm != null)
                                    if (nm.email != null) {
                                        String idemail = nm.email.toString().replace("@", "");
                                        idemail = idemail.replace("com", "");
                                        idemail = idemail.replace("br", "");
                                        idemail = idemail.replace(".", "");


                                        for (int ii = 0; ii < idemail.toString().length(); ii += 2) {
                                            for (int i = 0; i < lets.length; i++) {
                                                if (lets[i].equals(idemail.charAt(ii)))
                                                    idf += idsN.get(i);
                                            }
                                        }

                                        Log.i("ID", "ID NTF " + idemail + " " + idf);

                                        id = String.valueOf(idf);

                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        Intent resultIntent = new Intent(Chat_service.this, MainActivity_chat.class);
                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(Chat_service.this);
                                        stackBuilder.addNextIntent(resultIntent);
                                        resultIntent.putExtra("NTF", nm.email);
                                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                        mBuilder.setContentIntent(resultPendingIntent);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            CharSequence name = "0";
                                            String description = "01";
                                            NotificationChannel channel = new NotificationChannel("2", name, importance);
                                            channel.setSound(null, null);
                                            channel.setDescription(description);
                                            // Register the channel with the system; you can't change the importance
                                            // or other notification behaviors after this
                                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                            notificationManager.createNotificationChannel(channel);
                                        }

                                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        mNotificationManager.notify(idf, mBuilder.build());
                                        cancelntf = true;

                                        //remove request
                                        dataSnapshot.getRef().removeValue();

                                    }
                            }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        cancelntf=false;

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }


    public void getMychatnewmsguser(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }

        final String finalEmail = email;
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("UsersClient")
                .child(user.getUid()).child("chat").child("lojasOn").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                for(DataSnapshot lojasdata: dataSnapshot.getChildren()){

                    final String loja = String.valueOf(lojasdata.getValue());

                    FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                            .child(String.valueOf(lojasdata.getValue())).child(finalEmail).child("msgNtf").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("LOJASCHAT","LOAJS CHAT "+ dataSnapshot.getValue() );

                            if (false==false)
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())

                            {

                                Chatmsg nm = postSnapshot.getValue(Chatmsg.class);
                            if (nm.remetent.equals("LOJA")){
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Chat_service.this, "0");
                                mBuilder.setSmallIcon(R.drawable.ntficonchat);
                                mBuilder.setContentTitle(loja);
                                mBuilder.setContentText(nm.msg);
                                mBuilder.setPriority(Notification.PRIORITY_MAX);

                                String id = "";
                                int idf = 0;

                                        id = String.valueOf(idf);
                                        Intent resultIntent = new Intent(Chat_service.this, MainActivity_chat.class);
                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(Chat_service.this);
                                        stackBuilder.addNextIntent(resultIntent);
                                        resultIntent.putExtra("NTF", loja);
                                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                        mBuilder.setContentIntent(resultPendingIntent);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            int importance = NotificationManager.IMPORTANCE_HIGH;

                                            CharSequence name = "0";
                                            String description = "01";
                                            NotificationChannel channel = new NotificationChannel(id, name, importance);
                                            channel.setDescription(description);
                                            // Register the channel with the system; you can't change the importance
                                            // or other notification behaviors after this
                                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                            notificationManager.createNotificationChannel(channel);
                                        }


                                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        mNotificationManager.notify(idf, mBuilder.build());

                                      dataSnapshot.getRef().removeValue();

                                    }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    public   void sendOffline(){

        Context ctx = getBaseContext();

        if (mNotificationManager!=null)
        mNotificationManager.cancel(333);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user!=null)
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("DeliveryOn").child(l).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/")
                        .child("PerfilLojas").orderByChild("Nome").equalTo(l).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            postSnapshot.getRef().child("status").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    ctrlservice=true;
                                    FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("UsersEmpresa")
                                            .child(user.getUid()).child("ntfOn").removeValue();

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void notificationStatus(String loja){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("myEmpresa", "myEmpresa "+l);

        l=loja;

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Chat_service.this, "1");
        mBuilder.setSmallIcon(R.drawable.ntficonchat);
        mBuilder.setContentTitle("Loja online");
        mBuilder.setContentText("Você está online");
        mBuilder .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{0L});
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setOngoing(true);

        Intent snoozeIntent = new Intent(this, BroadcastReceiverNtf.class);
        snoozeIntent.setAction("0");
        snoozeIntent.putExtra("Clickntfoff", "true");
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        mBuilder.addAction(android.R.drawable.ic_lock_power_off,"Ficar off",
                snoozePendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "0";
            String description ="01";
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setSound(null,null);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(333, mBuilder.build());
        thisctrlservice=true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service","onBind service");

        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i("onTaskRemoved", "onTaskRemoved ");

        if (cancelntf==false){
        Intent broadcastIntent = new Intent(this, Broadcastreceiverservice.class);
        sendBroadcast(broadcastIntent);}

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");


    }

}
