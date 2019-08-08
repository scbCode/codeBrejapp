package brejapp.com.brejapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity_delivery extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {

    public ListView listitem;
    public ListView listapedidos;
    Context c;
    static  boolean ctrlfav=false;
    static ListView listafav;
    static ArrayList<Itemcesta> mycesta = new ArrayList<Itemcesta>();
    static GoogleMap mapa;
    static DatabaseReference database;
    static ArrayList<Itemloja> lojas = new ArrayList<Itemloja>();
    boolean ctrlbttopic;
    User myUser;
    static FirebaseListAdapter listB;
    int counter;
    int totalHeight;
    Item itemclicado;
    static boolean crtltopic;
    static int sc=0;
    static boolean crtltopicmove;
    ArrayList<String> topicosinscritosuser= new ArrayList<String>();
    boolean  ctrlservice;
    static ArrayList<PedidoReceiver> listapedds =  new ArrayList<>();
    static ArrayList<String> bairrosvals=  new ArrayList<>();
    static ArrayList<String> listausers = new ArrayList<>();
    static ArrayList<String> bairroslist =  new ArrayList<>();
    static ArrayList<String> idspedido =  new ArrayList<>();


    static RelativeLayout load;
    static  int mystar=-1;
    static Empresa myPerfil;

    static boolean ctrlcheckuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_delivery);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        c = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (MainActivity.myPerfil!=null){
            final Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_status).setVisible(true);

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

        }


        syncFirebase();

    }


    public void syncFirebase() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");

        if (user!=null){
            getloja( );
        }
    }

    public void getbairros(Empresa loja){


        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("BairrosLojas")
                .child(loja.Nome+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bairrosvals.clear();
                bairroslist.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    bairrosVal bairro= postSnapshot.getValue(bairrosVal.class);
                    bairroslist.add(bairro.Nome);
                    bairrosvals.add(bairro.Valor);
                }
                listapedidos();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })   ;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (ctrlfav==true){
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlfav);
                rl.setVisibility(View.INVISIBLE);
                ctrlfav=false;
            }
            else
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

        }else        if (id == R.id.nav_camera) {
            Intent intentMain = new Intent(MainActivity_delivery.this ,
                    MainActivity.class);
            MainActivity_delivery.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_delivery.this ,
                    MainActivity_lojas.class);
            MainActivity_delivery.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_delivery.this ,
                    MainActivity_categorias.class);
            MainActivity_delivery.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_delivery.this ,
                    MainActivity_topicos.class);
            MainActivity_delivery.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_delivery.this ,
                    MainActivity_contato.class);
            MainActivity_delivery.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_delivery.this ,
                    MainActivity_empresa.class);
            MainActivity_delivery.this.startActivity(intentMain);
        }

        else if (id == R.id.nav_cesta) {
            if (MainActivity.myUser!=null && MainActivity.mycesta.size()>0){
                Intent intentMain = new Intent(MainActivity_delivery.this ,
                        MainActivity_cesta.class);

                MainActivity_delivery.this.startActivity(intentMain);
            }
            else{
                Toast.makeText(c,"Cesta vazia",Toast.LENGTH_SHORT).show();
            }
        }  else if (id == R.id.nav_mypedidos) {
            Intent intentMain = new Intent(MainActivity_delivery.this ,
                    MainActivity_delivery.class);
            MainActivity_delivery.this.startActivity(intentMain);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getloja( ) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database.child("UsersEmpresa")
                .child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Empresa tlj = dataSnapshot.getValue(Empresa.class);
                myPerfil = tlj;
                getbairros(myPerfil);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        ctrlservice = false;
        Chat_service.cancelntf = true;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent servicechat = new Intent(getApplicationContext(), Pedido_service.class);
        stopService(servicechat);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();


        if (myPerfil!=null)
            if (MainActivity.myloja!=null && MainActivity.myPerfil.status==true) {

                Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
                startService(servicechat);
                Intent servicedel = new Intent(getApplicationContext(), Pedido_service.class);
                servicedel.putExtra("loja", myPerfil.Nome);
                Pedido_service.cancelntfdelivery=false;
                startService(servicedel);
                Log.i("Desctroy","Destroy SERVICE");

            }

    }


    @Override
    public void onPause(){
        super.onPause();

        Chat_service.cancelntf=false;

        if (MainActivity.myPerfil!=null){
            if (MainActivity .myPerfil!=null && MainActivity.myPerfil.status==true && ctrlservice==false ) {

                Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
                servicechat.putExtra("loja", myPerfil.Nome);
                startService(servicechat);

                Intent servicedel = new Intent(getApplicationContext(), Pedido_service.class);
//                servicedel.putExtra("loja", myPerfil.Nome);
                startService(servicedel);

            }
        }


    }


    public void listapedidos()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database.child("LojasPedidos")
                .child(myPerfil.Nome).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listapedds.clear();
                listausers.clear();
                idspedido.clear();
                for (DataSnapshot posttusrs : dataSnapshot.getChildren()){

                    for (DataSnapshot pedidosc : posttusrs.child("Pedidos").getChildren()){
                        Log.i("Pedido","Pedido 0 "+posttusrs.getKey());
                        listausers.add(posttusrs.getKey().toString());

                        PedidoReceiver pr = pedidosc.getValue(PedidoReceiver.class);

                        if (!pr.status.equals("finalizadoaceito") && !pr.status.equals("recusadofinal")) {listapedds.add(pr);
                        idspedido.add(pedidosc.getKey());}

                    }
                }

                listitem = (ListView) findViewById(R.id.listapedidos);
                final adapterPedidos adapter = new adapterPedidos(c, R.layout.lista_delivery, listapedds);
                listitem.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }





}
