package brejapp.com.brejapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import brejapp.com.brejapp.R;


import static android.view.MotionEvent.ACTION_MOVE;

public class MainActivity_topicos extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {
    public ListView listitem;
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
    static boolean crtltopic;
    static int sc=0;
    static boolean crtltopicmove;
    ArrayList<String> topicosinscritosuser= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topicos);
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

        syncFirebase();



    }



    public void syncFirebase() {

        //persistence start
        if (database == null) {
            persistent pst = new persistent();
            pst.onCreate();
            database = MainActivity.datab.getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");
        }
        gettopicson(0);


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
            Intent intentMain = new Intent(MainActivity_topicos.this ,
                    MainActivity.class);
            MainActivity_topicos.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_topicos.this ,
                    MainActivity_lojas.class);
            MainActivity_topicos.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_topicos.this ,
                    MainActivity_categorias.class);
            MainActivity_topicos.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_topicos.this ,
                    MainActivity_topicos.class);
            MainActivity_topicos.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_topicos.this ,
                    MainActivity_contato.class);
            MainActivity_topicos.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_topicos.this ,
                    MainActivity_empresa.class);
            MainActivity_topicos.this.startActivity(intentMain);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void gettopicson(final int scrol) {

        topicosinscritosuser.clear();

        String idClient = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
      //  DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");
        database.child("TopicosInscritos").child(idClient).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String slg = String.valueOf(postSnapshot.getValue());
                    topicosinscritosuser.add(slg);
                }
                gettopics("Categorias");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void gettopics(String escolha){

        final ArrayList<String> mytopics = new ArrayList<>();
        //DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com");
        database.child("Topicos").child(escolha).orderByChild("nome").addListenerForSingleValueEvent(new ValueEventListener()    {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    RelativeLayout consloadwindow = (RelativeLayout) findViewById(R.id.layoutloadpromo);
                    consloadwindow.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });


        final  ListView  mListView = (ListView) findViewById(R.id.listatopicos);


        FirebaseListAdapter<Topicnews>  mAdapter = new FirebaseListAdapter<Topicnews>(c,Topicnews.class, R.layout.topico,database.child("Topicos").child(escolha).orderByKey()) {

            @Override
            protected void populateView(View view, final Topicnews myObj,final int position) {

                //Set the value for the views
                final Topicnews it = myObj;
                TextView tn = (TextView) view.findViewById(R.id.txtnometopic);
                tn.setText("" + it.nome);
                String no = deAccent(it.nome);

                final Switch swtopic = (Switch) view.findViewById(R.id.switch1);
                crtltopic=false;
                swtopic.setChecked(false);

                if (sc != 0) {
                    mListView.smoothScrollToPosition(sc);
                    sc=0;
                }

                for (int i = 0; i < topicosinscritosuser.size(); i++) {
                    if (topicosinscritosuser.get(i).contains(no)) {
                        crtltopic=false;
                        swtopic.setChecked(true);
                    }
                }

                swtopic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (!swtopic.isChecked())crtltopicmove=false; else crtltopicmove=true;
                        if (crtltopic == true ) {
                            String topico = deAccent(it.nome);
                            crtltopic=false;

                            if (swtopic.isChecked()) {
                                Toast.makeText(c, "Notificação para " + it.nome + " ativada", Toast.LENGTH_SHORT).show();
                                String n = topico;
                                topicosinscritosuser.add(n);
                                FirebaseMessaging.getInstance().subscribeToTopic(topico);
                                sendtopicuser(topico);
                            } else {
                                if (crtltopicmove==false){
                                    sc = position;
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topico);
                                    Toast.makeText(c, "Notificação para " + it.nome + " desativada", Toast.LENGTH_SHORT).show();
                                    canceltopicuser(topico);
                                }

                            }

                        }
                    }
                });

                swtopic.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {

                        int action = MotionEventCompat.getActionMasked(event);

                        switch (action) {

                            case (ACTION_MOVE):
                                crtltopic=true;
                                if (swtopic.isChecked()==false)
                                    crtltopicmove=false;

                                return true;
                            default:
                                return false;

                        }

                    }
                });


                swtopic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String topico  = deAccent(it.nome);
                        if (swtopic.isChecked()) {
                            crtltopic=true;
                            swtopic.setChecked(true);
                        } else {
                            crtltopic=true;
                            swtopic.setChecked(false);
                        }
                    }
                });

                tn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String topico  = deAccent(it.nome);
                        if (!swtopic.isChecked()) {
                            crtltopic=true;
                            swtopic.setChecked(true);
                        } else {
                            crtltopic=true;
                            swtopic.setChecked(false);
                        }
                    }
                });


            };

        };

        mListView.setAdapter(mAdapter);
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }


    public void sendtopicuser(String topico){

        String idClient = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
        final DatabaseReference databasetopc= FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/TopicosInscritos");
        databasetopc.child(idClient).child(topico).setValue(topico);

    }

    public void canceltopicuser(String topico){

        String idClient = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
        final DatabaseReference databasetopc= FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/TopicosInscritos");
        databasetopc.child(idClient).child(topico).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                gettopicson(0);

            }
        });
    }

}
