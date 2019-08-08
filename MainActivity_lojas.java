package brejapp.com.brejapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity_lojas extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    public ListView listitem;
    public ListView listsuper;
    Context c;
    static GoogleMap mapa;
    static DatabaseReference database;
    static ArrayList<Itemloja> lojas = new ArrayList<Itemloja>();
    static ArrayList<Boolean> ctrlinscri = new ArrayList<Boolean>();
    boolean ctrlbttopic;
    boolean ctrlbttopicclick;
    User myUser;
    static FirebaseListAdapter listB;
    int counter;
    int totalHeight;
    Item itemclicado;
    boolean ctrlntf;
    String lojantf;
    Itemloja getloja;
    static ArrayList<String> listalojaschat = new ArrayList<>();

    Itemloja  myloja;
    String iduser;
     String prods="Produtos: ";
     static  Itemloja lojafav;

    boolean  ctrlfav;
    boolean ctrlpop;
    boolean ctrlpopitem;
    static int countfav;
    boolean ctrlcesta;
    static  Itemloja lojachat;
    static boolean loginsimples;
    static ArrayList<Itemloja> lojasf = new ArrayList<Itemloja>();
    static   ListView  listafav ;

    boolean lojaview;
    String lojaviewname;

    boolean ctrlservice;

    static ArrayList<String> mycesta = new ArrayList<String>();
    final ArrayList<String> listainscritosfav = new ArrayList<>();
    final ArrayList<String> listainscritosfavcopy = new ArrayList<>();
    static ArrayList<String> lojasfav = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_lojas);
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


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        c = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        syncFirebase();

        Button BTNCLOSEcad = (Button) findViewById(R.id.btnclosecads);
        BTNCLOSEcad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastroloja);
                rl.setVisibility(View.INVISIBLE);
            }
        });


        Button btnreturnperfil = (Button) findViewById(R.id.btnreturnloja);
        btnreturnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetperfillayout();
            }
        });
        ImageView btnmapa = (ImageView) findViewById(R.id.btnmapsloja);
        btnmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        Button btncpm = (Button) findViewById(R.id.btncomp);
        btncpm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkpermissao();
                compartwhats();
            }

        });

        //BOTAO FECHAR
        Button btnfecharinfor = (Button) findViewById(R.id.btnfecharinfoitem);
        btnfecharinfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rlinfoitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
                rlinfoitem.setVisibility(View.INVISIBLE);
                ctrlpopitem=false;

            }

        });

        Button btnCADclose= (Button) findViewById(R.id.btnclosecadscad);
        btnCADclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastrouser);
                rl.setVisibility(View.INVISIBLE);
            }
        });


        final   Button btncadastro = (Button) findViewById(R.id.btncadastrofav);


        btncadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                consloadwindow.setVisibility(View.VISIBLE);
                login(lojafav);
            }
        });

        final   Button BTNCLOSE = (Button) findViewById(R.id.btnclosecadloja);
        BTNCLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rlpop = (RelativeLayout) findViewById(R.id.popcadastroloja);
                rlpop.setVisibility(View.INVISIBLE);
            }
        });

        final   Button btnrecsenha = (Button) findViewById(R.id.btnrecupersenhafav);
        btnrecsenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtemail = (EditText)findViewById(R.id.emailuserfav);
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

        Button btnligar = (Button) findViewById(R.id.btnligarloja);
        btnligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getloja.status==true){
                checkpermissaocallphone();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+55"+getloja.call));
                if (ActivityCompat.checkSelfPermission(c, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                    ctrlservice=true;

                    startActivity(callIntent);
            }
            }

        });
        Button btnwhats = (Button) findViewById(R.id.btnwhats);
        btnwhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappContact(getloja.whats);
            }
        });
        if (MainActivity.myPerfil!=null){
            final Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_status).setVisible(true);
            menu.findItem(R.id.nav_delivery).setVisible(true);
            menu.findItem(R.id.nav_mypedidos).setVisible(false);

            if (MainActivity.myPerfil.status==true) {

                menu.findItem(R.id.nav_status).setTitle("ONLINE");

                menu.findItem(R.id.nav_status)
                        .setIcon(ContextCompat.getDrawable(c, R.drawable.status));
                Drawable drawable = menu.findItem(R.id.nav_status).getIcon();
                drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);
            }else {
                menu.findItem(R.id.nav_status).setTitle("OFFLINE");
                menu.findItem(R.id.nav_status)
                        .setIcon(ContextCompat.getDrawable(c, R.drawable.statusoff));
                Drawable drawable = menu.findItem(R.id.nav_status).getIcon();
                drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);

            }

        }else {
            final Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_status).setVisible(false);
            menu.findItem(R.id.nav_delivery).setVisible(false);
            menu.findItem(R.id.nav_mypedidos).setVisible(true);

        }
        checkPlayServices(c);
    }

    public  boolean checkPlayServices(Context context) {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(resultCode)) {
                Toast.makeText(c, "Google Service off", Toast.LENGTH_SHORT).show();
                api.getErrorDialog(((Activity) context), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }  else {
                Toast.makeText(c,"Google Service off",Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if (MainActivity.myPerfil!=null && MainActivity.myPerfil.status==true && ctrlservice==false) {
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            startService(servicechat);
        }

        if (MainActivity.nomeuser.equals("user")&& MainActivity.listalojaschat.size()>0){
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            servicechat.putExtra("usermsg","true");
            startService(servicechat);
        }
    }



    @Override
    public void onPause(){
        super.onPause();
        Chat_service.cancelntf=false;

        if (MainActivity.myPerfil!=null && MainActivity.myPerfil.status==true && ctrlservice==false) {
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            servicechat.putExtra("loja",MainActivity. myPerfil.Nome);
            startService(servicechat);
        }         if (MainActivity.nomeuser.equals("user")&& MainActivity.listalojaschat.size()>0){
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            servicechat.putExtra("usermsg","true");
            startService(servicechat);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i("ONLINE","ONLINE "+Chat_service.cancelntf);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ctrlservice=false;

        Chat_service.cancelntf=true;

        Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
        stopService(servicechat);

        //  Chat_service.listenerChat=false;

    }


    public void additem(Item item){

        if (myUser!=null){
            database.child("CestasClientes").child(myUser.Uid).child(item.supermercado).child(item.iditem).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });
        }else
        {

            Toast.makeText(c,"Necessário login", Toast.LENGTH_SHORT).show();

            RelativeLayout rli = (RelativeLayout) findViewById(R.id.rlinfoItem);
            rli.setVisibility(View.INVISIBLE);

            RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastroloja);
            rl.setVisibility(View.VISIBLE);

        }


    }

    public void removeitem(Item item){
        FirebaseAuth user = FirebaseAuth.getInstance();

        database.child("CestasClientes").child(user.getUid()).child(item.supermercado).child(item.iditem).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

    }

    void openWhatsappContact(String number) {
        PackageManager pm=getPackageManager();
        try {


            String toNumber = number; // Replace with mobile phone number without +Sign or leading zeros, but with country code.
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.



            Intent sendIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:" + "" + toNumber + "?body=" + ""));
            sendIntent.setPackage("com.whatsapp");
            ctrlservice=true;

            startActivity(sendIntent);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity_lojas.this,"it may be you dont have whats app",Toast.LENGTH_LONG).show();

        }
    }


    //
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setBuildingsEnabled(true);
    }


    public void getCesta(){
        if (myUser!=null)
        database.child("CestasClientes").child(myUser.Uid).addValueEventListener(new ValueEventListener() {

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


    public void syncFirebase() {

        //persistence Start
        if (database == null) {
            persistent pst = new persistent();
            pst.onCreate();
            database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");
        }

        Intent mainintent = getIntent();
        String extra="";
        String loja="";
        String title="";
        String msg="";
        String id="";
        String url="";


        if (mainintent.getExtras() != null){

            String lojaviewextra = mainintent.getStringExtra("LojaView");
            if (lojaviewextra!=null){
                lojaviewname=lojaviewextra;
                authentic();

            }


            extra = mainintent.getStringExtra("notify");

        if (extra!=null){
            loja=mainintent.getStringExtra("loja");
            title=mainintent.getStringExtra("title");
            msg=mainintent.getStringExtra("msg");
            id=mainintent.getStringExtra("id");
            url=mainintent.getStringExtra("Url");
            String Tipo=mainintent.getStringExtra("tipo");
            String Cor=mainintent.getStringExtra("cor");
            String uni=mainintent.getStringExtra("uni")+"";

            lojantf = loja;

            ctrlntf=true;

            FirebaseAuth user = FirebaseAuth.getInstance();
            String uid ="";
            if (user != null)
                uid = user.getUid();
            else
                uid=Calendar.getInstance().getTimeInMillis()+"";

            notifySimples ntfs = new notifySimples(title,msg,"off","test",loja,"off","off",id,Tipo,Cor,"",uni);


            loja.replace(".","");
            loja.replace("#","");
            loja.replace("$","");
            loja.replace("[","");
            loja.replace("]","");
            database.child("Central_Clicks").child("Notific")
                    .child(loja).child(id).child(uid).setValue(ntfs);


            authentic();


        }
        }else{

            authentic();
        }

    }


    public void lista (){


        listitem = (ListView) findViewById(R.id.listaLojas);
        listitem.setDividerHeight(0);

        RelativeLayout ltload = (RelativeLayout) findViewById(R.id.layoutloadpromo);
        ltload.setVisibility(View.INVISIBLE);

        database.child("PerfilLojas").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        Itemloja i = postSnapshot.getValue(Itemloja.class);
                        Log.i("super","super "+i.Nome);
                        lojas.add(i);

                    }
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseListAdapter<Itemloja> itemlistsuper = new FirebaseListAdapter<Itemloja>(c, Itemloja.class, R.layout.itemlojas,database.child("PerfilLojas") ){

            @Override
            protected void populateView(final View vw, final Itemloja model, final int position) {


                int cont=0;
                Double totalp=0.0;

                //get lista produto
                database.child("Catalogos").orderByChild("supermercado").equalTo(model.Nome).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Item i = postSnapshot.getValue(Item.class);
                                Log.i("super","super "+i.NomeProduto);
                                prods+= i.NomeProduto.replace("Cerveja","")+" ";
                            }

                        TextView produtos = (TextView) vw.findViewById(R.id.txtproduts);
                        produtos.setText(prods);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                countfav=0;
                final   ImageView iconrating = (ImageView) vw.findViewById(R.id.iconfav);
                iconrating.setBackgroundResource(R.drawable.starx);

                final   ImageView iconfav = (ImageView) vw.findViewById(R.id.iconfavloja);
                database.child("Level").child(model.Nome)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            countfav= Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()))   ;
                              int t = 0;
                              int cont=0;
                              for (DataSnapshot stars : dataSnapshot.getChildren()) {
                                  cont++;
                                  t +=(int)stars.getChildrenCount()*(Integer.parseInt(stars.getKey())+1);
                              }
                              Double tstart = Double.parseDouble(String.valueOf(t)) / 4.0;
                                TextView totalinsc = (TextView) vw.findViewById(R.id.txtfav);
                                Log.i("STAR COUNT","STAR COUNT" +t);

                                if (tstart>0.0)
                                    if (tstart<1.0)
                                        totalinsc.setText("1.0");
                                    else
                                    totalinsc.setText(tstart+"");
                                else
                                    totalinsc.setText("Novo!");

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                boolean ctrlbttopic=false;

                for(int ii = 0; ii < listainscritosfav.size();ii++) {

                    if (listainscritosfav.get(ii).equals(model.Nome))
                    {
                        iconfav.setBackgroundResource(R.drawable.tagfav);
                        ctrlbttopic=true;
                        ctrlinscri.add(true);
                    }

                }

                if (ctrlbttopic==false) {
                    iconfav.setBackgroundResource(R.drawable.tagfavoff);
                    ctrlinscri.add(false);

                }
                iconfav.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        if (myUser!=null)
                        {     RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                            consloadwindow.setVisibility(View.VISIBLE);
                               getid(model);}
                        if (myUser==null)
                        {
                            lojafav=model;
                            RelativeLayout rlpop = (RelativeLayout) findViewById(R.id.popcadastroloja);
                            rlpop.setVisibility(View.VISIBLE);
                        }


                    }
                });

                ImageView  tagcred = (ImageView) vw.findViewById(R.id.checkcred);
                ImageView  tagdel = (ImageView) vw. findViewById(R.id.checkdev);
                ImageView  tagdin = (ImageView)  vw.findViewById(R.id.checkdin);

                tagdin.setImageResource(R.drawable.check);

                if (model.delivery==true && model.status==true)
                    tagdel.setImageResource(R.drawable.check);
                else
                    tagdel.setImageResource(R.drawable.checkoff);

                if (model.credito==true)
                    tagcred.setImageResource(R.drawable.check);
                else
                    tagcred.setImageResource(R.drawable.checkoff);


                TextView nomenegocio = (TextView) vw.findViewById(R.id.nomenegocio);
                nomenegocio.setText(model.Nome);

                try {
                    final TextView dist = (TextView) vw.findViewById(R.id.txtdistcesta);
                    if (MainActivity.logdistances.size() > 0) {
                        for (int l = 0; l < MainActivity.lojas.size(); l++) {
                            if (MainActivity.lojas.get(l).Nome.equals(model.Nome)) {
                                Double d = (MainActivity.logdistances.get(l));
                                Double df = (MainActivity.logdistances.get(l) / 1000);
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

                ImageView logo = (ImageView) vw.findViewById(R.id.imgloja);
                Glide.with(mContext).load(model.urlic).into(logo);
                ImageView clickitem = (ImageView) vw.findViewById(R.id.clickitem);


                clickitem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PerfilLoja(model);
                    }
                });

                if (lojantf!=null)
                if (lojantf.equals(model.Nome))clickitem.performClick();


                Log.i("loja","lojafav "+lojaviewname);


                if (lojaviewname!=null)
                    if (lojaviewname.equals(model.Nome))
                       {   PerfilLoja(model);
                            lojaview=false;
                       }




            }

        };

        listitem.setAdapter(itemlistsuper);

    }


    public void localmaps(Itemloja loja){


        try {
            if (mapa!=null)
               mapa.clear();

            JSONArray ob = new JSONArray(String.valueOf(loja.coordenadas));
            for (int i = 0; i < ob.length(); i++) {

            Double lt = (Double) ob.getJSONObject(i).get("latitude");
            Double lg = (Double) ob.getJSONObject(i).get("longitude");

            Log.i("teste local","teste local "+lt+" "+lg );
            LatLng local = new LatLng(lt, lg);

                if (mapa!=null)

                    mapa.addMarker(new MarkerOptions()
                    .title("" + loja.Nome)
                    .position(local)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmarkercart))
            );
                if (mapa!=null)

                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 16));

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void resetperfillayout(){
        ctrlpop=false;

        RelativeLayout layoutloja = (RelativeLayout) findViewById(R.id.layoutLoja);
        layoutloja.setVisibility(View.INVISIBLE);

        TextView txtNomeLoja = (TextView) findViewById(R.id.NomeLoja);
        txtNomeLoja.setText("");

        TextView txtdescricaoloja = (TextView) findViewById(R.id.descricaoloja);
        txtdescricaoloja.setText("");

        ImageView imgbaner = (ImageView) findViewById(R.id.bannerloja);
        imgbaner.setImageDrawable(null);

    }


    @Override
    public void onBackPressed() {
        ctrlservice=true;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ( ctrlpopitem==true){
                RelativeLayout rlinfoitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
                rlinfoitem.setVisibility(View.INVISIBLE);
                ctrlpopitem=false;

            }else
            if (ctrlpop==true && ctrlpopitem==false){
                resetperfillayout();
            }
            else
            if (ctrlfav==true){
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlfav);
                rl.setVisibility(View.INVISIBLE);
                ctrlfav=false;
            }
            else
            if (ctrlpop==false && ctrlpopitem==false)
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
            Intent intentMain = new Intent(MainActivity_lojas.this ,
                    MainActivity.class);

            MainActivity_lojas.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_lojas.this ,
                    MainActivity_lojas.class);
            MainActivity_lojas.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_lojas.this ,
                    MainActivity_categorias.class);
            MainActivity_lojas.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_lojas.this ,
                    MainActivity_topicos.class);
            MainActivity_lojas.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_lojas.this ,
                    MainActivity_contato.class);
            MainActivity_lojas.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_lojas.this ,
                    MainActivity_empresa.class);
            MainActivity_lojas.this.startActivity(intentMain);
        }

        else if (id == R.id.nav_cesta) {
            if (MainActivity.myUser!=null && MainActivity.mycesta.size()>0){
                Intent intentMain = new Intent(MainActivity_lojas.this ,
                        MainActivity_cesta.class);

                MainActivity_lojas.this.startActivity(intentMain);
            }
            else{
                Toast.makeText(c,"Cesta vazia",Toast.LENGTH_SHORT).show();
            }
        }  else if (id == R.id.nav_mypedidos) {
            Intent intentMain = new Intent(MainActivity_lojas.this ,
                    MainActivity_pedidos.class);
            MainActivity_lojas.this.startActivity(intentMain);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void compartwhats() {

        checkpermissao();
        clickitemshare(itemclicado);
        Toast.makeText(c,"Compartilhando item",Toast.LENGTH_SHORT).show();


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
            ctrlservice=true;

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


    public void checkpermissao(){

        if (ContextCompat.checkSelfPermission(MainActivity_lojas.this,
                Manifest.permission.READ_EXTERNAL_STORAGE  )
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity_lojas.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE  )
                != PackageManager.PERMISSION_GRANTED  ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_lojas.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity_lojas.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }



    }


    public void checkpermissaocallphone(){
        if (ContextCompat.checkSelfPermission(MainActivity_lojas.this,
                Manifest.permission.CALL_PHONE  )
                != PackageManager.PERMISSION_GRANTED  ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_lojas.this,
                    Manifest.permission.CALL_PHONE ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity_lojas.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    //
    public void clickitem(final Item item, int position) {
        itemclicado = item;

        ctrlpopitem=true;

        Log.i("teste","teste click");
        RelativeLayout rlinfoitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
        rlinfoitem.setVisibility(View.VISIBLE);

        boolean ctrl = false;
        int cont = 0;
        double t = 0.0;
        String precoDec;
        String preco;

        TextView txtnome = (TextView) findViewById(R.id.txtnomeitem);
        txtnome.setText(item.NomeProduto);
        TextView txttipo = (TextView) findViewById(R.id.txttipoitem);
        txttipo.setText(item.Tipo);
        TextView txtpreco = (TextView) findViewById(R.id.txtprecoitem);
        TextView txtprecodec = (TextView) findViewById(R.id.txtdescip);

        final TextView btncesta = (TextView) findViewById(R.id.btncesta2);
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

        TextView txtDesc = (TextView) findViewById(R.id.txtdescricaoitem);
        txtDesc.setText(item.Descricao);

        TextView txtsuper = (TextView) findViewById(R.id.txtsupermerc);
        txtsuper.setText("" + item.supermercado);

        TextView txtinfouni = (TextView) findViewById(R.id.txtinfouni);
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

            TextView txtdatainfo = (TextView) findViewById(R.id.txtdatainfo);
            txtdatainfo.setText(item.supermercado);

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

        ImageView iconsupinfoitem = (ImageView) findViewById(R.id.iconsupinfoitem);

        Glide.with(c).load(lojas.get(pos).urlic).into(iconsupinfoitem);

        final int finalPosition = pos;
        iconsupinfoitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PerfilLoja(lojas.get(finalPosition));

            }
        });

        ImageView icon = (ImageView) findViewById(R.id.imgIteminfo);
        Glide.with(c).load(item.urlimg).into(icon);

//        if (shareitemclick==true) {
//            sendshare();
//            shareitemclick=false;
//        }


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


    public void PerfilLoja(final Itemloja loja){
        saveclickperfil(loja);
        getloja=loja;

        View v = (View) findViewById(R.id.includeLayoutloja);
        View w = (View) findViewById(R.id.includeitem);

        final TextView btncesta = (TextView) w.findViewById(R.id.btncesta2);
        btncesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myUser!=null){
                if (ctrlcesta==false){
                    additem(itemclicado);
                    btncesta.setText("Remover da cesta");
                    btncesta.setTextColor(0xffcc0000);
                    ctrlcesta=true;
                }else {
                    removeitem(itemclicado);
                    btncesta.setTextColor(0xFFFDC200);
                    btncesta.setText("Adicionar á cesta");
                    ctrlcesta=false;
                }}else{
                    Toast.makeText(c,"Necessário login", Toast.LENGTH_SHORT).show();
                    loginsimples=true;
                    RelativeLayout rli = (RelativeLayout) findViewById(R.id.rlinfoItem);
                    rli.setVisibility(View.INVISIBLE);
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastroloja);
                    rl.setVisibility(View.VISIBLE);
                }
            }
        });

        final TextView dist = (TextView) findViewById(R.id.txtendloja);

        if (MainActivity.logdistances.size()>0){
            for (int l=0;l<MainActivity.lojas.size();l++){
                if (MainActivity.lojas.get(l).Nome.equals(loja.Nome)){
                    Double d =  (MainActivity.logdistances.get(l));
                    Double df =  (MainActivity.logdistances.get(l)/1000);
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


        ImageView  tagcred = (ImageView) v.findViewById(R.id.checkcred);
        ImageView  tagdel = (ImageView)  v.findViewById(R.id.checkdev);
        ImageView  tagdin = (ImageView)  v.findViewById(R.id.checkdin);

        tagdin.setImageResource(R.drawable.check);

        if (loja.delivery==true && loja.status==true)
            tagdel.setImageResource(R.drawable.check);
        else
            tagdel.setImageResource(R.drawable.checkoff);

        if (loja.credito==true)
            tagcred.setImageResource(R.drawable.check);
        else
            tagcred.setImageResource(R.drawable.checkoff);

        if (loja.status==true) {

            if (loja.call != null) {
                Button btnligarloja = (Button) v.findViewById(R.id.btnligarloja);
                btnligarloja.setVisibility(View.VISIBLE);
                btnligarloja.setBackgroundResource(R.drawable.call);
            }else {
                Button btnligarloja = (Button) v.findViewById(R.id.btnligarloja);
                btnligarloja.setBackgroundResource(R.drawable.calloff);

            }
            Button btnchat = (Button) v.findViewById(R.id.btnchat);
            btnchat.setVisibility(View.VISIBLE);
            btnchat.setBackgroundResource(R.drawable.chat);

            btnchat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("BTNCHAT","BTNCHAT");
                    lojachat = loja;
                    chatconfig(lojachat,c);
                }
            });

        } else {
            Button rlchat = (Button) v.findViewById(R.id.btnchat);
            rlchat.setBackgroundResource(R.drawable.chatoff);
            Button rl = (Button) v.findViewById(R.id.btnligarloja);
            rl.setBackgroundResource(R.drawable.calloff);

        }

        listsuper = (ListView) v.findViewById(R.id.listProdutos);

        RelativeLayout layoutitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
        layoutitem.setVisibility(View.INVISIBLE);

        RelativeLayout layoutloja = (RelativeLayout) findViewById(R.id.layoutLoja);
        layoutloja.setVisibility(View.VISIBLE);
        ctrlpop=true;

        TextView txtNomeLoja = (TextView) findViewById(R.id.nomneg);
        txtNomeLoja.setText("" + loja.Nome);

        TextView txtdescricaoloja = (TextView) findViewById(R.id.descricaoloja);
        txtdescricaoloja.setText("" + loja.Descricao);

        final ArrayList<String> listainscritos = new ArrayList<>();

        database.child("Topicos").child("Lojas").child(loja.Nome).child("inscritos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            listainscritos.add(String.valueOf(postSnapshot.getValue()));

                        }
                        TextView totalinsc = (TextView) findViewById(R.id.totalinscritos);
                        totalinsc.setText("Total de inscritos: " +listainscritos.size());
                        perfilconfig(loja,listainscritos);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

    }


    public void chatconfig(Itemloja loja , Context ctx ){

        if (myUser!=null){
            try {
                Chat chat = new Chat();
                chat.sendStartChat(loja);
                Intent intentMain = new Intent(ctx,
                        MainActivity_chat.class);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                intentMain.putExtra("Chat", loja.Nome);
                ctrlservice=true;

                ctx.startActivity(intentMain);
            } catch (Exception e){
                Log.i("chatconfig","chatconfig E "+e.getMessage());
            }
            try{
                Chat chat = new Chat();
                chat.sendStartChat(loja);
                Intent intentMain = new Intent(c,
                        MainActivity_chat.class);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                intentMain.putExtra("Chat", loja.Nome);
                ctrlservice=true;

                MainActivity_lojas.this.startActivity(intentMain);
            } catch (Exception e){

            }

        }
        else {
            Toast.makeText(c,"Necessário login", Toast.LENGTH_SHORT).show();
            loginsimples=true;

            RelativeLayout rli = (RelativeLayout) findViewById(R.id.rlinfoItem);
            rli.setVisibility(View.INVISIBLE);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastroloja);
            rl.setVisibility(View.VISIBLE);
        }

    }


    public void perfilconfig(final Itemloja loja, ArrayList<String> listainscritos){

        final String time = ""+new Date().getTime();

        ImageView imglg = (ImageView) findViewById(R.id.imglogoemp);
        Glide.with(c).load(loja.urlic).into(imglg);

        database.child("PerfilLojas").child(loja.cod+"").child("urlBanner").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    ImageView imgbaner = (ImageView) findViewById(R.id.bannerloja);
                    Glide.with(c).load(dataSnapshot.getValue()).into(imgbaner);
                }
              localmaps(loja);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listB = new FirebaseListAdapter<Item>(c, Item.class, R.layout.listhj,database.child("Catalogos").orderByChild("supermercado").equalTo(""+loja.Nome)) {

            @SuppressLint("WrongConstant")
            @Override
            protected void populateView(final View vw, final Item model, final int position) {

                Log.i("LISTA","LISTA "+vw.getMeasuredHeight() );
                vw.setBackgroundColor(0xefefef);
                boolean ctral = false;
//
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

    }


    public void inscrever(final Itemloja loja){

        final   Button btntopico = (Button) findViewById(R.id.btninscrevase);
        String email="";
        for(int i =0; i < myUser.Email.length();i++){
            if (myUser.Email.charAt(i)=='.')
                email  = myUser.Email.replace(".","pt");
        }

        final String finalEmail = email;

        FirebaseMessaging.getInstance().subscribeToTopic(loja.topicontf).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                database.child("UsersClient").child(myUser.Uid).child("topicos").child(loja.Nome).setValue(loja.Nome).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.child("Topicos").child("Lojas").child(loja.Nome).child("inscritos").child(iduser).setValue(iduser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Loja favorita " + loja.Nome, Toast.LENGTH_LONG).show();
                                gettopicsisncritos();
                                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                                consloadwindow.setVisibility(View.INVISIBLE);
                                RelativeLayout cad = (RelativeLayout) findViewById(R.id.popcadastroloja);
                                cad.setVisibility(View.INVISIBLE);
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

        final   Button btntopico = (Button) findViewById(R.id.btninscrevase);
        String email="";

        for(int i =0; i < myUser.Email.length();i++){
            if (myUser.Email.charAt(i)=='.')
                email  = myUser.Email.replace(".","pt");
        }

        final String finalEmail = email;
        FirebaseMessaging.getInstance().unsubscribeFromTopic(loja.topicontf).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("unsubs","unsubs "+ loja.topicontf);
                database.child("UsersClient").child(myUser.Uid).child("topicos").child(loja.Nome).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.child("Topicos").child("Lojas").child(loja.Nome).child("inscritos").child(iduser).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Loja favorita removida: " + loja.Nome, Toast.LENGTH_LONG).show();
                                gettopicsisncritos();

                                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
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


    private void setUser(final Itemloja loja){

        EditText Temail = (EditText) findViewById(R.id.emailuserfav);
        String email = Temail.getText().toString();

        EditText Tpsw = (EditText) findViewById(R.id.pwsuserfav);
        String password = Tpsw.getText().toString();



        if (loginsimples==false) {
            email = Temail.getText().toString();
            password = Tpsw.getText().toString();
        }

        if (password.length() >0 && email.length() > 0   ) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("", "createUserWithEmail:success ");
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
                                    RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                                    consloadwindow.setVisibility(View.INVISIBLE);
                                }
                            }

                            // ...
                        }
                    });
        }else {Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();
            RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
            consloadwindow.setVisibility(View.INVISIBLE);}
    }


    public void saveDadosUser( final Itemloja loja) {

        final   Button btntopico = (Button) findViewById(R.id.btninscrevase);
        final String time = ""+new Date().getTime();
        RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
        consloadwindow.setVisibility(View.VISIBLE);

        database.child("UsersClient").child(myUser.Uid).setValue(myUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                consloadwindow.setVisibility(View.INVISIBLE);
                RelativeLayout rlpop = (RelativeLayout) findViewById(R.id.popcadastroloja);
                rlpop.setVisibility(View.INVISIBLE);
                saveid(loja);
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
                if (loginsimples==false)    inscrever(loja);
            }

        });

    }


    public void getchat(){

        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersClient")
                .child(myUser.Uid).child("chat").child("lojasOn").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.listalojaschat.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MainActivity. listalojaschat.add(String.valueOf(postSnapshot.getValue()));
                }
                if (MainActivity.listalojaschat.size()>0){
                    Chat chat = new Chat();
                    View v = (View) findViewById(R.id.includechatmain);
//
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


    public void checkid() {

        if (myUser!=null)
        database.child("UsersClient").child(myUser.Uid).child("iduser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()!=null)
                    iduser = dataSnapshot.getValue().toString();

                lista();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getid(final Itemloja loja) {
        ctrlbttopicclick = false;


        if (myUser!=null)
        database.child("UsersClient").child(myUser.Uid).child("iduser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    iduser = dataSnapshot.getValue().toString();
                    for(int ii = 0; ii < listainscritosfav.size();ii++) {

                        if (listainscritosfav.get(ii).equals(loja.Nome)) {
                            ctrlbttopicclick=true;

                    }
                    }
                    if (ctrlbttopicclick == true) {
                        listainscritosfavcopy.addAll(listainscritosfav);
                        for(int ii = 0; ii < listainscritosfavcopy.size();ii++) {

                            if (!listainscritosfavcopy.get(ii).equals(loja.Nome)) {
                                listainscritosfav.add(listainscritosfavcopy.get(ii));
                            }
                        }
                        ctrlbttopicclick = false;
                        desinscrever(loja);

                    } else if (ctrlbttopicclick == false) {
                        ctrlbttopicclick = true;
                        inscrever(loja);
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


    private void login(final Itemloja loja){

        EditText Temail = (EditText) findViewById(R.id.emailuserfav);
        String email = Temail.getText().toString();

        EditText Tpsw = (EditText) findViewById(R.id.pwsuserfav);
        String password = Tpsw.getText().toString();



        if (password.length() > 7 && email.length() > 0  ) {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                RelativeLayout rlpop = (RelativeLayout) findViewById(R.id.popcadastroloja);
                                rlpop.setVisibility(View.INVISIBLE);

                                if (user != null) {
                                    myUser = new User("",user.getUid(),user.getEmail(),null,null,"","FREEN");
                                }
                                if (loginsimples==false)   {  getid(loja);getchat();}

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("LOGIN","LOGIN X "+task.getException().getMessage());
                                if ( task.getException().getMessage() =="There is no user record corresponding to this identifier. The user may have been deleted.")
                                {
                                    setUser(loja);

                                }

                                if ( task.getException().getMessage() =="The password is invalid or the user does not have a password.")
                                    Toast.makeText(c, "Senha incorreta.",
                                            Toast.LENGTH_SHORT).show();

                                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                                consloadwindow.setVisibility(View.INVISIBLE);

                            }


                        }
                    });
        }else {
            Toast.makeText(c, "Campos incompletos...",
                    Toast.LENGTH_SHORT).show();

            RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
            consloadwindow.setVisibility(View.INVISIBLE);
        }

    }


    public void authentic(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            myUser = new User("",user.getUid(),user.getEmail(),null,null,"","FREEN");
            ImageView btnChat= (ImageView) findViewById(R.id.imgchatbar);

            btnChat.setBackgroundResource(R.drawable.comment);
            FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersEmpresa")
                    .child(myUser.Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Itemloja tlj = dataSnapshot.getValue(Itemloja.class);
                    if (dataSnapshot.exists()){
                        Log.i("LOJA","myLOJA "+tlj.Nome);
                        Chat chat = new Chat();
                        ImageView icon =(ImageView) findViewById(R.id.imgchatbar);
                        chat.getMychatnewmsg(tlj.Nome,icon);


                        myloja = tlj;
                        getCesta();
                        barBottom();
                    }else {


                        Log.i("LOJA","myUser "+myUser.Email);
                        getchat();
                        getCesta();
                        barBottom();

                    }
                    lista();;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            gettopicsisncritos();
        }else{
            ImageView btnChat= (ImageView) findViewById(R.id.imgchatbar);
            btnChat.setBackgroundResource(R.drawable.commentoff);
            lista();;
            barBottom();
        }

    }


    public void gettopicsisncritos(){

        listainscritosfav.clear();
        lojasfav.clear();

        database.child("UsersClient").child(myUser.Uid).child("topicos").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    listainscritosfav.add(String.valueOf(postSnapshot.getValue()));
                    lojasfav.add(String.valueOf(postSnapshot.getValue()));
                }

                lista();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();
    }


    public void listafav (){
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


    public void barBottom(){
        View  vfav = (View) findViewById(R.id.includechatmain);

        Button btncasas= (Button) vfav.findViewById(R.id.btncasas);

        btncasas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentMain = new Intent(MainActivity_lojas.this ,
                        MainActivity_lojas.class);
                ctrlservice=true;

                MainActivity_lojas.this.startActivity(intentMain);
            }

        });

        ImageView imgfav = (ImageView) vfav.findViewById(R.id.imgfav);
        ImageView imgcestabar = (ImageView) vfav.findViewById(R.id.imgcestabar);

        if (MainActivity.myPerfil!=null) {

            imgfav.setBackgroundResource(R.drawable.enviandonav);
            TextView txt = (TextView) findViewById(R.id.txtfavbabrb);
            txt.setText("Delivery");

            imgcestabar.setBackgroundResource(R.drawable.office);
            TextView txtc = (TextView) findViewById(R.id.txtbarbbcesta);
            txtc.setText("Empresa");
        }



        Button btnfav= (Button) vfav.findViewById(R.id.btnfav);
        btnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("super","super lista" );
                if (MainActivity.myPerfil==null) {
                    if (MainActivity.lojasfav.size() > 0) {
                        Log.i("super", "super lista");
                        ctrlfav = true;
                        MainActivity.lojasf.clear();
                        lista()
                        ;
                    } else {
                        Toast.makeText(c, "Sem favoritos", Toast.LENGTH_SHORT).show();
                    }
                }else
                {


                    Intent intentMain = new Intent(MainActivity_lojas.this ,
                            MainActivity_delivery.class);
                    MainActivity_lojas.this.startActivity(intentMain);
                }
            }

        });

        Button btncesta= (Button) vfav.findViewById(R.id.btncestabar);
        btncesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.myPerfil == null) {
                    if (MainActivity.mycesta.size() > 0) {
                        Intent intentMain = new Intent(MainActivity_lojas.this,
                                MainActivity_cesta.class);
                        MainActivity_lojas.this.startActivity(intentMain);
                    } else {
                        Toast.makeText(c, "Sem itens na cesta", Toast.LENGTH_SHORT).show();
                    }
                }
                else

                {
                    Intent intentMain = new Intent(MainActivity_lojas.this,
                            MainActivity_dashempresa.class);
                    MainActivity_lojas.this.startActivity(intentMain);
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
            }
        });

        Button btnChat= (Button) vfav.findViewById(R.id.btnchat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myUser!=null) {
                    if (MainActivity.listalojaschat.size() > 0 && !MainActivity.nomeuser.equals("LOJA")) {
                        Intent intentMain = new Intent(MainActivity_lojas.this,
                                MainActivity_chat.class);
                        ctrlservice=true;

                        MainActivity_lojas.this.startActivity(intentMain);
                    } else {
                        if (MainActivity.nomeuser.equals("LOJA")) {
                            Intent intentMain = new Intent(MainActivity_lojas.this,
                                    MainActivity_chat.class);
                            ctrlservice=true;

                            MainActivity_lojas.this.startActivity(intentMain);
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


}
