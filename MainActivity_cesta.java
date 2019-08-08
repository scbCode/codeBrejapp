package brejapp.com.brejapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.L;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity_cesta extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {
    static ListView listitem;
    public ListView listsuper;
   static   Context c;
    static DatabaseReference database;
    static ArrayList<Itemcesta> mycesta = new ArrayList<Itemcesta>();
    static ArrayList<String> mycestaloja = new ArrayList<String>();
    static ArrayList<Itemloja> lojas = new ArrayList<Itemloja>();
    static   Typeface mTypeface;
    static   Typeface mTypeface2;
    adapterLojascesta adapter;
    boolean ctrallojas;
    static boolean opencesta;

    static Button btnpedido;
    static TextView nomenegc;
    static TextView distloja;
    static TextView totalcesta;
    static ImageView tagdel;
    static ImageView tagdin;
    static ImageView tagcred;
    static Button ligarbtn;
    static String numeroligar;
    static boolean ctrlnumeroligar;
    static Double total = 0.0;

    static  RelativeLayout rlcesta;
    static FirebaseListAdapter feed;
    static int qnt=1;
    static ListView listafav;
    boolean ctrlfav;
    static ArrayList<String> lojasfav = new ArrayList<String>();
    static ArrayList<Itemloja> lojasf = new ArrayList<Itemloja>();

    static TextView freteview;

    static TextView txttotalpedido;
    static TextView nomelojapedido;
    static TextView listapedidotxt;
    static TextView txttotalfrete;
    static EditText txttroco;

    static CheckBox checkBoxdinheiro;
    static CheckBox checkBoxcartao;
    static CheckBox checkBoxdebito;
    static EditText editTextendpedido;
    static Button btnsendpedido;
    static Button btverfrete;
    static boolean ctrlpoppedido;

    static RelativeLayout rlpedido;
    static Intent  intentmain;
    static ArrayList<String> bairroslist =  new ArrayList<>();
    static ArrayList<String> bairrosvals=  new ArrayList<>();
    static Spinner spinner  ;
    static Spinner spinner2 ;
    static String bairro;
    static String bairroval;
    static String enderec;
    static  View  vfav;
    static  Itemloja_cartoes Itemlojacarts;
    static RelativeLayout rlpoptotal;
    static RelativeLayout load;
static                                  Intent servicpedidos ;

    static Button btncontinuarcomp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cesta);
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

        mTypeface = Typeface.createFromAsset(getAssets(),"font/brandongrotesqueregular.otf");
        mTypeface2 = Typeface.createFromAsset(getAssets(),"font/himonday.ttf");

        spinner = (Spinner) findViewById(R.id.spinnerBairros);
        spinner2 = (Spinner) findViewById(R.id.spinbairrosveiw);
        bairroslist.add("Escolha um bairro");
        vfav = (View) findViewById(R.id.incbar);
        load = (RelativeLayout) findViewById(R.id.layoutloadpromo);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        c = getApplicationContext();
        intentmain=new Intent(this,MainActivity_pedidos.class);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
          servicpedidos = new Intent(getAppContext(), MainActivity_pedidos.class);

        nomenegc = (TextView) findViewById(R.id.nomneg);
        distloja = (TextView) findViewById(R.id.txtendloja);
        totalcesta = (TextView) findViewById(R.id.totalcesta);
        listitem = (ListView) findViewById(R.id.lsitacesta);
        rlcesta = (RelativeLayout) findViewById(R.id.rlcesta);
        btnpedido = (Button) findViewById(R.id.btnpedido);

        txttotalpedido= (TextView) findViewById(R.id.txttotalpedido);
        nomelojapedido= (TextView) findViewById(R.id.nomelojapedido);
        listapedidotxt= (TextView) findViewById(R.id.listapedidotxt);
        checkBoxdinheiro= (CheckBox) findViewById(R.id.checkBoxdinheiro);
        checkBoxcartao= (CheckBox) findViewById(R.id.checkBoxcartao);
        checkBoxdebito= (CheckBox) findViewById(R.id.checkBoxcartaodebito);
        editTextendpedido= (EditText) findViewById(R.id.editTextendpedido);
        btnsendpedido=(Button)findViewById(R.id.btnfinalpedido);
        freteview= (TextView) findViewById(R.id.fretepreview);
        txttroco = (EditText) findViewById(R.id.edttroco);
        txttotalfrete= (TextView) findViewById(R.id.txttotalfrete);

        rlpoptotal = (RelativeLayout) findViewById(R.id.rlpopototal);

        rlpedido = (RelativeLayout) findViewById(R.id.rlfinalpedido);
//        ligarbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ctrlnumeroligar==true)
//                ligar(numeroligar);
//            }
//        });

        if (MainActivity.myPerfil!=null)
        if (MainActivity.myPerfil.status==true) {
            final Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_status).setTitle("ONLINE");

            menu.findItem(R.id.nav_status)
                    .setIcon(ContextCompat.getDrawable(c, R.drawable.status));
            Drawable drawable = menu.findItem(R.id.nav_status).getIcon();
            drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);
        }else {
            final Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_status).setTitle("OFFLINE");
            menu.findItem(R.id.nav_status)
                    .setIcon(ContextCompat.getDrawable(c, R.drawable.statusoff));
            Drawable drawable = menu.findItem(R.id.nav_status).getIcon();
            drawable.setColorFilter(0xFFF8FF, PorterDuff.Mode.SRC_ATOP);

        }


          btverfrete=(Button)findViewById(R.id.btnverfrete);

          btncontinuarcomp=(Button)findViewById(R.id.btncontinuarcomp);


        syncFirebase();



    }

    public void syncFirebase() {

        //persistence Start
        if (database == null) {
            persistent pst = new persistent();
            pst.onCreate();
            database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");
        }

        if (MainActivity.myUser!=null){

            FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersEmpresa")
                    .child(MainActivity.myUser.Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Itemloja tlj = dataSnapshot.getValue(Itemloja.class);
                    if (dataSnapshot.exists()){
                        Log.i("LOJA","myLOJA "+tlj.Nome);
                        MainActivity. nomeuser="LOJA";
                        Chat chat = new Chat();
                        ImageView icon =(ImageView) findViewById(R.id.imgchatbar);
                        chat.getMychatnewmsg(tlj.Nome,icon);

//                      getdeliverys();

                    }else {
                        MainActivity. nomeuser="user";
                        getchat();
                      //getdeliverys();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        getCesta();
        barBottom();
        getchat();

    }

    public void getCesta(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.getUid()!=null)

        database.child("CestasClientes").child(user.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mycesta.clear();
                mycestaloja.clear();
                lojas.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                        mycestaloja.add(postSnapshot.getKey());
                        Itemcesta i = postSnapshot1.getValue(Itemcesta.class);
                        mycesta.add(i);
                        Log.i("lojas","lojas "+i.NomeProduto    );
                    }
                }
                setlistacestaloja();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void checkpermissaocallphone(){
        if (ContextCompat.checkSelfPermission(MainActivity_cesta.this,
                Manifest.permission.CALL_PHONE  )
                != PackageManager.PERMISSION_GRANTED  ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_cesta.this,
                    Manifest.permission.CALL_PHONE ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity_cesta.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    static void getloja(final Itemloja loja, final String dist){
        database.child("PerfilLojas").child(""+loja.cod).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Itemloja_cartoes LOJA = snapshot.getValue(Itemloja_cartoes.class);
                if (LOJA.Nome.equals(loja.Nome))

                if (LOJA.call!=null && LOJA.call.contains("OFF")==false ) {
                    numeroligar = LOJA.call;
                    ctrlnumeroligar=true;
                        }else {
                    ctrlnumeroligar=false;
                    numeroligar=null;
                }
                if (LOJA.Nome.equals(loja.Nome))
                     openlojacesta(LOJA,dist);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Context getAppContext(){
        return c;
    }

    static void poppedido(final Itemloja_cartoes loja, final ArrayList<Itemcesta> cesta, final Double total){

        if (bairro==null)
             getenderec();

        ctrlpoppedido=true;
        vfav.setVisibility(View.GONE);
        double frete=0.0;

        if (loja.Debito==false)
            checkBoxdebito.setEnabled(false);
        else
            checkBoxdebito.setEnabled(true);

        checkBoxdinheiro.   setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){txttroco.setVisibility(View.VISIBLE);}else{txttroco.setVisibility(View.GONE);}
            }
        });


        if (loja.Cred==false)
            checkBoxcartao.setEnabled(false);
        else
            checkBoxcartao.setEnabled(true);

        String t=  String.format("%.2f", total);
        txttotalpedido.setText("Total: R$ "+t);
        nomelojapedido.setText(loja.Nome);

        String lista="";
        for (int i=0;i<cesta.size();i++){
            lista+=cesta.get(i).NomeProduto +"\n";
        }

        listapedidotxt.setText(lista);
        btnsendpedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendpedido(loja.Nome, cesta, String.valueOf(total));
            }
        });

    }

    static void getenderec(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database.child("UsersClient").child(user.getUid()).child("End").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    enderec = dataSnapshot.child("End").getValue().toString();
                    bairroval = dataSnapshot.child("Bairro").getValue().toString();
                    for (int i = 1; i < bairroslist.size(); i++) {
                        if (bairroslist.get(i).equals(bairroval))
                            spinner.setSelection(i);

                        Log.i("bairro","bairro x "+bairroslist.get(i));
                    }
                    editTextendpedido.setText(enderec);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static void sendpedido(final String lojanome, final ArrayList<Itemcesta> cestalista,String total){

        load.setVisibility(View.VISIBLE);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email= user.getEmail();
        for(int i =0; i < user.getEmail().length();i++){
                email  = email.replace(".","AAA");
                email  = email.replace("@","BBB");
        }
        final String finalEmail = email;
        final String end = editTextendpedido.getText().toString();
        String status = "solicitando";
        String pagamento = "";
        ///////
        if (checkBoxcartao.isChecked())pagamento+="Crédito|";
        else
        if (checkBoxdinheiro.isChecked())pagamento+="Dinheiro|";
        else
        if (checkBoxdebito.isChecked())pagamento+="Débito|";



        String troco = "";
        if (txttroco.length()>0)
            troco=txttroco.getText().toString();

        final Pedido myp = new Pedido(end,status,pagamento,cestalista,ServerValue.TIMESTAMP,total,lojanome,bairro,troco);

        if (checkBoxdinheiro.isChecked() && troco.length()==0){
            Toast.makeText(c,"Troco para quanto?", Toast.LENGTH_SHORT).show();
            load.setVisibility(View.INVISIBLE);

        }
        else
        if (!pagamento.equals("") && end.length()>0 && bairro!=null && bairro!="Escolha um bairro" && (checkBoxcartao.isChecked() ==true|| checkBoxdinheiro.isChecked() ==true || checkBoxdebito.isChecked() ==true)){
            double totalfret= Double.parseDouble(bairroval)+Double.parseDouble(total);
            total = String.valueOf(totalfret);


        final int time = (int) Calendar.getInstance().getTimeInMillis();
            final int finalTime = time;
            database.child("LojasPedidos").child(lojanome).child(email).child("UIDUSER").setValue(user.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database.child("LojasPedidos").child(lojanome).child(finalEmail).child("Pedidos").child(String.valueOf(finalTime)).setValue(myp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.child("UsersClient").child(user.getUid()).child("Pedidos")
                                .child(finalTime +"").setValue(myp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                database.child("UsersClient").child(user.getUid()).child("End").child("End").setValue(end);
                                database.child("UsersClient").child(user.getUid()).child("End").child("Bairro").setValue(bairro);
                                Toast.makeText(c,"Pedido enviado, acompanhe seu pedido", Toast.LENGTH_SHORT).show();
//                                Intent servicechat = new Intent(getAppContext(), Pedido_service.class);
//                                getAppContext().startService(servicechat);
                                servicpedidos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                c.startActivity(servicpedidos);

                                database.child("CestasClientes").child(user.getUid()).child(lojanome).removeValue();
                            }
                        });


                    }
                });
            }
        });}
        else{
            load.setVisibility(View.INVISIBLE);
            Toast.makeText(c,"Complete os campos", Toast.LENGTH_SHORT).show();
        }
    }



    static void openlojacesta(final Itemloja_cartoes loja, String dist) {

        getbairros(loja);
        vfav.setVisibility(View.GONE);
        opencesta=true;

        total=0.0;
        qnt=0;

        rlcesta.setVisibility(View.VISIBLE);
        nomenegc.setText(loja.Nome);
        distloja.setText(loja.End);

        if (loja.status==true && loja.delivery == true) {

            btnpedido.setText("Fazer pedido");
            btnpedido.setBackgroundColor(0xFFFDC200);
            btnpedido.setTextColor(0xFFFFFFFF);
            btverfrete.setVisibility(View.VISIBLE);
            freteview.setVisibility(View.VISIBLE);

            btverfrete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    spinner2.setVisibility(View.VISIBLE);
                    spinner2.performClick();
                }

            });

            btnpedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlpedido.setVisibility(View.VISIBLE);
                    ctrlpoppedido=true;
                    poppedido(loja,mycesta,total);
                }
            });

        }else{
            Log.i("STATUSOFF","STATUSOFF");
            btnpedido.setText("Loja Offline");
            btnpedido.setBackgroundColor(0xDBDBDB);
            btnpedido.setTextColor(0XFF2D2D2D);
            btverfrete.setVisibility(View.GONE);
            freteview.setVisibility(View.GONE);
        }

        btncontinuarcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(c ,
                        MainActivity_lojas.class);
                intentMain.putExtra("LojaView",loja.Nome);
                c.startActivity(intentMain);
            }
        });

        totalcesta(loja.Nome);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user!=null)
        if (user.getUid()!=null)

        feed = new FirebaseListAdapter<Itemcesta>(c, Itemcesta.class, R.layout.itemlistacesta, database.child("CestasClientes")
                .child(user.getUid()).child(loja.Nome)) {

            @Override
            protected void populateView(View vw, final Itemcesta model, final int position) {


                TextView txtnome = (TextView) vw.findViewById(R.id.nomeItem);
                final TextView txtp = (TextView) vw.findViewById(R.id.Preco);
                final TextView txtpuni = (TextView) vw.findViewById(R.id.valoruni);
                TextView txtDesc = (TextView) vw.findViewById(R.id.descricao);
                final TextView txtqnt = (TextView) vw.findViewById(R.id.qnttxt);
                txtDesc.setText(model.Descricao);
                txtnome.setText(model.NomeProduto);
                ImageView iconItem = (ImageView) vw.findViewById(R.id.iconItemcesta);
                Glide.with(c).load(model.urlimg).into(iconItem);

                qnt = model.quantidade;
                if (qnt == 0)qnt=1;
                txtqnt.setText(""+qnt);
                Double prec = Double.valueOf(model.Preco);
                Double p = qnt * prec;
                String t=  String.format("%.2f", p);
                txtp.setText(t);

                txtp.setTypeface(mTypeface2);
                txtpuni.setTypeface(mTypeface);


                Button adduni = (Button) vw.findViewById(R.id.btnadduni);
                adduni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.i("additem","additem");
                        total=0.0;
                        qnt=model.quantidade;
                        qnt++;
                        feed.getRef(position).child("quantidade").setValue(qnt);
                    }
                });


                Button remvni = (Button) vw.findViewById(R.id.btnremuni);
                remvni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        total=0.0;
                        qnt=model.quantidade;
                        if (qnt>1){
                        qnt--;
                        feed.getRef(position).child("quantidade").setValue(qnt);

                        }
                    }
                });

            }
        };
        listitem.setAdapter(feed);
        listitem.setDividerHeight(0);
        if(listitem.isScrollContainer()==false){
            rlpoptotal.setAlpha(1.0f);
        }

        listitem.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            if (visibleItemCount!= totalItemCount)
             if (listitem.getAdapter().getCount()-1 == listitem.getLastVisiblePosition())
                {
                    rlpoptotal.setAlpha(0.1f);
                }
                else
                {
                    rlpoptotal.setAlpha(1.0f);
                }


            }
        });


    }

    static void totalcesta(String loja){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        if (user.getUid()!=null)
        database.child("CestasClientes").child(user.getUid()).child(loja).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total=0.0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Itemcesta i = postSnapshot.getValue(Itemcesta.class);
                    Double p = Double.valueOf(i.Preco);

                    int qp = (i.quantidade)+0;
                    if (qp==0)qp=1;

                    Double t = p *qp;
                    total+=t;

                }

                String t=  String.format("%.2f", total);

                totalcesta.setText("Total: R$ "+t);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setlistacestaloja(){

        ctrallojas=false;
        for (int i=0;i<MainActivity.lojas.size();i++){
            for (int ii=0;ii<mycestaloja.size();ii++) {
                if (mycestaloja.get(ii).equals(MainActivity.lojas.get(i).Nome))
                    ctrallojas=true;
            }
            if (ctrallojas==true){
                lojas.add(MainActivity.lojas.get(i));
                ctrallojas=false;
            }
        }
        Log.i("lojas","lojas "+lojas.size());
        if (lojas.size()>0) {
            listsuper = (ListView) findViewById(R.id.listalojascesta);
            adapter = new adapterLojascesta(c, R.layout.itemcesta, lojas);
            listsuper.setAdapter(adapter);
        }
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (ctrlpoppedido==true){
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlfinalpedido);
                rl.setVisibility(View.INVISIBLE);
                ctrlpoppedido=false;
            }
            else
            if (opencesta==true){
                vfav.setVisibility(View.VISIBLE);

                RelativeLayout rlcesta = (RelativeLayout) findViewById(R.id.rlcesta);
                rlcesta.setVisibility(View.INVISIBLE);
                opencesta=false;
            }else
            if (ctrlfav==true){
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlfav);
                rl.setVisibility(View.INVISIBLE);
                ctrlfav=false;
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

        if (id == R.id.nav_status) {

            if (MainActivity.ctrlmenustatus==false){
                Toast.makeText(c,"preparando...",Toast.LENGTH_LONG).show();

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
                Toast.makeText(c,"preparando...",Toast.LENGTH_LONG).show();
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
            Intent intentMain = new Intent(MainActivity_cesta.this ,
                    MainActivity.class);

            MainActivity_cesta.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_cesta.this ,
                    MainActivity_cesta.class);
            MainActivity_cesta.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_cesta.this ,
                    MainActivity_categorias.class);
            MainActivity_cesta.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_cesta.this ,
                    MainActivity_topicos.class);
            MainActivity_cesta.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_cesta.this ,
                    MainActivity_contato.class);
            MainActivity_cesta.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_cesta.this ,
                    MainActivity_empresa.class);
            MainActivity_cesta.this.startActivity(intentMain);
        }
        else if (id == R.id.nav_cesta) {
            if (MainActivity.myUser!=null && mycesta.size()>0){
                Intent intentMain = new Intent(MainActivity_cesta.this ,
                        MainActivity_cesta.class);

                MainActivity_cesta.this.startActivity(intentMain);
            }
            else{
                Toast.makeText(c,"Cesta vazia",Toast.LENGTH_SHORT).show();
            }
        }  else if (id == R.id.nav_mypedidos) {
            Intent intentMain = new Intent(MainActivity_cesta.this ,
                    MainActivity_pedidos.class);
            MainActivity_cesta.this.startActivity(intentMain);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if (MainActivity.nomeuser.equals("user")&& MainActivity.listalojaschat.size()>0){
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            servicechat.putExtra("usermsg","true");
            startService(servicechat);
        }

    }


    @Override
    public void onPause(){
        super.onPause();
        if (MainActivity.nomeuser.equals("user")&& MainActivity.listalojaschat.size()>0){
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            servicechat.putExtra("usermsg","true");
            startService(servicechat);
        }

    }


    public void chatconfig(Itemloja loja , Context ctx ){

        if (MainActivity.myUser!=null){
            try {
                Chat chat = new Chat();
                chat.sendStartChat(loja);
                Intent intentMain = new Intent(ctx,
                        MainActivity_chat.class);
//            intentMain.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                intentMain.putExtra("Chat", loja.Nome);
                ctx.startActivity(intentMain);

            }catch (Exception e){
                Log.i("chatconfig","chatconfig E "+e.getMessage());
            }


            try{
                Chat chat = new Chat();
                chat.sendStartChat(loja);
                Intent intentMain = new Intent(c,
                        MainActivity_chat.class);
//            intentMain.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                intentMain.putExtra("Chat", loja.Nome);
                MainActivity_cesta.this.startActivity(intentMain);
            }catch (Exception e){
            }

        }
        else {
            Toast.makeText(c,"Necessário login", Toast.LENGTH_SHORT).show();
            MainActivity.loginsimples=true;
            MainActivity. rlpopcad
                    .setVisibility(View.VISIBLE);
        }

    }


    public void barBottom(){
        View  vfav = (View) findViewById(R.id.incbar);

        Button btncasas= (Button) vfav.findViewById(R.id.btncasas);
        btncasas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentMain = new Intent(MainActivity_cesta.this ,
                        MainActivity_lojas.class);
                MainActivity_cesta.this.startActivity(intentMain);
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


                    Intent intentMain = new Intent(MainActivity_cesta.this ,
                            MainActivity_delivery.class);
                    MainActivity_cesta.this.startActivity(intentMain);
                }
            }

        });

        Button btncesta= (Button) vfav.findViewById(R.id.btncestabar);
        btncesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.myPerfil == null) {
                    if (MainActivity.mycesta.size() > 0) {
                        Intent intentMain = new Intent(MainActivity_cesta.this,
                                MainActivity_cesta.class);
                        MainActivity_cesta.this.startActivity(intentMain);
                    } else {
                        Toast.makeText(c, "Sem itens na cesta", Toast.LENGTH_SHORT).show();
                    }
                }
                else

                {

                    Intent intentMain = new Intent(MainActivity_cesta.this,
                            MainActivity_dashempresa.class);
                    MainActivity_cesta.this.startActivity(intentMain);
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
                        Intent intentMain = new Intent(MainActivity_cesta.this,
                                MainActivity_chat.class);
                        MainActivity_cesta.this.startActivity(intentMain);
                    } else {
                        if (MainActivity.nomeuser.equals("LOJA")) {
                            Intent intentMain = new Intent(MainActivity_cesta.this,
                                    MainActivity_chat.class);
                            MainActivity_cesta.this.startActivity(intentMain);
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


    public void getchat(){

        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersClient")
                .child(MainActivity.myUser.Uid).child("chat").child("lojasOn").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.listalojaschat.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MainActivity. listalojaschat.add(String.valueOf(postSnapshot.getValue()));
                }
                if ( MainActivity.listalojaschat.size()>0){
                    Chat chat = new Chat();
                    View v = (View) findViewById(R.id.incbar);

                    ImageView imgbarchat =(ImageView) v.findViewById(R.id.imgchatbar);
                    imgbarchat.setBackgroundResource(R.drawable.comment);
                    chat.cont=0;
                    chat.checknewmsguser(imgbarchat);
                }else{
                    View v = (View) findViewById(R.id.incbar);
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


    @Override
    public void onStart(){
        super.onStart();

    }


    static void getbairros(Itemloja_cartoes loja){


        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com")
                .child("BairrosLojas")
                .child(loja.Nome+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bairroslist.clear();
                bairroslist.add("Escolha o bairro");
                bairrosvals.clear();
                bairrosvals.add("0.00");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    bairrosVal bairro= postSnapshot.getValue(bairrosVal.class);
                    bairroslist.add(postSnapshot.getKey().toString());
                    Log.i("BAIRROS","BAIRROS" +bairroslist.size());
                    bairrosvals.add(bairro.Valor);
                }
                configSpinnbairros();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })   ;
    }


    static void configSpinnbairros(){

        final ArrayList<String> listafiltros = new ArrayList<>();
//
        listafiltros.addAll(bairroslist);
//
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_list_item_1, listafiltros);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
//
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    String value = (String) parent.getItemAtPosition(position);
                    bairro=value;
                    bairroval=bairrosvals.get(position);
                    Double totaldouble= Double.valueOf(bairroval);
                    String t=  String.format("%.2f", totaldouble);
                    String vb = t.replace(".",",");
                    txttotalfrete.setText("+ R$ "+vb+" de frete");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c,
                android.R.layout.simple_spinner_item, listafiltros);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    String value = (String) parent.getItemAtPosition(position);
                    bairro=value;
                    bairroval=bairrosvals.get(position).toString();
                    Double totaldouble= Double.valueOf(bairroval);
                    String t=  String.format("%.2f", totaldouble);
                    bairroval.replace(".",",");

                    freteview.setText("+ frete "+t);
                    spinner.setSelection(position);
                }else{

                    bairro=null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
