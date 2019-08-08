package brejapp.com.brejapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.L;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_OUTSIDE;
import static android.view.MotionEvent.ACTION_UP;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    static  Empresa myEmpresa;
    static  MainActivity main;
    static ListView listitem;
    static ListView listmsg;
    static   ListView listsuper;
    static   ListView   listafav;
    static ArrayList<LatLng> localloja = new ArrayList<LatLng>();
    static ArrayList<Double> logdistances = new ArrayList<Double>();
    static RelativeLayout load;
    static FirebaseListAdapter<Chatmsg>   msglist;
    String nomelojatopc;
    static Context c;
    boolean shareitemclick;
    static boolean moveinfo = false;
    static ListView lj;
    static TextView txtdatainfo;
    static TextView txtinfouni;
    static FirebaseListAdapter listB;
    int counterfotos;
    int totalHeight;
    static RelativeLayout rlinfoitem;

    static ImageView icon;
    Intent servcGPS;

    static   String iduser;
    static    FirebaseListAdapter feed;
    boolean btnbusc;
    boolean ctrlnome = false;
    boolean ctrlb = false;
    boolean ctrlc = false;
    boolean ctrld = false;
    static boolean ctrlfav=false;
    static DatabaseReference database;
    static FirebaseDatabase datab;
    static   NavigationView   navigationView ;
    static boolean ctrlpedidos;

    static String nomeuser;
    static boolean loginsimples;
    static Typeface fontA;
    static Typeface fontBGB;
    static Typeface fontBGR;
    private MenuItem itemToShow;
    private boolean itemToShobw;
    static ArrayList<String> deliveryOn = new ArrayList<String>();
    static ArrayList<String> mycesta = new ArrayList<String>();
    static ArrayList<Item> myListaItens = new ArrayList<Item>();

    static ArrayList<Itemloja> lojas = new ArrayList<Itemloja>();
    static ArrayList<Itemloja> lojasf = new ArrayList<Itemloja>();
    static ArrayList<String> lojasfav = new ArrayList<String>();
    static ArrayList<String> itensAutoC = new ArrayList<String>();
    static Item itemclicado = new Item();
    static ArrayList<String> palavrasb = new ArrayList<String>();
    int contbuscapalavra = 0;
    String stringbusca;
    boolean ctrlbuscatxt;
    String busca;
    boolean movebanner = false;
    static double ymf = 0.0;
    static double ymi = 0.0;
    static double heightBanner = 0.0;
    static double xtch = 0.0;
    static double xtchf = 0.0;
    static GoogleMap mapa;
    static  User myUser;
    final ArrayList<String> listafiltroskeys = new ArrayList<>();
    static ArrayList<String> listalojaschat = new ArrayList<>();
    static Itemloja getloja;
    static String local;

    boolean ctrlbttopic;
    boolean ctrlbusca=false;
    boolean ctrlbuscafinal=false;
     int cont=0;
    static TextView txtpreco;
    static TextView txtprecodec;
    static TextView txtnome;
    static TextView txttipo;
    static TextView txtDesc;
    static TextView txtsuper;
    static TextView txtinfouniItem;
    static TextView txtdatainfoItem;
    static ImageView iconsupinfoitem;
    static ImageView iconItem;
    static RelativeLayout rlbtnligar;
    static RelativeLayout rlbtnwhats;
    static RelativeLayout layoutitem;
    static RelativeLayout layoutloja;
    static TextView txtNomeLoja;
    static TextView txtdescricaoloja;
    static ViewGroup.LayoutParams params  ;
    static   Button btncadastro;
    static  RelativeLayout consloadwindow ;
    static   Button btnrecsenha  ;
    static EditText edtemail;
    static EditText edtemailcaduser ;
    static EditText edtpwscad ;

    static Button BTNCLOSE;
    static RelativeLayout rlpop;
    static Button btntopico;
    static RelativeLayout rlpopperfil;
    static ImageView imglogo;
    static ImageView imgbaner;
    static  Display display;
    static  TextView totalinsc ;
    static EditText Temail;
    static EditText Tpsw;
    static TextView btncesta ;
    static   RelativeLayout rlpopcad ;
    static   RelativeLayout rlfav ;

    static   boolean clickitem;
    static   boolean clickperfil;

    static  Double lat;
    static  Double lng;

    static   boolean ctrlgps;
    static   boolean ctrlchat;

   static boolean ctrlcesta;

    static   View w;
    static  Itemloja lojachat;
    static   Itemloja myloja;
    static  Itemloja myPerfil;
     boolean ctrlservice;
    static boolean ctrlmenustatus;
    static   NotificationManager    mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        try {
            fontBGB = Typeface.createFromAsset(getAssets(),
                    "font/brandongrotesqueblack.otf");


            fontBGR = Typeface.createFromAsset(getAssets(),
                    "font/brandongrotesqueregular.otf");
        }catch (Exception e) {}
        load = (RelativeLayout) findViewById(R.id.layoutloadpromo);


        rlinfoitem = (RelativeLayout) findViewById(R.id.rlinfoItem);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

             mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        txtpreco = (TextView) findViewById(R.id.txtprecoitem);
        txtprecodec = (TextView) findViewById(R.id.txtdescip);
        txtnome = (TextView) findViewById(R.id.txtnomeitem);
        txttipo = (TextView) findViewById(R.id.txttipoitem);
        txtDesc = (TextView) findViewById(R.id.txtdescricaoitem);
        txtsuper = (TextView) findViewById(R.id.txtsupermerc);
        txtinfouniItem = (TextView) findViewById(R.id.txtinfouni);
        txtdatainfoItem = (TextView) findViewById(R.id.txtdatainfo);
        iconsupinfoitem = (ImageView) findViewById(R.id.iconsupinfoitem);
        iconItem = (ImageView) findViewById(R.id.imgIteminfo);
        rlbtnligar = (RelativeLayout) findViewById(R.id.rlbtnligar);
        rlbtnwhats = (RelativeLayout) findViewById(R.id.rlbtnwhats);
        layoutitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
        rlpopperfil   =  (RelativeLayout) findViewById(R.id.popcadastro);

        layoutloja = (RelativeLayout) findViewById(R.id.layoutLoja);
        txtNomeLoja = (TextView) findViewById(R.id.nomneg);
        txtdescricaoloja = (TextView) findViewById(R.id.descricaoloja);
        listsuper = (ListView) findViewById(R.id.listProdutos) ;
        btncadastro = (Button) findViewById(R.id.btncadastro);
        consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
        btnrecsenha = (Button) findViewById(R.id.btnrecupersenha);
        edtemail = (EditText)findViewById(R.id.emailuser);

        BTNCLOSE = (Button) findViewById(R.id.btnclosecads);
        rlpop = (RelativeLayout) findViewById(R.id.popcadastro);
        btntopico = (Button) findViewById(R.id.btninscrevase);
        rlpop = (RelativeLayout) findViewById(R.id.popcadastro);
        imglogo = (ImageView) findViewById(R.id.imglogoemp);
        imgbaner = (ImageView) findViewById(R.id.bannerloja);
        totalinsc = (TextView) findViewById(R.id.totalinscritos);
        Temail = (EditText) findViewById(R.id.emailuser);
        edtemailcaduser = (EditText)findViewById(R.id.emailusercad);
        edtpwscad = (EditText)findViewById(R.id.pwsusercad);

        Tpsw = (EditText) findViewById(R.id.pwsuser);
        btncesta = (TextView) findViewById(R.id.btncesta);
        display = getWindowManager().getDefaultDisplay();
        w = (View) findViewById(R.id.includelojalayout);
        rlfav = (RelativeLayout) findViewById(R.id.rlfav);
        rlpopcad = (RelativeLayout) findViewById(R.id.popcadastrouser);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MapsInitializer.initialize(this);

        c = getApplicationContext();

        isGooglePlayServicesAvailable(this);

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Drawable drawable = navigationView.getMenu().findItem(R.id.nav_status).getIcon();
        drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);

        LocationRequest locationRequest = LocationRequest.create();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API) .build(); googleApiClient.connect();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder() .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); // this is the key ingredient

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be
                        // fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 1000);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });


        final TextView btncesta = (TextView) findViewById(R.id.btncesta);
        btncesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctrlcesta==false){
                    if (myUser!=null) {
                        additem(itemclicado);
                        btncesta.setText("Remover da cesta");
                        btncesta.setTextColor(0xffcc0000);
                        ctrlcesta=true;
                    }else {
                        loginsimples=true;
                        Toast.makeText(c,"Necessário login", Toast.LENGTH_SHORT).show();
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastrouser);
                        rl.setVisibility(View.VISIBLE);
                    }

                }else {
                    removeitem(itemclicado);
                    btncesta.setTextColor(0xFFFDC200);
                    btncesta.setText("Adicionar á cesta");
                    ctrlcesta=false;
                }
            }
        });

        Button btnreturnperfil = (Button) findViewById(R.id.btnreturnloja);
        btnreturnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutloja = (RelativeLayout) findViewById(R.id.layoutLoja);
                layoutloja.setVisibility(View.INVISIBLE);
                clickperfil=false;
            }
        });


        //BOTAO FECHAR
        Button btnfecharinfor = (Button) findViewById(R.id.btnfecharinfoitem);
        btnfecharinfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rlinfoitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
                rlinfoitem.setVisibility(View.INVISIBLE);
                clickitem=false;

            }

        });


        ImageView btnmapa = (ImageView) w.findViewById(R.id.btnmapsloja);
        btnmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("MAPA","MAPA ");
                RelativeLayout mapa = (RelativeLayout) findViewById(R.id.mapa);
                mapa.setVisibility(View.VISIBLE);

            }
        });

        Button btnreturnmapa = (Button) findViewById(R.id.btnreturnloja2);
        btnreturnmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mapa = (RelativeLayout) findViewById(R.id.mapa);
                mapa.setVisibility(View.INVISIBLE);
            }
        });


        Button btn = (Button) findViewById(R.id.btnbusca);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnbusc == false) {
                    ConstraintLayout autocb = (ConstraintLayout) findViewById(R.id.autocompletbusca);
                    autocb.setVisibility(View.VISIBLE);
                    btnbusc = true;
                } else {
                    ConstraintLayout autocb = (ConstraintLayout) findViewById(R.id.autocompletbusca);
                    autocb.setVisibility(View.INVISIBLE);
                    listpromoHj("time", "");
                    btnbusc = false;
                }
            }

        });


        final ImageView btnbanner = (ImageView) findViewById(R.id.btnBanner);
        btnbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                movebanner = false;
                animBannerout((float) (heightBanner * -1));
                btnbanner.setVisibility(View.INVISIBLE);

            }
        });



        Button btncpm = (Button) findViewById(R.id.btncomp);
        btncpm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkpermissao();
                compartwhats();
            }

        });



        Button btnligar = (Button) findViewById(R.id.btnligar);
        btnligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkpermissaocallphone();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+55"+getloja.call));
                if (ActivityCompat.checkSelfPermission(c, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }

        });

        Button btnwhats = (Button) findViewById(R.id.btnwhats);
        btnwhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappContact(getloja.whats);
            }
        });

        syncFirebase();
      //  checkpermissao();
        touch();


        Button btntermos = (Button) findViewById(R.id.btnlertermos);
        btntermos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://devscb-7afe7.firebaseapp.com/"));
                startActivity(browserIntent);

            }

        });


        Button btnsair = (Button) findViewById(R.id.btnsair);
        btnsair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.exit(0);
            }

        });
        Button btnaceitar = (Button) findViewById(R.id.btnaceitar);
        btnaceitar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String time = Calendar.getInstance().getTimeInMillis()+"";
                String idClient = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
                database.child("termos_aceitos").child(idClient).setValue(time);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.poptermos);
                rl.setVisibility(View.INVISIBLE);
            }

        });



        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION  )
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        servcGPS   = new Intent(getApplicationContext(), locationService.class);
        startgps();
        FirebaseMessaging.getInstance().subscribeToTopic("487827306brejapparbgmailpcomAPP");
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        if (myPerfil!=null)
        if (myloja!=null && myPerfil.status==true) {
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            startService(servicechat);

            Intent servicedel = new Intent(getApplicationContext(), Pedido_service.class);
            servicedel.putExtra("loja", myloja.Nome);
            Pedido_service.cancelntfdelivery=false;
            startService(servicedel);
            Log.i("Desctroy","Destroy SERVICE");

        }else
        if (MainActivity.nomeuser.equals("user")&& MainActivity.listalojaschat.size()>0){
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            servicechat.putExtra("usermsg","true");
            startService(servicechat);
            Intent servicedelx = new Intent(getApplicationContext(), Pedido_service.class);
            servicedelx.putExtra("loja", myloja.Nome);
            startService(servicedelx);

        }


    }



    @Override
    public void onPause(){
        super.onPause();


        Chat_service.cancelntf=false;

        try {
            if (myPerfil != null) {
                if (myloja != null && myPerfil.status == true && ctrlservice == false) {

                    Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
                    servicechat.putExtra("loja", myloja.Nome);
                    startService(servicechat);

                    Intent servicedel = new Intent(getApplicationContext(), Pedido_service.class);
                    servicedel.putExtra("loja", myloja.Nome);
                    startService(servicedel);
                }
            } else if (nomeuser.equals("user") && listalojaschat.size() > 0 || ctrlpedidos == true) {
                Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
                servicechat.putExtra("usermsg", "true");
                startService(servicechat);
            }

        }catch (Exception e){

        }
    }



    @Override
    public void onResume(){
        super.onResume();
        ctrlservice=false;
        Chat_service.cancelntf=true;
        Log.i("ONLINE","ONLINE "+Chat_service.cancelntf);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);

        stopService(servicechat);

        if (myPerfil!=null){
            if (myloja!=null && myPerfil.status==true && ctrlservice==false ) {
                Intent servicedel = new Intent(getApplicationContext(), Pedido_service.class);
                servicedel.putExtra("loja", myloja.Nome);
                startService(servicedel);
            }
        }
    }


    public void getCesta(){

        String local="";
        if (myUser!=null)
            local=myUser.Uid;

        database.child("CestasClientes").child(local).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mycesta.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                    Item i = postSnapshot2.getValue(Item.class);
                    mycesta.add(i.cod);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    public void additem(Item item){
        if (myUser!=null){
            local=myUser.Uid;
        database.child("CestasClientes").child(local).child(item.supermercado).child(item.iditem).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getCesta();
            }
        });
        }else
        {
            loginsimples=true;
            Toast.makeText(c,"Necessário login", Toast.LENGTH_SHORT).show();
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastrouser);
            rl.setVisibility(View.VISIBLE);
        }
    }

    public void removeitem(Item item){
        local=myUser.Uid;
        database.child("CestasClientes").child(local).child(item.supermercado).child(item.iditem).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    private void checkFirstRun() {

        String idClient = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);


       FirebaseDatabase.getInstance().getReference().child("termos_aceitos").child(idClient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.poptermos);
                    rl.setVisibility(View.INVISIBLE);
                }else
                {
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.poptermos);
                    rl.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION  )
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1000) {
            servcGPS   = new Intent(getApplicationContext(), locationService.class);
            startgps();
        }

    }


    public void checkpermissaocallphone(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE  )
                != PackageManager.PERMISSION_GRANTED  ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CALL_PHONE ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    public void touch() {


        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlbanner);
        final ImageView btn = (ImageView) findViewById(R.id.btnBanner2);

        layout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int action = MotionEventCompat.getActionMasked(event);

                RelativeLayout       banner = (RelativeLayout) findViewById(R.id.rlbanner);

                switch (action) {

                    case (ACTION_DOWN):

                        ymi = event.getY();
                        xtch = event.getX();

                        return true;

                    case (ACTION_MOVE):

                        ymf = event.getY();
                        xtchf = event.getX();

                        if (ymi - ymf > 50.0 && movebanner == false) {

                            movebanner = true;

                            ImageView btnbanner = (ImageView) findViewById(R.id.btnBanner);
                            btnbanner.setVisibility(View.INVISIBLE);
                            ImageView btnb = (ImageView) findViewById(R.id.btnBanner2);
                            btnb.setVisibility(View.VISIBLE);
                            heightBanner = banner.getHeight() * -1;
                            animBannerout((float) heightBanner);

                            Log.i("Info", "up");

                        }

                        return true;
                    case (ACTION_UP):
                        ymf = event.getY();
                        return true;
                    case (ACTION_CANCEL):
                        ymf = event.getY();
                        return true;
                    case (ACTION_OUTSIDE):
                        Log.d("i", "Movement occurred outside bounds " +
                                "of current screen element");
                        return true;
                    default:
                        return false;
                }

            }

        });

        btn.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int action = MotionEventCompat.getActionMasked(event);

                RelativeLayout       banner = (RelativeLayout) findViewById(R.id.rlbanner);


                switch (action) {

                    case (ACTION_DOWN):

                        ymi = event.getY();
                        movebanner = false;
                        animBannerout((float) (heightBanner * -1));
                        //hide btn
                        ImageView btnb = (ImageView) findViewById(R.id.btnBanner);
                        btnb.setVisibility(View.VISIBLE);
                        return true;

                    case (ACTION_MOVE):
                        ymf = event.getY();
                        return true;
                    case (ACTION_UP):
                        ymf = event.getY();
                        return true;
                    case (ACTION_CANCEL):
                        ymf = event.getY();
                        return true;
                    case (ACTION_OUTSIDE):
                        Log.d("i", "Movement occurred outside bounds " +
                                "of current screen element");
                        return true;
                    default:
                        return false;
                }

            }

        });


    }


    public void animBannerout(final float height) {

        if (movebanner == true) {

            ViewFlipper img = (ViewFlipper) findViewById(R.id.imgbanner);
            img.setVisibility(View.GONE);

            ImageView btnbanner2 = (ImageView) findViewById(R.id.btnBanner2);
            btnbanner2.setVisibility(View.VISIBLE);

            ImageView btnbanner = (ImageView) findViewById(R.id.btnBanner);
            btnbanner.setVisibility(View.VISIBLE);

        } else

        {

            ViewFlipper img = (ViewFlipper) findViewById(R.id.imgbanner);
            img.setVisibility(View.VISIBLE);

            ImageView btnbanner = (ImageView) findViewById(R.id.btnBanner);
            btnbanner.setVisibility(View.VISIBLE);

            ImageView btnbanner2 = (ImageView) findViewById(R.id.btnBanner2);
            btnbanner2.setVisibility(View.INVISIBLE);


        }

    }


    public void getbanners(){

        final ArrayList<ImageView> imgviewarr = new ArrayList<>();

        ImageView s01 = (ImageView) findViewById(R.id.img01);
        ImageView s02 = (ImageView) findViewById(R.id.img02);
        ImageView s03 = (ImageView) findViewById(R.id.img03);
        ImageView s04 = (ImageView) findViewById(R.id.img04);

        imgviewarr.add(s01);
        imgviewarr.add(s02);
        imgviewarr.add(s03);
        imgviewarr.add(s04);

        database.child("banners").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int cont=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String url = String.valueOf(postSnapshot.getValue());
                    Glide.with(c).load(url).into(imgviewarr.get(cont));
                    cont++;
                }
                //Start slide banners
                bannerSlide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        database.child("imglogolink").addValueEventListener(new ValueEventListener() {

        @Override
        public void onDataChange (@NonNull DataSnapshot dataSnapshot){
            int cont = 0;

            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                String url = String.valueOf(postSnapshot.getValue());
//                ImageView img = (ImageView) findViewById(R.id.logoheader);
//                Glide.with(c).load(url).into(img);
            }

        }

        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){

        }

    });
}

    // Animslide
    public void bannerSlide() {
        ViewFlipper flipperbanner = (ViewFlipper) findViewById(R.id.imgbanner);

        flipperbanner.startFlipping(); // start the flipping of views
        flipperbanner.setFlipInterval(4000);
        flipperbanner.setAutoStart(true);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left); // load an animation
        flipperbanner.setInAnimation(in);

        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right); // load an animation
        flipperbanner.setOutAnimation(out);

    }


    public void syncFirebase() {

          // persistence start
          datab = FirebaseDatabase.getInstance();
          persistent pst = new persistent();
          pst.onCreate();
          database = datab.getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");

        checkFirstRun();
        getdeliverys();
        getitensAutocomplet();
        getFiltrs();
        getbanners();
        getSupermercados();
        authentic();
        startgps();


    }




    public void getFiltrs(){

        final ArrayList<String> listafiltros = new ArrayList<>();

        database.child("Categorias").orderByKey().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {

                        listafiltros.add("" + dataSnapshot2.getValue());
                        listafiltroskeys.add("" + dataSnapshot2
                                .getKey());
                    }
                }

                configspinFiltro(listafiltros);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

    });

    }


    public void configspinFiltro(ArrayList<String> lista){

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerfiltro);
        final ArrayList<String> listafiltros = new ArrayList<>();

        listafiltros.addAll(lista);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_spinner_item, listafiltros);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) parent.getItemAtPosition(position);
               // load.setVisibility(View.VISIBLE);

                if (value.equals("Todos"))
                    listpromoHj("time", "");

                else

                if (position > 0) {

                    String fbusca = "";
                    String child = listafiltroskeys.get(position);

                    if (!value.equals("Todos")){
                       // load.setVisibility(View.VISIBLE);
                        engineSearch(value);

                }}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


    }


    public void getitensAutocomplet() {

        database.child("Catalogos").orderByChild("NomeProduto").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Item i = snapshot.getValue(Item.class);
                    for (int ii = 0; ii <itensAutoC.size();ii++){
                        if (itensAutoC.get(ii).equals(i.NomeProduto)){
                            ctrlnome=true;
                        }
                    }
                    for (int ii = 0; ii <itensAutoC.size();ii++){
                        if (itensAutoC.get(ii).equals(i.supermercado)){
                            ctrlb=true;
                        }
                    }
                    for (int ii = 0; ii <itensAutoC.size();ii++){
                        if (itensAutoC.get(ii).equals(i.marca)){
                            ctrlc=true;
                        }
                    }
                    for (int ii = 0; ii <itensAutoC.size();ii++){
                        if (itensAutoC.get(ii).equals(i.Tipo)){
                            ctrld=true;
                        }
                    }

                    if (ctrlnome==false)
                        itensAutoC.add("" + i.NomeProduto);
                    if (ctrlb==false)
                        itensAutoC.add(""+i.supermercado);
                    if (ctrlc==false)
                        itensAutoC.add(""+i.marca);
                    if (ctrld==false)
                        itensAutoC.add(""+i.Tipo);

                    ctrlnome = false;
                    ctrlb = false;
                    ctrlc = false;
                    ctrld = false;
                }


                autocompletsearch(itensAutoC);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }


    public void autocompletsearch(ArrayList<String> itens) {
        AutoCompleteTextView autoc = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        View v = findViewById(R.id.autoCompleteTextView);
        InputMethodManager imm = (InputMethodManager) getSystemService(c.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, itens);

        autoc = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        autoc.setAdapter(adapter);
        autoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                Object item = parent.getItemAtPosition(position);

            }

        });

        //bt softkey
        autoc.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i("action key", "Search");
                    palavrasb.clear();
                    AutoCompleteTextView autoc = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                    String txtupper = autoc.getText().toString();
                    String cap = txtupper.substring(0, 1).toUpperCase() +  txtupper.substring(1);
                    cap = deAccent(cap);
                    //load.setVisibility(View.VISIBLE);

                    engineSearch(cap);
                    //searchItem();
                    handled = true;
                }
                return handled;
            }
        });


    }


    public void searchItem() {

        AutoCompleteTextView autoc = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        String palavrabusca="";
        boolean palavrabuscactrl=false;


        if (autoc.getText().length() > 0 ) {

            if (palavrabuscactrl == false)
                for (int i = 0; i < autoc.getText().length(); i++) {

                    if (autoc.getText().charAt(i) == ' ') {
                        palavrasb.add(palavrabusca);
                        palavrabusca = "";
                        palavrabuscactrl = true;
                    }

                    if (autoc.getText().charAt(i) != ' ') palavrabusca += autoc.getText().charAt(i);


                }

            palavrasb.add(palavrabusca);

            String localrl = "buscaitem";
            String txtbusca = "";

            if (palavrasb.get(contbuscapalavra).length() >= 2) {

                txtbusca = deAccent(palavrasb.get(contbuscapalavra));
                stringbusca = txtbusca;
                Log.i("Search", "<<<<<<searchItem>>>>>" + txtbusca);

                ctrlbuscatxt = false;
                final String txt = String.valueOf(autoc.getText());
                final String txtlowercase = (txtbusca.toLowerCase());
                final String txtUppercase = txtlowercase.substring(0, 1).toUpperCase() + txtlowercase.substring(1);

                listpromoHj("NomeProduto",txtUppercase);
            }
        }


    }


    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }


    public void compartwhats() {

     if ( checkpermissao()==true){
        clickitemshare(itemclicado);
        Toast.makeText(c,"Compartilhando item",Toast.LENGTH_SHORT).show();
     }
    }


    public void clickitemshare(final Item item) {



        boolean ctrl = false;
        int cont = 0;
        double t = 0.0;

        String preco="";
        String precoDec="";


        TextView txtnome = (TextView) findViewById(R.id.textNomepshare);
        txtnome.setText("\n"+item.NomeProduto);

        TextView txtpreco = (TextView) findViewById(R.id.txtprecoitem2);

        TextView txtprecodec = (TextView) findViewById(R.id.txtdescip2);

        //CONVERT PRECO ITEM , - ./
        if (item.Preco.charAt(1) == '.') {

            preco = String.valueOf(item.Preco.charAt(0));
            precoDec = String.valueOf(item.Preco.charAt(2)) + String.valueOf(item.Preco.charAt(3));
            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60);
            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);

        } else if (item.Preco.charAt(2) == '.') {

            preco =
                    String.valueOf(item.Preco.charAt(0)) +
                            String.valueOf(item.Preco.charAt(1));
            precoDec = String.valueOf(item.Preco.charAt(3)) + String.valueOf(item.Preco.charAt(4));
            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60);
            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);

        } else if (item.Preco.charAt(3) == '.') {

            preco = String.valueOf(item.Preco.charAt(0)) + String.valueOf(item.Preco.charAt(1)) + String.valueOf(item.Preco.charAt(2));
            precoDec = String.valueOf(item.Preco.charAt(4)) + String.valueOf(item.Preco.charAt(5));
            txtpreco.setPadding(0, 0, 0, 0);
            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60);
            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        } else if (item.Preco.charAt(4) == '.') {

            txtpreco.setPadding(0, 0, 0, 0);
            preco = String.valueOf(String.valueOf(item.Preco.charAt(0)) + String.valueOf(item.Preco.charAt(1)) + String.valueOf(item.Preco.charAt(2)) + String.valueOf(item.Preco.charAt(3)));
            precoDec = String.valueOf(String.valueOf(item.Preco.charAt(5)) + String.valueOf(item.Preco.charAt(6)));

            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 58);
            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);

        } else {

            preco = String.valueOf(item.Preco);
            precoDec = ".00";

        }


        //edit text preço
        txtpreco.setText(preco + ",");
        txtprecodec.setText(precoDec);

        TextView txtDesc = (TextView) findViewById(R.id.txtdescipshare);
        txtDesc.setText(item.Descricao);


        TextView txtinfouni = (TextView) findViewById(R.id.txtinfouni2);
        txtinfouni.setText(item.unidade);

        final Calendar cal = Calendar.getInstance(); // Get Calendar Instance

        //GET DATA INICIO ITEM
        String dateInString = item.datai;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String dateInicio="";
        //FORMATO MES/DIA
        if (item.datai != null) {

            try {
                cal.setTime(sdf.parse(dateInString));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            sdf = new SimpleDateFormat("dd/MM");//FORMATO DIA/MES

            Date resultdateInit = new Date(cal.getTimeInMillis());//GET DATE EXP.
            dateInicio = sdf.format(resultdateInit);

            cal.add(Calendar.DATE, Integer.parseInt(item.expirar));  // add  days

            Date resultdateExp = new Date(cal.getTimeInMillis());//GET DATE EXP.
            String dateInexp = sdf.format(resultdateExp);

            TextView txtdatainfo = (TextView) findViewById(R.id.txtdatainfoshare);
            txtdatainfo.setText(item.supermercado);

        }

        Gson gson = new Gson();
        String json = "";
        json = gson.toJson(item.loja);
        ArrayList<String> listalo = new ArrayList<String>();

        ImageView iconIMGsup = (ImageView) findViewById(R.id.iconsupinfoitemshare);
        ImageView iconIMG = (ImageView) findViewById(R.id.imgshare);
        ImageView img = (ImageView) findViewById(R.id.imgIteminfo);
        iconIMG.setImageDrawable(img.getDrawable());

        for (int i =0; i < lojas.size(); i++){
            if (lojas.get(i).Nome.equals(item.supermercado))
                Glide.with(c).load(lojas.get(i).urlic).into(iconIMGsup);

        }

        sendshr();

    }


    public boolean checkpermissao(){

        boolean ctrl =false;
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE  )
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE  )
                != PackageManager.PERMISSION_GRANTED  ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {
            ctrl=true;

        }


        return ctrl;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 150: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void sendshr(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                RelativeLayout r = (RelativeLayout) findViewById(R.id.rlinfoitemshare);
                r.setVisibility(View.INVISIBLE);
                sendshare();
            }
        }, 100);

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


    //CLICK BT SUPERMERC
    public void getSupermercados() {

        database.child("PerfilLojas").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Gson gson = new Gson();
                String json = "";
                boolean ctrl=false;
                lojas.clear();

                if (snapshot != null){

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        Itemloja i = postSnapshot.getValue(Itemloja.class);
                        Log.i("super","super "+i.Nome);
                        lojas.add(i);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("getSupermercados", "getSupermercados databaseError" );

            }
        });


    }

    //
    public void clickitem(final Item item, int position) {

        itemclicado = item;
        moveinfo = true;
        clickitem=true;
        btncesta.setTextColor(0xFFFDC200);
        btncesta.setText("Adicionar á cesta");
        ctrlcesta=false;

        for (int i = 0;i<mycesta.size();i++){
            if (item.cod.equals(mycesta.get(i))){
                btncesta.setText("Remover da cesta");
                btncesta.setTextColor(0xffcc0000);
                ctrlcesta=true;

                Log.i("LOOP", "mycesta  " );

            }
        }


        rlinfoitem.setVisibility(View.VISIBLE);

        boolean ctrl = false;
        int cont = 0;
        double t = 0.0;
        String precoDec;
        String preco;


        txtnome.setText(item.NomeProduto);
        txttipo.setText(item.Tipo);

        //CONVERT PRECO ITEM , - ./
        if (item.Preco.charAt(1) == '.') {

            preco = String.valueOf(item.Preco.charAt(0));
            precoDec = String.valueOf(item.Preco.charAt(2)) + String.valueOf(item.Preco.charAt(3));
            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60);
            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);

        } else if (item.Preco.charAt(2) == '.') {

            preco =
                    String.valueOf(item.Preco.charAt(0)) +
                            String.valueOf(item.Preco.charAt(1));
            precoDec = String.valueOf(item.Preco.charAt(3)) + String.valueOf(item.Preco.charAt(4));
            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60);
            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);

        } else if (item.Preco.charAt(3) == '.') {

            preco = String.valueOf(item.Preco.charAt(0)) + String.valueOf(item.Preco.charAt(1)) + String.valueOf(item.Preco.charAt(2));
            precoDec = String.valueOf(item.Preco.charAt(4)) + String.valueOf(item.Preco.charAt(5));
            txtpreco.setPadding(0, 0, 0, 0);
            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60);
            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        } else if (item.Preco.charAt(4) == '.') {

            txtpreco.setPadding(0, 0, 0, 0);
            preco = String.valueOf(String.valueOf(item.Preco.charAt(0)) + String.valueOf(item.Preco.charAt(1)) + String.valueOf(item.Preco.charAt(2)) + String.valueOf(item.Preco.charAt(3)));
            precoDec = String.valueOf(String.valueOf(item.Preco.charAt(5)) + String.valueOf(item.Preco.charAt(6)));

            txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 58);
            txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);

        } else {

            preco = String.valueOf(item.Preco);
            precoDec = ".00";

        }


        //edit text preço
        txtpreco.setText(preco + ",");
        txtprecodec.setText(precoDec);

        txtDesc.setText(item.Descricao);

        txtsuper.setText("" + item.supermercado);

        txtinfouniItem.setText(item.unidade);

        final Calendar cal = Calendar.getInstance(); // Get Calendar Instance

        //GET DATA INICIO ITEM
        String dateInString = item.datai;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String dateInicio="";
        //FORMATO MES/DIA
        if (item.datai != null) {

            try {
                cal.setTime(sdf.parse(dateInString));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            sdf = new SimpleDateFormat("dd/MM");//FORMATO DIA/MES

            Date resultdateInit = new Date(cal.getTimeInMillis());//GET DATE EXP.
            dateInicio = sdf.format(resultdateInit);

            cal.add(Calendar.DATE, Integer.parseInt(item.expirar));  // add  days

            Date resultdateExp = new Date(cal.getTimeInMillis());//GET DATE EXP.
            String dateInexp = sdf.format(resultdateExp);
            txtdatainfoItem.setText( item.supermercado);


        }

        Gson gson = new Gson();
        String json = "";
        json = gson.toJson(item.loja);
        ArrayList<String> listalo = new ArrayList<String>();

        int pos = 0;
        for (int i =0 ;i<lojas.size();i++){
            if (lojas.get(i).Nome.equals(item.supermercado)){
                pos = i;
            }
        }

        Glide.with(c).load(lojas.get(pos).urlic).into(iconsupinfoitem);
        final int finalPosition = pos;
        iconsupinfoitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PerfilLoja(lojas.get(finalPosition));
                clickperfil=true;

            }
        });


        Glide.with(c).load(item.urlimg).into(iconItem);


    }

    public void saveclickperfil(Itemloja loja){

        final String time = ""+new Date().getTime();
        String idClient = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);

        database.child("Central_Clicks").child("PerfilLoja_Click").child(loja.Nome).child(idClient).child(time).setValue(time).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    ///////////////////////////////////////////////
    //CONFIG PERFIL LOJA///////////////////////////
    public void PerfilLoja(final Itemloja loja){
        saveclickperfil(loja);
        getid(loja);

        getloja=loja;

        final TextView dist = (TextView) w.findViewById(R.id.txtendloja);

        try {
            if (logdistances.size() > 0) {
                for (int l = 0; l < lojas.size(); l++) {
                    if (lojas.get(l).Nome.equals(loja.Nome)) {
                        Double d = (logdistances.get(l));
                        Double df = (logdistances.get(l) / 1000);
                        if (d >= 1000)
                            dist.setText("Distância: " + String.format("%.2f", df) + " km");
                            else if (d < 1000)
                            dist.setText("Distância: " + String.format("%.2f", d) + " m");
                    }
                }
            } else {
                dist.setText("");
            }

        }catch (Exception e){

        }

        ImageView  tagcred = (ImageView) w.findViewById(R.id.checkcred);
        ImageView  tagdel = (ImageView)  w.findViewById(R.id.checkdev);
        ImageView  tagdin = (ImageView)  w.findViewById(R.id.checkdin);

        tagdin.setImageResource(R.drawable.check);

        if (loja.delivery==true && loja.status==true)
            tagdel.setImageResource(R.drawable.check);
        else
            tagdel.setImageResource(R.drawable.checkoff);

        Log.i("credito","credito "+loja.Nome + " "+loja.credito);

        if (loja.credito==true)
            tagcred.setImageResource(R.drawable.check);
        else
            tagcred.setImageResource(R.drawable.checkoff);

        ///////////////////////  //
        if (loja.status==true) {

            if (loja.call != null) {
                Button btnligarloja = (Button) w.findViewById(R.id.btnligarloja);
                btnligarloja.setVisibility(View.VISIBLE);
                btnligarloja.setBackgroundResource(R.drawable.call);
            }else {
                Button btnligarloja = (Button) w.findViewById(R.id.btnligarloja);
                btnligarloja.setBackgroundResource(R.drawable.calloff);

            }

            Button btnchat = (Button) w.findViewById(R.id.btnchat);
            btnchat.setVisibility(View.VISIBLE);
            btnchat.setBackgroundResource(R.drawable.chat);

            btnchat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("BTNCHAT","BTNCHAT");
                    boolean ctl=false;
                    for(int i =0;i<deliveryOn.size();i++){
                        if (loja.Nome.equals(deliveryOn.get(i))){
                            ctl=true;
                        }
                    }
                    if (ctl==true){
                        lojachat = loja;
                        chatconfig(lojachat,c);
                    }else {
                        Toast.makeText(c,"A loja ficou offline :/",Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {

            Button rlchat = (Button) w.findViewById(R.id.btnchat);
            rlchat.setBackgroundResource(R.drawable.chatoff);

            Button rl = (Button) w.findViewById(R.id.btnligarloja);
            rl.setBackgroundResource(R.drawable.calloff);

        }

        layoutitem.setVisibility(View.INVISIBLE);

        layoutloja.setVisibility(View.VISIBLE);

        txtNomeLoja.setText("" + loja.Nome);

        txtdescricaoloja.setText("" + loja.Descricao);

        final ArrayList<String> listainscritos = new ArrayList<>();

        database.child("Topicos").child("Lojas").child(loja.Nome).child("inscritos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            listainscritos.add(String.valueOf(postSnapshot.getValue()));
                        }
                        totalinsc.setText("Total de inscritos: " +listainscritos.size());
                        perfilconfig(loja,listainscritos);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                }
        });

    }

    public void getdeliverys(){

        final boolean[] n = {false};
        database.child("DeliveryOn")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        deliveryOn.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            deliveryOn.add(String.valueOf(postSnapshot.getKey()));
                            Log.i("DEL","DEL "+String.valueOf(postSnapshot.getKey())+" "+deliveryOn.size());
                        }

                        if (n[0] ==false) {
                            listpromoHj("time", "");
                            n[0] =  true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    public void perfilconfig(final Itemloja loja, ArrayList<String> listainscritos){


        final String time = ""+new Date().getTime();



        btncadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consloadwindow.setVisibility(View.VISIBLE);
                login(loja);
            }
        });


        btnrecsenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = ""+String.valueOf(edtemail.getText());
                if (email != ""){
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("rec senha", "Email sent.");
                                        Toast.makeText(c,"EMAIL ENVIADO PARA "+ email,Toast.LENGTH_LONG).show();
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(c,"Falha, usuário não cadastrado "+ email,Toast.LENGTH_LONG).show();
                        }
                    });}
                    else{
                    Toast.makeText(c,"Digite seu email",Toast.LENGTH_LONG).show();
                }
            }
        });

        BTNCLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlpopperfil.setVisibility(View.INVISIBLE);
            }
        });

        ctrlbttopic=false;

        if (iduser!=null)
        for(int ii = 0; ii < listainscritos.size();ii++) {

            if (myUser!=null)
            if (listainscritos.get(ii).equals(iduser))
            {
                btntopico.setBackgroundResource(R.drawable.star);
                ctrlbttopic=true;
            }
        }

        if (ctrlbttopic == false){
            btntopico.setBackgroundResource(R.drawable.staroff);
        }

        Log.i("INSCRITO ","INSCRITO "+listainscritos.size());

        btntopico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consloadwindow.setVisibility(View.VISIBLE);
                if (ctrlbttopic ==false){
                    if (myUser == null) {
                        rlpopperfil.setVisibility(View.VISIBLE);
                        consloadwindow.setVisibility(View.INVISIBLE);
                    }else {
                        checkid(loja);
                    }
                }else
                if (ctrlbttopic==true){
                    checkid(loja);

                }
            }
        });

        Glide.with(c).load(loja.urlic).into(imglogo);
        imgbaner .setImageResource(0);
        database.child("PerfilLojas").child(loja.cod+"").child("urlBanner").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    Glide.with(c).load(dataSnapshot.getValue()).into(imgbaner);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listB = new FirebaseListAdapter<Item>(c, Item.class, R.layout.listhj,database.child("Catalogos").orderByChild("supermercado").equalTo(""+loja.Nome)) {

            @SuppressLint("WrongConstant")
            @Override
            protected void populateView(final View vw, final Item model, final int position) {

                vw.setBackgroundColor(0xefefef);
                boolean ctral = false;

//                counterfotos = listB.getCount();
//
//                if (vw.getMeasuredHeight()>0) {
//                    ViewGroup.LayoutParams params = listsuper.getLayoutParams();
//                    totalHeight = vw.getMeasuredHeight() * (listB.getCount());
//                    params.height = totalHeight;
//                    listsuper.setLayoutParams(params);
//                    listsuper.requestLayout();
//                }
                Item it = model;

                TextView txtnome = (TextView) vw.findViewById(R.id.nomeItem);
                TextView txtDesc = (TextView) vw.findViewById(R.id.descricao);
                TextView textsuperm = (TextView) vw.findViewById(R.id.superm);

                textsuperm.setText(model.supermercado);
                txtDesc.setText(model.Descricao);
                txtnome.setText(model.NomeProduto);

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
                txtsuperm.setText( model.supermercado);

                if (model.corsuper != null) {
                    int corsuper = Color.parseColor("" + model.corsuper);
                    txtsuperm.setTextColor(corsuper);
                }

                ImageView icon = (ImageView) vw.findViewById(R.id.iconItemdash);
                Glide.with(c).load(model.urlic).into(icon);
                vw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickitem(model,position);

                    }
                });


            }
        };
        listsuper.setAdapter(listB);


        localmaps(loja);
    }


    public void inscrever(final Itemloja loja){


        FirebaseMessaging.getInstance().subscribeToTopic(loja.topicontf).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database.child("UsersClient").child(myUser.Uid).child("topicos").child(loja.Nome).setValue(loja.Nome).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.child("Topicos").child("Lojas").child(loja.Nome).child("inscritos").child(iduser).setValue(iduser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Inscrito em " + loja.Nome, Toast.LENGTH_LONG).show();
                                btntopico.setBackgroundResource(R.drawable.star);
                                consloadwindow.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(c, "Desculpe, ocorreu um erro, tente novamente", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void desinscrever(final Itemloja loja){


        String email="";

        final String finalEmail = email;
        FirebaseMessaging.getInstance().unsubscribeFromTopic(loja.topicontf).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database.child("UsersClient").child(myUser.Uid).child("topicos").child(loja.Nome).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.child("Topicos").child("Lojas").child(loja.Nome).child("inscritos").child(iduser).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Tópico removido: " + loja.Nome, Toast.LENGTH_LONG).show();
                                btntopico.setBackgroundResource(R.drawable.staroff);
                                consloadwindow.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(c, "Desculpe, ocorreu um erro, tente novamente", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveid(final Itemloja loja){
        Random r = new Random();
        int rdn = r.nextInt(999999999 -  100000000) + 100000000;
        iduser = new Date().getTime()+""+rdn+"";
        database.child("UsersClient").child(myUser.Uid).child("iduser").setValue(iduser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (loginsimples==false)  inscrever(loja);
            }

        });

    }

    public void getid(final Itemloja loja) {


        if (myUser!=null)
        database.child("UsersClient").child(myUser.Uid).child("iduser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()!= null)
                    iduser = dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void checkid(final Itemloja loja) {

        database.child("UsersClient").child(myUser.Uid).child("iduser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    iduser = dataSnapshot.getValue().toString();

                    if (ctrlbttopic == true) {
                        desinscrever(loja);
                        ctrlbttopic = false;

                    } else if (ctrlbttopic == false) {
                        inscrever(loja);
                        ctrlbttopic = true;
                    }
                }else
                {
                    saveid(loja);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void setUser(final Itemloja loja){
        String email ="";
        String password ="";
        if (loginsimples==false) {
            email = Temail.getText().toString();
            password = Tpsw.getText().toString();
        }else {
            email = edtemailcaduser.getText().toString();
            password = edtpwscad.getText().toString();

        }


        if (password.length() >0 && email.length() > 0   ) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("", "createUserWithEmail:success");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                myUser = new User("",user.getUid(),user.getEmail(),null,null,"","FREEN");
                                saveDadosUser(loja);
                                if (loginsimples==true)getchat();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("setuser", "setuser "+ task.getException().getMessage());

                                if (task.getException().getMessage() == "The email address is already in use by another account."){
                                    Toast.makeText(c, "Este EMAIL ja está cadastrado! ",
                                            Toast.LENGTH_SHORT).show();
                                    consloadwindow.setVisibility(View.INVISIBLE);
                                }
                            }

                            // ...
                        }
                    });
        }else {Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();
            consloadwindow.setVisibility(View.INVISIBLE);}
    }


    public void saveDadosUser( final Itemloja loja) {
        final String time = ""+new Date().getTime();
        consloadwindow.setVisibility(View.VISIBLE);
        database.child("UsersClient").child(myUser.Uid).setValue(myUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                consloadwindow.setVisibility(View.INVISIBLE);
                rlpop.setVisibility(View.INVISIBLE);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastrouser);
                rl.setVisibility(View.INVISIBLE);
              saveid(loja);
            }

        });
    }


    public void login(final Itemloja loja){
        String email ="";
        String password ="";
        if (loginsimples==false) {
            email = Temail.getText().toString();
            password = Tpsw.getText().toString();
        }else {
            email = edtemailcaduser.getText().toString();
            password = edtpwscad.getText().toString();

        }
        if (password.length() > 0 && email.length() > 0  ) {

             FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                rlpop.setVisibility(View.INVISIBLE);
                                if (user != null) {
                                    myUser = new User("",user.getUid(),user.getEmail(),null,null,"","FREEN");
                                }
                                if (loginsimples==false)   {  checkid(loja);getchat();}
                                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastrouser);
                                rl.setVisibility(View.INVISIBLE);


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("LOGIN",""+task.getException().getMessage());
                                if ( task.getException().getMessage() =="There is no user record corresponding to this identifier. The user may have been deleted.")
                                {
                                    setUser(loja);
                                }else

                                if ( task.getException().getMessage() =="The password is invalid or the user does not have a password.")
                                    Toast.makeText(c, "Senha incorreta.",
                                            Toast.LENGTH_SHORT).show();

                                else
                                    Toast.makeText(c, "Ocorreu um erro.",
                                            Toast.LENGTH_SHORT).show();

                                consloadwindow.setVisibility(View.INVISIBLE);

                            }


                            // ...
                        }
                    });
        }else {
            Toast.makeText(c, "Campos incompletos...",
                    Toast.LENGTH_SHORT).show();

            consloadwindow.setVisibility(View.INVISIBLE);
        }

    }


    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 0, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                }).show();
            }
            else {Toast.makeText(this, "This app can not run on this device", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }


    static void getloja(final Empresa loja){
        database.child("PerfilLojas").child(""+loja.Id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(snapshot.exists()){
                Itemloja LOJA = snapshot.getValue(Itemloja.class);
                myPerfil=LOJA;


                if (myPerfil.status==true) {
                    ctrlmenustatus=true;

                    Log.i("status ","status "+myPerfil.status);
                    final Menu menu = navigationView.getMenu();
                    menu.findItem(R.id.nav_status).setTitle("ONLINE");

                    menu.findItem(R.id.nav_status)
                            .setIcon(ContextCompat.getDrawable(c, R.drawable.status));
                    Drawable drawable = menu.findItem(R.id.nav_status).getIcon();
                    drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);

                }else{

                    ctrlmenustatus=false;

                    mNotificationManager.cancel(333);

                    final Menu menu = navigationView.getMenu();
                    menu.findItem(R.id.nav_status).setTitle("OFFLINE");
                    menu.findItem(R.id.nav_status)
                            .setIcon(ContextCompat.getDrawable(c, R.drawable.statusoff));
                    Drawable drawable = menu.findItem(R.id.nav_status).getIcon();
                    drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);

                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getClient(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersClient")
                .child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("getClient","getClient ");
                myUser = new User("",user.getUid(),user.getEmail(),null,null,"","FREEN");;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getpedidos(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database  .child("UsersClient").child(user.getUid()).child("Pedidos").orderByChild("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    ctrlpedidos=true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void authentic(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            myUser = new User("",user.getUid(),user.getEmail(),null,null,"","FREEN");
            Button btnChat= (Button) findViewById(R.id.btnchat);
            btnChat.setBackgroundResource(R.drawable.comment);

            FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersEmpresa")
                    .child(myUser.Uid).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Itemloja tlj = dataSnapshot.getValue(Itemloja.class);
                    myEmpresa = dataSnapshot.getValue(Empresa.class);

                    if (dataSnapshot.exists()){

                        Log.i("LOJA","myLOJA "+tlj.Nome);
                        nomeuser="LOJA";
                        Chat chat = new Chat();
                        ImageView icon =(ImageView) findViewById(R.id.imgchatbar);
                        chat.getMychatnewmsg(tlj.Nome,icon);
                        final Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.nav_status).setVisible(true);
                        menu.findItem(R.id.nav_delivery).setVisible(true);
                        menu.findItem(R.id.nav_mypedidos).setVisible(false);

                        getloja(myEmpresa);
                        myloja = tlj;
                        getCesta();
                        barBottom();

                        getdeliverys();

                    }else {

                        NavigationView   navigationView = (NavigationView) findViewById(R.id.nav_view);
                        Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.nav_status).setVisible(false);
                        menu.findItem(R.id.nav_delivery).setVisible(false);
                        menu.findItem(R.id.nav_mypedidos).setVisible(true);

                        getchatoff();
                        getpedidos();

                        Log.i("LOJA","myUser "+myUser.Email);
                        nomeuser="user";
                        getClient();
                        getchat();
                        getCesta();
                        barBottom();

                        getdeliverys();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            gettopicsisncritos();
        }else{
            nomeuser="user";
            ImageView btnChat= (ImageView) findViewById(R.id.imgchatbar);
            btnChat.setBackgroundResource(R.drawable.commentoff);
            barBottom();
        }
    }



    public void localmaps(Itemloja loja){



        try {
            JSONArray ob = new JSONArray(String.valueOf(loja.coordenadas));
            for (int i = 0; i < ob.length(); i++) {

                Double lt = (Double) ob.getJSONObject(i).get("latitude");
                Double lg = (Double) ob.getJSONObject(i).get("longitude");

                try {
                    LatLng local = new LatLng(lt, lg);
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lt, lg), 16));
                    mapa.addMarker(new MarkerOptions()
                            .title("" + loja.Nome)
                            .position(local)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmarkercart))
                    );
                }catch (Exception e){

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //CONFIG PERFIL LOJA FINAL
    ///////////////////////////////////////////////
    ///////////////////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setBuildingsEnabled(true);
    }

    public void sendshare(){

        Random r = new Random();

        int i1 = r.nextInt(8000000 - 99) + 99;

        RelativeLayout content = (RelativeLayout)findViewById(R.id.rlinfoitemshare);
//        content.layout(0, 0, content.getMeasuredWidth(), content.getMeasuredHeight());
//        Bitmap bmap = content.getDrawingCache();

        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        content.measure(spec, spec);
        content.layout(0, 0, content.getMeasuredWidth(),
                content.getMeasuredHeight() );

        Bitmap b = Bitmap.createBitmap(content.getWidth(), content.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(b);
        can.translate(-content.getScrollX(), -content.getScrollY());
        content.draw(can);
        content.setDrawingCacheEnabled(true);

        Bitmap adv =b;

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        adv.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + i1+"share.png");

        try {
            f.createNewFile();
            new FileOutputStream(f).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse( Environment.getExternalStorageDirectory()+ File.separator+ i1+"share.png"));
        if(isPackageInstalled("com.whatsapp",this)){
            share.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(share, "Share Image"));

        }else{

            Toast.makeText(getApplicationContext(), "Please Install Whatsapp", Toast.LENGTH_LONG).show();
        }

    }


    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private void listpromoHj(String child, String value) {

        listitem = (ListView) findViewById(R.id.listapromohj);
        listitem.setDivider(null);
        listitem.setDividerHeight(0);
        load.setVisibility(View.INVISIBLE);

        if (lat!=null && lng !=null) {

        }else{
            getdist();
        }

        database.child("Catalogos").orderByChild(child).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Item it = new Item();
                it = snapshot.getValue(Item.class);


                RelativeLayout ltload = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                ltload.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        String precoitem = "";

        if  (value.length()>0){


         feed = new FirebaseListAdapter<Item>(c, Item.class, R.layout.listhj,database.child("Catalogos").orderByChild(child).startAt(value).endAt(value+ "\uf8ff") ) {

            @SuppressLint("WrongConstant")
            @Override
            protected void populateView(final View vw, final Item model, final int position) {

                vw.setBackgroundColor(0xefefef);
                boolean ctral = false;
                Item it = model;

                TextView txtnome = (TextView) vw.findViewById(R.id.nomeItem);
                TextView txtDesc = (TextView) vw.findViewById(R.id.descricao);
                TextView textsuperm = (TextView) vw.findViewById(R.id.superm);

                TextView dist = (TextView) vw.findViewById(R.id.txtdist);
                if (logdistances.size()>0){
                    for (int l=0;l<lojas.size();l++){
                    if (lojas.get(l).Nome.equals(model.supermercado)){
                        Double d =  (logdistances.get(l));
                        Double df =  (logdistances.get(l)/1000);
                        Log.i("DIST","DIST X "+logdistances.get(l));
                        if(d>=1000)
                            dist.setText("Distância: " + String.format("%.2f", df)+" km");
                        else
                        if(d<1000)
                            dist.setText("Distância: " + String.format("%.2f", d)+" m");
                    }
                }
                }else{
                    dist.setText("");
                }

                textsuperm.setText(model.supermercado);
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



                final ImageView deliveryon = (ImageView) vw.findViewById(R.id.deliveryOn);
                deliveryon.setVisibility(View.INVISIBLE);

                for (int i = 0; i < deliveryOn.size(); i++){

                    Log.i("DEL","DEL ON");

                    if (deliveryOn.get(i).equals(model.supermercado)){
                        deliveryon.setVisibility(View.VISIBLE);

                    }

                }


                //edit text preço
                txtpreco.setText(preco + ",");
                txtprecodec.setText(precoDec);

                TextView txttipo = (TextView) vw.findViewById(R.id.unidade);
                txttipo.setText("" + model.unidade);

                TextView txtsuperm = (TextView) vw.findViewById(R.id.superm);
                txtsuperm.setText( model.supermercado);

                if (model.corsuper != null) {
                    int corsuper = Color.parseColor("" + model.corsuper);
                    txtsuperm.setTextColor(corsuper);
                }

                ImageView icon = (ImageView) vw.findViewById(R.id.iconItemdash);
                Glide.with(c).load(model.urlic).into(icon);
                vw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickitem(model,position);
                    }
                });


            }
        };
        listitem.setAdapter(feed);
        }else {
            feed = new FirebaseListAdapter<Item>(this, Item.class, R.layout.listhj, database.child("Catalogos").orderByChild(child)) {

                @SuppressLint("WrongConstant")
                @Override
                protected void populateView(final View vw, final Item model, final int position) {

                    vw.setBackgroundColor(0xefefef);
                    boolean ctral = false;
                    Item it = model;

                    TextView txtnome = (TextView) vw.findViewById(R.id.nomeItem);
                    TextView txtDesc = (TextView) vw.findViewById(R.id.descricao);
                    TextView textsuperm = (TextView) vw.findViewById(R.id.superm);

                    textsuperm.setText(model.supermercado );
                    txtDesc.setText(model.Descricao);
                    txtnome.setText(model.NomeProduto);

                    TextView dist = (TextView) vw.findViewById(R.id.txtdist);
                    try {
                        if (logdistances.size() > 0)
                            for (int l = 0; l < lojas.size(); l++) {
                                if (lojas.get(l).Nome.equals(model.supermercado)) {
                                    Double d = (logdistances.get(l));
                                    Double df = (logdistances.get(l) / 1000);
                                    Log.i("DIST", "DIST X " + logdistances.get(l));
                                    if (d >= 1000)
                                        dist.setText("Distância: " + String.format("%.2f", df) + " km");
                                    else if (d < 1000)
                                        dist.setText("Distância: " + String.format("%.2f", d) + " m");
                                }
                            }
                        else {
                            ctrlgps = true;
                            startgps();
                        }
                    }catch (Exception e){

                    }

                    final ImageView deliveryon = (ImageView) vw.findViewById(R.id.deliveryOn);
                    deliveryon.setVisibility(View.INVISIBLE);

                    for (int i = 0; i < deliveryOn.size(); i++){

                        Log.i("DEL","DEL ON");

                        if (deliveryOn.get(i).equals(model.supermercado)){
                            deliveryon.setVisibility(View.VISIBLE);

                        }

                    }

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
                        sdf = new SimpleDateFormat("dd/MM/yyyy");//FORMATO DIA/MES
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
                    txtsuperm.setText( model.supermercado );

                    if (model.corsuper != null) {
                        int corsuper = Color.parseColor("" + model.corsuper);
                        txtsuperm.setTextColor(corsuper);
                    }

                    ImageView icon = (ImageView) vw.findViewById(R.id.iconItemdash);
                    Glide.with(c).load(model.urlic).into(icon);

                    vw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickitem(model,position);

                        }
                    });
                }
            };
            listitem.setAdapter(feed);

        }


    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (ctrlchat==true){
                RelativeLayout rlinfoitem = (RelativeLayout) findViewById(R.id.rlchat);
                rlinfoitem.setVisibility(View.INVISIBLE);
                ctrlchat=false;
            }else
            if (clickperfil==true){
                RelativeLayout rlinfoitem = (RelativeLayout) findViewById(R.id.layoutLoja);
                rlinfoitem.setVisibility(View.INVISIBLE);
                clickperfil=false;
            }else
            if (clickitem==true){
                RelativeLayout rlinfoitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
                rlinfoitem.setVisibility(View.INVISIBLE);
                clickitem=false;
            }else
                if (ctrlfav==true){
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlfav);
                rl.setVisibility(View.INVISIBLE);
                ctrlfav=false;
            }
            else{
                super.onBackPressed();
            }
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        ctrlservice=true;

        if (id == R.id.nav_camera) {

        }
            if (id == R.id.nav_status) {
                Toast.makeText(c,"preparando...",Toast.LENGTH_LONG).show();
                if (ctrlmenustatus==false){
                    final FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();
                    database.child("DeliveryOn").child(myPerfil.Nome).child("status").setValue("on").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(c,"Loja online.",Toast.LENGTH_LONG).show();
                            database.child("PerfilLojas").child(myEmpresa.Id).child("status").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("UsersEmpresa")
                                            .child(user.getUid()).child("ntfOn").setValue(ServerValue.TIMESTAMP);

                                    item .setIcon(ContextCompat.getDrawable(c, R.drawable.status));
                                    Drawable drawable = item.getIcon();
                                    drawable.setColorFilter(0xFFFFFF, PorterDuff.Mode.SRC_ATOP);
                                    ctrlmenustatus=true;
                                    Chat chat = new Chat();
                                    chat.setUID_LOJA(myPerfil);
                                }
                            });
                        }
                    });
                }else {

                    database.child("DeliveryOn").child(myEmpresa.Nome).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c,"Loja offline.",Toast.LENGTH_LONG).show();
                                item .setIcon(ContextCompat.getDrawable(c, R.drawable.statusoff));
                                Drawable drawable = item.getIcon();
                                drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);
                                ctrlmenustatus=false;

                                database.child("PerfilLojas").child(myEmpresa.Id).child("status").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        final FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();

                                        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("UsersEmpresa")
                                                .child(user.getUid()).child("ntfOn").removeValue();
                                    }
                                });
                            }
                        });
                    }

            }
             else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity.this ,
                    MainActivity_lojas.class);
            MainActivity.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity.this ,
                    MainActivity_categorias.class);

            MainActivity.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity.this ,
                    MainActivity_topicos.class);

            MainActivity.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity.this ,
                    MainActivity_contato.class);

            MainActivity.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity.this ,
                    MainActivity_empresa.class);

            MainActivity.this.startActivity(intentMain);
        }
        else if (id == R.id.nav_cesta) {
            if (myUser!=null && mycesta.size()>0){
            Intent intentMain = new Intent(MainActivity.this ,
                    MainActivity_cesta.class);

            MainActivity.this.startActivity(intentMain);
            }
            else{
                    Toast.makeText(c,"Cesta vazia",Toast.LENGTH_SHORT).show();
            }
         }  else if (id == R.id.nav_mypedidos) {
                Intent intentMain = new Intent(MainActivity.this ,
                        MainActivity_pedidos.class);

                MainActivity.this.startActivity(intentMain);
            }
            else if (id == R.id.nav_delivery) {
                Intent intentMain = new Intent(MainActivity.this ,
                        MainActivity_delivery.class);
                MainActivity.this.startActivity(intentMain);
            }
        //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void openWhatsappContact(String number) {
        PackageManager pm=getPackageManager();
        try {

            String toNumber = number; // Replace with mobile phone number without +Sign or leading zeros, but with country code.
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:" + "" + toNumber + "?body=" + ""));
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"it may be you dont have whats app",Toast.LENGTH_LONG).show();

        }
    }


    static void setLocal(LatLng local){

        try {
            load.setVisibility(View.INVISIBLE);
            lat = local.latitude;
            lng = local.longitude;
            getdist();

            if (ctrlgps == true) {
                feed.notifyDataSetChanged();
                ctrlgps = false;
            }
        }catch (Exception e){

        }
    }

    static void getdist(){

        logdistances.clear();
        localloja.clear();

        final ArrayList<Double> difdist = new ArrayList<>();

        if (lat!=null && lng !=null) {

            Double glt = lat;
            Double glg = lng;

            LatLng local = new LatLng(glt, glg);
            Log.i("DISTANC", "DISTANC lc " + glt);

            for (int ii = 0; ii < lojas.size(); ii++) {
                try {
                    JSONArray ob = new JSONArray(String.valueOf(lojas.get(ii).coordenadas));
                    Double lt = (Double) ob.getJSONObject(0).get("latitude");
                    Double lg = (Double) ob.getJSONObject(0).get("longitude");
                    double dist = SphericalUtil.computeDistanceBetween(local, new LatLng(lt, lg));
                    logdistances.add(dist);
                    Log.i("DISTANC", "DISTANC " + lt + "  " + lg);
                    localloja.add(new LatLng(lt, lg));

                } catch (Exception e) {

                }
            }

            Double[] itemDist = logdistances.toArray(new Double[logdistances.size()]);
            Arrays.sort(itemDist, new Comparator<Double>() {
                public int compare(Double a, Double b) {
                    int xComp = Double.compare(a, b);
                    return xComp;
                }
            });

            for (Double s : itemDist) {
                difdist.add(s);
            }

        }

    }

    public void engineSearch(final String textbusca){

        if(lat!=null)
            load.setVisibility(View.INVISIBLE);

        final ArrayList<Item> itens = new ArrayList<>();
        final ArrayList<Item> itensdist = new ArrayList<>();
        final ArrayList<String> lojadist = new ArrayList<>();
        final ArrayList<Double> difdist = new ArrayList<>();

//        logdistances.clear     ();
//        localloja.clear();
        ctrlbusca=false;

        if (lat!=null && lng !=null) {

        }else{
            //getdist();
        }

        listitem = (ListView) findViewById(R.id.listapromohj);
        final adapterBusca adapter = new adapterBusca(c, R.layout.listhj, itens);
        listitem.setAdapter(adapter);
        cont=0;
        ctrlbuscafinal=false;

        database.child("Catalogos").orderByChild("time").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              if (dataSnapshot.getValue() != null) {

                  for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                      final Item i = postSnapshot.getValue(Item.class);

                      if (i.tags != null)
                          database.child("Catalogos").child(postSnapshot.getKey()).child("tags").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

                              @Override
                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                  for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                      Log.i("busca", "Busca " + postSnapshot.getValue().toString().indexOf(textbusca));

                                      if (ctrlbusca == false) {

                                          if (postSnapshot.getValue().toString().indexOf(textbusca) != -1) {
                                              ctrlbusca = true;
                                              ctrlbuscafinal = true;
                                              itens.add(i);
                                          } else {

                                              String t = textbusca;
                                              for (int ii = 0; ii < 2; ii++) {
                                                  t = t.substring(0, t.length() - 1);
                                                  if (postSnapshot.getValue().toString().indexOf(t) != -1) {
                                                      ctrlbusca = true;
                                                      ctrlbuscafinal = true;
                                                      itens.add(i);
                                                      break;
                                                  }

                                              }

                                          }

                                      }

                                  }

                                  int positloja = 0;

//                              }
                                  ctrlbusca = false;
                                  adapter.notifyDataSetChanged();

                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError databaseError) {
                              }
                          });



                  }

              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void gettopicsisncritos(){
        database.child("UsersClient").child(myUser.Uid).child("topicos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lojasfav.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    nomelojatopc = postSnapshot.getValue().toString();
                    lojasfav.add(nomelojatopc);

                    database.child("PerfilLojas").orderByChild("Nome").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                Itemloja loja = postSnapshot.getValue(Itemloja.class);
                            if (loja.Nome.equals(nomelojatopc)){
                                FirebaseMessaging.getInstance().subscribeToTopic(loja.topicontf);
                                Log.i("TOPIC","TOPIC "+nomelojatopc+ " "+loja.topicontf);
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


    public void startgps(){

        if (servcGPS != null)
            startService(servcGPS);
    }


    public LatLng getLocalUser(){
        Double latL = locationService.getLatitude();
        Double lngL = locationService.getLongitude();
        return new LatLng(latL,lngL);
    }


    public void chatconfig(Itemloja loja , Context ctx ){

        if (myUser!=null){
        try {
            Log.i("BACK","BACK chatconfig");

            Chat chat = new Chat();
            chat.sendStartChat(loja);

            ctrlservice=true;

            Intent intentMain = new Intent(MainActivity.this,
                    MainActivity_chat.class);
            intentMain.putExtra("Chat", loja.Nome);
            MainActivity.this.startActivity(intentMain);

        }catch (Exception e){
            Log.i("chatconfig","chatconfig E "+e.getMessage());


        try{
            Chat chat = new Chat();
            chat.sendStartChat(loja);


            Intent intentMain = new Intent(MainActivity.this,
                    MainActivity_chat.class);
            intentMain.putExtra("Chat", loja.Nome);
            ctrlservice=true;

            MainActivity.this.startActivity(intentMain);
        }catch (Exception ex){
        }
        }

        }
        else {
            Toast.makeText(c,"Necessário login", Toast.LENGTH_SHORT).show();
            loginsimples=true;
            rlpopcad
            .setVisibility(View.VISIBLE);
        }

    }

    public void getchatoff(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }

        final String finalEmail = email;
        final String finalEmail1 = email;
        database.child("Chat")
                .child("chatoff").child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot msgsdata : dataSnapshot.getChildren()){

                    database.child("UsersClient")
                            .child(user.getUid()).child("chat").child("lojasOn").child(String.valueOf(msgsdata.getValue())).setValue(msgsdata.getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            database.child("Chat").child(msgsdata.getValue().toString())
                                    .child(finalEmail).child("UIDUSER").setValue(user.getEmail())
                                    .  addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            database.child("Chat")
                                                    .child("chatoff").child(finalEmail1).removeValue().
                                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                        }
                                                    });
                                        }
                                    });


                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }


    public void barBottom(){


        View  vfav = (View) findViewById(R.id.includechatmain);

        Button btncasas= (Button) vfav.findViewById(R.id.btncasas);
        btncasas.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           ctrlservice=true;

                                           Intent intentMain = new Intent(MainActivity.this ,
                                                   MainActivity_lojas.class);
                                           MainActivity.this.startActivity(intentMain);
                                       }

                                   });


        final Button btnfav= (Button) vfav.findViewById(R.id.btnfav);
        ImageView imgfav = (ImageView) vfav.findViewById(R.id.imgfav);
        ImageView imgcestabar = (ImageView) vfav.findViewById(R.id.imgcestabar);

        if (myloja != null) {

            imgfav.setBackgroundResource(R.drawable.enviandonav);
            TextView txt = (TextView) findViewById(R.id.txtfavbabrb);
            txt.setText("Delivery");

            imgcestabar.setBackgroundResource(R.drawable.office);
            TextView txtc = (TextView) findViewById(R.id.txtbarbbcesta);
            txtc.setText("Empresa");

        }
        else
        {


        }

        btnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("super","super lista" );
                if (myPerfil==null){
                if (lojasfav.size()>0){
                ctrlfav=true;
                lojasf.clear();
                lista ();
                }else{
                    Toast.makeText(c,"Sem favoritos",Toast.LENGTH_SHORT).show();
                }
                }
                else
                {

                    Intent intentMain = new Intent(MainActivity.this ,
                            MainActivity_delivery.class);
                    MainActivity.this.startActivity(intentMain);
                }

            }

        });

        final Button btncesta= (Button) vfav.findViewById(R.id.btncestabar);
        btncesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (myPerfil == null) {
                    if (mycesta.size() > 0) {
                        ctrlservice = true;


                        Intent intentMain = new Intent(MainActivity.this,
                                MainActivity_cesta.class);
                        MainActivity.this.startActivity(intentMain);
                    } else {
                        Toast.makeText(c, "Sem itens na cesta", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Intent intentMain = new Intent(MainActivity.this,
                            MainActivity_dashempresa.class);
                    MainActivity.this.startActivity(intentMain);
                }
            }
        });

        Button btnCAD= (Button) findViewById(R.id.btncadastrocad);
        btnCAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(null);
            }
        });
        Button btnCADclose= (Button) findViewById(R.id.btnclosecadscad);
        btnCADclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastrouser);
                rl.setVisibility(View.INVISIBLE);
                Log.i("BTNCLOSE","CLOSE cad");
            }
        });

        Button btnChat= (Button) vfav.findViewById(R.id.btnchat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myUser!=null) {
                    if (listalojaschat.size() > 0 && !nomeuser.equals("LOJA")) {
                        ctrlservice=true;

                        Intent intentMain = new Intent(MainActivity.this,
                                MainActivity_chat.class);
                            MainActivity.this.startActivity(intentMain);
                    } else {
                        if (nomeuser.equals("LOJA")) {
                            ctrlservice=true;

                            Intent intentMain = new Intent(MainActivity.this,
                                MainActivity_chat.class);
                            MainActivity.this.startActivity(intentMain);
                        }else {
                            Toast.makeText(c,"Chat sem conversas",Toast.LENGTH_SHORT).show();
                        }

                    }
                }else {
                    loginsimples=true;

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastrouser);
                    rl.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void getchat(){

        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersClient")
                .child(myUser.Uid).child("chat").child("lojasOn").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listalojaschat.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    listalojaschat.add(String.valueOf(postSnapshot.getValue()));
                }
                if (listalojaschat.size()>0){
                Chat chat = new Chat();
                    View v = (View) findViewById(R.id.includechatmain);

                    ImageView imgbarchat =(ImageView) v.findViewById(R.id.imgchatbar);
                    imgbarchat.setBackgroundResource(R.drawable.comment);
                chat.cont=0;
                chat.checknewmsguser(imgbarchat);
            }else{
                    View v = (View) findViewById(R.id.includechatmain);
                    ImageView imgchat = (ImageView) v.findViewById(R.id.imgchatbar);
                    imgchat.setBackgroundResource(R.drawable.commentoff);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getfav(){

    }
    public void lista (){
        Log.i("super","super lista" );
        lojasf.clear();
        final View vwl = (View) findViewById(R.id.includefavlist);
        listafav = (ListView)vwl. findViewById(R.id.listfav);
        listafav.setDividerHeight(0);

        RelativeLayout ltload = (RelativeLayout) findViewById(R.id.rlfav);
        ltload.setVisibility(View.VISIBLE);

        database.child("PerfilLojas").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null){

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Itemloja l = postSnapshot.getValue(Itemloja.class);

                        for(int i =0;i< lojasfav.size();i++){

                            if (lojasfav.get(i).equals(l.Nome)){

                                Log.i("super","super x"+l.Nome);
                                lojasf.add(l);

                            }
                        }
                    }

                    adapterLojasfav   adapter = new adapterLojasfav(c, R.layout.itemlojas, lojasf);
                    listafav.setAdapter(adapter);
                }
                else {



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
