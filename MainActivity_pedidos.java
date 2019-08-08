package brejapp.com.brejapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import java.util.Collections;
import java.util.Date;


public class MainActivity_pedidos extends AppCompatActivity
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
    static ArrayList<String> bairroslist =  new ArrayList<>();
    static ArrayList<String> bairrosvals=  new ArrayList<>();
    static ArrayList<String> listalojaschat = new ArrayList<>();
    static FirebaseListAdapter  feed;
    static ImageView star;
    static ImageView stara;
    static ImageView starb;
    static ImageView starc;
    static Button entregue;
    static RelativeLayout rlaval;
    static ArrayList<ImageView> liststar= new ArrayList<>();
    static RelativeLayout load;
    static  int mystar=-1;

    static boolean ctrlcheckuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pedidos);
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
        Log.i("ERROR","user "+user);

        if (user!=null){
            getCesta();
            barBottom();
            getchat();
            getbairros();
        }
    }


    public void getbairros( ){


        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("PerfilLojas")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bairroslist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Itemloja_cartoes item = postSnapshot.getValue(Itemloja_cartoes.class);
                    postSnapshot.getRef().child("Bairros")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                        bairrosVal bairro= postSnapshot.getValue(bairrosVal.class);

                                        bairroslist.add(bairro.Nome);
                                        bairrosvals.add(bairro.Valor);

                                    }
                                }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                        });

                }
                openlist();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void openlist(){


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        listapedidos = (ListView) findViewById(R.id.listapedidos);
        String email=user.getEmail();


        for (int i=0;i<email.length();i++){
            email=email.replace(".","AAA");
            email=email.replace("@","BBB");
        }

        final String finalEmail1 = email;
        database  .child("UsersClient").child(user.getUid()).child("Pedidos").orderByChild("status")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {

                            final PedidoReceiver pr = postSnapshot1.getValue(PedidoReceiver.class);
                            if (pr.status.equals("solicitando")){

                                database.child("LojasPedidos").child(pr.loja).child(finalEmail1).child("Pedidos").
                                        addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            final PedidoReceiver prx = postSnapshot.getValue(PedidoReceiver.class);

                                            if (postSnapshot.getKey().equals(postSnapshot1.getKey()))
                                               if (pr.status.equals("solicitando")||pr.status.equals("recebido")||pr.status.equals("acaminho")){

                                                    Log.i("ERROR","STATUS "+prx.status);

                                                database  .child("UsersClient").child(user.getUid()).child("Pedidos")
                                                        .child(postSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            database  .child("UsersClient").child(user.getUid()).child("Pedidos")
                                                                    .child(postSnapshot1.getKey()).child("status").setValue(prx.status);
                                                        }else {
                                                            database  .child("UsersClient").child(user.getUid()).child("Pedidos")
                                                                    .child(postSnapshot1.getKey()).removeValue();
                                                        }
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
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


           feed = new FirebaseListAdapter<PedidoReceiver>(c, PedidoReceiver.class, R.layout.lista_pedidos,database  .child("UsersClient").child(user.getUid()).child("Pedidos").orderByChild("timestamp")) {
            @Override
            protected void populateView(View v, final PedidoReceiver model, final int position) {

                TextView nome = (TextView) v.findViewById(R.id.txtnomeloja);
                TextView nmero = (TextView) v.findViewById(R.id.txtnumpedido);
                TextView hora = (TextView) v.findViewById(R.id.txthorapedido);
                TextView total = (TextView) v.findViewById(R.id.txttotalpedido);
                TextView fret = (TextView) v.findViewById(R.id.txtfret);
                TextView txtstt = (TextView) v.findViewById(R.id.txtstt);

                TextView statusenv = (TextView) v.findViewById(R.id.txtenviadoped);
                TextView statusrec = (TextView) v.findViewById(R.id.pedidoaceitoped);
                TextView statusacam = (TextView) v.findViewById(R.id.txtacaminhoped);

                liststar.clear();
                final ImageView    star = (ImageView) v.findViewById(R.id.star1);
                final ImageView stara = (ImageView)  v.findViewById(R.id.star2);
                final ImageView  starb = (ImageView)  v.findViewById(R.id.star3);
                final ImageView starc = (ImageView) v. findViewById(R.id.star4);
                final RelativeLayout rlaval = (RelativeLayout) v. findViewById(R.id.rlavalie);

                liststar.add(star);
                liststar.add(stara);
                liststar.add(starb);
                liststar.add(starc);

                String email = user.getEmail();
                for (int i = 0;i < email.length();i++){
                    email=   email.replace(".","AAA");
                    email=  email.replace("@","BBB");
                }

                if (model!=null)
            if  (user.getUid() !=null)
                database.child("UsersClient").child(uid).child("Level").child(model.loja).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            int id =Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                            mystar =id;
                            for (int i =0;i <=id;i++) {
                              if (i==0)
                                star.setBackgroundResource(R.drawable.starx);
                                if (i==1)
                                    stara.setBackgroundResource(R.drawable.starx);
                                if (i==2)
                                    starb.setBackgroundResource(R.drawable.starx);
                                if (i==3)
                                    starc.setBackgroundResource(R.drawable.starx);

                            }
                        }else {
                            mystar=-1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final String finalEmail5 = email;

                Button BTNREMOVE = (Button) v. findViewById(R.id.btnremov);
                BTNREMOVE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRef(position).removeValue();
                    }
                });

                Button entregue = (Button) v. findViewById(R.id.btnentregue);
                entregue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stt ="";
                        if (model.status.equals("RECUSADO")==true)
                        stt="RECUSADO";
                        else
                        stt ="entregue";


                        if (stt.equals("entregue")){
                            database.child("LojasPedidos").child(model.loja).child(finalEmail5).child("Pedidos").child(getRef(position).getKey())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists())
                                                database.child("LojasPedidos").child(model.loja).child(finalEmail5).child("Pedidos").child(getRef(position).getKey()).child("status").setValue("entregue");

                                            getRef(position).removeValue();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                        else
                        {
                            getRef(position).removeValue();
                            database.child("LojasPedidos").child(model.loja).child(finalEmail5).child(getRef(position).getKey()).removeValue();
                        }

                    }
                });

                for (int i =0;i<bairroslist.size();i++) {
                    if (bairroslist.get(i).equals(model.bairro))
                    fret.setText("Frete R$ "+bairrosvals.get(i));
                }

                nome.setText(model.loja);
                nmero.setText("N° "+(position+1));
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(String.valueOf(model.timestamp)));
                Date resultdateExp = new Date(calendar.getTimeInMillis());//GET DATE EXP.
                String dateInexp = formatter.format(resultdateExp);
                double t = Double.parseDouble(model.total);

                String tt=  String.format("%.2f", t);

                total.setText("Total: R$ "+tt);

                ImageView img = (ImageView) v.findViewById(R.id.imgstatus);
                Log.i("ERROR","ERROR "+model.status);
                if (model.status.equals("RECUSADO")==true || model.status.equals("recusadofinal")) {
                    statusrec.setVisibility(View.INVISIBLE);
                    img.setColorFilter(0xFFCC0000);
                    statusenv.setVisibility(View.VISIBLE);
                    txtstt.setText("");
                    statusacam.setVisibility(View.INVISIBLE);
                    entregue.setVisibility(View.GONE);
                    entregue.setText("APAGAR");
                    rlaval.setVisibility(View.GONE);
                    BTNREMOVE.setVisibility(View.VISIBLE);
                    hora.setText("recusado às "+dateInexp);
                    statusenv.setText ("Por algum motivo a loja não poderá atender este pedido.");
                    statusenv.setTextColor(0xFFCC0000);
                    statusrec.setText("Status: RECUSADO");
                    statusacam.setText("RECUSADO");
                }else
                if (model.status.equals("solicitando")==true){
                    statusenv.setVisibility(View.VISIBLE);

                    statusrec.setVisibility(View.INVISIBLE);
                    statusacam.setVisibility(View.INVISIBLE);
                    hora.setText("enviado às "+dateInexp);
                    img.setColorFilter(0xFFFFFFF);
                    statusenv.setText("Status:  Solicitando");
                    BTNREMOVE.setVisibility(View.GONE);
                    entregue.setVisibility(View.GONE);
                    rlaval.setVisibility(View.GONE);
                    img.setImageResource(R.drawable.linhatempoenviado);}
                else
                if (model.status.equals("recebido")==true){
                    statusenv.setVisibility(View.INVISIBLE);
                    statusrec.setVisibility(View.VISIBLE);
                    statusenv.setVisibility(View.INVISIBLE);
                    statusacam.setVisibility(View.INVISIBLE);
                    hora.setText("recebido às "+dateInexp);
                    entregue.setVisibility(View.GONE);
                    rlaval.setVisibility(View.GONE);
                    img.setColorFilter(0xFFFFFFF);
                    statusenv.setText("Status do pedido: Aceito");
                    BTNREMOVE.setVisibility(View.GONE);
                    img.setImageResource(R.drawable.linhatemporecebido);}
                else
                if (model.status.equals("acaminho")==true || model.status.equals("finalizadoaceito")){
                    statusenv.setVisibility(View.INVISIBLE);
                    statusrec.setVisibility(View.INVISIBLE);
                    statusenv.setVisibility(View.INVISIBLE);
                    statusacam.setVisibility(View.VISIBLE);
                    img.setColorFilter(0xFFFFFFF);
                    statusenv.setText("Status do pedido: Á caminho");
                    BTNREMOVE.setVisibility(View.GONE);
                    hora.setText("Saiu às "+dateInexp);
                    img.setImageResource(R.drawable.linhatempofinal);
                    entregue.setVisibility(View.VISIBLE);
                    rlaval.setVisibility(View.VISIBLE);
                    star.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setlevel(model.loja,"0");
                            Log.i("clickstar","clickstar 0" );
                            star.setBackgroundResource(R.drawable.starx);
                            stara.setBackgroundResource(R.drawable.starxoff);
                            starb.setBackgroundResource(R.drawable.starxoff);
                            starc.setBackgroundResource(R.drawable.starxoff);

                        }
                    });

                    stara.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setlevel(model.loja,"1");
                            Log.i("clickstar","clickstar 1" );
                            star.setBackgroundResource(R.drawable.starx);
                            stara.setBackgroundResource(R.drawable.starx);
                            starb.setBackgroundResource(R.drawable.starxoff);
                            starc.setBackgroundResource(R.drawable.starxoff);

                        }
                    });

                    starb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setlevel(model.loja,"2");
                            Log.i("clickstar","clickstar 2" );
                            star.setBackgroundResource(R.drawable.starx);
                            stara.setBackgroundResource(R.drawable.starx);
                            starb.setBackgroundResource(R.drawable.starx);
                            starc.setBackgroundResource(R.drawable.starxoff);

                        }
                    });


                    starc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setlevel(model.loja,"3");
                            Log.i("clickstar","clickstar 03" );
                            star.setBackgroundResource(R.drawable.starx);
                            stara.setBackgroundResource(R.drawable.starx);
                            starb.setBackgroundResource(R.drawable.starx);
                            starc.setBackgroundResource(R.drawable.starx);

                        }
                    });

                }
            }
        };
        listapedidos.setAdapter(feed);
    }


    public void setlevel(String Loja, String star){

        final FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();
        load = (RelativeLayout) findViewById(R.id.layoutloadpromo);
        load.setVisibility(View.VISIBLE);

        String email = user.getEmail();

        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }

        database.child("Level").child(Loja).child(star).child(email).setValue(user.getEmail());
        database.child("Level").child(Loja).child(String.valueOf(mystar)).child(email).removeValue();
        database.child("UsersClient").child(user.getUid()).child("Level").child(Loja).setValue(star);

        mystar= Integer.parseInt(star);

        Toast.makeText(c,"Loja avaliada " + (1+mystar) + " estrelas",Toast.LENGTH_SHORT).show();

        int idstar = Integer.parseInt(star);


        load.setVisibility(View.INVISIBLE);

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
            {   Intent intentMain = new Intent(MainActivity_pedidos.this ,
                    MainActivity.class);
                MainActivity_pedidos.this.startActivity(intentMain);}
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
            Intent intentMain = new Intent(MainActivity_pedidos.this ,
                    MainActivity.class);
            MainActivity_pedidos.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_pedidos.this ,
                    MainActivity_lojas.class);
            MainActivity_pedidos.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_pedidos.this ,
                    MainActivity_categorias.class);
            MainActivity_pedidos.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_pedidos.this ,
                    MainActivity_topicos.class);
            MainActivity_pedidos.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_pedidos.this ,
                    MainActivity_contato.class);
            MainActivity_pedidos.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_pedidos.this ,
                    MainActivity_empresa.class);
            MainActivity_pedidos.this.startActivity(intentMain);
        }

        else if (id == R.id.nav_cesta) {
            if (MainActivity.myUser!=null && MainActivity.mycesta.size()>0){
                Intent intentMain = new Intent(MainActivity_pedidos.this ,
                        MainActivity_cesta.class);

                MainActivity_pedidos.this.startActivity(intentMain);
            }
            else{
                Toast.makeText(c,"Cesta vazia",Toast.LENGTH_SHORT).show();
            }
        }  else if (id == R.id.nav_mypedidos) {
            Intent intentMain = new Intent(MainActivity_pedidos.this ,
                    MainActivity_pedidos.class);
            MainActivity_pedidos.this.startActivity(intentMain);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void barBottom(){

        View  vfav = (View) findViewById(R.id.includechatmain);



        Button btncasas= (Button) findViewById(R.id.btncasas);
        btncasas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentMain = new Intent(MainActivity_pedidos.this ,
                        MainActivity_lojas.class);
                MainActivity_pedidos.this.startActivity(intentMain);
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


                    Intent intentMain = new Intent(MainActivity_pedidos.this ,
                            MainActivity_delivery.class);
                    MainActivity_pedidos.this.startActivity(intentMain);
                }
            }

        });

        Button btncesta= (Button) vfav.findViewById(R.id.btncestabar);
        btncesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.myPerfil == null) {
                    if (MainActivity.mycesta.size() > 0) {
                        Intent intentMain = new Intent(MainActivity_pedidos.this,
                                MainActivity_cesta.class);
                        MainActivity_pedidos.this.startActivity(intentMain);
                    } else {
                        Toast.makeText(c, "Sem itens na cesta", Toast.LENGTH_SHORT).show();
                    }
                }
                else

                {
                    Intent intentMain = new Intent(MainActivity_pedidos.this,
                            MainActivity_dashempresa.class);
                    MainActivity_pedidos.this.startActivity(intentMain);
                }
            }
        });

        Button btnCAD= (Button) findViewById(R.id.btncadastrocad);
        final MainActivity main = new MainActivity();

        btnCAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.login(null);
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
                if (MainActivity.myUser!=null) {
                    if (MainActivity.listalojaschat.size() > 0 && !MainActivity.nomeuser.equals("LOJA")) {
                        Intent intentMain = new Intent(MainActivity_pedidos.this,
                                MainActivity_chat.class);
                        MainActivity_pedidos.this.startActivity(intentMain);
                    } else {
                        if (MainActivity.nomeuser.equals("LOJA")) {
                            Intent intentMain = new Intent(MainActivity_pedidos.this,
                                    MainActivity_chat.class);
                            MainActivity_pedidos.this.startActivity(intentMain);
                        }else {
                            Toast.makeText(c,"Chat sem conversas",Toast.LENGTH_SHORT).show();
                        }

                    }
                }else {
                    MainActivity.loginsimples=true;

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.popcadastrouser);
                    rl.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
            if (MainActivity.nomeuser.equals("user")&& MainActivity.listalojaschat.size()>0){
                Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
                servicechat.putExtra("usermsg","true");
                startService(servicechat);
                Intent servicedelx = new Intent(getApplicationContext(), Pedido_service.class);
                startService(servicedelx);

            }


    }


    @Override
    public void onPause(){
        super.onPause();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
        Chat_service.cancelntf=false;

            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            servicechat.putExtra("usermsg","true");
            startService(servicechat);
        Intent servicedelx = new Intent(getApplicationContext(), Pedido_service.class);
        startService(servicedelx);
        }
    }


    public void getchat(){


        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersClient")
                .child(MainActivity.myUser.Uid).child("chat").child("lojasOn").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listalojaschat.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                       listalojaschat.add(String.valueOf(postSnapshot.getValue()));

                }

                if (listalojaschat.size()>0){
                    Chat chat = new Chat();
                    View v = (View) findViewById(R.id.includechatmain);
                    Log.i("CHECKMSG","listalojaschat "+listalojaschat.get(0));
                    ImageView imgbarchat =(ImageView) v.findViewById(R.id.imgchatbar);
                    imgbarchat.setBackgroundResource(R.drawable.comment);
                    chat.cont=0;
                    chat.checknewmsguseroutclass(imgbarchat,listalojaschat);
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


    public void lista (){

        Log.i("super","super lista" );
        MainActivity.lojasf.clear();
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

                        for(int i =0;i< MainActivity.lojasfav.size();i++){

                            if (MainActivity.lojasfav.get(i).equals(l.Nome)){

                                Log.i("super","super x"+l.Nome);
                                MainActivity.lojasf.add(l);

                            }
                        }
                    }

                    adapterLojasfav   adapter = new adapterLojasfav(c, R.layout.itemlojas, MainActivity.lojasf);
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


    public void getCesta(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        if (user.getUid()!=null)

            database.child("CestasClientes").child(user.getUid()).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mycesta.clear();

                    lojas.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                            Itemcesta i = postSnapshot1.getValue(Itemcesta.class);
                            mycesta.add(i);
                            Log.i("lojas","lojas "+i.NomeProduto    );
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }



    @Override
    public void onResume(){
        super.onResume();


    }

}
