package brejapp.com.brejapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
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
import brejapp.com.brejapp.R;


public class MainActivity_categorias extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    public ListView listitem;
    public ListView listacatitem;
    public ListView listsuper;
    Context c;
    static GoogleMap mapa;
    static DatabaseReference database;
    static ArrayList<Itemloja> lojas = new ArrayList<Itemloja>();
    boolean ctrlbttopic;
    User myUser;
    static FirebaseListAdapter listB;
    int counter;
    int totalHeight;
    Item itemclicado;
    Itemloja getloja;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categorias);
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

        Button btnreturnperfil = (Button) findViewById(R.id.btnreturnloja);
        btnreturnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetperfillayout();
            }
        });
        Button btnmapa = (Button) findViewById(R.id.btnmapa);
        btnmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout mapa = (RelativeLayout) findViewById(R.id.mapa);
                mapa.setVisibility(View.VISIBLE);

            }
        });

        //BOTAO FECHAR
        Button btnfecharinfor = (Button) findViewById(R.id.btnfecharinfoitem);
        btnfecharinfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rlinfoitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
                rlinfoitem.setVisibility(View.INVISIBLE);
            }

        });


        ImageView btnreturnlistacat = (ImageView) findViewById(R.id.btnreturnlistacat);
        btnreturnlistacat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout rl = (RelativeLayout) findViewById(R.id.listaItemCat);
                rl.setVisibility(View.INVISIBLE);
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
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }

        });


    }

    public void checkpermissaocallphone(){
        if (ContextCompat.checkSelfPermission(MainActivity_categorias.this,
                Manifest.permission.CALL_PHONE  )
                != PackageManager.PERMISSION_GRANTED  ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_categorias.this,
                    Manifest.permission.CALL_PHONE ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity_categorias.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }



    //
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
            database = MainActivity.datab.getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");
        }
        getSupermercados();
        authentic();
        lista ();

    }


    public void lista (){


        listitem = (ListView) findViewById(R.id.listaCategoria);

        RelativeLayout ltload = (RelativeLayout) findViewById(R.id.layoutloadpromo);
        ltload.setVisibility(View.INVISIBLE);
        database.child("CategoriaProdutos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null){

                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseListAdapter<itemCategoria>  itemlistsuper = new FirebaseListAdapter<itemCategoria>(c, itemCategoria.class, R.layout.item_categoria,database.child("CategoriaProdutos")) {

            @Override
            protected void populateView(final View vw, final itemCategoria model, final int position) {


                int posit=0;


                TextView nome = (TextView) vw.findViewById(R.id.nomeCat);
                nome.setText(""+model.nome);

                ImageView img = (ImageView) vw.findViewById(R.id.iconCat);
                Glide.with(c).load(model.icon).into(img);


                vw.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        listaitens(model.nome);


                    };


                });


            }

        };

        listitem.setAdapter(itemlistsuper);

    }



    public void clickitem(Item item) {
        itemclicado = item;

        Log.i("LOOP", "clickitem  ");
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
            txtdatainfo.setText("Oferta válida de " + dateInicio + " até " + dateInexp +" \nOu enquanto durar o estoque.");

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



    public void resetperfillayout(){

        RelativeLayout layoutloja = (RelativeLayout) findViewById(R.id.layoutLoja);
        layoutloja.setVisibility(View.INVISIBLE);

        TextView txtNomeLoja = (TextView) findViewById(R.id.NomeLoja);
        txtNomeLoja.setText("");

        TextView txtdescricaoloja = (TextView) findViewById(R.id.descricaoloja);
        txtdescricaoloja.setText("");

        ImageView imgbaner = (ImageView) findViewById(R.id.bannerloja);
        imgbaner.setImageDrawable(null);

    }


    public void listaitens(final String categoria){


        listacatitem = (ListView) findViewById(R.id.listaitemporcat);


        database.child("Catalogos").orderByChild("Tipo").equalTo(categoria).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null){
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.listaItemCat);
                    rl.setVisibility(View.VISIBLE);
                    RelativeLayout rlOAD = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                    rlOAD.setVisibility(View.INVISIBLE);
                    TextView txtnome =(TextView) findViewById(R.id.nomecateg);
                    txtnome.setText(""+categoria);
                }
                else {

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.listaItemCat);
                    rl.setVisibility(View.INVISIBLE);
                    Toast.makeText(c,"Categoria sem item",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseListAdapter listB = new FirebaseListAdapter<Item>(c, Item.class, R.layout.listhj,database.child("Catalogos").orderByChild("Tipo").equalTo(categoria)) {

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
                        clickitem(model);

                    }
                });


            }
        };
        listacatitem.setAdapter(listB);
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
            Intent intentMain = new Intent(MainActivity_categorias.this ,
                    MainActivity.class);
            MainActivity_categorias.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_categorias.this ,
                    MainActivity_lojas.class);
            MainActivity_categorias.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_categorias.this ,
                    MainActivity_categorias.class);
            MainActivity_categorias.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_categorias.this ,
                    MainActivity_topicos.class);
            MainActivity_categorias.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_categorias.this ,
                    MainActivity_contato.class);
            MainActivity_categorias.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_categorias.this ,
                    MainActivity_empresa.class);
            MainActivity_categorias.this.startActivity(intentMain);
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
            txtdatainfo.setText("Oferta válida de " + dateInicio + " até " + dateInexp +" \nOu enquanto durar o estoque.");

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
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        adv.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + i1+"share.jpg");

        try {
            f.createNewFile();
            new FileOutputStream(f).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse( Environment.getExternalStorageDirectory()+ File.separator+ i1+"share.jpg"));
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

    public void checkpermissao(){

        if (ContextCompat.checkSelfPermission(MainActivity_categorias.this,
                Manifest.permission.READ_EXTERNAL_STORAGE  )
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity_categorias.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE  )
                != PackageManager.PERMISSION_GRANTED  ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_categorias.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity_categorias.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }




    ///////////////////////////////////////////////
    ///////////////////////////////////////////////
    //CONFIG PERFIL LOJA

    public void PerfilLoja(final Itemloja loja){

        getloja=loja;

        listsuper = (ListView) findViewById(R.id.listProdutos);

        RelativeLayout layoutitem = (RelativeLayout) findViewById(R.id.rlinfoItem);
        layoutitem.setVisibility(View.INVISIBLE);

        RelativeLayout layoutloja = (RelativeLayout) findViewById(R.id.layoutLoja);
        layoutloja.setVisibility(View.VISIBLE);

        TextView txtNomeLoja = (TextView) findViewById(R.id.NomeLoja);
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
                        TextView txtdescricaoloja = (TextView) findViewById(R.id.totalinscritos);
                        txtdescricaoloja.setText("Total de inscritos: " +listainscritos.size());
                        perfilconfig(loja,listainscritos);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void perfilconfig(final Itemloja loja, ArrayList<String> listainscritos){



        ViewGroup.LayoutParams params = listsuper.getLayoutParams();
        totalHeight = 0;
        params.height = totalHeight;
        listsuper.setLayoutParams(params);
        listsuper.requestLayout();



        final String time = ""+new Date().getTime();
        final   Button btncadastro = (Button) findViewById(R.id.btncadastro);


        btncadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                consloadwindow.setVisibility(View.VISIBLE);
                login(loja);
            }
        });


        final   Button btnrecsenha = (Button) findViewById(R.id.btnrecupersenha);
        btnrecsenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtemail = (EditText)findViewById(R.id.emailuser);
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

        final   Button BTNCLOSE = (Button) findViewById(R.id.btnclosecads);
        BTNCLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rlpop = (RelativeLayout) findViewById(R.id.popcadastro);
                rlpop.setVisibility(View.INVISIBLE);
            }
        });

        final   Button btntopico = (Button) findViewById(R.id.btninscrevase);
        ctrlbttopic=false;

        for(int ii = 0; ii < listainscritos.size();ii++) {

            if (myUser!=null)
                if (listainscritos.get(ii).equals(myUser.Email))
                {
                    btntopico.setText("Inscrito");
                    btntopico.setBackgroundResource(R.drawable.rect_08);
                    ctrlbttopic=true;
                }
        }

        if (ctrlbttopic == false){
            btntopico.setText("Inscreva-se");
            btntopico.setBackgroundResource(R.drawable.rect_05);
        }
        Log.i("INSCRITO ","INSCRITO "+listainscritos.size());

        btntopico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                consloadwindow.setVisibility(View.VISIBLE);
                if (ctrlbttopic == false){
                    if (myUser == null) {
                        RelativeLayout rlpop = (RelativeLayout) findViewById(R.id.popcadastro);
                        rlpop.setVisibility(View.VISIBLE);
                        consloadwindow.setVisibility(View.INVISIBLE);

                    }else {
                        inscrever(loja);
                        ctrlbttopic=true;
                    }}else
                if (ctrlbttopic==true){
                    desinscrever(loja);
                    ctrlbttopic=false;

                }
            }
        });

        ImageView imgbaner = (ImageView) findViewById(R.id.imglogoemp);
        Glide.with(c).load(loja.urlic).into(imgbaner);

        database.child("PerfilLojas").child(loja.cod+"").child("urlBanner").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    ImageView imgbaner = (ImageView) findViewById(R.id.bannerloja);
                    Glide.with(c).load(dataSnapshot.getValue()).into(imgbaner);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        listB = new FirebaseListAdapter<Item>(c, Item.class, R.layout.listhj, database.child("Catalogos").orderByChild("supermercado").equalTo(""+loja.Nome)) {

            @SuppressLint("WrongConstant")
            @Override
            protected void populateView(final View vw, final Item model, final int position) {

                vw.setBackgroundColor(0xefefef);
                boolean ctral = false;

                counter= listB.getCount();

                ViewGroup.LayoutParams params = listsuper.getLayoutParams();
                totalHeight = vw.getMeasuredHeight() * listB.getCount();
                params.height = totalHeight;
                listsuper.setLayoutParams(params);
                listsuper.requestLayout();

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
                        clickitem(model);

                    }
                });


            }
        };
        listsuper.setAdapter(listB);


        localmaps(loja);
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
                        database.child("Topicos").child("Lojas").child(loja.Nome).child("inscritos").child(finalEmail).setValue(myUser.Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Inscrito em " + loja.Nome, Toast.LENGTH_LONG).show();
                                btntopico.setText("Inscrito");
                                btntopico.setBackgroundResource(R.drawable.rect_08);
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

    public void desinscrever(final Itemloja loja){


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
                database.child("UsersClient").child(myUser.Uid).child("topicos").child(loja.Nome).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.child("Topicos").child("Lojas").child(loja.Nome).child("inscritos").child(finalEmail).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(c, "Tópico removido: " + loja.Nome, Toast.LENGTH_LONG).show();
                                btntopico.setText("Inscreva-se");
                                btntopico.setBackgroundResource(R.drawable.rect_05);
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

        EditText Temail = (EditText) findViewById(R.id.emailuser);
        String email = Temail.getText().toString();

        EditText Tpsw = (EditText) findViewById(R.id.pwsuser);
        String password = Tpsw.getText().toString();


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
                RelativeLayout rlpop = (RelativeLayout) findViewById(R.id.popcadastro);
                rlpop.setVisibility(View.INVISIBLE);
                inscrever(loja);
            }

        });
    }

    private void login(final Itemloja loja){

        EditText Temail = (EditText) findViewById(R.id.emailuser);
        String email = Temail.getText().toString();

        EditText Tpsw = (EditText) findViewById(R.id.pwsuser);
        String password = Tpsw.getText().toString();

        if (password.length() > 0 && email.length() > 0  ) {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                RelativeLayout rlpop = (RelativeLayout) findViewById(R.id.popcadastro);
                                rlpop.setVisibility(View.INVISIBLE);


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

                                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                                consloadwindow.setVisibility(View.INVISIBLE);

                            }


                            // ...
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

            gettopicsisncritos();
        }else{


        }
    }

    public void gettopicsisncritos(){
        database.child("UsersClient").child(myUser.Uid).child("topicos").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void localmaps(Itemloja loja){



        try {
            JSONArray ob = new JSONArray(String.valueOf(loja.coordenadas));
            for (int i = 0; i < ob.length(); i++) {

                Double lt = (Double) ob.getJSONObject(i).get("lat");
                Double lg = (Double) ob.getJSONObject(i).get("lng");

                LatLng local = new LatLng(lt, lg);
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lt,lg), 13));
                mapa.addMarker(new MarkerOptions()
                        .title("" + loja.Nome)
                        .position(local)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmarkercart))
                );

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getSupermercados() {


        database.child("PerfilLojas").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {


                Gson gson = new Gson();
                String json = "";
                boolean ctrl=false;

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


    //CONFIG PERFIL LOJA FINAL
    ///////////////////////////////////////////////
    ///////////////////////////////////////////////
}
