package brejapp.com.brejapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import brejapp.com.brejapp.R;


public class MainActivity_empresa extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    public ListView listitem;
    public ListView listsuper;
    static ListView listaLojas;
    static listaAdapterLocal  adapterComp;
    Context c;
    static GoogleMap mapa;
    static DatabaseReference database;
    static ArrayList<Itemloja> lojas = new ArrayList<Itemloja>();
    static ArrayList<String> lojaslocal = new ArrayList<String>();
    static ArrayList<String> Categorias = new ArrayList<String>();
    static ArrayList<localloja> listalojas = new ArrayList<localloja>();
    static ArrayList<LatLng> locaismarker = new ArrayList<LatLng>();
    static ArrayList<posLocais> lojaposLocais = new ArrayList<posLocais>();
    static ArrayList<Marker>  markers = new ArrayList<Marker>();
    Marker localatual;
    String nomelocalatual;
    boolean finalcad=false;
    String urlb ="";

    LatLng locallojacoord;
    static ArrayList<LatLng> locallojacoordpoint = new ArrayList<LatLng>();

    boolean imgcaptured;
    static  View listItem;

    static int totalHeight;
    Empresa empresa;
    String categorialoja="";
    int contcad=0;
    String id = "";
    int contlojas;
    Empresa myLoja;
    boolean edtloja;
    Intent servcGPS;
    static boolean servgps;
    static boolean finalcadctrl;
    static RelativeLayout load;
    boolean returnfinalizcad;
    Itemloja_cartoes myPerfil;
    static   Marker myuser;

    static boolean userarker=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_empresa);
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

        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        load = (RelativeLayout) findViewById(R.id.layoutloadpromo);

        startgps();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        c = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button btnmaisinfo = (Button) findViewById(R.id.btnmaisinfo);
        btnmaisinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);

            }
        });


        Button btnrecsenha = (Button) findViewById(R.id.btnrecupersenha);
        btnrecsenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtemail = (EditText)findViewById(R.id.edtEmail);
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


        Button btnlogin = (Button) findViewById(R.id.btnloginempresa);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl =(RelativeLayout) findViewById(R.id.poplogin);
                rl.setVisibility(View.VISIBLE);
            }
        });

        Button btnloginreturn = (Button) findViewById(R.id.btnreturnpoplogin);
        btnloginreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl =(RelativeLayout) findViewById(R.id.poplogin);
                rl.setVisibility(View.INVISIBLE);
            }
        });

        Button btnloginok = (Button) findViewById(R.id.btnloginok);
        btnloginok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText email  = (EditText) findViewById(R.id.edtEmail);
                EditText pwds = (EditText) findViewById(R.id.edtPws);
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity_empresa.this);

                if (email.getText().toString().length()>0&& pwds.getText().toString().length()>0){
                    progressDialog.show();
                    progressDialog.setMessage("Logando...");

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), pwds.getText().toString())
                            .addOnCompleteListener(MainActivity_empresa.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.show();
                                        progressDialog.setMessage("Logado");
                                        Intent intentMain = new Intent(MainActivity_empresa.this ,
                                                MainActivity_dashempresa.class);
                                        MainActivity_empresa.this.startActivity(intentMain);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("login", "signInWithEmail:failure "+ task.getException().getMessage());
                                        String msgerro = task.getException().getMessage();
                                        progressDialog.hide();
                                        if (msgerro.equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                                            Toast.makeText(c,"Email não cadastrado",Toast.LENGTH_LONG).show();
                                        }
                                        if (msgerro.equals("The password is invalid or the user does not have a password.")){
                                            Toast.makeText(c,"Senha incorreta",Toast.LENGTH_LONG).show();

                                        }

                                    }

                                }
                            });

                }

                else{
                    snackbarshow("Complete os campos");
                }

            }
        });

        syncFirebase();
        btnsConfig();

    }


    public void startgps(){
        servcGPS   = new Intent(getApplicationContext(), locationService.class);
        startService(servcGPS);
    }


    public  void getMyLoja(){



        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database.child("UsersEmpresa").child(user.getUid()).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myLoja = dataSnapshot.getValue(Empresa.class);
                empresa  =  myLoja;
                getPerfilpublico();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void finalcad(){


        returnfinalizcad=true;
        Button btnfeitocad = (Button) findViewById(R.id.btnfeitocad);
        if (myPerfil!=null)
            btnfeitocad.setVisibility(View.VISIBLE);
        btnfeitocad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=empresa.Id;
                finalcadctrl=true;
                empresa = new Empresa(empresa.Nome,empresa.Tel,empresa.CNPJCPF,empresa.Desc,empresa.Categoria,empresa.Lojas,empresa.Locais,empresa.Horarios,empresa.UrlImg,id,empresa.TimeCreate,empresa.Email,empresa.Topico,empresa.Uid,empresa.End);
                saveperfilpublico();

            }
        });

        Button btreturncad = (Button) findViewById(R.id.btnreturncad);

        if (contcad==0)btreturncad.setVisibility(View.INVISIBLE);

        TextView txttile = (TextView) findViewById(R.id.txtcadastrotitle);
        txttile.setText("Editando cadastro");

        TextView txt = (TextView) findViewById(R.id.txtfrasecad);
        txt.setText("");

        TextView txtfrase2 = (TextView) findViewById(R.id.txtcadb);
        txtfrase2.setText("Edite as informações :)");

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlCadastro);
        rl.setVisibility(View.VISIBLE);
        contcad=2;

        ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewcad);
        vf.showNext();
        vf.showNext();

        final EditText nomeloja = (EditText) findViewById(R.id.edtnomeloja);
        nomeloja.setText(empresa.Nome);

        final EditText tellloja = (EditText) findViewById(R.id.edttelloja);
        tellloja.setText(empresa.Tel);

        final EditText cnpjcpf = (EditText) findViewById(R.id.edtcnpj);
        cnpjcpf.setText(empresa.CNPJCPF);

        final EditText desc = (EditText) findViewById(R.id.edtDescricaoloja);
        desc.setText(empresa.Desc);

        final EditText hora = (EditText) findViewById(R.id.edthorario);
        hora.setText(empresa.Horarios);

        final EditText end = (EditText) findViewById(R.id.editTextEnd);
        end.setText(empresa.End);

        if (empresa.UrlImg != null || empresa.UrlImg.equals("temp")) {
            imgcaptured=true;

            ImageView img = (ImageView) findViewById(R.id.imgloja);
            Glide.with(c).load(empresa.UrlImg).into(img);

        }

        Spinner spincat = (Spinner) findViewById(R.id.spincatloja);

        for (int i = 0; i < Categorias.size();i++){

            if (Categorias.get(i).equals(empresa.Categoria)){
                spincat.setSelection(i);
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(empresa);


        try {


            JSONObject jsonobj = new JSONObject(json);
            ArrayList<posLocais> getlocais = new ArrayList<>();
            ArrayList<LatLng> getcoords= new ArrayList<>();
            ArrayList<String> getlojas = new ArrayList<>();


//            for (int i= 0; i <jsonobj.getJSONArray("Lojas").length();i++ ) {
//                getlojas.add(String.valueOf(jsonobj.getJSONArray("Lojas").get(i)));
//            }


            for (int i= 0; i <jsonobj.getJSONArray("Locais").length();i++ ) {

                Gson gsoncoords = new Gson();
                String jsoncds = gsoncoords.toJson(jsonobj.getJSONArray("Locais").get(i));

                JSONObject coords = new JSONObject(jsoncds);

                Double lat = coords.getJSONObject("nameValuePairs").getDouble("latitude");
                Double lng = coords.getJSONObject("nameValuePairs").getDouble("longitude");

                posLocais latlng = new posLocais(lat,lng);
                getlocais.add(latlng);

                LatLng cods= new LatLng(lat,lng);
                locallojacoord = cods;

                getcoords.add(cods);

            }
            locallojacoordpoint.clear();
            locallojacoordpoint.add(locallojacoord);

//            lojaposLocais.addAll(getlocais);
//            adapterComp.clear();
//            lojaslocal.clear();
//            listaLojas.setAdapter(null);
//
//            for (int i= 0; i <jsonobj.getJSONArray("Lojas").length();i++ ){
//
//                lojaslocal.addAll(getlojas);
//                locaismarker.addAll(getcoords);
//                nomelocalatual = jsonobj.getJSONArray("Lojas").get(i).toString();
//                localloja newUser = new localloja(nomelocalatual);
//                adapterComp.add(newUser);
//                ViewGroup.LayoutParams params = listaLojas.getLayoutParams();
//                totalHeight = listaLojas.getAdapter().getCount() * 250;
//                params.height = totalHeight;
//                listaLojas.setLayoutParams(params);
//                listaLojas.requestLayout();
//            }

        }catch (Exception e){

        }

    }


    public LatLng getLocalUser(){

        Double latL = locationService.getLatitude();
        Double lngL = locationService.getLongitude();

        if (latL ==null)getLocalUser();

        return new LatLng(latL,lngL);
    }

    public void getPerfilpublico(){

        database.child("PerfilLojas").child(empresa.Id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPerfil = dataSnapshot.getValue(Itemloja_cartoes.class);
                finalcad();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btnsConfig(){

        startgps();

        if (ContextCompat.checkSelfPermission(MainActivity_empresa.this,
                Manifest.permission.ACCESS_FINE_LOCATION  )
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_empresa.this,
                    Manifest.permission.ACCESS_FINE_LOCATION ) ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity_empresa.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        150);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        final ViewFlipper viewcad= (ViewFlipper) findViewById(R.id.viewcad);

        final CheckBox  cbtermos = (CheckBox) findViewById(R.id.checkBoxtermos);
        Button btntermos = (Button) findViewById(R.id.btntermos);
        btntermos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://devscb-7afe7.firebaseapp.com/termo_empresa.html"));
                startActivity(browserIntent);
            }
        });

        final EditText email = (EditText) findViewById(R.id.edtemaillojacad);
        final EditText pws = (EditText) findViewById(R.id.edtpwsloja);
        Button btnlogin = (Button) findViewById(R.id.btncadlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().length() >0 && pws.getText().length()>0 && cbtermos.isChecked()==true){
                    RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                    consloadwindow.setVisibility(View.VISIBLE);
                    setUser();
                }else {
                    if (email.getText().length() ==0 && pws.getText().length()==0)
                        Toast.makeText(c,"Completo os campos",Toast.LENGTH_LONG).show();
                    else
                    if (email.getText().length()==0)
                        Toast.makeText(c,"Digite o email",Toast.LENGTH_LONG).show();
                    else
                    if (pws.getText().length()<8)
                        Toast.makeText(c,"A senha deve conter pelo menos 8 digitos",Toast.LENGTH_LONG).show();
                    else
                    if (cbtermos.isChecked()==false)
                        Toast.makeText(c,"Você precisa aceitar os termos de utilização para realizar o cadastro",Toast.LENGTH_LONG).show();

                    RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                    consloadwindow.setVisibility(View.INVISIBLE);
                }
            }
        });


        final EditText nomeloja = (EditText) findViewById(R.id.edtnomeloja);
        final String nomelj=nomeloja.getText()+"";
        final EditText tellloja = (EditText) findViewById(R.id.edttelloja);
        final String telllj=tellloja.getText()+"";
        final EditText cnpjcpf = (EditText) findViewById(R.id.edtcnpj);
        final String cnpjcpflj=cnpjcpf.getText()+"";

        Button btnprox1 = (Button) findViewById(R.id.btnproximo1);
        btnprox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nomeloja.getText().length() >0 && tellloja.getText().length()>0 && cnpjcpf.getText().length()>0) {
                    Log.i("btn"," " + nomeloja.getText());
                    String topicoloja = "";
                    String txt ="";
                    for (int i= 0; i < empresa.Email.length();i++){
                        if (empresa.Email.charAt(i)=='.')     txt=empresa.Email.replace('.','p');
                    }

                    txt = txt.replace("@","arb");
                    topicoloja = empresa.Id +txt;

                    id=empresa.Id;
                    empresa = new Empresa(nomeloja.getText().toString(),tellloja.getText().toString(),cnpjcpf.getText().toString(),empresa.Desc,empresa.Categoria,empresa.Lojas,empresa.Locais,empresa.Horarios,empresa.UrlImg,empresa.Id,empresa.TimeCreate,empresa.Email,topicoloja,empresa.Uid,empresa.End);
                    savedados();
                    contcad++;
                    Button btreturncad = (Button) findViewById(R.id.btnreturncad);
                    btreturncad.setVisibility(View.VISIBLE);
                    viewcad.showNext();

                } else {
                    Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();
                }
            }
        });


        final EditText desc = (EditText) findViewById(R.id.edtDescricaoloja);
        Button btnprox2 = (Button) findViewById(R.id.btnproximo2);
        btnprox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nomelj=empresa.Nome;
                final String telllj=empresa.Tel;
                final String cnpjcpflj=empresa.CNPJCPF;
                final String txtdesc=desc.getText()+"";


                if (txtdesc.length() >0 && categorialoja.length()>0){
                    empresa = new Empresa(empresa.Nome,empresa.Tel,empresa.CNPJCPF,txtdesc,categorialoja,empresa.Lojas,empresa.Locais,empresa.Horarios,empresa.UrlImg,empresa.Id,empresa.TimeCreate,empresa.Email,empresa.Topico,empresa.Uid,empresa.End);
                    savedados();
                    viewcad.showNext();
                    contcad++;
                }

            }
        });

        //cad 3 localização // NomeLoja e horario loja
        final EditText hora = (EditText) findViewById(R.id.edthorario);
        final EditText end= (EditText) findViewById(R.id.editTextEnd);
        final Button btnaddloja = (Button) findViewById(R.id.btnaddlocal);
        btnaddloja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                servgps=true;

                startgps();

                RelativeLayout map = (RelativeLayout) findViewById(R.id.rlmapa);
                map.setVisibility(View.VISIBLE);

                if (locationService.getLongitude()==0.0){
                    load = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                    load.setVisibility(View.VISIBLE);}
                else {

                    final LatLng local = getLocalUser();
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 11));

                    myuser =  mapa.addMarker(new MarkerOptions()
                            .title("Meu local")
                            .position(mapa.getCameraPosition().target)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmarkeruser))
                    );
                    Log.i("LOCAL","LOCAL X"+locationService.getLongitude());

                }



            }
        });

        listaCompart();

        final Button btnmarcar = (Button) findViewById(R.id.btnmarcar);
        btnmarcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                localatual =   mapa.addMarker(new MarkerOptions()
                        .title("Loja")
                        .position(mapa.getCameraPosition().target)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmarkercart))
                );

                //  locaismarker.add(localatual.getPosition());
                //     lojaposLocais.add(new posLocais(localatual.getPosition().latitude,localatual.getPosition().longitude));
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapa.getCameraPosition().target.latitude-0.00001f,mapa.getCameraPosition().target.longitude), mapa.getCameraPosition().zoom));
                btnmarcar.setVisibility(View.INVISIBLE);
                Button btnoklocal = (Button) findViewById(R.id.btnoklocal);
                btnoklocal.setVisibility(View.VISIBLE);
                Button btedt = (Button) findViewById(R.id.btneditar);
                btedt.setVisibility(View.VISIBLE);
                btnaddloja.setText("Loja localizada");
            }
        });


        final Button btnoklocal = (Button) findViewById(R.id.btnoklocal);
        btnoklocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (localatual.getPosition() != null){

                    markers.add(localatual);
                    locallojacoord = new LatLng(mapa.getCameraPosition().target.latitude,mapa.getCameraPosition().target.longitude);
                    locallojacoordpoint.clear();
                    locallojacoordpoint.add(locallojacoord);
                    empresa.Locais = locallojacoord;
                    RelativeLayout map = (RelativeLayout) findViewById(R.id.rlmapa);
                    map.setVisibility(View.INVISIBLE);
                    btnmarcar.setVisibility(View.INVISIBLE);
                    Button btnoklocal = (Button) findViewById(R.id.btnoklocal);
                    btnoklocal.setVisibility(View.VISIBLE);

                }else{
                    snackbarshow("Marque uma posição no mapa");
                }
            }
        });


        final Button btneditarlocal = (Button) findViewById(R.id.btneditar);
        btneditarlocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                localatual.remove();
                localatual=null;
                if (lojaposLocais.size()>0)
                    lojaposLocais.remove(lojaposLocais.size()-1);
                if (locaismarker.size()>0)
                    locaismarker.remove(lojaposLocais.size()-1);
                Button btnoklocal = (Button) findViewById(R.id.btnoklocal);
                btnoklocal.setVisibility(View.INVISIBLE);
                btneditarlocal.setVisibility(View.INVISIBLE);
                btnmarcar.setVisibility(View.VISIBLE);

            }
        });


        Button btnreturnmapa = (Button) findViewById(R.id.btnreturnloja2);
        btnreturnmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout map = (RelativeLayout) findViewById(R.id.rlmapa);
                map.setVisibility(View.INVISIBLE);
                if (localatual == null){
                    locaismarker.add(new LatLng(0.0f,0.0f));
                }
            }
        });


        Button btnprox3 = (Button) findViewById(R.id.btnproximo3);
        btnprox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locallojacoord!=null && hora.getText().length()>0 && end.getText().length()>0){
                    id=empresa.Id;

                    empresa = new Empresa(empresa.Nome,empresa.Tel,empresa.CNPJCPF,empresa.Desc,empresa.Categoria,lojaslocal,locallojacoordpoint,hora.getText()+"",empresa.UrlImg,empresa.Id,empresa.TimeCreate,empresa.Email,empresa.Topico,empresa.Uid,end.getText().toString());
                    savedados();
                    contcad++;

                    viewcad.showNext();

                }else {
                    if (lojaslocal.size() == 0)
                        Toast.makeText(c,"Adicione pelo menos um local",Toast.LENGTH_LONG).show();
                    else
                    if (hora.length()== 0)
                        Toast.makeText(c,"Adicione um horário de funcionamento",Toast.LENGTH_LONG).show();
                    else
                    if (end.length() ==0 )
                        Toast.makeText(c,"Complete o endereço",Toast.LENGTH_LONG).show();

                }

            }
        });

        //CAD 4 IMAGE
        Button btngetimg = (Button) findViewById(R.id.btnbuscarimg);
        btngetimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarimginternal();

            }
        });



        Button btncadastro = (Button) findViewById(R.id.btncadastrolojainicio);
        btncadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlCadastro);
                rl.setVisibility(View.VISIBLE);
            }
        });

        Button btnfinalizar = (Button) findViewById(R.id.btnfinalizar);
        btnfinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgcaptured){

                    saveImgLoja(empresa.Nome);

                }else {
                    if (lojaslocal.size() == 0)
                        Toast.makeText(c,"Adicione pelo menos um local",Toast.LENGTH_LONG).show();
                    else
                    if (hora.length()== 0)
                        Toast.makeText(c,"Adicione um horário de funcionamento",Toast.LENGTH_LONG).show();
                    else
                    if (lojaslocal.size() ==0 && hora.length()==0)
                        Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();

                }
            }
        });


        Button btreturncad = (Button) findViewById(R.id.btnreturncad);
        btreturncad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (contcad>0) {
                    viewcad.showPrevious();
                    contcad--;

                    if (returnfinalizcad==true && contcad==1){
                        Button btreturncad = (Button) findViewById(R.id.btnreturncad);
                        btreturncad.setVisibility(View.INVISIBLE);
                    }
                }else{
                    if (contcad == 0){
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlCadastro);
                        rl.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setBuildingsEnabled(true);
    }


    static void setLocal(LatLng local){

        load.setVisibility(View.INVISIBLE);

        if (myuser == null)
        {
            myuser =   mapa.addMarker(new MarkerOptions()
                    .title("Meu local")
                    .anchor(-0.5f,-0.5f)
                    .position(mapa.getCameraPosition().target)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmarkeruser))
            );

            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 11));

        }

    }


    public void listaCompart(){
        listaLojas = (ListView) findViewById(R.id.listalojacad);
        adapterComp = new listaAdapterLocal(listalojas,c,listaLojas);
        listaLojas.setAdapter(adapterComp);
    }


    static void removelocal(int position) {

        final ArrayList<localloja> list = new ArrayList<localloja>();
        final ArrayList<LatLng> listmarkes = new ArrayList<LatLng>();
        final ArrayList<Marker> markes = new ArrayList<Marker>();
        final ArrayList<posLocais> coords = new ArrayList<posLocais>();
        final ArrayList<String> locaislojasstg = new ArrayList<String>();

        coords.addAll(lojaposLocais);
        markes.addAll(markers);
        listmarkes.addAll(locaismarker);
        list.addAll(listalojas);
        locaislojasstg.addAll(lojaslocal);

        locaismarker.clear();
        listalojas.clear();
        adapterComp.clear();
        lojaposLocais.clear();
        lojaslocal.clear();

        totalHeight = 0;
        if (markers.size()>0)
            markers.get(position).remove();
        markers.clear();

        for(int i = 0;i < list.size(); i++){
            if (position != i) {
                Log.i("locais","pos "+list.get(i));
                listalojas.add(list.get(i));
                locaismarker.add(listmarkes.get(i));
                lojaposLocais.add(coords.get(i));
                lojaslocal.add(locaislojasstg.get(i));
                if (markes.get(i)!=null)
                    markers.add(markes.get(i));
                totalHeight +=  250;
            }
        }


        ViewGroup.LayoutParams params = listaLojas.getLayoutParams();
        params.height = totalHeight;
        listaLojas.setLayoutParams(params);
        listaLojas.requestLayout();

    }


    private void setUser(){

        EditText Temail = (EditText) findViewById(R.id.edtemaillojacad);
        final String email = Temail.getText().toString();

        EditText Tpsw = (EditText) findViewById(R.id.edtpwsloja);
        final String password = Tpsw.getText().toString();
        final ViewFlipper viewcad= (ViewFlipper) findViewById(R.id.viewcad);

        Random r = new Random();
        int i1 = r.nextInt(999999999 -  100000000) + 100000000;
        final String id = String.valueOf(i1);


        final Date datatime = new Date();
        final String time = datatime.getTime()+"";
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity_empresa.this);

        if (password.length() >0 && email.length() > 0  ) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("", "createUserWithEmail:success");
                                snackbarshow( "Email cadastrado!");
                                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlCadastro);
                                rl.setVisibility(View.VISIBLE);
                                contcad++;
                                viewcad.showNext();
                                RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                                consloadwindow.setVisibility(View.INVISIBLE);
                                empresa = new Empresa("temp","temp","temp","temp","temp",null,null,"temp","temp",id,time,email,"temp",user.getUid(),"temp");
                                savedados();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("setuser", "setuser "+ task.getException().getMessage());

                                if (task.getException().getMessage() == "The email address is already in use by another account."){
                                    Toast.makeText(c, "Este EMAIL ja está cadastrado! ",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(MainActivity_empresa.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.show();
                                                        progressDialog.setMessage("Logado");

                                                        FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();

                                                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlCadastro);
                                                        rl.setVisibility(View.VISIBLE);
                                                        contcad++;
                                                        viewcad.showNext();
                                                        RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                                                        consloadwindow.setVisibility(View.INVISIBLE);
                                                        empresa = new Empresa("temp","temp","temp","temp","temp",null,null,"temp","temp",id,time,email,"temp",user.getUid(),"temp");
                                                        savedados();

                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Log.w("login", "signInWithEmail:failure "+ task.getException().getMessage());
                                                        String msgerro = task.getException().getMessage();
                                                        progressDialog.hide();
                                                        if (msgerro.equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                                                            Toast.makeText(c,"Email não cadastrado",Toast.LENGTH_LONG).show();
                                                        }
                                                        if (msgerro.equals("The password is invalid or the user does not have a password.")){
                                                            Toast.makeText(c,"Senha incorreta",Toast.LENGTH_LONG).show();

                                                        }
                                                        RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                                                        consloadwindow.setVisibility(View.INVISIBLE);
                                                    }

                                                }
                                            });

                                }
                            }

                            // ...
                        }
                    });
        }else {Toast.makeText(c,"Complete os campos",Toast.LENGTH_LONG).show();
            RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
            consloadwindow.setVisibility(View.INVISIBLE);}
    }


    public void savedados(){

        FirebaseUser user =    FirebaseAuth.getInstance().getCurrentUser();

        database.child("UsersEmpresa").child(user.getUid()).setValue(empresa).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (finalcad == false)
                    snackbarshow("Cadastro pré-salvo...");
                if (finalcad == true) {
                    snackbarshow("Cadastro completo");
                    Intent intentMain = new Intent(MainActivity_empresa.this ,
                            MainActivity_dashempresa.class);
                    MainActivity_empresa.this.startActivity(intentMain);
                }
            }
        });

    }


    public void snackbarshow(String texto){

        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(parentLayout, texto, Snackbar.LENGTH_LONG)
                ;
        snackbar.show();

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
                ImageView img = (ImageView) findViewById(R.id.imgloja);
                Uri imgem = data.getData();
                Glide.with(c).load(imgem).into(img);
                imgcaptured=true;
            }
        }

    }


    public void syncFirebase() {

        //persistence start
        if (database == null) {
            persistent pst = new persistent();
            pst.onCreate();
            database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");

        }
        getCategorias();

    }



    public void checkloginempresa() {

        boolean ctrl=false;

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            database.child("UsersEmpresa").child(user.getUid()).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null){

                        Empresa emp = dataSnapshot.getValue(Empresa.class);

                        if (user.getUid().equals(emp.Uid))
                        {
                            Intent intentMain = new Intent(MainActivity_empresa.this ,
                                    MainActivity_dashempresa.class);
                            MainActivity_empresa.this.startActivity(intentMain);

                        } else {
                            RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                            consloadwindow.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    public void  getCategorias(){

        Categorias.clear();

        Categorias.add("Escolha uma Categorias");

        database.child("CategoriasEmpresas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Categorias.add(postSnapshot.getValue().toString());
                    Log.i("Cat","cat "+postSnapshot.getValue().toString());
                }
                setCategoriasempresa();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setCategoriasempresa(){
        //
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, Categorias);
        //
        final Spinner catg = (Spinner) findViewById(R.id.spincatloja);
        catg.setAdapter(dataAdapter);
        //
        dataAdapter.notifyDataSetChanged();
        catg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position>0) {
                    String p = catg.getSelectedItem().toString();
                    categorialoja = p;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        catg.setSelection(3);
        Intent intputextra = getIntent();
        String finalcad = intputextra.getStringExtra("FINALCAD");
        if(finalcad != null){
            getMyLoja();
            //
        }else{
            checkloginempresa();
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
            Intent intentMain = new Intent(MainActivity_empresa.this ,
                    MainActivity.class);
            MainActivity_empresa.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_empresa.this ,
                    MainActivity_lojas.class);
            MainActivity_empresa.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_empresa.this ,
                    MainActivity_categorias.class);
            MainActivity_empresa.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_empresa.this ,
                    MainActivity_topicos.class);
            MainActivity_empresa.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_empresa.this ,
                    MainActivity_contato.class);
            MainActivity_empresa.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_empresa.this ,
                    MainActivity_empresa.class);
            MainActivity_empresa.this.startActivity(intentMain);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void saveImgLoja(String nomeloja) {

        //creating reference to firebase storage
        String nome  =  nomeloja;

        nome = nome.replace("\\u","");


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://devscb-7afe7.appspot.com/LogoLojas");    //change the url according to your firebase app

        final StorageReference childRef = storageRef.child(nome+".png");

        //uploading the image
        ImageView imageView = (ImageView) findViewById(R.id.imgloja);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();


        final UploadTask uploadTask = childRef.putBytes(data);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity_empresa.this);

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
                        String url = String.valueOf(String.valueOf(uri));
                        id=empresa.Id;
                        empresa = new Empresa(empresa.Nome,empresa.Tel,empresa.CNPJCPF,empresa.Desc,empresa.Categoria,empresa.Lojas,empresa.Locais,empresa.Horarios,url,id,empresa.TimeCreate,empresa.Email,empresa.Topico,empresa.Uid, empresa.End);
                        contcad++;

                        saveperfilpublico();
                    }
                });

            }


        });

    }


    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public void saveperfilpublico(){

        int contcor=0;
        ImageView imageView = (ImageView) findViewById(R.id.imgloja);
        String cor ="";


        if ( finalcadctrl==false) {
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();
            cor = String.format("#%06X", (0xFFFFFF & getDominantColor(bitmap)));
            Log.i("Cor", "Cor " + cor);
            for (int i = 0; i < cor.length(); i++) {
                if (cor.charAt(i) == 'F') contcor++;
            }
            if (contcor > 2) cor = "#FE9E1E";
            if (cor.equals("#FFFFFF")) cor = "#333333";
        }else
            cor = myPerfil.Cor;

        final String finalCor = cor;
        String call = null;
        String w = null;
        String urlb = null;

        boolean deliv=false;
        boolean cartao=false;
        boolean status = false;
        boolean Cred =false;
        boolean Debito =false;

        if (myPerfil!=null){
            deliv=myPerfil.delivery;
            cartao=myPerfil.credito;


        try {

            status = myPerfil.status;
            Cred = myPerfil.Cred;
            Debito = myPerfil.Debito;

            if (myPerfil.call != null)
                call = myPerfil.call.toString();

            if (myPerfil.whats != null)
                w = myPerfil.whats.toString();

            if (myPerfil.urlBanner != null)
                urlb = myPerfil.urlBanner.toString();


        }catch (Exception e){

        }

        }

        final Itemloja_cartoes loja = new Itemloja_cartoes(empresa.Nome,empresa.Desc, finalCor,1,empresa.Locais,empresa.UrlImg,empresa.Categoria,Integer.parseInt(empresa.Id),empresa.Lojas,null,empresa.Topico,call,w,urlb,"Endereço",deliv,cartao,status, Cred,Debito);

        database.child("PerfilLojas").child(empresa.Id).setValue(loja).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(c,"Perfil público salvo",Toast.LENGTH_LONG).show();
                finalcad=true;
                savedados();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error cad","Error cad" + e.getMessage());
            }
        });



    }



}
