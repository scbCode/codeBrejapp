package brejapp.com.brejapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import brejapp.com.brejapp.R;


public class MainActivity_notifications extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {
    Context c;
    static DatabaseReference database;
    Empresa empresa;
    String urlbanner;
    boolean imgcaptured;
    ImageView imgntf;
    notifySimples nft;
    FirebaseListAdapter       listB;
    ListView listsuper;
    Item prdselecionado;
    Notification customNotification = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

        imgntf=(ImageView) findViewById(R.id.imgNtf);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        c = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        empresa = MainActivity_dashempresa.myLoja;

        setparamLayoutNtf();

        syncFirebase();


        Button btnntfA = (Button) findViewById(R.id.btnselecntfA);
        btnntfA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfSimples);
                rls.setVisibility(View.VISIBLE);

            }
        });


        Button btnenviarsimple = (Button) findViewById(R.id.btnenviar);
        btnenviarsimple .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfsimples2);
                EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgasimples);
                String ttl = edtxttitle.getText().toString();
                String msg = edtxtmsg.getText().toString();

                String time = String.valueOf(new Date().getTime());
                Log.i("teset","teste "+msg.getBytes().length);
                imgcaptured=false;
                String t = new Date().getTime()+"";
                String id = t+ empresa.Id;
                ImageView imageView = (ImageView) findViewById(R.id.imgBig);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                String cor = String.format("#%06X", (0xFFFFFF & getDominantColor(bitmap)));
                if (cor.equals("#FFFFFF"))cor = "#333333";

                if (ttl.length() >0 && msg.length() >0){
                     nft = new notifySimples(ttl,msg,empresa.Topico,empresa.UrlImg,empresa.Nome,"",time+"",id,"a",cor,"","");
                     checktimenntf();
                }else
                    Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();

            }

        });

        Button btnntfB = (Button) findViewById(R.id.btnselecntfB);
        btnntfB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfbIGTEXT);
                rls.setVisibility(View.VISIBLE);

            }
        });
//////////////////
        Button btnenvB = (Button) findViewById(R.id.btnenviarbgttxt);
        btnenvB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfbgtxt);
                EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgabgtxt);
                String ttl = edtxttitle.getText().toString();
                String msg = edtxtmsg.getText().toString();
                String time = String.valueOf(new Date().getTime());
                imgcaptured=false;
                String t = new Date().getTime()+"";
                String id = t+ empresa.Id;
                ImageView imageView = (ImageView) findViewById(R.id.imgBig);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                String cor = String.format("#%06X", (0xFFFFFF & getDominantColor(bitmap)));
                if (cor.equals("#FFFFFF"))cor = "#333333";


                if (ttl.length() >0 && msg.length() >0) {
                    RelativeLayout rlload = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                    rlload.setVisibility(View.VISIBLE);
                    nft = new notifySimples(ttl, msg, empresa.Topico, empresa.UrlImg, empresa.Nome, "", time + "",id,"a",cor,"","");
                    checktimenntf();
                } else
                    Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();


            }
        });
        Button btnenviarB = (Button) findViewById(R.id.btntestarntfbgt);
        btnenviarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfbgtxt);
                EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgabgtxt);
                String ttl = edtxttitle.getText().toString();
                String msg = edtxtmsg.getText().toString();
                String t = new Date().getTime()+"";
                String id = t+ empresa.Id;

                if (ttl.length() >0 && msg.length() >0)
                    sendtestentfbigtext(edtxttitle.getText().toString(),edtxtmsg.getText().toString(),empresa.UrlImg,empresa.Nome,id);
                else
                    Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();


            }
        });
        Button btntestar = (Button) findViewById(R.id.btntestarntf);
        btntestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfsimples2);
                EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgasimples);
                String ttl = edtxttitle.getText().toString();
                String msg = edtxtmsg.getText().toString();

                Log.i("teset","teste "+msg.getBytes().length);
                String t = new Date().getTime()+"";
                String id = t+ empresa.Id;
                if (ttl.length() >0 && msg.length() >0)
                    sendNotificationLink(edtxttitle.getText().toString(),edtxtmsg.getText().toString(),empresa.UrlImg,empresa.Nome,id);
                else
                    Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();
            }

        });


        Button btnreturnbgttext = (Button) findViewById(R.id.btnreturnpopbgtxt);
        btnreturnbgttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfbIGTEXT);
                rls.setVisibility(View.INVISIBLE);

            }
        });


        Button btnreturn = (Button) findViewById(R.id.btnreturnpop);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfSimples);
                rls.setVisibility(View.INVISIBLE);

            }
        });


        Button btnntfC = (Button) findViewById(R.id.btnselecntfC);
        btnntfC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfBigImg);
                rls.setVisibility(View.VISIBLE);

            }

        });


        Button btnntfD = (Button) findViewById(R.id.btnselecntfD);
        btnntfD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfProdut);
                rls.setVisibility(View.VISIBLE);

                RelativeLayout rlslist = (RelativeLayout) findViewById(R.id.rllistaitens);
                rlslist.setVisibility(View.VISIBLE);


                getlistaitensprd();

            }

        });




        Button btnntfenviarPrd = (Button) findViewById(R.id.btnenviarprd);
        btnntfenviarPrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfprd);
                EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgaprd);
                String ttl = edtxttitle.getText().toString();
                String msg = edtxtmsg.getText().toString();
                String t = new Date().getTime()+"";
                String id = t+ empresa.Id;
                ImageView imageView = (ImageView) findViewById(R.id.imgBig);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                String cor = String.format("#%06X", (0xFFFFFF & getDominantColor(bitmap)));
                if (cor.equals("#FFFFFF"))cor = "#333333";

                Log.i("preco","preco " + prdselecionado.Preco);
                if (ttl.length() >0 && msg.length() >0 ) {
                    nft = new notifySimples(ttl, msg, empresa.Topico, prdselecionado.urlimg, empresa.Nome, "", t+ "", id,"b",cor,""+prdselecionado.Preco,prdselecionado.unidade);
                    checktimenntf();
                } else
                    Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();




            }

        });


        Button btnsendC = (Button) findViewById(R.id.btnenviarbgimg);
        btnsendC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfbgimg);
                EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgabgimg);
                String ttl = edtxttitle.getText().toString();
                String msg = edtxtmsg.getText().toString();
                String t = new Date().getTime()+"";
                String id = t+ empresa.Id;
                ImageView imageView = (ImageView) findViewById(R.id.imgBig);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                String cor = String.format("#%06X", (0xFFFFFF & getDominantColor(bitmap)));
                if (cor.equals("#FFFFFF"))cor = "#333333";


                if (ttl.length() >0 && msg.length() >0 ) {
                    checktimenntf();
                } else
                    Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();



            }

        });

        Button retbtnntfC = (Button) findViewById(R.id.btnreturnpopbgimg);
        retbtnntfC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfBigImg);
                rls.setVisibility(View.INVISIBLE);

            }

        });

        Button btntestimgbig = (Button) findViewById(R.id.btntestarntfbgimg);
        btntestimgbig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfbgimg);
                EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgabgimg);
                String ttl = edtxttitle.getText().toString();
                String msg = edtxtmsg.getText().toString();

                if (ttl.length()> 0 && msg.length()>0 && imgcaptured==true) {

                    sendtestentfbigimg(edtxttitle.getText().toString(), edtxtmsg.getText().toString(), empresa.UrlImg, empresa.Nome, urlbanner);
                }else{

                    if (ttl.length()== 0 || msg.length()==0) {
                        Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();

                    }else
                    if (imgcaptured==true) {
                        Toast.makeText(c,"Escolha uma imagem",Toast.LENGTH_LONG).show();

                    }
                }
            }

        });

        Button btntestentfprodut = (Button) findViewById(R.id.btntestarntfprd);
        btntestentfprodut.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 ImageView imageView = (ImageView) findViewById(R.id.imgBig);
                                                 imageView.setDrawingCacheEnabled(true);
                                                 imageView.buildDrawingCache();
                                                 Bitmap bitmap = imageView.getDrawingCache();
                                                 String cor = String.format("#%06X", (0xFFFFFF & getDominantColor(bitmap)));
                                                 if (cor.equals("#FFFFFF"))cor = "#333333";

                                                 EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfprd);
                                                 EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgaprd);
                                                 String ttl = edtxttitle.getText().toString();
                                                 String msg = edtxtmsg.getText().toString();
                                                 prdselecionado.corsuper = String.valueOf(cor);

         sendNotificationProduto(ttl,msg,prdselecionado.urlic, empresa.Nome,prdselecionado.urlimg,prdselecionado.Preco,prdselecionado.unidade,cor);

                                             }

         });


        Button retbtnntfITEMD = (Button) findViewById(R.id.btnreturnpopprd2);
        retbtnntfITEMD .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.rllistaitens);
                rls.setVisibility(View.INVISIBLE);

            }

        });


        Button retbtnntfD = (Button) findViewById(R.id.btnreturnpopprd);
        retbtnntfD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfProdut);
                rls.setVisibility(View.INVISIBLE);

            }

        });
        Button btnbuscarimg = (Button) findViewById(R.id.btngetImgntf);
        btnbuscarimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarimginternal();
            }

        });


    }

    public void saveImg(String nomeloja) {

        //creating reference to firebase storage
        String nome  =  nomeloja;

        nome = nome.replace("\\u","");


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://devscb-7afe7.appspot.com/NTF_BIG_IMG");    //change the url according to your firebase app

        final StorageReference childRef = storageRef.child(nome+".png");

        //uploading the image
        ImageView imageView = (ImageView) findViewById(R.id.imgNtf);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();


        final UploadTask uploadTask = childRef.putBytes(data);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity_notifications.this);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                        .getTotalByteCount());
                progressDialog.show();
                progressDialog.setMessage("Savando imagem... "+(int)progress+"%");

            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error ao salvar imagem, tente novamente", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressDialog.show();
                        progressDialog.setMessage("Imagem salva");
                        progressDialog.hide();

                        EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfbgimg);
                        EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgabgimg);
                        String ttl = edtxttitle.getText().toString();
                        String msg = edtxtmsg.getText().toString();
                        String time = String.valueOf(new Date().getTime());

                        String url = String.valueOf(String.valueOf(uri));
                        String t = new Date().getTime()+"";
                        String id = t+ empresa.Id;
                        ImageView imageView = (ImageView) findViewById(R.id.imgBig);
                        imageView.setDrawingCacheEnabled(true);
                        imageView.buildDrawingCache();
                        Bitmap bitmap = imageView.getDrawingCache();
                        String cor = String.format("#%06X", (0xFFFFFF & getDominantColor(bitmap)));
                        if (cor.equals("#FFFFFF"))cor = "#333333";
                        nft = new notifySimples(ttl, msg, empresa.Topico, empresa.UrlImg, empresa.Nome,url,  time + "",id,"a",cor,"0","0");
                        sendNotfSimples();

                    }
                });

            }


        });

    }


    public void buscarimginternal() {

        final int RESULT_GALLERY = 0;
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_GALLERY);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (null != data) {
                ImageView img = (ImageView) findViewById(R.id.imgNtf);
                Uri imgem = data.getData();
                Glide.with(c).load(imgem).into(img);
                imgntf.setImageDrawable(img.getDrawable());
                imgcaptured=true;
            }

        }

    }


    public void sendNotificationLink(String title, String msg, final String idicon, String nometopic,String id) {

        ImageView img= (ImageView)null;

        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //layout simples
        final RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layoutnotify);

        //SET LAYOUT VALUES SIMPLES
        notificationLayout.setTextViewText(R.id.txtTitleNotif,title);
        notificationLayout.setTextViewText(R.id.txt2Notif,msg);
        notificationLayout.setTextViewText(R.id.nometopic_simples,nometopic);


        Integer idinterg= 1;
        Intent mainintent = new Intent(this, MainActivity_lojas.class);
        mainintent.putExtra("notify","true");
        mainintent.putExtra("loja",nometopic);
        mainintent.putExtra("id",id);
        mainintent.putExtra("title",title);
        mainintent.putExtra("msg",msg);
        mainintent.putExtra("Url",idicon);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                mainintent   , PendingIntent.FLAG_UPDATE_CURRENT);
        notificationLayout.setOnClickPendingIntent(R.id.rlntf,contentIntent);


        Notification customNotification = null;
            //SIMPLES NOTIFICATION
            customNotification = new NotificationCompat.Builder(getApplicationContext(), "0")
                        .setVibrate(new long[]{1000})
                        .setSmallIcon(R.drawable.beer)
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setCustomContentView(notificationLayout)
                        .setPriority(Notification.PRIORITY_MAX)
                        .build();

       final NotificationTarget notificationTarget = new NotificationTarget(
               getApplicationContext(),
               R.id.img_notify_simples,
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


   public void sendtestentfbigtext(String title, String msg, final String idicon, String nometopic,String id) {

        ImageView img= (ImageView)null;

        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //layout simples
        final RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layoutnotify);

        //SET LAYOUT VALUES SIMPLES
        notificationLayout.setTextViewText(R.id.txtTitleNotif,title);
        notificationLayout.setTextViewText(R.id.txt2Notif,msg);
        notificationLayout.setTextViewText(R.id.nometopic_simples,nometopic);

        Notification customNotification = null;
        RemoteViews   remoteViews_bigtext = new RemoteViews(getPackageName(), R.layout.layoutnotify_bigtext);

        remoteViews_bigtext.setTextViewText(R.id.txtTitleNotif,title);
        remoteViews_bigtext.setTextViewText(R.id.txt2Notif,msg);
        remoteViews_bigtext.setTextViewText(R.id.nometopic_big,nometopic);

        //SIMPLES NOTIFICATION
        customNotification = new NotificationCompat.Builder(getApplicationContext(), "0")
                .setVibrate(new long[]{1000})
                .setSmallIcon(R.drawable.beer)
                .setContentText(title)
                .setAutoCancel(false)
                .setCustomBigContentView(remoteViews_bigtext)
                .setCustomContentView(notificationLayout)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        //img Icon
        final NotificationTarget notificationTargetbigtext = new NotificationTarget(
                getApplicationContext(),
                R.id.img_notify_bigtext,
                remoteViews_bigtext,
                customNotification,
                0);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Glide
                        .with(getApplicationContext())
                        .asBitmap()
                        .load(idicon)
                        .into(notificationTargetbigtext);

            }});

        //img Icon
        final NotificationTarget notificationTarget = new NotificationTarget(
                getApplicationContext(),
                R.id.img_notify_simples,
                notificationLayout,
                customNotification,
                0);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Glide
                        .with(getApplicationContext())
                        .asBitmap()
                        .load(idicon)
                        .into(notificationTarget);

            }});


        mNotificationManager.notify(0,customNotification);

    }


   public void sendtestentfbigimg(String title, String msg, final String idicon, String nometopic, final String urlBanner) {

        ImageView img= (ImageView)null;

        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //layout simples
        final RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layoutnotify);

        //SET LAYOUT VALUES SIMPLES
        notificationLayout.setTextViewText(R.id.txtTitleNotif,title);
        notificationLayout.setTextViewText(R.id.txt2Notif,msg);
       notificationLayout.setTextViewText(R.id.nometopic_simples,nometopic);

        Notification customNotification = null;


       RemoteViews   remoteViews_big = new RemoteViews(getPackageName(), R.layout.layoutnotify_big);

       remoteViews_big.setTextViewText(R.id.txtTitleNotif,title);
       remoteViews_big.setTextViewText(R.id.txt2Notif,msg);
       remoteViews_big.setTextViewText(R.id.nometopic_big,nometopic);
       ImageView imgget = (ImageView) findViewById(R.id.imgNtf);

       BitmapDrawable bitmapDrawable = (BitmapDrawable) imgget.getDrawable();
       remoteViews_big.setImageViewBitmap(R.id.imgBig,bitmapDrawable.getBitmap());

       //SIMPLES NOTIFICATION
       customNotification = new NotificationCompat.Builder(getApplicationContext(), "0")

               .setVibrate(new long[]{1000})
               .setSmallIcon(R.drawable.beer)
               .setContentText(title)
               .setAutoCancel(false)
               .setCustomBigContentView(remoteViews_big)
               .setCustomContentView(notificationLayout)
               .setPriority(Notification.PRIORITY_MAX)
               .build();

       final NotificationTarget notificationTarget_topic = new NotificationTarget(
               getApplicationContext(),
               R.id.img_notify_simples,
               notificationLayout,
               customNotification,
               0);

       new Handler(Looper.getMainLooper()).post(new Runnable() {
           @Override
           public void run() {
               Glide
                       .with(getApplicationContext())
                       .asBitmap()
                       .load(idicon)
                       .into(notificationTarget_topic);

           }});

       //big img big layout
       final NotificationTarget notificationTargetbigimg = new NotificationTarget(
               getApplicationContext(),
               R.id.img_notify_big,
               remoteViews_big,
               customNotification,
               0);

       new Handler(Looper.getMainLooper()).post(new Runnable() {
           @Override
           public void run() {
               Glide
                       .with(getApplicationContext())
                       .asBitmap()
                       .load(idicon)
                       .into(notificationTargetbigimg);

           }
       });




        mNotificationManager.notify(0,customNotification);

    }


   public void sendNotificationProduto(String title, String msg, final String idicon, String nometopic,String id,String prec,String uni,String color) {

        ImageView img= (ImageView)null;

        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //layout simples
        final RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layoutnotify_produto);

        //SET LAYOUT VALUES SIMPLES
        notificationLayout.setTextViewText(R.id.txtTitleNotif,title);
        notificationLayout.setTextViewText(R.id.txt2Notif,msg);
       notificationLayout.setTextViewText(R.id.nometopic_simples_prd,nometopic);
       notificationLayout.setTextViewText(R.id.txtunidadeprd,uni);
       prec=prec.replace(".",",");

       notificationLayout.setTextViewText(R.id.tdtPrc,prec);
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
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                mainintent   , PendingIntent.FLAG_UPDATE_CURRENT);
//        notificationLayout.setOnClickPendingIntent(R.id.rlntf,contentIntent);




        //SIMPLES NOTIFICATION
        customNotification = new NotificationCompat.Builder(getApplicationContext(), "0")
                .setVibrate(new long[]{1000})
                .setSmallIcon(R.drawable.beer)
                .setContentText(title)
                .setAutoCancel(true)
//                .setContentIntent(contentIntent)

                .setCustomContentView(notificationLayout)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        customNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        final NotificationTarget notificationTarget = new NotificationTarget(
                getApplicationContext(),
                R.id.img_notify_simples_pdrt,
                notificationLayout,
                customNotification,
                idinterg);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {


                final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + getPackageName() + "/raw/ntfsound");
                customNotification.sound = alarmSound;

                Glide
                        .with(getApplicationContext())
                        .asBitmap()
                        .load(idicon)
                        .into(notificationTarget);


            }
        });
       mNotificationManager.notify(idinterg,customNotification);

    }


    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }


    public void checktimenntf(){

        RelativeLayout rlload = (RelativeLayout) findViewById(R.id.layoutloadpromo);
        rlload.setVisibility(View.VISIBLE);

        database.child("UsersEmpresa").child(empresa.Uid).child("Notificacoes_time").child("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("teste time","time : "+new Date().getTime());

                if (dataSnapshot.getValue()!= null){
                long mll =Math.abs( new Date().getTime())- Math.abs(Long.parseLong(
                         dataSnapshot.getValue().toString()));
                    long seconds = mll / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
               Log.i("teste time","time : "+minutes);
               if (minutes>=3  ) {
                   if (imgcaptured==false)
                       sendNotfSimples();
                   if (imgcaptured==true)
                       saveImg(empresa.Nome);

               } else{

                   if (seconds > 3)
                   Toast.makeText(c,"Você precisa aguardar 3 minutos para enviar outra notificação. Restando "+(180-seconds)+" segundos.",Toast.LENGTH_LONG).show();
                   RelativeLayout rlload = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                   rlload.setVisibility(View.INVISIBLE);
               }
           }else{

                        sendNotfSimples();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void sendNotfSimples(){

        final String time = new Date().getTime()+"";

        database.child("UsersEmpresa").child(empresa.Uid).child("Notificacoes_time").child("time").setValue(time)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

        database.child("UsersEmpresa").child(empresa.Uid).child("Notificacoes").child(time).setValue(nft)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database.child("req_notify")
                        .child(time+empresa.Id).setValue(nft)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(c,"Notificação enviada",Toast.LENGTH_LONG).show();

                                RelativeLayout rlload = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                                rlload.setVisibility(View.INVISIBLE);
                                RelativeLayout rls = (RelativeLayout) findViewById(R.id.popNtfSimples);
                                rls.setVisibility(View.INVISIBLE);
                                RelativeLayout rlsbgimg = (RelativeLayout) findViewById(R.id.popNtfBigImg);
                                rlsbgimg.setVisibility(View.INVISIBLE);
                                RelativeLayout rlsbgt = (RelativeLayout) findViewById(R.id.popNtfbIGTEXT);
                                rlsbgt.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });     }

                });
    }


    public void syncFirebase() {

        //persistence start
        if (database == null) {
            persistent pst = new persistent();
            pst.onCreate();
            database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");

        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intentMain = new Intent(MainActivity_notifications.this ,
                    MainActivity.class);
            MainActivity_notifications.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_notifications.this ,
                    MainActivity_lojas.class);
            MainActivity_notifications.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_notifications.this ,
                    MainActivity_categorias.class);
            MainActivity_notifications.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_notifications.this ,
                    MainActivity_topicos.class);
            MainActivity_notifications.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_notifications.this ,
                    MainActivity_contato.class);
            MainActivity_notifications.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_notifications.this ,
                    MainActivity_empresa.class);
            MainActivity_notifications.this.startActivity(intentMain);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////


    public void  setparamLayoutNtf(){

        ImageView logo = (ImageView) findViewById(R.id.img_notify_simples);
        Glide.with(c).load(empresa.UrlImg).into(logo);

        TextView nome = (TextView) findViewById(R.id.nometopic_simples);
        nome.setText(empresa.Nome);

        ImageView logobigtext = (ImageView) findViewById(R.id.img_notify_bigtext);
        Glide.with(c).load(empresa.UrlImg).into(logobigtext);

        TextView nomebigtxt= (TextView) findViewById(R.id.nometopic_bigtxt);
        nomebigtxt.setText(empresa.Nome);

        ImageView logobigimg = (ImageView) findViewById(R.id.img_notify_big);
        Glide.with(c).load(empresa.UrlImg).into(logobigimg);

        ImageView bigimg = (ImageView) findViewById(R.id.imgBig);
        Glide.with(c).load(empresa.UrlImg).into(bigimg);

        TextView nomebigimg= (TextView) findViewById(R.id.nometopic_big);
        nomebigimg.setText(empresa.Nome);

        TextView nomebigimgloja= (TextView) findViewById(R.id.nometopic_big);
        nomebigimgloja.setText(empresa.Nome);


        //LAYOUT PRODUTO
        ImageView imgsmall = (ImageView) findViewById(R.id.img_notify_simples_pdrt);
        imgsmall.setImageResource(R.drawable.prdicon);
        TextView nomeljaprd= (TextView) findViewById(R.id.nometopic_simples_prd);
        nomeljaprd.setText(empresa.Nome);
        //LAYOUT PRODUTO

    }


    public void   getlistaitensprd(){

         listsuper = (ListView) findViewById(R.id.lsitaprdtitem);

                listB = new FirebaseListAdapter<Item>(c, Item.class, R.layout.listhj,database.child("Catalogos").orderByChild("supermercado").equalTo(""+empresa.Nome)) {

                @SuppressLint("WrongConstant")
                @Override
                protected void populateView(final View vw, final Item model, final int position) {

                    vw.setBackgroundColor(0xefefef);
                    boolean ctral = false;

                    Item it = model;

                    TextView txtnome = (TextView) vw.findViewById(R.id.nomeItem);
                    TextView txtDesc = (TextView) vw.findViewById(R.id.descricao);
                    TextView textsuperm = (TextView) vw.findViewById(R.id.superm);

                    textsuperm.setText(model.supermercado+" "+model.detalsup);
                    txtDesc.setText(model.Descricao);
                    txtnome.setText(model.NomeProduto);

                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int w = size.x;


                    TextView txtpreco = (TextView) vw.findViewById(R.id.Preco);

                    TextView txtprecodec = (TextView) vw.findViewById(R.id.precoDec);

                    TextView txtexp = (TextView) vw.findViewById(R.id.txtexpira);

                    final Calendar cal = Calendar.getInstance(); // Get Calendar Instance

                    //GET DATA INICIO ITEM
                    String dateInString = model.datai;  // Start date
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

                    //FORMATO MES/DIA
                    if (model.datai != null) {
                        try {
                            cal.setTime(sdf.parse(dateInString));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        cal.add(Calendar.DATE, Integer.parseInt(model.expirar));  // add  days
                        sdf = new SimpleDateFormat("dd/MM");//FORMATO DIA/MES
                        Date resultdateExp = new Date(cal.getTimeInMillis());//GET DATE EXP.
                        String dateInexp = sdf.format(resultdateExp);

                        Calendar calend = Calendar.getInstance();
                        //DATA atual
                        Date resultdatediai = new Date(calend.getTimeInMillis());   // Get new time
                        String diahj = sdf.format(resultdatediai);
                        //Log.i("DATE TESTE",model.NomeProduto+" dateInexp " + dateInexp +" diahj "+diahj);

                        try {
                            Date dateexp = sdf.parse(dateInexp);
                            Date datehj = sdf.parse(diahj);
                            long diff = dateexp.getTime() - datehj.getTime();
                            Log.i("DATE TESTE", model.NomeProduto + " di " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                            String d = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                            if (diff > 0)
                                txtexp.setText("");
                            else if (diff < 0) {
                                txtexp.setText("EXPIROU");

                            } else if (diff == 0)
                                txtexp.setText("");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    //CONVERT PRECO ITEM , - .
                    String precoitem = model.Preco;
                    int positionpoint = 0;
                    String preco= "";
                    String precoDec= "";

                    if (precoitem != null) {

                        if (precoitem.length() == 4) positionpoint = 1;
                        else
                        if (precoitem.length() == 5) positionpoint = 2;
                        else
                        if (precoitem.length() == 6) positionpoint = 3;
                        else
                        if (precoitem.length() == 7) positionpoint = 4;

                        //CONVERT PRECO ITEM , - .
                        if (positionpoint == 1) {
                            preco = String.valueOf(model.Preco.charAt(0));
                            precoDec = String.valueOf(model.Preco.charAt(2)) + String.valueOf(model.Preco.charAt(3));
                            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36);
                            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        } else if (positionpoint == 2) {
                            preco = String.valueOf(model.Preco.charAt(0)) + String.valueOf(model.Preco.charAt(1));
                            precoDec = String.valueOf(model.Preco.charAt(3)) + String.valueOf(model.Preco.charAt(4));
                            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36);
                            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        } else if (positionpoint == 3) {
                            preco = String.valueOf(model.Preco.charAt(0)) + String.valueOf(model.Preco.charAt(1)) + String.valueOf(model.Preco.charAt(2));
                            precoDec = String.valueOf(model.Preco.charAt(4)) + String.valueOf(model.Preco.charAt(5));
                            txtpreco.setPadding(0, 0, 0, 0);
                            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
                            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        } else if (positionpoint == 4) {
                            txtpreco.setPadding(0, 0, 0, 0);
                            preco = String.valueOf(String.valueOf(model.Preco.charAt(0)) + String.valueOf(model.Preco.charAt(1)) + String.valueOf(model.Preco.charAt(2)) + String.valueOf(model.Preco.charAt(3)));
                            precoDec = String.valueOf(String.valueOf(model.Preco.charAt(5)) + String.valueOf(model.Preco.charAt(6)));
                            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
                            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                        }

                    }

                    //edit text preço
                    txtpreco.setText(preco + ",");
                    txtprecodec.setText(precoDec);

                    TextView txttipo = (TextView) vw.findViewById(R.id.unidade);
                    txttipo.setText("" + model.unidade);

                    TextView txtsuperm = (TextView) vw.findViewById(R.id.superm);
                    txtsuperm.setText( model.supermercado+" "+model.detalsup);

                    if (model.corsuper != null) {
                        int corsuper = Color.parseColor("" + model.corsuper);
                        txtsuperm.setTextColor(corsuper);
                    }

                    ImageView icon = (ImageView) vw.findViewById(R.id.iconItemdash);
                    Glide.with(c).load(model.urlic).into(icon);
                    vw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            prdselecionado=model;
                            RelativeLayout rl  = (RelativeLayout) findViewById(R.id.rllistaitens);
                            rl.setVisibility(View.INVISIBLE);

                            EditText edtxttitle = (EditText) findViewById(R.id.editTexttitutlontfprd);
                            EditText edtxtmsg = (EditText) findViewById(R.id.editTextmsgaprd);
                            edtxttitle.setText(model.NomeProduto);
                            edtxtmsg.setText(model.Descricao);

                            ImageView imgprd = (ImageView) findViewById(R.id.imgNtfprd);
                            Glide.with(c).load(model.urlic).into(imgprd);


                        }
                    });


                }
            };
            listsuper.setAdapter(listB);
            listsuper.setDividerHeight(0);


    }



}
