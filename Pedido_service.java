package brejapp.com.brejapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Pedido_service extends Service {

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
    static boolean cancelntfdelivery=false;
    static boolean listenerChat=true;
    static boolean listenerChatNft=false;
    static   Intent intt;
    static Thread t;
    static ValueEventListener eventListener;
    static int flagsc;
    static Intent snoozeIntent;
    Empresa myEmpresa=null;
    static boolean myEmpresac=false;
    static String lojanome;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        //TODO do something useful



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
                        Thread.sleep(10000);
                        Log.i("Pedidos","Pedidos start ");
                        if(myEmpresac==true)
                        listenerdelivery(lojanome);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                if (ctrl==false){
                    stopSelf();
                }

            }
        };
        getdelivery();

        if (intent!=null) {
            if (intent.hasExtra("loja")) {
              String  loja = intent.getStringExtra("loja");
                listenerdelivery(loja);
                Log.i("PEDIDOS","PEDIDOS ");

            }
            else
                getpedidos();
        }


        t.start();

        return Service.START_STICKY;

    }

    public void getpedidos(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com")
                .child("UsersClient").child(user.getUid()).child("Pedidos").orderByChild("status")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    PedidoReceiver pedido = postSnapshot.getValue(PedidoReceiver.class);

                    l = pedido.loja+"";
                    meusPedidosMonitorNtf(l,pedido.timestamp+"",pedido.status);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getdelivery(){

        try {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null)
                FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com")
                        .child("UsersEmpresa").child(user.getUid()).child("Nome")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    myEmpresac = true;
                                    lojanome = dataSnapshot.getValue().toString();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
        }catch (Exception e) {

        }

    }

    public void notificationStatus(String loja){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("myEmpresa", "myEmpresa "+l);

        l=loja;

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Pedido_service.this, "0");
        mBuilder.setSmallIcon(R.drawable.beerappicon);
        mBuilder.setContentTitle("Loja online");
        mBuilder.setContentText("Você está online");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setOngoing(true);

        Intent snoozeIntent = new Intent(this, BroadcastReceiverNtf.class);
        snoozeIntent.setAction("0");
        snoozeIntent.putExtra("Clickntfoff", "true");
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        mBuilder.addAction(R.drawable.beer,"Ficar off",
                snoozePendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "0";
            String description ="01";
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(333, mBuilder.build());

    }


    public void listenerdelivery(String loja){

        try {
            FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/")
                    .child("LojasPedidos").child(loja).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot pedidosdata : dataSnapshot.getChildren()) {
                            for (DataSnapshot emaildata : pedidosdata.child("Pedidos").getChildren()) {

                                PedidoReceiver pedd = emaildata.getValue(PedidoReceiver.class);

                                if (pedd.status.equals("solicitando") ) {
                                    if (myEmpresac==false)
                                       listenerChatNft = true;
                                    Log.i("PEDIDOS", "PEDIDOS "+pedd.status);

                                }
                            }
                        }

                        if (listenerChatNft==true) {

                            myEmpresac=true;
                            listenerChatNft=false;
                            cancelntfdelivery = true;

                            Log.i("PEDIDOS", "listenerChatNft ");

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Pedido_service.this, "9");
                            mBuilder.setSmallIcon(R.drawable.enviandonav);
                            mBuilder.setContentTitle("Pedidos aguardando");
                            mBuilder.setContentText("Você tem novo pedido!\n");
                            mBuilder.setPriority(Notification.PRIORITY_MAX);

                            int id = 12266;

                            Intent resultIntent = new Intent(Pedido_service.this, MainActivity_delivery.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(Pedido_service.this);
                            stackBuilder.addNextIntent(resultIntent);
//                          resultIntent.putExtra("pedido",loja);
                            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(resultPendingIntent);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                CharSequence name = "06";
                                String description = "0166";
                                NotificationChannel channel = new NotificationChannel("9", name, importance);
                                channel.setDescription(description);
                                // Register the channel with the system; you can't change the importance
                                // or other notification behaviors after this
                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);
                            }
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(id, mBuilder.build());
                        }
                        cancelntfdelivery = false;
                        myEmpresac=false;

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e ){

        }

    }


    public void meusPedidosMonitorNtf(final String loja, final String idpedido, final String status){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        try{
        if (user!=null) {
            String email = user.getEmail();


            for (int i = 0; i < email.length(); i++) {
                email = email.replace(".", "AAA");
                email = email.replace("@", "BBB");
            }
            FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("LojasPedidos").child(loja).child(email)
                    .child("Pedidos").orderByChild("status").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {
                    PedidoReceiver pedido = dataSnapshot.getValue(PedidoReceiver.class);
                    if (pedido.status.equals("solicitando"))
                        sendntf(loja, "Pedido solicitado!");
                    else if (pedido.status.equals("recebido"))
                        sendntf(loja, "Pedido aceito");
                    else if (pedido.status.equals("acaminho"))
                        sendntf(loja, "Pedido à caminho!");
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {
                    PedidoReceiver pedido = dataSnapshot.getValue(PedidoReceiver.class);
                    Log.i("status", "status2 " + pedido.status);
                    if (pedido.status.equals("solicitando"))
                        sendntf(loja, "Pedido solicitado!");
                    else if (pedido.status.equals("recebido"))
                        sendntf(loja, "Pedido aceito");
                    else if (pedido.status.equals("acaminho"))
                        sendntf(loja, "Pedido à caminho!");
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }catch (Exception e){

    }

    }


    public void sendntf(String loja,String status){ ctrlservice=false;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Pedido_service.this,"0");
        mBuilder.setSmallIcon(R.drawable.enviandonav);
        mBuilder.setContentTitle(loja);
        mBuilder.setContentText(status);
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        int id= 122;
        Intent resultIntent = new Intent(Pedido_service.this, MainActivity_pedidos.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(Pedido_service.this);
        stackBuilder.addNextIntent(resultIntent);
        resultIntent.putExtra("pedido",loja);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;

            CharSequence name = "0";
            String description ="01";
            NotificationChannel channel = new NotificationChannel("956", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }



        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());

    }
    public   void sendOffline(String idpedido, final String loja){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String email=user.getEmail();


        for (int i=0;i<email.length();i++){
            email=email.replace("AAA",".");
            email=email.replace("BBB","@");
        }

        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("LojasPedidos").child(loja).child(email)
                .child("Pedidos").child(idpedido).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    }


}
