package brejapp.com.brejapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Date;

import brejapp.com.brejapp.R;



public class firebaseServiceReceive extends FirebaseMessagingService {

        private static final String TAG = "MyFirebaseMsgService";
        public int cont=0;
        static String imgurl;
     Notification customNotification = null;

    /**
         * Called when message is received.
         *
         * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
         */
        // [START receive_message]
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            // [START_EXCLUDE]

            // TODO(developer): Handle FCM messages here.
            Log.i("sendNotificationLink","onMessageReceived " );

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {

                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                    String title = remoteMessage.getData().get("title");
                    String msg = remoteMessage.getData().get("body");
                    String icone = remoteMessage.getData().get("urlimg");
                    String nometopic = remoteMessage.getData().get("nomeTopic");
                    String nome= remoteMessage.getData().get("topico");
                    String bigImg= remoteMessage.getData().get("bigImg");
                    String id= remoteMessage.getData().get("id");
                    String tipo= remoteMessage.getData().get("tipo");
                    String cor= remoteMessage.getData().get("cor");

                    if (tipo != null)
                    if (tipo.equals("a"))
                        sendNotificationLink(title, msg, nome,icone,nometopic, bigImg,id,tipo,cor);
                    else
                    if (tipo.equals("b")) {
                        String preco= remoteMessage.getData().get("preco");
                        String uni= remoteMessage.getData().get("uni");
                        Log.i("Preco","Preco "+preco);
                        sendNtfProdut(title, msg, icone, nome, id, preco, uni, cor);
                    }
                } else {
                    // Handle message within 10 seconds
                    handleNow();
                }

            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
//                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotificationLink(String title, String msg, String topico, final String img64, String nometopic, final String bigimg,String id,String Tipo, String cor) {
        Log.i("teste", "teste ntf 1");

        Date dt = new Date();

        String CHANNEL_ID = "0";
        int idn = (int) dt.getTime();
        CharSequence name = "" + dt.getTime();
        String Description = "This is my channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                FirebaseDatabase.getInstance().getReference().child("Central_Clicks").child("Receiver_Notific")
                        .child(nometopic).child(id).child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(id);


                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(Description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                ImageView img = (ImageView) null;

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                String packageName = getPackageName();

                final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layoutnotify);

                RemoteViews remoteViews_big = new RemoteViews(getPackageName(), R.layout.layoutnotify_big);

                RemoteViews remoteViews_bigtext = new RemoteViews(getPackageName(), R.layout.layoutnotify_bigtext);

                remoteViews.setTextViewText(R.id.txtTitleNotif, title);
                remoteViews.setTextViewText(R.id.txt2Notif, msg);

                remoteViews_big.setTextViewText(R.id.txtTitleNotif, title);
                remoteViews_big.setTextViewText(R.id.txt2Notif, msg);
                remoteViews_big.setTextViewText(R.id.nometopic_big, topico);

                remoteViews_bigtext.setTextViewText(R.id.txtTitleNotif, title);
                remoteViews_bigtext.setTextViewText(R.id.txt2Notif, msg);
                remoteViews_bigtext.setTextViewText(R.id.nometopic_big, topico);

                Intent mainintent = new Intent(this, MainActivity_lojas.class);
                mainintent.putExtra("notify", "true");
                mainintent.putExtra("loja", topico);
                mainintent.putExtra("id", id);
                mainintent.putExtra("title", title);
                mainintent.putExtra("msg", msg);
                mainintent.putExtra("Url", imgurl);
                mainintent.putExtra("tipo", "a");
                mainintent.putExtra("cor", cor);

                PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                        mainintent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.rlntf, contentIntent);
                remoteViews_big.setOnClickPendingIntent(R.id.rlntf, contentIntent);
                remoteViews_bigtext.setOnClickPendingIntent(R.id.rlntf, contentIntent);


                PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

                Notification customNotification = null;
                if (bigimg.length() > 0) {
                    customNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.beer)
                            .setVibrate(new long[]{1000})
                            .setSound(alarmSound)
                            .setContentText(title)
                            .setContentIntent(pendingNotificationIntent)
                            .setAutoCancel(true)
                            .setCustomBigContentView(remoteViews_big)
                            .setCustomContentView(remoteViews)
                            .setPriority(Notification.PRIORITY_MAX)
                            .build();
                } else {

                    if (msg.length() <= 14) {
                        customNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.beer)
                                .setVibrate(new long[]{1000})
                                .setSound(alarmSound).setContentIntent(pendingNotificationIntent)

                                .setContentText(title)
                                .setAutoCancel(true)
                                .setCustomContentView(remoteViews)
                                .setPriority(Notification.PRIORITY_MAX)
                                .build();
                    }
                    if (msg.length() > 14) {
                        customNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.beer)
                                .setVibrate(new long[]{1000})
                                .setSound(alarmSound).setContentIntent(pendingNotificationIntent)

                                .setContentText(title)
                                .setAutoCancel(true)
                                .setCustomContentView(remoteViews)
                                .setCustomBigContentView(remoteViews_bigtext)
                                .setPriority(Notification.PRIORITY_MAX)
                                .build();
                    }
                }


                if (msg.length() > 14) {
                    //img Icon
                    final NotificationTarget notificationTargetbigtext = new NotificationTarget(
                            getApplicationContext(),
                            R.id.img_notify_bigtext,
                            remoteViews_bigtext,
                            customNotification,
                            idn);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Glide
                                    .with(getApplicationContext())
                                    .asBitmap()
                                    .load(img64)
                                    .into(notificationTargetbigtext);

                        }
                    });

                } else {

                    if (msg.length() <= 14) {
                        //img Icon
                        final NotificationTarget notificationTarget = new NotificationTarget(
                                getApplicationContext(),
                                R.id.iconCat,
                                remoteViews,
                                customNotification,
                                idn);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Glide
                                        .with(getApplicationContext())
                                        .asBitmap()
                                        .load(img64)
                                        .into(notificationTarget);

                            }
                        });
                    }
                }


                if (bigimg.length() > 0) {
                    //img Topic big layout
                    final NotificationTarget notificationTarget_topic = new NotificationTarget(
                            getApplicationContext(),
                            R.id.img_notify_big,
                            remoteViews_big,
                            customNotification,
                            idn);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Glide
                                    .with(getApplicationContext())
                                    .asBitmap()
                                    .load(img64)
                                    .into(notificationTarget_topic);

                        }
                    });

                    //big img big layout
                    final NotificationTarget notificationTargetbigimg = new NotificationTarget(
                            getApplicationContext(),
                            R.id.imgBig,
                            remoteViews,
                            customNotification,
                            idn);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Glide
                                    .with(getApplicationContext())
                                    .asBitmap()
                                    .load(bigimg)
                                    .into(notificationTargetbigimg);

                        }
                    });
                }
                notifySimples ntfs = new notifySimples(title, msg, "off", "test", topico, "off", "off", id, Tipo,cor,"","");

                FirebaseDatabase.getInstance().getReference().child("Central_Clicks").child("Receiver_Notific")
                        .child(topico).child(id).setValue(ntfs);
//
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(idn, customNotification);

            } else {

                notifySimples ntfs = new notifySimples(title, msg, "off", "test", topico, "off", "off", id, Tipo,cor,"","");

                FirebaseDatabase.getInstance().getReference().child("Central_Clicks").child("Receiver_Notific")
                        .child(nometopic).child(id).child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(id);


                ImageView img = (ImageView) null;

                final NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                //layout simples
                final RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layoutnotify);


                //layout BIG IMG
                RemoteViews remoteViews_big = new RemoteViews(getPackageName(), R.layout.layoutnotify_big);


                //LAYOUT BIG TEXT
                RemoteViews remoteViews_bigText = new RemoteViews(getPackageName(), R.layout.layoutnotify_bigtext);


                //SOUND
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                //SET LAYOUT VALUES SIMPLES
                notificationLayout.setTextViewText(R.id.txtTitleNotif, title);
                notificationLayout.setTextViewText(R.id.txt2Notif, msg);
                notificationLayout.setTextViewText(R.id.nometopic_simples, topico);

                //SET LAYOUT VALUES BIG IMG
                remoteViews_big.setTextViewText(R.id.txtTitleNotif, title);
                remoteViews_big.setTextViewText(R.id.txt2Notif, msg);
                remoteViews_big.setTextViewText(R.id.nometopic_big, topico);

                //SET LAYOUT VALUES BIG TEXT
                remoteViews_bigText.setTextViewText(R.id.txtTitleNotif, title);
                remoteViews_bigText.setTextViewText(R.id.txt2Notif, msg);
                remoteViews_bigText.setTextViewText(R.id.nometopic_big, topico);

                Intent mainintent = new Intent(this, MainActivity_lojas.class);
                mainintent.putExtra("notify", "true");
                mainintent.putExtra("loja", topico);
                mainintent.putExtra("id", id);
                mainintent.putExtra("title", title);
                mainintent.putExtra("msg", msg);
                mainintent.putExtra("Url", imgurl);
                mainintent.putExtra("tipo", "a");
                mainintent.putExtra("cor", cor);

                PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                        mainintent, PendingIntent.FLAG_UPDATE_CURRENT);

                notificationLayout.setOnClickPendingIntent(R.id.rlntf, contentIntent);
                remoteViews_big.setOnClickPendingIntent(R.id.rlntf, contentIntent);
                remoteViews_bigText.setOnClickPendingIntent(R.id.rlntf, contentIntent);


                Notification customNotification = null;

                //BIG IMG NOTIFICATION
                if (bigimg.length() > 0) {

                    customNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.beer)
                            .setVibrate(new long[]{1000})
                            .setSound(alarmSound)
                            .setContentText(title)
                            .setAutoCancel(true)
                            .setContentIntent(contentIntent)
                            .setCustomBigContentView(remoteViews_big)
                            .setCustomContentView(notificationLayout)
                            .setPriority(Notification.PRIORITY_MAX)
                            .build();

                } else {

                    //SIMPLES NOTIFICATION
                    if (msg.length() <= 14) {

                        customNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.beer)
                                .setVibrate(new long[]{1000})
                                .setSound(alarmSound)
                                .setContentText(title)
                                .setAutoCancel(true)
                                .setContentIntent(contentIntent)
                                .setCustomContentView(notificationLayout)
                                .setPriority(Notification.PRIORITY_MAX)
                                .build();

                    }

                    //BIG TEXT NOTIFICATION
                    if (msg.length() > 14) {

                        customNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.beer)
                                .setVibrate(new long[]{1000})
                                .setSound(alarmSound)
                                .setContentText(title)
                                .setAutoCancel(true)
                                .setContentIntent(contentIntent)
                                .setCustomContentView(notificationLayout)
                                .setCustomBigContentView(remoteViews_bigText)
                                .setPriority(Notification.PRIORITY_MAX)
                                .build();

                    }
                }


                //SET ICON IMG SMALL LAYOUT - DEFAULT -
                final NotificationTarget notificationTargeticon = new NotificationTarget(
                        getApplicationContext(),
                        R.id.img_notify_simples,
                        notificationLayout,
                        customNotification,
                        idn);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Glide
                                .with(getApplicationContext())
                                .asBitmap()
                                .load(img64)
                                .into(notificationTargeticon);

                    }
                });


                //IF BIG IMG - SET IMG GLIDE
                if (bigimg.length() > 0) {

                    //ICON Topic
                    final NotificationTarget notificationTarget_topic = new NotificationTarget(
                            getApplicationContext(),
                            R.id.img_notify_big,
                            remoteViews_big,
                            customNotification,
                            idn);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Glide
                                    .with(getApplicationContext())
                                    .asBitmap()
                                    .load(img64)
                                    .into(notificationTarget_topic);

                        }
                    });

                    //BANNER big img
                    final NotificationTarget notificationTargetbigimg = new NotificationTarget(
                            getApplicationContext(),
                            R.id.imgBig,
                            remoteViews_big,
                            customNotification,
                            idn);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Glide
                                    .with(getApplicationContext())
                                    .asBitmap()
                                    .load(bigimg)
                                    .into(notificationTargetbigimg);

                        }
                    });

                } else {

                    //IF BIG TEXT - SET IMG GLIDE
                    if (msg.length() > 14) {
                        //SET ICON IMG LARG LAYOUT
                        final NotificationTarget notificationTargetbigtext = new NotificationTarget(
                                getApplicationContext(),
                                R.id.img_notify_bigtext,
                                remoteViews_bigText,
                                customNotification,
                                idn);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Glide
                                        .with(getApplicationContext())
                                        .asBitmap()
                                        .load(img64)
                                        .into(notificationTargetbigtext);

                            }
                        });


                    }
                }
//

                mNotificationManager.notify(idn, customNotification);

            }


    }


    public void sendNtfProdut(final String title, String msg, final String idicon, String nometopic, String id, String prec, String uni, String color){

        notifySimples ntfs = new notifySimples(title, msg, "off", "test", nometopic, "off", "off", id, "b",color,prec,uni);

        FirebaseDatabase.getInstance().getReference().child("Central_Clicks").child("Receiver_Notific")
                .child(nometopic).child(id).child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(id);


        ImageView img= (ImageView)null;

        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //layout simples
        final RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layoutnotify_produto);

        //SET LAYOUT VALUES SIMPLES
        notificationLayout.setTextViewText(R.id.txtTitleNotif,title);
        notificationLayout.setTextViewText(R.id.txt2Notif,msg);
        notificationLayout.setTextViewText(R.id.nometopic_simples_prd,  nometopic);
        notificationLayout.setTextViewText(R.id.txtunidadeprd,uni);

        String p  = prec.replace(".",",");
        notificationLayout.setTextViewText(R.id.tdtPrc,p);

        Log.i("preco","preco "+prec);

        int cor = Color.parseColor(color);

        notificationLayout.setTextColor(R.id.nometopic_simples_prd,cor);

        Integer idinterg= 1;
        Intent mainintent = new Intent(this, MainActivity_lojas.class);
        mainintent.putExtra("notify","true");
        mainintent.putExtra("loja",nometopic);
        mainintent.putExtra("id",id);
        mainintent.putExtra("title",title);
        mainintent.putExtra("msg",msg);
        mainintent.putExtra("Url",idicon);

        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                mainintent   , PendingIntent.FLAG_UPDATE_CURRENT);
        notificationLayout.setOnClickPendingIntent(R.id.rlntf,contentIntent);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/ntfsound");


        //SIMPLES NOTIFICATION
        customNotification = new NotificationCompat.Builder(getApplicationContext(), "0")
                .setVibrate(new long[]{1000})
                .setSmallIcon(R.drawable.beer)
                .setContentText(title)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setCustomContentView(notificationLayout)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(alarmSound)
                .build();

        final NotificationTarget notificationTarget = new NotificationTarget(
                getApplicationContext(),
                R.id.img_notify_simples_pdrt,
                notificationLayout,
                customNotification,
                idinterg);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {


                Glide
                        .with(getApplicationContext())
                        .asBitmap()
                        .load(idicon)
                        .into(notificationTarget);

            }
        });


        mNotificationManager.notify(idinterg,customNotification);

    }



    /**
         * Schedule a job using FirebaseJobDispatcher.
         */
        private void scheduleJob() {
            // [START dispatch_job]
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
            Job myJob = dispatcher.newJobBuilder()
                    .setTag("my-job-tag")
                    .build();
            dispatcher.schedule(myJob);
            // [END dispatch_job]
        }
        /**
         * Handle time allotted to BroadcastReceivers.
         */
        private void handleNow() {
            Log.d(TAG, "Short lived task is done.");
        }


    }
