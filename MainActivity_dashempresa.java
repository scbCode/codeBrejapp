package brejapp.com.brejapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainActivity_dashempresa extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    public ListView listitem;
    public ListView listbairros;
    public ListView listhist;
    static ListView listaLojas;
    static double valorfrete=0.0;
    static double valorfretecopy=0.0;
    static ArrayList<Integer> valoresfrete = new ArrayList<Integer>();
    static ArrayList<bairrosVal> meusbairros = new ArrayList<bairrosVal>();
    static ArrayList<bairrosVal> meusbairroscopy = new ArrayList<bairrosVal>();

    boolean ctodos;
    static listaAdapterLocal  adapterComp;
    boolean imgcaptured;
    Context c;
    static GoogleMap mapa;
    static DatabaseReference database;
    static ArrayList<Itemloja> lojas = new ArrayList<Itemloja>();
    static ArrayList<localloja> listalojas = new ArrayList<localloja>();
    static ArrayList<String> inscritos = new ArrayList<String>();
    static ArrayList<String> lojasselect = new ArrayList<String>();
    String id = "";
    static Empresa myLoja;
    ListView listvimg;
    FirebaseListAdapter  listaimg;
    String catitem;
    String catitemf;
    String unidade;
    String unidadef="";
    String datacalender="";
    static ArrayList<String> Lojas = new ArrayList<String>();
     boolean ctrl=false;
     String ulrimg;
     Item removeitem;
    String textp  =  "";
    String textpupdate  =  "";
     int digit=0;
    boolean ctrlpreco;
    boolean ctrlfunc;
    int t=0;
    int tup=0;
    ListView listntf;
    boolean switchctrl;
    boolean switchctrlwts;
    boolean switchcartao;
    boolean switchcards;
    boolean switchdeb;
    boolean switchstatus;
    boolean switchdelivery;
    boolean switchctrltipo;
    boolean imgb;
    ArrayList<Integer> tv = new ArrayList<>();
    ArrayList<Integer> tc = new ArrayList<>();
    ArrayList<String> ntfs = new ArrayList<>();
    ArrayList<String> tcids = new ArrayList<>();
    ArrayList<String> tvids = new ArrayList<>();
     ArrayList<String> categoriasspnprd = new ArrayList<>();
     ArrayList<String> unidadespn = new ArrayList<>();
    private static final int RC_BARCODE_CAPTURE = 9001;
    FirebaseListAdapter<Item>  itemlistsuper;
     boolean selectitem;
    String numbersupp;
    Item itemselecionado;
    boolean ctrlupdatep;
    Item itemUpdate;
    boolean ctrlservice;
    static  Itemloja_cartoes lojacarts;

    static ArrayList<bairrosVal> bairrosVal = new ArrayList<bairrosVal>();
    static ArrayList<Double> bairrosVallocal = new ArrayList<Double>();
    FirebaseListAdapter listB;

    boolean ctrlback_ellview;

    static Itemloja myPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashempresa);
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

        listvimg = (ListView) findViewById(R.id.listaimgs);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        c = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView txtwhatssup = (TextView) findViewById(R.id.suportnumb);
        txtwhatssup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappContact();
            }
        });


        Button btnbarcode = (Button) findViewById(R.id. btnbarcode);
        btnbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }

        });

        Button btntrocarbannersalvar = (Button) findViewById(R.id. btntrocarbanner2);
        btntrocarbannersalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveImgBanner(myLoja.Nome,Calendar.getInstance().getTime()+"");


            }

        });


        Button btntrocarbanner = (Button) findViewById(R.id. btntrocarbanner);
        btntrocarbanner.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   buscarimginternalbanner();

                                               }

                                           });


        Button returnbtnnumber = (Button) findViewById(R.id.btnreturnarpopnumber);
        returnbtnnumber.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             RelativeLayout rl = (RelativeLayout) findViewById(R.id.popnumero);
                                             rl.setVisibility(View.INVISIBLE);

                                             if (switchctrltipo==true){
                                             final Switch btnligar = (Switch) findViewById(R.id.switch2);
                                             btnligar.setChecked(false);}

                                             if (switchctrltipo==false){
                                                 final Switch btnligar = (Switch) findViewById(R.id.switch4);
                                                 btnligar.setChecked(false);}
                                         }

                                     });

        Button btnnumber = (Button) findViewById(R.id.btnconfirmNumber);
        btnnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText number = (EditText) findViewById(R.id.editTextNumber);
                String n = number.getText().toString();
                if (n.length() >0 ) {
                    if (switchctrltipo==true) {
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.popnumero);
                        rl.setVisibility(View.INVISIBLE);
                        TextView txt = (TextView) findViewById(R.id.txtvpopnumber);
                        txt.setText("ATIVE O BOTÃO DE LIGAR NO SEU PERFIL");

                        TextView txtnmb = (TextView) findViewById(R.id.txtnumeroligar);
                        txtnmb.setText("número ativado: " + n);

                        database.child("PerfilLojas").child(myLoja.Id).child("call").setValue(n);
                    }
                    else
                        if (switchctrltipo==false){
                            RelativeLayout rl = (RelativeLayout) findViewById(R.id.popnumero);
                            rl.setVisibility(View.INVISIBLE);
                            TextView txt = (TextView) findViewById(R.id.txtvpopnumber);
                            txt.setText("ATIVE O BOTÃO DE WHATSAPP NO SEU PERFIL");

                            TextView txtnmb = (TextView) findViewById(R.id.txtnumerowhats);
                            txtnmb.setText("número ativado: " + n);

                            database.child("PerfilLojas").child(myLoja.Id).child("whats").setValue(n);

                        }
                }else {
                    Toast.makeText(c,"Digite um número",Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnb = (Button) findViewById(R.id.btnbairros);
        btnb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl  = (RelativeLayout) findViewById(R.id.rlbairros);
                rl.setVisibility(View.VISIBLE);
                ctodos=true;
                getlistabairros();
            }
        });


        final Switch btnligar = (Switch) findViewById(R.id.switch2);
        btnligar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && switchctrl==false){

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.popnumero);
                    rl.setVisibility(View.VISIBLE);
                    switchctrltipo=true;
                    ctrlback_ellview=true;

                    TextView txt = (TextView) findViewById(R.id.txtvpopnumber);
                    txt.setText("ATIVE O BOTÃO DE LIGAR NO SEU PERFIL");

                }else {

                    if ( !isChecked  ) {
                        database.child("PerfilLojas").child(myLoja.Id).child("call").removeValue();
                        Toast.makeText(c,"Ligação desativada",Toast.LENGTH_LONG).show();
                        ctrlback_ellview=false;

                    }


                }
                if (switchctrl==true)
                    switchctrl=false;
            }
        });


        final Switch btnwhats= (Switch) findViewById(R.id.switch4);
        btnwhats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked && switchctrlwts==false){

                    Button btnb = (Button) findViewById(R.id.btnbairros);
                    btnb.setVisibility(View.VISIBLE);
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.popnumero);
                    rl.setVisibility(View.VISIBLE);
                    TextView txt = (TextView) findViewById(R.id.txtvpopnumber);
                    txt.setText("ATIVE O BOTÃO DE WHATSAPP NO SEU PERFIL");
                    switchctrlwts=true;

                }else {

                    if (!isChecked ){
                        Button btnb = (Button) findViewById(R.id.btnbairros);
                        btnb.setVisibility(View.INVISIBLE);

                        database.child("PerfilLojas").child(myLoja.Id).child("whats").removeValue();
                        Toast.makeText(c,"Whatsapp desativado",Toast.LENGTH_LONG).show();
                    }


                }

                if ( switchctrlwts==true )
                    switchctrlwts=false;

            }
        });


        final Switch delivery= (Switch) findViewById(R.id.switch3);
        delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && switchdelivery==false){
                    if (bairrosVal.size()>0){
                    RelativeLayout rlb = (RelativeLayout) findViewById(R.id.rlbairros);
                    rlb.setVisibility(View.VISIBLE);

                        Button btnb = (Button) findViewById(R.id.btnbairros);
                        btnb.setVisibility(View.VISIBLE);
                        getbairros();

                    }
                    database.child("PerfilLojas").child(myLoja.Id).child("delivery").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                                Toast.makeText(c,"Delivery ativado, aguarde os pedidos",Toast.LENGTH_LONG).show();

                        }
                    });
                    switchdelivery=false;

                }else {

                    if ( !isChecked  )

                    database.child("PerfilLojas").child(myLoja.Id).child("delivery").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(c,"Delivery DESATIVADO.",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                if ( switchdelivery==true )
                    switchdelivery=false;

            }
        });

        final Switch status= (Switch) findViewById(R.id.switch6);
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && switchstatus==false){
                    final FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();
                    database.child("DeliveryOn").child(myLoja.Nome).child("status").setValue("on").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(c,"Loja online.",Toast.LENGTH_LONG).show();
                            database.child("PerfilLojas").child(myLoja.Id).child("status").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("UsersEmpresa")
                                            .child(user.getUid()).child("ntfOn").setValue(ServerValue.TIMESTAMP);
                                    Chat chat = new Chat();
                                    chat.setUID_LOJA(myPerfil);
                                }
                            });
                        }
                    });
                }else {
                    if ( !isChecked  ){
                        database.child("DeliveryOn").child(myLoja.Nome).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c,"Loja offline.",Toast.LENGTH_LONG).show();
                                database.child("PerfilLojas").child(myLoja.Id).child("status").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                if ( switchstatus==true )
                    switchstatus=false;
            }
        });

        final Switch cartao= (Switch) findViewById(R.id.switch8);
        cartao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && switchcards==false){
                    database.child("PerfilLojas").child(myLoja.Id).child("Cred").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(c,"Cartão ativado.",Toast.LENGTH_LONG).show();
                        }
                    });
                    database.child("PerfilLojas").child(myLoja.Id).child("credito").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });


                        switchcards=false;

                }else {

                    if (!isChecked) {

                        database.child("PerfilLojas").child(myLoja.Id).child("Cred").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Cartão DESATIVADO.", Toast.LENGTH_LONG).show();
                            }
                        });

                        if (switchdeb==false){
                            database.child("PerfilLojas").child(myLoja.Id).child("credito").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                        }
                    }
                }
                if ( switchcards==true )
                    switchcards=false;

            }
        });


        final Switch deb= (Switch) findViewById(R.id.switch7);
        deb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && switchdeb==false){

                    database.child("PerfilLojas").child(myLoja.Id).child("credito").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    database.child("PerfilLojas").child(myLoja.Id).child("Debito").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(c,"Débito ativado.",Toast.LENGTH_LONG).show();
                        }
                    });
                    switchdeb=false;

                }else {

                    if (!isChecked){
                        database.child("PerfilLojas").child(myLoja.Id).child("Debito").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Débito DESATIVADO.", Toast.LENGTH_LONG).show();
                            }
                        });

                        if (switchcards==false){
                            database.child("PerfilLojas").child(myLoja.Id).child("credito").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                        }

                    }
                }

                if ( switchdeb==true )
                    switchdeb=false;

            }
        });

        Button btnedtperfil = (Button) findViewById(R.id.btngenperfil);
        btnedtperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarcad();
            }
        });


        Button btpopremove = (Button) findViewById(R.id.btnexcluirpop);
        btpopremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popremoveconta);
                rl.setVisibility(View.VISIBLE);

            }
        });

        Button btnreturnpopexclud = (Button) findViewById(R.id.btnreturnexcludcont);
        btnreturnpopexclud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popremoveconta);
                rl.setVisibility(View.INVISIBLE);

            }
        });
        Button btnprdtpers = (Button) findViewById(R.id.btnprdtpers);
        btnprdtpers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rlC = (RelativeLayout) findViewById(R.id.rlitemcat);
                rlC.setVisibility(View.INVISIBLE);
                itemselecionado=null;
                selectitem=false;

                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlFormItem);
                rl.setVisibility(View.VISIBLE);

            }
        });


        Button btncancelremove = (Button) findViewById(R.id.cancelremove);
        btncancelremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popremoveconta);
                rl.setVisibility(View.INVISIBLE);

            }
        });


        Button btnconfirmremove = (Button) findViewById(R.id.BTNREMOVECONTA);
        btnconfirmremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            removeconta();

            }
        });

        Button btnnotif = (Button) findViewById(R.id.btnnovanotf);
        btnnotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctrlservice=true;

                Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                        MainActivity_notifications.class);        ctrlservice=true;

                MainActivity_dashempresa.this.startActivity(intentMain);
            }
        });

        Button btnhistoricontf = (Button) findViewById(R.id.btnnovanotf2);
        btnhistoricontf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popClicksnotfs);
                rl.setVisibility(View.VISIBLE);
                listntf();

            }
        });
        Button returnbtnhistoricontf = (Button) findViewById(R.id.btnreturnlistantf);
        returnbtnhistoricontf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popClicksnotfs);
                rl.setVisibility(View.INVISIBLE);
            }
        });


        Button btnlogout = (Button) findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                        MainActivity_empresa.class);
                ctrlservice=true;

                MainActivity_dashempresa.this.startActivity(intentMain);
            }
        });
        Button btnlista = (Button) findViewById(R.id.btnlistaitens);
        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openlistaitens();

            }
        });

        Button btnreeturnlist = (Button) findViewById(R.id.btnreturnlista);
        btnreeturnlist .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rllistaitens);
                rl.setVisibility(View.INVISIBLE);

            }
        });

        Button btncadastroitem = (Button) findViewById(R.id.btnnovoitem);
        btncadastroitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlnovoitem);
                rl.setVisibility(View.VISIBLE);

                EditText nomeitemcadfocus = (EditText) findViewById(R.id.editTextnomeitem);
                nomeitemcadfocus.requestFocus();

                ScrollView scrv = (ScrollView) findViewById(R.id.scrvnvitm);
                scrv.scrollTo(0,0);


                RelativeLayout rli = (RelativeLayout) findViewById(R.id.rlhistitem);
                rli.setVisibility(View.VISIBLE);

                setlistahist();

            }
        });

        Button btnreturnimglist = (Button) findViewById(R.id.btnreturnlistaimg);
        btnreturnimglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rllistaimg);
                rl.setVisibility(View.INVISIBLE);



            }
        });

        Button btnimginter = (Button) findViewById(R.id.btnimgitem);
        btnimginter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unidadef=unidade;
                catitemf=catitem;
                buscarimginternal();

            }
        });


        Button btnlistimg = (Button) findViewById(R.id.btnimgcatalogo);
        btnlistimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getimgslist();


            }
        });

        Button btnreturn = (Button) findViewById(R.id.btnreturnnovoitem);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlnovoitem);
                rl.setVisibility(View.INVISIBLE);

            }
        });


        Button btnlistahist = (Button) findViewById(R.id.btnhistoricoitens);
        btnlistahist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlhistitem);
                rl.setVisibility(View.VISIBLE);

                setlistahist();

            }
        });

        Button btnreturnhist = (Button) findViewById(R.id.btnreturnnovoitem2);
        btnreturnhist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlhistitem);
                rl.setVisibility(View.INVISIBLE);

            }
        });


        Button btnreturnremove = (Button) findViewById(R.id.btnreturnpop);
        btnreturnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rlremove = (RelativeLayout) findViewById(R.id.popexcluir);
                rlremove.setVisibility(View.INVISIBLE);
                removeitem=null;
            }
        });



        Button btnremoveitem = (Button) findViewById(R.id.btnconfirmeremove);
        btnremoveitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              database.child("Catalogos").child(removeitem.cod+"-"+myLoja.Id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      snackbarshow("Item removido");
                      RelativeLayout rlremove = (RelativeLayout) findViewById(R.id.popexcluir);
                      rlremove.setVisibility(View.INVISIBLE);
                  }
              });

            }
        });



        Button btnsaveselect = (Button) findViewById(R.id.btnenviarprdpers);
        btnsaveselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectitem == true) {
                    saveitemselect();
                }
            }
    });
        Button btnsave = (Button) findViewById(R.id.btnsaveitem);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectitem == false)
                    if (imgcaptured == true) {
                        Random r = new Random();
                        int i1 = r.nextInt(99999999 - 10000000) + 10000000;
                        id = String.valueOf(i1) + myLoja.Id;
                        saveImgItem(myLoja.Nome, id);
                    } else {
                        Toast.makeText(c, "Selecione uma imagem", Toast.LENGTH_LONG).show();
                    }



            }
         });

        CalendarView calendar = (CalendarView) findViewById(R.id.calview);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                datacalender = dayOfMonth+"/"+(month+1)+ "/" + year;

            }
        });

        final EditText changepreco = (EditText) findViewById(R.id.editTexpreco);
        String text = "";
        changepreco.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {


                if (textp.length() > s.length()){
                    textp="";
                    changepreco.setText("");
                }else
                if (!textp.equals(s.toString())){

                for (int i=0; i < s.length();i++){
                    if (s.toString().charAt(i)=='.'){
                        t++;
                    }

                }


                textp =  s.toString().replace(".","");


                if (s.toString().length() < 4)
                    if (t > 0)
                        changepreco.setText(textp);

                t=0;

                    if (textp.length()==3)
                    {
                        textp = s.charAt(0)+"."+s.charAt(1)+s.charAt(2);
                        changepreco.setText(textp);
                    }
                    else

                    if (textp.length()==4)
                    {
                        textp = textp.charAt(0)+""+textp.charAt(1)+"."+textp.charAt(2)+""+textp.charAt(3);
                        changepreco.setText(textp);

                    }
                    else

                    if (textp.length()==5)
                    {
                        textp = textp.charAt(0)+""+textp.charAt(1)+""+textp.charAt(2)+"."+textp.charAt(3)+""+textp.charAt(4);
                        changepreco.setText(textp);
                    }

                    changepreco.setSelection(changepreco.getText().length());

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {



            }
        });

        FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();
        prec_txtedt();

        if (user != null)
        syncFirebase();
        else {
            Intent intentMain = new Intent(MainActivity_dashempresa.this,
                    MainActivity_empresa.class);
            ctrlservice=true;

            MainActivity_dashempresa.this.startActivity(intentMain);
        }


        Intent intputextra = getIntent();
        String update = intputextra.getStringExtra("UPDATEITEM");
        if (update!=null){
            Log.i("UPDATE","UPDATE");
            btnlistahist.performClick();
        }

    }


    public void prec_txtedt(){
        View v = (View) findViewById(R.id.include);
        View vw = (View) v.findViewById(R.id.inc);
        final EditText changepreco = (EditText) findViewById(R.id.edtprec_);
        final TextView txtunipreco= (TextView) vw.findViewById(R.id.Preco);
        final TextView txtDecpreco= (TextView) vw.findViewById(R.id.precoDec);

        String text = "";
        textp="";
        t=0;
        changepreco.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                if (textp.length() > s.length()){
                    textp="";
                    changepreco.setText("");
                }else
                if (!textp.equals(s.toString())){

                    for (int i=0; i < s.length();i++){
                        if (s.toString().charAt(i)=='.'){
                            t++;
                        }
                    }

                    textp =  s.toString().replace(".","");

                    if (s.toString().length() < 4)
                        if (t > 0)
                            changepreco.setText(textp);

                    t=0;

                    if (textp.length()==3)
                    {
                        textp = s.charAt(0)+"."+s.charAt(1)+s.charAt(2);
                        txtunipreco.setText( s.charAt(0)+"");
                        txtDecpreco.setText( "."+s.charAt(1)+""+s.charAt(2));
                        changepreco.setText(textp);
                        txtunipreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36);
                        txtDecpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    }
                    else

                    if (textp.length()==4)
                    {
                        textp = textp.charAt(0)+""+textp.charAt(1)+"."+textp.charAt(2)+""+textp.charAt(3);
                        txtunipreco.setText( textp.charAt(0)+""+textp.charAt(1));
                        txtDecpreco.setText("."+textp.charAt(3)+""+textp.charAt(4));
                        changepreco.setText(textp);
                        txtunipreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36);
                        txtDecpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    }
                    else

                    if (textp.length()==5)
                    {
                        textp = textp.charAt(0)+""+textp.charAt(1)+""+textp.charAt(2)+"."+textp.charAt(3)+""+textp.charAt(4);
                        txtunipreco.setText( textp.charAt(0)+""+textp.charAt(1)+""+textp.charAt(2));
                        txtDecpreco.setText("."+textp.charAt(4)+""+textp.charAt(5));
                        changepreco.setText(textp);
                        txtunipreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
                        txtDecpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

                    }

                    changepreco.setSelection(changepreco.getText().length());

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

        });


    }

    public void setlistahist(){

        listhist = (ListView) findViewById(R.id.lisstaitenshistorico);

          itemlistsuper = new FirebaseListAdapter<Item>(c,Item.class, R.layout.listhj,database.child("Catalogo").orderByChild("NomeProduto")) {

            @Override
            protected void populateView(final View vw, final Item model, final int position) {


                vw.setBackgroundColor(0xefefef);
                boolean ctral = false;
                Item it = model;

                TextView txtnome = (TextView) vw.findViewById(R.id.nomeItem);
                TextView txtDesc = (TextView) vw.findViewById(R.id.descricao);
                TextView textsuperm = (TextView) vw.findViewById(R.id.superm);

                textsuperm.setText(model.supermercado);
                txtDesc.setText(model.Descricao);
                txtnome.setText(model.NomeProduto);


                if (vw.getMeasuredHeight()>0) {

                    ViewGroup.LayoutParams params = listhist.getLayoutParams();
                    int totalHeight = vw.getMeasuredHeight() * (itemlistsuper.getCount());
                    params.height = totalHeight;
                    listhist.setLayoutParams(params);
                }

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

                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlhistitem);
                        rl.setVisibility(View.INVISIBLE);
                        RelativeLayout rlf = (RelativeLayout) findViewById(R.id.rlFormItem);
                        rlf.setVisibility(View.INVISIBLE);
                        RelativeLayout rlc = (RelativeLayout) findViewById(R.id.rlitemcat);
                        rlc.setVisibility(View.VISIBLE);

                        selectitem(model);
                    }
                });

            }

        };
        listhist.setAdapter(itemlistsuper);
    }



    public void saveitemselect() {

        EditText prec = (EditText) findViewById(R.id.edtprec_);

        if (prec.length() > 3) {

            CalendarView calendar = (CalendarView) findViewById(R.id.calview);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String exp = "";
            try {
                if (!datacalender.equals("")) {
                    Date date1 = sdf.parse(datacalender);
                    long diff = (date1.getTime() - (Calendar.getInstance().getTimeInMillis()));
                    exp = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                    Log.i("SAVE", "SAVE DATE " + exp);
                } else {
                    exp = "0";
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("d", "Days: error " + e.getMessage());
            }

            int expiraarday = Integer.parseInt(exp) + 1;
            SimpleDateFormat sdfdatai = new SimpleDateFormat("MM/dd");
            String dataf = datacalender;
            String datai = sdfdatai.format(calendar.getDate());
            String expira = String.valueOf(expiraarday);
            Double time = Double.parseDouble(String.valueOf(Calendar.getInstance().getTimeInMillis()));

            int precorder = 0;
            if (prec.getText().length() > 0)
                precorder = Integer.parseInt(prec.getText().toString().replace(".", "")) * 100;

            itemselecionado.time = time * -1;
            itemselecionado.precoorder = precorder;
            itemselecionado.Preco = prec.getText().toString();
            itemselecionado.expirar = exp;
            itemselecionado.datai = datai;
            itemselecionado.supermercado = myLoja.Nome;
            String codloja = itemselecionado.cod + "-" + myLoja.Id;

            database.child("Catalogos").child(codloja).setValue(itemselecionado).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    database.child("HistoricoDeItens").child(myLoja.Uid).child("HistoricoDeItens").child(itemselecionado.cod).setValue(itemselecionado).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(c, "ITEM SALVO!", Toast.LENGTH_LONG).show();
                            //                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlnovoitem);
                            Intent intentMain = new Intent(MainActivity_dashempresa.this,
                                    MainActivity_dashempresa.class);
                            ctrlservice=true;

                            MainActivity_dashempresa.this.startActivity(intentMain);
                        }
                    });
                }

            });
        }else {
            Toast.makeText(c,"Complete o preço",Toast.LENGTH_LONG).show();
        }
    }


    public void selectitem(Item model){

        itemselecionado=model;
        selectitem=true;

        id = model.cod;
        catitem = model.Tipo;
        unidade = model.unidade;


        imgcaptured=true;

        View v = (View) findViewById(R.id.include);
        View vw = (View) v.findViewById(R.id.include);

        ImageView img = (ImageView) vw.findViewById(R.id.iconItemdash);
        Glide.with(c).load(model.urlic).into(img);
//
        TextView nomeitem = (TextView) vw.findViewById(R.id.nomeItem);
         nomeitem.setText(model.NomeProduto);

        TextView descimte = (TextView) vw.findViewById(R.id.descricao);
        descimte.setText(model.Descricao);

        TextView uni = (TextView) vw.findViewById(R.id.unidade);
        uni.setText(model.unidade);


        EditText prec = (EditText) findViewById(R.id.edtprec_);

        String precit = prec.getText().toString();

        String Tipo="";
        String cor=model.corsuper;
        String cod=model.cod;

        String marca=model.marca;

        String supermercado=model.supermercado;

        String urlic=model.urlic;

        String urlimg=model.urlimg;

        final Spinner spinneruni = (Spinner) findViewById(R.id.spinunidade);
        for (int i = 0; i < unidadespn.size();i++) {

            if (unidadespn.get(i).equals(model.unidade)) {
                spinneruni.setSelection(i);
            }
        }

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerCategorias);
        for (int i = 0; i < categoriasspnprd.size();i++) {

            if (categoriasspnprd.get(i).equals(model.Tipo)){
                Tipo = categoriasspnprd.get(i);
                spinner.setSelection(i);
        }}

        final Item itemnew = new Item(nomeitem.getText().toString(),"",descimte.getText().toString(),catitem,model.cod,unidade,supermercado,cor,urlic,urlimg,"x","x",0,cod,lojas,itemselecionado.tags,"tag",0,"detal",marca);
        itemselecionado = itemnew;

    }

    public void  getnumberwhatssup(){
        database.child("Caton_confg").child("supportNumb").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numbersupp = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void openWhatsappContact() {
        PackageManager pm=getPackageManager();//
        try {
            String toNumber =  numbersupp; // Replace with mobile phone number without +Sign or leading zeros, but with country code.
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:" + "" + toNumber + "?body=" + ""));
            sendIntent.setPackage("com.whatsapp");
            ctrlservice=true;

            startActivity(sendIntent);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity_dashempresa.this,"it may be you dont have whats app",Toast.LENGTH_LONG).show();
        }

    }

    public void removeconta(){

        database.child("Catalogos").orderByChild("supermercado").equalTo(myLoja.Nome).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    data.getRef().removeValue();
                }

                database.child("PerfilLojas").child(myLoja.Id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.child("UsersEmpresa").child(myLoja.Uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                database.child("deleteUser").child(myLoja.Uid).setValue(myLoja.Uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intentMain = new Intent(MainActivity_dashempresa.this,
                                                MainActivity.class);
                                        ctrlservice = true;

                                        MainActivity_dashempresa.this.startActivity(intentMain);

                                    }

                                });

                            }
                        });

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
            syncFirebase();
        else {
            ctrlservice=true;

            Intent intentMain = new Intent(MainActivity_dashempresa.this,
                    MainActivity_empresa.class);
            MainActivity_dashempresa.this.startActivity(intentMain);
        }


    }

    public void listntf(){


        listntf = (ListView) findViewById(R.id.listantf);

        database.child("Central_Clicks").child("Receiver_Notific").child(myLoja.Topico).orderByKey().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                tvids.add(snapshot.getKey());
                                Log.i("KEY","KEY "+snapshot.getKey());
                                tv.add(Integer.parseInt(String.valueOf(snapshot.getChildrenCount())));
                            }


                            database.child("Central_Clicks").child("Notific").child(myLoja.Nome).orderByKey().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        tcids.add(snapshot.getKey());
                                        tc.add(Integer.parseInt(String.valueOf(snapshot.getChildrenCount())));
                                    }
                                    listntfcount();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }


                        @Override
                        public void onCancelled (@NonNull DatabaseError databaseError){

                        }

             });


    }

    public void listntfcount(){



        FirebaseListAdapter<notifySimples>  itemlistsuper = new FirebaseListAdapter<notifySimples>(c, notifySimples.class, R.layout.ntflistclicks,database.child("UsersEmpresa").child(myLoja.Uid).child("Notificacoes").orderByChild("time")) {

            @Override
            protected void populateView(final View vw, final notifySimples model, final int position) {

                TextView txt =(TextView) vw.findViewById(R.id.txttitlentf);
                TextView txtmsg =(TextView) vw.findViewById(R.id.txtmsgntf);
                TextView total =(TextView) vw.findViewById(R.id.txttotalclicks);
                final TextView totalviews =(TextView) vw.findViewById(R.id.xtxreceive);

                txt.setText(model.title);
                txtmsg.setText(model.msg);

                for (int i =0; i < tvids.size();i++){
                    if ( model.id.equals(tvids.get(i)) )
                        totalviews.setText( "Views: " + tv.get(i) );
                }

                if (tvids.size()==0)
                    totalviews.setText( "Views: " + 0);

                for (int i =0; i < tcids.size();i++){
                    if ( model.id.equals(tcids.get(i)) )
                        total.setText( "Clicks: " + tc.get(i) );
                }

                if (tcids.size()==0)
                    total.setText( "Clicks: " + 0);


            }

        };

        listntf.setAdapter(itemlistsuper);
    }

    public void snackbarshow(String texto){
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(parentLayout, texto, Snackbar.LENGTH_LONG)
                ;
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (null != data) {
                ImageView img = (ImageView) findViewById(R.id.imgproduto);
                Uri imgem = data.getData();
                Glide.with(c).load(imgem).into(img);
                imgcaptured=true;
            }

        }

        if (requestCode == 1) {

            if (null != data) {
                imgb=true;
                ImageView img = (ImageView) findViewById(R.id.bannersup);
                Uri imgem = data.getData();
                Glide.with(c).load(imgem).into(img);

                Button btnsavar = (Button) findViewById(R.id. btntrocarbanner2);
                btnsavar.setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Log.d("", "Barcode read: " + barcode.displayValue);
                } else {
                    Log.d("", "No barcode captured, intent data is null");
                }
            } else {
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void buscarimginternal() {

        final int RESULT_GALLERY = 0;
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ctrlservice=true;

        startActivityForResult(galleryIntent, RESULT_GALLERY);

    }

    public void buscarimginternalbanner() {

        final int RESULT_GALLERY = 1;

        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ctrlservice=true;

        startActivityForResult(galleryIntent, RESULT_GALLERY);

    }


    public void getCategorias(){
        ArrayList<String> categorias = new ArrayList<>();

        database.child("Categorias").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {

                        categoriasspnprd.add("" + dataSnapshot2.getValue());

                    }
                }

                confispincateg(categoriasspnprd);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });}


    public void getunidades(){


        database.child("Unidades").orderByKey().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    unidadespn.add(""+snapshot.getValue());
                }

                configspinuni(unidadespn);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });}


    public void confispincateg(ArrayList<String> lista){

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerCategorias);
        final ArrayList<String> listafiltros = new ArrayList<>();

        listafiltros.addAll(lista);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_spinner_item, listafiltros);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    String value = (String) parent.getItemAtPosition(position);
                    catitem=value;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public void configspinuni(ArrayList<String> lista){

        final Spinner spinner = (Spinner) findViewById(R.id.spinunidade);
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
                    unidade=value;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


//    public void configspinlojas() {
//
//        final Spinner spinner = (Spinner) findViewById(R.id.spinlojas);
//      //  final ListView lista = (ListView) findViewById(R.id.listalojascheck);
//        final ArrayList<String> listafiltros = new ArrayList<>();
//        final ArrayList<String> copyremove = new ArrayList<>();
//        listafiltros.add("Todas as lojas");
//        try {
//            JSONArray lojas = new JSONArray(String.valueOf(myLoja.Lojas));
//            for (int i = 0; i < lojas.length(); i++) {
//                listafiltros.add(lojas.getString(i));
//            }
//
//        } catch (Exception e) {
//
//        }
//
//
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, listafiltros);
//
//        lista.setAdapter(adapter);
//        ViewGroup.LayoutParams params = lista.getLayoutParams();
//        int totalHeight = 0;
//        totalHeight = 150 * lista.getCount();
//        params.height = totalHeight;
//        lista.setLayoutParams(params);
//        lista.requestLayout();
//
//        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                ctrl = false;
//
//                lista.setFocusable(true);
//                if (position > 0) {
//                    position--;
//
//                    if (lojasselect.size() > 0)
//                        for (int i = 0; i < lojasselect.size(); i++) {
//                            for (int ii = 0; ii < listafiltros.size(); ii++) {
//                                if (lojasselect.get(i).equals(listafiltros.get(position))) {
//                                    ctrl = true;
//                                }
//                            }
//                        }
//
//                    if (ctrl == false) {
//                        view.setBackgroundColor(Color.GREEN);
//                        lojasselect.add(listafiltros.get(position));
//                    } else {
//                        copyremove.clear();
//                        copyremove.addAll(lojasselect);
//                        lojasselect.clear();
//                        for (int i = 0; i < copyremove.size(); i++) {
//                            if (i != position) {
//                                lojasselect.add(copyremove.get(i));
//                            }
//                        }
//
//                        view.setBackgroundColor(Color.WHITE);
//                    }
//                }
//                else
//                if (position == 0) {
//                    if (lojasselect.size() > 0)
//                        for (int i = 0; i < lojasselect.size(); i++) {
//                            for (int ii = 0; ii < listafiltros.size(); ii++) {
//                                if (lojasselect.get(i).equals(listafiltros.get(position))) {
//                                    ctrl = true;
//                                }
//                            }
//                        }
//
//                        if (ctrl==false) {
//                            view.setBackgroundColor(Color.GREEN);
//                            lojasselect.add("Todas as lojas");
//                        }else
//                        {
//                            copyremove.clear();
//                            copyremove.addAll(lojasselect);
//                            lojasselect.clear();
//                            for (int i = 0; i < copyremove.size(); i++) {
//                                if (!copyremove.get(i).equals("Todas as lojas")) {
//                                    lojasselect.add(copyremove.get(i));
//                                }
//                            }
//
//                            view.setBackgroundColor(Color.WHITE);
//                        }
//                }
//            }
//
//
//        });
//
//
//
//    }


    public void saveImgItem(String nomeloja,String id) {

        //creating reference to firebase storage
        String nome  =  nomeloja;

        nome = nome.replace("\\u","");


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://devscb-7afe7.appspot.com/iconitem");    //change the url according to your firebase app

        String dat = new Date().getTime()+"";
        final StorageReference childRef = storageRef.child(dat+id+nome+".png");

        //uploading the image
        ImageView imageView = (ImageView) findViewById(R.id.imgproduto);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
        byte[] data = baos.toByteArray();


        final UploadTask uploadTask = childRef.putBytes(data);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity_dashempresa.this);

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

                        ulrimg = String.valueOf(String.valueOf(uri));

                        saveItem();

                    }
                });

            }


        });

    }


    public void saveImgBanner(String nomeloja,String id) {

        //creating reference to firebase storage
        String nome  =  nomeloja;

        nome = nome.replace("\\u","");


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://devscb-7afe7.appspot.com/");    //change the url according to your firebase app

        String dat = new Date().getTime()+"";
        final StorageReference childRef = storageRef.child(dat+id+nome+".png");

        //uploading the image
        ImageView imageView = (ImageView) findViewById(R.id.bannersup);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();


        final UploadTask uploadTask = childRef.putBytes(data);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity_dashempresa.this);

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

                        database.child("PerfilLojas").child(myLoja.Id).child("urlBanner").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                progressDialog.show();
                                progressDialog.setMessage("Imagem salva");
                                progressDialog.hide();

                                Button btntrocarbannersalvar = (Button) findViewById(R.id. btntrocarbanner2);
                                btntrocarbannersalvar.setVisibility(View.GONE);

                            }
                        });

                    }
                });

            }


        });

    }





    public void saveItem(){

        EditText nomeitem = (EditText) findViewById(R.id.editTextnomeitem);
        EditText marcaitem = (EditText) findViewById(R.id.edtmarcaitem);
        EditText descimte = (EditText) findViewById(R.id.editTexdescitem);
        EditText prec= (EditText) findViewById(R.id.editTexpreco);
        String nomeit = nomeitem.getText()+"";
        String descit = descimte.getText()+"";
        String precit = prec.getText()+"";
      //  if (catitemf!="")catitem=catitemf;
        String categ = catitem;
        String unidad = unidade;
        ArrayList<String> lojas = new ArrayList<>();
        lojas.addAll(lojasselect);

        ArrayList<String> tagsbusca = new ArrayList<>();

        CalendarView calendar = (CalendarView) findViewById(R.id.calview);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String  exp="";
        try {

            if (!datacalender.equals("")) {
                Date date1 = sdf.parse(datacalender);
                long diff = (date1.getTime() - (Calendar.getInstance().getTimeInMillis()));
                exp = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            }else
            {
                exp = "0";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int expiraarday = Integer.parseInt(exp)+1;

        String pcodr = precit.replace(".", "");
        SimpleDateFormat sdfdatai = new SimpleDateFormat("MM/dd");

        String Tipo=categ;
        String cor="#333333";
        String cod=id;
        String dataf= datacalender;
        String datai=sdfdatai.format(calendar.getDate());
        String expira= String.valueOf(expiraarday);
        String marca=marcaitem.getText().toString();
        String preco =prec.getText().toString();
        int precorder=0;
        if (precit.length()>0)
        precorder = Integer.parseInt(precit.replace(".","")) * 100;
        Double time=Double.parseDouble(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        time = time*-1;
        String supermercado=myLoja.Nome;
        String urlic=ulrimg;
        String urlimg=ulrimg;
        String nametag = nomeit;

        ArrayList<String> tasgb = new ArrayList<>();

        //NOME ITEM SPLIT
        String[] tags = nametag.split(" ");
        for (int i = 0; i < tags.length; i++){
                String cap = tags[i].substring(0, 1).toUpperCase() +  tags[i].substring(1);
                cap = deAccent(cap);
                tasgb.add(cap);
        }

        //NOME LOJA
        String[] tagsname = myLoja.Nome.split(" ");
        for (int i = 0; i < tagsname.length; i++){
            String cap = tagsname[i].substring(0, 1).toUpperCase() +  tagsname[i].substring(1);
            cap = deAccent(cap);
            tasgb.add(cap);
        }

        tasgb.add(unidade);

        tasgb.add(categ);

        if (tags.length > 1)
            tasgb.add(deAccent(nametag));

        tasgb.add(deAccent(myLoja.Categoria));
        tasgb.add(deAccent(marca));

        final Item itemnew = new Item(nomeit,preco,descit,Tipo,id,unidade,supermercado,cor,urlic,urlimg,datai,expira,time,cod,lojas,tasgb,"tag",precorder,"detal", marca);

            database.child("Catalogos").child(id).setValue(itemnew).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    database.child("HistoricosdeItens").child(myLoja.Uid).child("HistoricoDeItens").child(id).setValue(itemnew).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(c,"ITEM SALVO!",Toast.LENGTH_LONG).show();
                            Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                                    MainActivity_dashempresa.class);
                            ctrlservice=true;

                            MainActivity_dashempresa.this.startActivity(intentMain);

                        }
                    });
                }
            });

    }



    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }


    public void checkcadastro(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database.child("UsersEmpresa").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (ctrlfunc==false) {
                    myLoja = dataSnapshot.getValue(Empresa.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(myLoja);
                    try {
                        JSONObject jsonobj = new JSONObject(json);
                        Log.i("LJ", "LJ " + json);
                        for (int ii = 0; ii < jsonobj.names().length(); ii++) {
                            if (jsonobj.get(jsonobj.names().get(ii).toString()).equals("temp") == true || jsonobj.get(jsonobj.names().get(ii).toString()) == null) {
                                ctrlfunc = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (ctrlfunc == true) {
                        try {
                            snackbarshow("Termine seu cadastro :)");
                            finalizarcad();
                        } catch (Exception e) {
                            Toast.makeText(c, "ERROR", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void openlistaitens(){

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rllistaitens);
        rl.setVisibility(View.VISIBLE);

            listitem = (ListView) findViewById(R.id.listaitens);
            listitem.setDivider(null);
            listitem.setDividerHeight(0);



                FirebaseListAdapter listB = new FirebaseListAdapter<Item>( c,Item.class, R.layout.itemlayoutloja, database.child("Catalogos").orderByChild("supermercado").startAt(myLoja.Nome).endAt(myLoja.Nome+ "\uf8ff")) {

                    @SuppressLint("WrongConstant")
                    @Override
                    protected void populateView(final View vw, final Item model, final int position) {

                        vw.setBackgroundColor(0xefefef);
                        boolean ctral = false;
                        Item it = model;
                        if (vw.getMeasuredHeight()>0) {

                            ViewGroup.LayoutParams params = listitem.getLayoutParams();
                           int totalHeight = vw.getMeasuredHeight() * (listitem.getCount());

                            params.height = totalHeight;
                            listitem.setLayoutParams(params);
                        }
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

                        TextView txtexp = (TextView) vw.findViewById(R.id.dataitem);

                        final Calendar cal = Calendar.getInstance(); // Get Calendar Instance
                        Calendar calend = Calendar.getInstance();
                        Date dataAgora = calend.getTime();

                        //GET DATA INICIO ITEM
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String dtstrg = model.datai.toString()+"/2019";
                        Date date1= null;

                        try {
                            date1 = new SimpleDateFormat("MM/dd/yyyy").parse(dtstrg);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        cal.setTime(date1);
                        cal.add(Calendar.DATE, Integer.parseInt(model.expirar));

                        Log.i("time"," time "+cal.getTimeInMillis() +" "+ dataAgora.getTime());
                        long diff = cal.getTimeInMillis() - dataAgora.getTime();

                           if ( cal.getTimeInMillis() >= dataAgora.getTime()) {
                                   txtexp.setText("Expira: "+sdf.format(cal.getTime()));

                           }
                                else if ( cal.getTimeInMillis() < dataAgora.getTime()) {
                                    txtexp.setText("EXPIROU");
                                } else if ( cal.getTimeInMillis() == dataAgora.getTime())
                                    txtexp.setText("");


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

                     final   Button btnremo = (Button) vw.findViewById(R.id.btnremoveitem);
                        btnremo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                RelativeLayout rlremove = (RelativeLayout) findViewById(R.id.popexcluir);
                                rlremove.setVisibility(View.VISIBLE);
                                ImageView icon = (ImageView) findViewById(R.id.imgitemremove);
                                Glide.with(c).load(model.urlic).into(icon);
                                removeitem = model;

                            }
                        });



                        final EditText precoupdate = (EditText) vw.findViewById(R.id.editprecoupdate);
                        precoupdate.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void afterTextChanged(Editable s) {


//                                if (ctrlupdatep==true)
//                                if (textpupdate.length() > s.length()){
//                                    textpupdate="";
//                                    tup=0;
//                                    precoupdate.setText("" );
//                                    Log.i("TEXT","TEXT " );
//
//                                }else
                                if (!textpupdate.equals(s.toString())){

                                    for (int i=0; i < s.length();i++){
                                        if (s.toString().charAt(i)=='.'){
                                            tup++;
                                        }
                                    }

                                    textpupdate =  s.toString().replace(".","");
                                    if (s.toString().length() < 4)
                                        if (tup > 0)
                                            precoupdate.setText(textpupdate);

                                    tup=0;

                                    if (textpupdate.length()==3)
                                    {
                                        textpupdate = s.charAt(0)+"."+s.charAt(1)+s.charAt(2);
                                        precoupdate.setText(textpupdate);
                                    }
                                    else

                                    if (textpupdate.length()==4)
                                    {
                                        textpupdate = textpupdate.charAt(0)+""+textpupdate.charAt(1)+"."+textpupdate.charAt(2)+""+textpupdate.charAt(3);
                                        precoupdate.setText(textpupdate);

                                    }
                                    else

                                    if (textpupdate.length()==5)
                                    {
                                        textpupdate = textpupdate.charAt(0)+""+textpupdate.charAt(1)+""+textpupdate.charAt(2)+"."+textpupdate.charAt(3)+""+textpupdate.charAt(4);
                                        precoupdate.setText(textpupdate);
                                    }

                                    precoupdate.setSelection(precoupdate.getText().length());

                                }

                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start,
                                                          int count, int after) {
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start,
                                                      int before, int count) {
                            }
                        });


                        precoupdate.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                                if(keyCode == KeyEvent.KEYCODE_DEL) {
                                    //this is for backspace
                                    textpupdate="";
                                    tup=0;
                                    precoupdate.setText("");
                                }
                                return false;
                            }
                        });


                        final  Button btnupdatepreco = (Button) vw.findViewById(R.id.btnrenoveprec);
                        btnupdatepreco.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //reset
                                textpupdate="";
                                tup=0;

                                if (ctrlupdatep==false){


                                    btnremo.setVisibility(View.GONE);

                                    precoupdate.setVisibility(View.VISIBLE);
                                    precoupdate.setText("");
                                    btnupdatepreco.setBackgroundResource(R.drawable.rect_02);
                                    btnupdatepreco.setText("OK");

                                    ctrlupdatep=true;

                                }

                                else{

                                    if (precoupdate.length() >= 4 ){
                                        //sendpreduto
                                        model.Preco = precoupdate.getText().toString();
                                        int precorder = Integer.parseInt(precoupdate.getText().toString().replace(".","")) * 100;
                                        model.precoorder =precorder;
                                        database.child("Catalogos").child(model.cod+"-"+myLoja.Id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                textpupdate="";
                                                Toast.makeText(c,"Item atualizado",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    btnremo.setVisibility(View.VISIBLE);

                                    btnupdatepreco.setBackgroundResource(R.drawable.rect_03);

                                    hideKeyboard(MainActivity_dashempresa.this);
                                    btnupdatepreco.setText("Novo Preço");
                                    precoupdate.setVisibility(View.INVISIBLE);
                                    ctrlupdatep=false;


                                }
                            }
                        });
                    }
                };
                listitem.setAdapter(listB);



    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    public void finalizarcad() {

        Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                MainActivity_empresa.class);
        intentMain.putExtra("FINALCAD","true");
        ctrlservice=true;

        MainActivity_dashempresa.this.startActivity(intentMain);

    }


    public  void getMyLoja(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database.child("UsersEmpresa").child(user.getUid()).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myLoja = dataSnapshot.getValue(Empresa.class);
                getTotalInscritos();
                setlayout();
                getbairros();

//                configspinlojas();
                getCategorias();
                getunidades();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void getimgslist(){

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rllistaimg);
        rl.setVisibility(View.VISIBLE);


        listaimg = new FirebaseListAdapter<Item>(c,Item.class, R.layout.itemimglist,database.child("Catalogo").orderByKey()) {
            @Override
            protected void populateView(View v, final Item model, final int position) {

                ImageView img = (ImageView) v.findViewById(R.id.img);
                Glide.with(c).load(model.urlic).into(img);

                TextView key = (TextView) v.findViewById(R.id.nomeitemkey);
                key.setText(model.NomeProduto);



                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rllistaimg);
                        rl.setVisibility(View.INVISIBLE);

                        imgcaptured=true;

                        ImageView img = (ImageView) findViewById(R.id.imgproduto);
                        Glide.with(c).load(model.urlic).into(img);

                        EditText nomeitem = (EditText) findViewById(R.id.editTextnomeitem);
                        nomeitem.setText(model.NomeProduto);
                        EditText marcaitem = (EditText) findViewById(R.id.edtmarcaitem);
                        marcaitem.setText(model.marca);
                        EditText descimte = (EditText) findViewById(R.id.editTexdescitem);
                        descimte.setText(model.Descricao);

                        final Spinner spinneruni = (Spinner) findViewById(R.id.spinunidade);
                        for (int i = 0; i < unidadespn.size();i++) {

                            if (unidadespn.get(i).equals(model.unidade))
                                spinneruni.setSelection(i);
                        }

                        final Spinner spinner = (Spinner) findViewById(R.id.spinnerCategorias);
                        for (int i = 0; i < categoriasspnprd.size();i++) {

                            if (categoriasspnprd.get(i).equals(model.Tipo))
                            spinner.setSelection(i);
                        }
                    }
                });
            }
        };

        listvimg.setAdapter(listaimg);

    }



    public  void getTotalInscritos(){
        inscritos.clear();
        if (myLoja != null)
        database.child("Topicos").child("Lojas").child(myLoja.Nome).child("inscritos").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    inscritos.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        inscritos.add(snapshot.getValue().toString());

                    }
                    TextView insc = (TextView) findViewById(R.id.txtinscritosdash);
                    insc.setText("INSCRITOS: " + inscritos.size());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void setlayout(){

        final Switch btnligar = (Switch) findViewById(R.id.switch2);
        final Switch btnwhats = (Switch) findViewById(R.id.switch4);

        database.child("PerfilLojas").child(myLoja.Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){

                    Itemloja loja = dataSnapshot.getValue(Itemloja.class);
                    Itemloja_cartoes lojacarts = dataSnapshot.getValue(Itemloja_cartoes.class);
                    myPerfil = loja;

                    if (loja.call!=null) {
                        switchctrl=true;
                        btnligar.setChecked(true);
                        TextView txtnmb =(TextView) findViewById(R.id.txtnumeroligar);
                        txtnmb.setText(""+loja.call);
                    }   else {
                        switchctrl = false;
                        btnligar.setChecked(false);
                    }

                    final Switch cartao= (Switch) findViewById(R.id.switch8);
                    final Switch cards= (Switch) findViewById(R.id.switch5);

                    if (lojacarts.Cred==true){
                        switchcartao=true;
                        cartao.setChecked(true);
                     }else
                        cartao.setChecked(false);

                    final Switch debt = (Switch) findViewById(R.id.switch7);

                    if (lojacarts.Debito==true){
                        switchdeb=true;
                        debt.setChecked(true);
                    }else{
                        switchdeb=false;
                        debt.setChecked(false);
                    }

                    if (switchdeb==true || switchcartao==true)
                        cards.setChecked(true);

                    final Switch delivery= (Switch) findViewById(R.id.switch3);

                    if (loja.delivery==true){
                        switchdelivery=true;
                        delivery.setChecked(true);
                    }else
                        delivery.setChecked(false);


                    final Switch status= (Switch) findViewById(R.id.switch6);

                    if (loja.status==true){
                        switchdelivery=true;
                        status.setChecked(true);
                    }else
                        status.setChecked(false);

                    if (loja.whats!=null) {
                        switchctrlwts=true;
                        btnwhats.setChecked(true);
                        TextView txtnmb =(TextView) findViewById(R.id.txtnumerowhats);
                        txtnmb.setText(""+loja.whats);
                    }   else {
                        switchctrlwts = false;
                        btnwhats.setChecked(false);
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ImageView img = (ImageView) findViewById(R.id.imgLogoEmpresaPerfil);
        Glide.with(c).load(myLoja.UrlImg).into(img);


        if (imgb==false)
        database.child("PerfilLojas").child(myLoja.Id+"").child("urlBanner").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    ImageView imgbaner = (ImageView) findViewById(R.id.bannersup);
                    Glide.with(c).load(dataSnapshot.getValue()).into(imgbaner);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TextView nome = (TextView) findViewById(R.id.nomeEmpresaPerfil);
        nome.setText(myLoja.Nome);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setBuildingsEnabled(true);
    }


    public void syncFirebase() {

        //persistence start
        if (database == null) {
            persistent pst = new persistent();
            pst.onCreate();
            database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");
        }
        getnumberwhatssup();
        getMyLoja();
        checkcadastro();

    }


    @Override
    public void onBackPressed() {
        ctrlservice=true;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (ctodos==true){
                if (bairrosVal.size()>0){
                RelativeLayout rlbairros = (RelativeLayout) findViewById(R.id.rlbairros);
                rlbairros.setVisibility(View.INVISIBLE);
                    ctodos=false;
                }
                else
                {
                    Toast.makeText(c,"Selecione ao menos um bairros", Toast.LENGTH_SHORT).show();
                    ctodos=false;

                }
            }else
                if (ctrlback_ellview==true){
                    RelativeLayout rlbairros = (RelativeLayout) findViewById(R.id.popnumero);
                    rlbairros.setVisibility(View.INVISIBLE);
                    ctrlback_ellview=false;
                }
                else {
                    super.onBackPressed();
                }
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
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        ctrlservice=true;

        if (id == R.id.nav_status) {
            Toast.makeText(c,"preparando...",Toast.LENGTH_LONG).show();
            if (MainActivity.ctrlmenustatus==false){
                final FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();
                database.child("DeliveryOn").child(MainActivity.myPerfil.Nome).child("status").setValue("on").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(c,"Loja online.",Toast.LENGTH_LONG).show();
                        database.child("PerfilLojas").child(MainActivity.myEmpresa.Id).child("status").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("UsersEmpresa")
                                        .child(user.getUid()).child("ntfOn").setValue(ServerValue.TIMESTAMP);

                                item .setIcon(ContextCompat.getDrawable(c, R.drawable.status));
                                Drawable drawable = item.getIcon();
                                drawable.setColorFilter(0xFFFFFF, PorterDuff.Mode.SRC_ATOP);
                                MainActivity.ctrlmenustatus=true;
                                Chat chat = new Chat();
                                chat.setUID_LOJA(MainActivity.myPerfil);
                            }
                        });
                    }
                });
            }else {

                database.child("DeliveryOn").child(MainActivity.myEmpresa.Nome).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(c,"Loja offline.",Toast.LENGTH_LONG).show();
                        item .setIcon(ContextCompat.getDrawable(c, R.drawable.statusoff));
                        Drawable drawable = item.getIcon();
                        drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);
                        MainActivity.ctrlmenustatus=false;

                        database.child("PerfilLojas").child(MainActivity.myEmpresa.Id).child("status").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        }else
        if (id == R.id.nav_camera) {
            Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                    MainActivity.class);
            MainActivity_dashempresa.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                    MainActivity_lojas.class);
            MainActivity_dashempresa.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                    MainActivity_categorias.class);
            MainActivity_dashempresa.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                    MainActivity_topicos.class);
            MainActivity_dashempresa.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                    MainActivity_contato.class);
            MainActivity_dashempresa.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_dashempresa.this ,
                    MainActivity_empresa.class);
            MainActivity_dashempresa.this.startActivity(intentMain);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (myPerfil!=null)
            if ( myPerfil.status==true) {
                Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
                startService(servicechat);

                Intent servicedel = new Intent(getApplicationContext(), Pedido_service.class);
                servicedel.putExtra("loja", myPerfil.Nome);
                startService(servicedel);
            }


    }



    @Override
    public void onPause(){
        super.onPause();

        Chat_service.cancelntf=false;

        if (myPerfil!=null)
            if ( myPerfil!=null && myPerfil.status==true && ctrlservice==false ) {

                Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
                servicechat.putExtra("loja", myPerfil.Nome);
                startService(servicechat);

                Intent servicedel = new Intent(getApplicationContext(), Pedido_service.class);
                servicedel.putExtra("loja", myPerfil.Nome);
                startService(servicedel);
            }

    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i("ONLINE","ONLINE "+Chat_service.cancelntf);
        ctrlservice=false;
        Chat_service.cancelntf=true;
        Log.i("ONLINE","ONLINE "+Chat_service.cancelntf);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
        stopService(servicechat);
    }


    public void getbairros(){

        final Switch delivery= (Switch) findViewById(R.id.switch3);

        database.child("BairrosLojas").child(myLoja.Nome).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bairrosVal.clear();
                bairrosVallocal.clear();

                if (dataSnapshot.exists()){

                for( DataSnapshot datab : dataSnapshot.getChildren()){
                    brejapp.com.brejapp.bairrosVal bairro = datab.getValue(brejapp.com.brejapp.bairrosVal.class);
                    bairrosVal.add(bairro);
                    bairrosVallocal.add(0.0);
                }


                Button btnb = (Button) findViewById(R.id.btnbairros);
                btnb.setVisibility(View.VISIBLE);
                listabairros();

            }else {




                }

                if (delivery.isChecked() && bairrosVal.size()==0){
                    RelativeLayout rlb = (RelativeLayout) findViewById(R.id.rlbairros);
                    rlb.setVisibility(View.VISIBLE);
                    Button btnb = (Button) findViewById(R.id.btnbairros);
                    btnb.setVisibility(View.VISIBLE);
                    ctodos=true;
                    listabairros();
                    getlistabairros();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getlistabairros(){
        final double[] t = {0.0};

       final TextView totalfret = (TextView) findViewById(R.id.txtfreteminimo);

        database.child("UsersEmpresa")
                .child(myLoja.Uid).child("Frete").child("Minimo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ( dataSnapshot.exists()) {
                    valorfrete = Double.parseDouble(String.valueOf(dataSnapshot.getValue()));
                    String total = String.format("%.2f",valorfrete);
                    totalfret.setText("Frete mínimo: R$ " + total);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        CheckBox checall = (CheckBox) findViewById(R.id.checktodos);
        checall
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    for (int i=0; i < listbairros.getChildCount();i++){

                        ctodos=true;

                        brejapp.com.brejapp.bairrosVal b = (brejapp.com.brejapp.bairrosVal)listbairros.getAdapter().getItem(i);
                        if (b.Valor.equals("0.0") || b.Valor.equals("0.00")  )
                            b.Valor = String.valueOf(valorfrete);

                        database.child("BairrosLojas").child(myLoja.Nome).child(b.Nome).setValue(b);


                    }

                }else {

                    for (int i=0; i < listbairros.getChildCount();i++){


                        brejapp.com.brejapp.bairrosVal b = (brejapp.com.brejapp.bairrosVal)listbairros.getAdapter().getItem(i);

                        database.child("BairrosLojas").child(myLoja.Nome).child(b.Nome).removeValue();


                    }
                }
                }

                });

        Button addfreteall = (Button) findViewById(R.id.btnmaisfrete);
        addfreteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valorfrete+=1.00;
                String total = String.format("%.2f",valorfrete);
                database.child("UsersEmpresa")
                        .child(myLoja.Uid).child("Frete").child("Minimo").setValue(valorfrete+"");


                totalfret.setText("Frete mínimo: R$ "+total);

                for(int i = 0; i < bairrosVal.size();i++){
                    String  myv=bairrosVal.get(i).Valor;
                    double ft = Double.parseDouble(myv)+1;
                    String ftm = String.format("%.2f",ft);
                    ftm = ftm.replace(",",".");
                    database.child("BairrosLojas").child(myLoja.Nome).child(bairrosVal.get(i).Nome).child("Valor").setValue(ftm);
                }



            }
        });


        Button menosfreteall = (Button) findViewById(R.id.btnmenosfrete);
        menosfreteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valorfretecopy=valorfrete;

                valorfrete-=1.00;
                String total = String.format("%.2f",valorfrete);
                for(int i = 0; i < bairrosVal.size();i++){
                    String  myv=bairrosVal.get(i).Valor;
                    double ft = Double.parseDouble(myv)-1;
                    String ftm = String.valueOf(ft);
                    database.child("BairrosLojas").child(myLoja.Nome).child(bairrosVal.get(i).Nome).child("Valor").setValue(ftm);
                    database.child("UsersEmpresa")
                            .child(myLoja.Uid).child("Frete").child("Minimo").setValue(valorfrete);
                }

                totalfret.setText("Frete mínimo: R$ "+total);
            }
        });

        listabairros();
    }


    public void listabairros(){


        listbairros = (ListView) findViewById(R.id.listabairros);

        listB = new FirebaseListAdapter<bairrosVal>( c,bairrosVal.class, R.layout.listabairros, database.child("Bairros").orderByKey()) {

            @SuppressLint("WrongConstant")
            @Override
            protected void populateView(final View vw, final bairrosVal model, final int position) {

                Log.i("model","model "+model);
                boolean ctral = false;
                bairrosVal it = model;

                final RelativeLayout rlval = (RelativeLayout) vw.findViewById(R.id.rlvalorb);
                CheckBox checkbairro = (CheckBox) vw.findViewById(R.id.checkbairro);

                final TextView totalfret = (TextView) vw.findViewById(R.id.txtvalorfreteb);


                for(int i = 0; i < bairrosVal.size();i++) {

                    if (bairrosVal.get(i).Nome.equals(model.Nome)){
                        rlval.setVisibility(View.VISIBLE);
                        String total = String.format("%.2f",Double.parseDouble(bairrosVal.get(i).Valor));
                        totalfret.setText("R$ "+total);
                        checkbairro.setChecked(true);
                    }

                }

                TextView bairro = (TextView) vw.findViewById(R.id.nomebairro);
                bairro.setText(model.Nome);

                Button addv = (Button) vw.findViewById(R.id.btnmaisfreteb);
                addv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(int i = 0; i < bairrosVal.size();i++) {

                         if (bairrosVal.get(i).Nome.equals(model.Nome)) {

                        double ft = Double.parseDouble(bairrosVal.get(i).Valor);
                        double ftm = ft+1;
                        String totalf = String.format("%.2f",ftm);
                        totalf = totalf. replace(",",".");
                        database.child("BairrosLojas").child(myLoja.Nome).child(model.Nome).child("Valor").setValue(totalf);
                            }
                        }
                    }
                });

                Button menosv = (Button) vw.findViewById(R.id.btnmenosfreteb);
                menosv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(int i = 0; i < bairrosVal.size();i++) {
                            if (Double.parseDouble(bairrosVal.get(i).Valor)>valorfrete)
                            if (bairrosVal.get(i).Nome.equals(model.Nome)) {

                                double ft = Double.parseDouble(bairrosVal.get(i).Valor);
                                double ftm = ft-1;
                                String totalf = String.format("%.2f",ftm);
                                totalf = totalf. replace(",",".");
                                database.child("BairrosLojas").child(myLoja.Nome).child(model.Nome).child("Valor").setValue(totalf);
                            }
                        }
                    }
                });

                checkbairro
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if (isChecked ){

                                    String v = "";

                                    if (valorfrete>0)
                                        v = String.valueOf(valorfrete);
                                    else
                                        v = "0.00";

                                    bairrosVal bv = new bairrosVal(model.Nome,v);
                                    database.child("BairrosLojas").child(myLoja.Nome).child(model.Nome).setValue(bv);
                                    rlval.setVisibility(View.VISIBLE);

                                }else {

                                    if (!isChecked ){

                                        database.child("BairrosLojas").child(myLoja.Nome).child(model.Nome).removeValue();

                                        rlval.setVisibility(View.INVISIBLE);

                                    }
                                }



                            }
                        });
            }
        };
        listbairros.setAdapter(listB);
    }

}
