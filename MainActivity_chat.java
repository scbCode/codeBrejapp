package brejapp.com.brejapp;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity_chat extends AppCompatActivity
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
    static ListView listmsg;
    Itemloja lojachat;
    Itemloja lojaextra;
    Itemloja myLoja;
    String usertype;
    boolean ctrlchat;
    String userchat;
    boolean ctrlview;
    boolean viewchat;
   ListView  listafav;
    boolean ctrlfav;
    boolean ctrlservice;
    boolean cltropenchat;
    Character lets[]={'a','b','c','d','e','f','g','h','i','j','l','m','n','o','p','q','r','s','t','u','x','v','z','k','w','y'};
    static ArrayList<Integer> idsN = new ArrayList<Integer>();
    static NotificationManager mNotificationManager;

    FirebaseListAdapter<Chatmsg>   msglist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        c = getApplicationContext();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        syncFirebase();
    }



    @Override
    public void onResume(){
        super.onResume();

        Log.i("CHECKMSG","onResume");
        ctrlservice=false;

        Chat_service.cancelntf=true;

        Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
        stopService(servicechat);

        Intent intentmy = getIntent();
        String emailcontat=intentmy.getStringExtra("NTF");
        if (emailcontat!=null){
            ctrlchat=true;
            userchat=emailcontat;
        }


    }

    @Override
    public void onPause(){
        super.onPause();
        Chat_service.cancelntf=false;

        if (MainActivity.myPerfil!=null && MainActivity.myPerfil.status==true && ctrlservice==false) {
            Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
            servicechat.putExtra("loja",MainActivity. myPerfil.Nome);
            servicechat.putExtra("usermsg",MainActivity. myPerfil.Nome);
            startService(servicechat);
        }else {
         if (MainActivity.nomeuser.equals("user")&& MainActivity.listalojaschat.size()>0){
             Intent servicechat = new Intent(getApplicationContext(), Chat_service.class);
             servicechat.putExtra("usermsg","true");
             startService(servicechat);
         }
        }
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


    public void listconversasLoja(final String loja){

        listmsg  = (ListView) findViewById(R.id.listaconversas);

        View  vfav = (View) findViewById(R.id.incbar);

        vfav.setVisibility(View.GONE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idsN.clear();
        for (int i = 0; i < 26; i++) {
            idsN.add(i);
        }

        final FirebaseListAdapter<String>   msglist = new FirebaseListAdapter<String>(c, String.class, R.layout.listaconversas,database.child("Chat")
                .child(loja).child("Users").orderByKey()) {

            @SuppressLint("WrongConstant")
            @Override
            protected void populateView(final View vw, final String model, final int position) {

                int idf =0;
                String idemail =model.toString().replace("@", "");
                idemail = idemail.replace("com", "");
                idemail = idemail.replace("br", "");
                idemail = idemail.replace(".", "");

                for (int ii = 0; ii < idemail.toString().length(); ii += 2) {
                    for (int i = 0; i < lets.length; i++) {
                        if (lets[i].equals(idemail.charAt(ii)))
                            idf += idsN.get(i);
                    }
                }

                mNotificationManager.cancel(idf);

                TextView nome = (TextView) vw.findViewById(R.id.txtlojachatcv);
                nome.setText(model);

                String email = model;

                ImageView logo = (ImageView)vw.findViewById(R.id.logolojachatcv);
                logo.setBackgroundResource(R.drawable.usermsgicon);

                for (int i = 0;i < email.length();i++){
                    email=   email.replace(".","AAA");
                    email=  email.replace("@","BBB");
                }

                final ImageView imgnewmsg = (ImageView)vw.findViewById(R.id.imgnewmsg);
                imgnewmsg.setVisibility(View.INVISIBLE);

                Log.i("BREJAPP","mynewmsguser "+Chat.mynewmsguser.size());
                    for (int i =0; i < Chat.mynewmsguser.size();i++)
                    {
                        if (model.equals(Chat.mynewmsguser.get(i))){
                            imgnewmsg.setVisibility(View.VISIBLE);
                        }

                    }

                Button btnfinla =(Button)vw.findViewById(R.id.btnfinalcnv);
                final String finalEmail = email;
                btnfinla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.child("Chat")
                                        .child(myLoja.Nome).child("Users").child(finalEmail).removeValue();
                         Toast.makeText(c,"Conversa arquivada",Toast.LENGTH_SHORT).show();
                    }
                });

                RelativeLayout click =(RelativeLayout) vw.findViewById(R.id.clickcnv);


                click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cltropenchat=true;
                        sendmsgChatbtnLoja( myLoja,model);
                        chatlistmsg(myLoja,model);}
                });

                if (ctrlchat==true)
                    if (userchat!=null )
                        if (userchat.equals(model))
                             vw.performClick();

            }

        };

        listmsg.setAdapter(msglist);

    }

    public void listconversas(){


        View  vfav = (View) findViewById(R.id.incbar);

        vfav.setVisibility(View.GONE);

        listmsg  = (ListView) findViewById(R.id.listaconversas);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final FirebaseListAdapter<String>   msglist = new FirebaseListAdapter<String>(c, String.class, R.layout.listaconversas,database.child("UsersClient")
                .child(user.getUid()).child("chat").child("lojasOn").orderByKey()) {

            @SuppressLint("WrongConstant")
            @Override
            protected void populateView(final View vw, final String model, final int position) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String email = user.getEmail();

                for (int i = 0;i < email.length();i++){
                    email=   email.replace(".","AAA");
                    email=  email.replace("@","BBB");
                }
                TextView nome = (TextView) vw.findViewById(R.id.txtlojachatcv);
                nome.setText(model);

                Log.i("CHECKMSG","mynewmsg ok "+model);

                ImageView logo = (ImageView)vw.findViewById(R.id.logolojachatcv);
                for (int i =0; i < MainActivity.lojas.size();i++){
                    if (MainActivity.lojas.get(i).Nome.equals(model))
                        Glide.with(c).load(MainActivity.lojas.get(i).urlic).into(logo);
                }

                final ImageView imgnewmsg = (ImageView)vw.findViewById(R.id.imgnewmsg);
                imgnewmsg.setVisibility(View.INVISIBLE);

                FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("Chat")
                        .child(model).child(email).child("MSGS").orderByKey().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Chatmsg cm = postSnapshot.getValue(Chatmsg.class);

                            if (cm.viewMsg.equals("false") && cm.remetent.equals("LOJA")) {
                                imgnewmsg.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


                Button btnfinla =(Button)vw.findViewById(R.id.btnfinalcnv);
                btnfinla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String email = user.getEmail();

                        for (int i = 0;i < email.length();i++){
                            email=   email.replace(".","AAA");
                            email=  email.replace("@","BBB");
                        }



                        final String finalEmail = email;
                        database.child("Chat")
                                .child(model).child("Users").child(email).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                database.child("Chat")
                                    .child("chatoff").child(finalEmail).child(model).removeValue();

                                Log.i("time","Chat x"+model);

                                database.child("UsersClient")
                                        .child(user.getUid()).child("chat").child("lojasOn").child(model).removeValue();

                                database.child("Chat")
                                        .child(model).child("newmsg").child(finalEmail).removeValue();

                                database.child("Chat")
                                        .child(model).child("newmsgnotification").child(finalEmail).removeValue();

                                Toast.makeText(c,"Conversa arquivada",Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });


                RelativeLayout click =(RelativeLayout) vw.findViewById(R.id.clickcnv);


                click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i =0; i < MainActivity.lojas.size();i++){
                            if ( MainActivity.lojas.get(i).Nome.equals(model) ){
                                if ( MainActivity.lojas.get(i).status==true ){
                                lojachat=MainActivity.lojas.get(i);
                                sendmsgChatbtn( lojachat);
                                chatlistmsg(MainActivity.lojas.get(i),user.getEmail());
                                ctrlview=true;
                                }else{
                                  Toast.makeText(c,"Loja está offline.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
                if(lojaextra!=null ) {
                    if (model.equals(lojaextra.Nome)) {vw.performClick();}
                }
            }

        };

        listmsg.setAdapter(msglist);

    }

    public void sendmsgChatbtnLoja(final Itemloja lj, final String user ){

        View v = (View) findViewById(R.id.includechat);

        final EditText msg = (EditText) v.findViewById(R.id.editTexmsgsend);

        Button send = (Button) findViewById(R.id.btnsendmsg);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("time","sendmsg ");
                Chat chat = new Chat();
                chat.sendmsgLoja(msg.getText().toString(),lj,user,"LOJA");
                msg.setText("");

            }
        });

    }

    public void sendmsgChatbtn(final Itemloja lj ){

        View v = (View) findViewById(R.id.includechat);

        final EditText msg = (EditText) v.findViewById(R.id.editTexmsgsend);

        Button send = (Button) v.findViewById(R.id.btnsendmsg);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("time","sendmsg ");
                Chat chat = new Chat();
                chat.sendmsg(msg.getText().toString(),lj,"user");
                msg.setText("");

            }
        });

    }

    public void chatlistmsg(final Itemloja Loja, final String nome){

        View v = (View) findViewById(R.id.includechat);
Log.i("chatlistmsg","chatlistmsg ");
        RelativeLayout rl = (RelativeLayout)  findViewById(R.id.rlchat);
        rl.setVisibility(View.VISIBLE);

        String email =  nome;

        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }

        listmsg  = (ListView) findViewById(R.id.listamsgs);
        listmsg.setDividerHeight(0);
        final String finalEmail = email;


        msglist = new FirebaseListAdapter<Chatmsg>(c, Chatmsg.class, R.layout.msg_chat_layout,database.child("Chat").child(""+Loja.Nome).child(finalEmail).child("MSGS").orderByChild("timestamp")) {

            @SuppressLint("WrongConstant")
            @Override
            protected void populateView(final View vw, final Chatmsg model, final int position) {


                if (model.remetent.equals("user")) {

                    ImageView imgcheck = (ImageView) vw.findViewById(R.id.checkview);

                    if (usertype.equals("LOJA")) {
                        Log.i("user","user LOJA1 "+model.msg);
//                        database.child("Chat")
//                                .child(Loja.Nome).child("newmsg").child(finalEmail).removeValue();
                        msglist.getRef(position).child("viewMsg").setValue("true");
                        imgcheck.setVisibility(View.INVISIBLE);

                        RelativeLayout rl1 = (RelativeLayout) vw.findViewById(R.id.rluser);
                        rl1.setVisibility(View.VISIBLE);

                        RelativeLayout rl1j = (RelativeLayout) vw.findViewById(R.id.rlloja);
                        rl1j.setVisibility(View.INVISIBLE);

                        TextView msg = (TextView) vw.findViewById(R.id.txtmsg);
                        msg.setText(model.msg);

                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(String.valueOf(model.timestamp)));

                        Date resultdateExp = new Date(calendar.getTimeInMillis());//GET DATE EXP.
                        String dateInexp = formatter.format(resultdateExp);

                        TextView msgh = (TextView) vw.findViewById(R.id.horamsg);
                        msgh.setText(dateInexp);

                        ImageView img = (ImageView) vw.findViewById(R.id.iconmg);
                        Glide.with(c).load(Loja.urlic).into(img);



                    }
                     else {

                        Log.i("user","user LOJA2 "+model.msg);

                        ImageView imgcheck2 = (ImageView) vw.findViewById(R.id.checkview2);
                        imgcheck2.setBackgroundResource(R.drawable.checkoff);
                        imgcheck2.setVisibility(View.VISIBLE);
                        if (model.viewMsg.equals("false"))imgcheck2.setBackgroundResource(R.drawable.checkoff);
                        else
                            imgcheck2.setBackgroundResource(R.drawable.check);

                        RelativeLayout rl1 = (RelativeLayout) vw.findViewById(R.id.rluser);
                        rl1.setVisibility(View.INVISIBLE);

                        RelativeLayout rl1j = (RelativeLayout) vw.findViewById(R.id.rlloja);
                        rl1j.setVisibility(View.VISIBLE);

                        TextView msg = (TextView) vw.findViewById(R.id.msgloja);
                        msg.setText(model.msg);

                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(String.valueOf(model.timestamp)));
                        Date resultdateExp = new Date(calendar.getTimeInMillis());//GET DATE EXP.
                        String dateInexp = formatter.format(resultdateExp);

                        TextView msgh = (TextView) vw.findViewById(R.id.msghoraloja);
                        msgh.setText(dateInexp);

                        Log.i("TIME","TIME "+model.timestamp);

                        ImageView img = (ImageView) vw.findViewById(R.id.imglojamsg);
                        img.setBackgroundResource(R.drawable.manuser);

                    }

                }
                else {
                    if (model.remetent.equals("LOJA"))
                        if (usertype.equals("user")) {
                            Log.i("user","user LOJA3 "+model.msg);

                            Chat.mynewmsg.clear();
                        Chat.cont = 0;
                        msglist.getRef(position).child("viewMsg").setValue("true");

                        ImageView imgcheck2 = (ImageView) vw.findViewById(R.id.checkview);
                        imgcheck2.setVisibility(View.INVISIBLE);

                        RelativeLayout rl1 = (RelativeLayout)   vw.findViewById(R.id.rluser);
                        rl1.setVisibility(View.VISIBLE);

                        RelativeLayout rl1j = (RelativeLayout)       vw.findViewById(R.id.rlloja);
                        rl1j.setVisibility(View.INVISIBLE);

                        TextView msg = (TextView) vw.findViewById(R.id.txtmsg);
                        msg.setText(model.msg);

                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");

                        // Create a calendar object that will convert the date and time value in milliseconds to date.
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(String.valueOf(model.timestamp)));
                        Date resultdateExp = new Date(calendar.getTimeInMillis());//GET DATE EXP.
                        String dateInexp = formatter.format(resultdateExp);

                        TextView msgh = (TextView) vw.findViewById(R.id.horamsg);
                        msgh.setText(dateInexp);

                        ImageView imgl = (ImageView) vw.findViewById(R.id.iconmg);
                            Glide.with(c).load(Loja.urlic).into(imgl);

                    }else {

                        Log.i("user","user LOJA4 "+model.msg);

                        ImageView imgcheck2 = (ImageView) vw.findViewById(R.id.checkview2);

                        RelativeLayout rl1 = (RelativeLayout)   vw.findViewById(R.id.rluser);
                        rl1.setVisibility(View.INVISIBLE);

                        RelativeLayout rl1j = (RelativeLayout)       vw.findViewById(R.id.rlloja);
                        rl1j.setVisibility(View.VISIBLE);

                        TextView msg = (TextView) vw.findViewById(R.id.msgloja);
                        msg.setText(model.msg);

                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(String.valueOf(model.timestamp)));
                        Date resultdateExp = new Date(calendar.getTimeInMillis());//GET DATE EXP.
                        String dateInexp = formatter.format(resultdateExp);

                        TextView msgh = (TextView) vw.findViewById(R.id.msghoraloja);
                        msgh.setText(dateInexp);

                        ImageView imgl = (ImageView) vw.findViewById(R.id.imglojamsg);

                        Glide.with(c).load(Loja.urlic).into(imgl);

                        //
                        imgcheck2.setVisibility(View.VISIBLE);

                        if (model.viewMsg.equals("false"))imgcheck2.setBackgroundResource(R.drawable.checkoff);
                        else
                            imgcheck2.setBackgroundResource(R.drawable.check);


                    }



                }


            }




        };

        listmsg.setAdapter(msglist);

    }

    public void syncFirebase() {

        //persistence start
        if (database == null) {
             database = MainActivity.database;

        }

        Intent check = getIntent();
        String chatcheck=check.getStringExtra("Chat");
        if (chatcheck!=null){
            getIntent().removeExtra("Chat");

            for (int i =0; i < MainActivity.lojas.size();i++){
                if (MainActivity.lojas.get(i).Nome.equals(chatcheck)){
                    Log.i("BACK","BACK Chat");
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    RelativeLayout rl =(RelativeLayout) findViewById(R.id.rlchat);
                    rl.setVisibility(View.VISIBLE);
                    sendmsgChatbtn(MainActivity.lojas.get(i));
                    chatlistmsg(MainActivity.lojas.get(i),user.getEmail());
                    lojaextra=(MainActivity.lojas.get(i));
                }
            }
        }

        final String checkuserchat=check.getStringExtra("Chatuser");
        if (checkuserchat!=null){

            String checkuserchatloja=check.getStringExtra("Loja");
            getIntent().removeExtra("Chatuser");

            for (int i =0; i < MainActivity.lojas.size();i++){
                if (MainActivity.lojas.get(i).Nome.equals(checkuserchatloja)){
                    Log.i("BACK","BACK Chatuser");
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    RelativeLayout rl =(RelativeLayout) findViewById(R.id.rlchat);
                    rl.setVisibility(View.VISIBLE);
                    String email = checkuserchat;
                    for(int ie =0; ie < email.length();ie++){
                        email  = email.replace("AAA",".");
                        email  = email.replace("BBB","@");
                    }

                    final int finalI = i;
                    database.child("Chat") .child("chatoff").child(checkuserchat).child(MainActivity.lojas.get(i).Nome).setValue(MainActivity.lojas.get(i).Nome).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sendmsgChatbtnLoja(MainActivity.lojas.get(finalI),checkuserchat);
                          //  chatlistmsg(MainActivity.lojas.get(finalI),checkuserchat);
                        }
                    });



//                    lojaextra=(MainActivity.lojas.get(i));
                }
            }
        }

        if (MainActivity.nomeuser!=null)
      if(!MainActivity.nomeuser.equals("LOJA")){
          usertype="user";
          listconversas();
      }
      else
      if(MainActivity.nomeuser.equals("LOJA")) {
          usertype="LOJA";
          FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersEmpresa")
                  .child(MainActivity.myUser.Uid).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  Itemloja tlj = dataSnapshot.getValue(Itemloja.class);
                  if (dataSnapshot.exists()){
                      myLoja=tlj;
                      listconversasLoja(myLoja.Nome);
                   //   listmsg  = (ListView) findViewById(R.id.listaconversas);
                     // listmsg.setVisibility(View.INVISIBLE);
                      usertype="LOJA";
                  }else {

                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }

          });

       } barBottom();
        getClient();
    }

    @Override
    public void onBackPressed() {
  ctrlservice=true;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            listmsg  = (ListView) findViewById(R.id.listaconversas);
            listmsg.setVisibility(View.VISIBLE);

            if ( ctrlview==true && lojaextra==null) {
                ctrlview=false;
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlchat);
                rl.setVisibility(View.INVISIBLE);
                View  vfav = (View) findViewById(R.id.incbar);
                vfav.setVisibility(View.VISIBLE);
            }else
            if ( cltropenchat==true){
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlchat);
                rl.setVisibility(View.INVISIBLE);
                View  vfav = (View) findViewById(R.id.incbar);
                vfav.setVisibility(View.VISIBLE);
                cltropenchat=false;
            }
            else
                if (ctrlfav==true){
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlfav);
                    rl.setVisibility(View.INVISIBLE);
                    ctrlfav=false;
                }
                else {
                    Log.i("CHECKMSG","onBackPressed");
                    lojaextra=null;
                    ctrlview=false;
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
                if (user!=null)
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
                                if (user!=null)
                                FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("UsersEmpresa")
                                        .child(user.getUid()).child("ntfOn").removeValue();
                            }
                        });
                    }
                });
            }

        }else
        if (id == R.id.nav_camera) {
            Intent intentMain = new Intent(MainActivity_chat.this ,
                    MainActivity.class);
            MainActivity_chat.this.startActivity(intentMain);
        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_chat.this ,
                    MainActivity_lojas.class);
            MainActivity_chat.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_chat.this ,
                    MainActivity_categorias.class);
            MainActivity_chat.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_chat.this ,
                    MainActivity_topicos.class);
            MainActivity_chat.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_chat.this ,
                    MainActivity_chat.class);
            MainActivity_chat.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_chat.this ,
                    MainActivity_empresa.class);
            MainActivity_chat.this.startActivity(intentMain);
        }
        else if (id == R.id.nav_cesta) {
            if (myUser!=null && MainActivity.mycesta.size()>0){
                Intent intentMain = new Intent(MainActivity_chat.this ,
                        MainActivity_cesta.class);

                MainActivity_chat.this.startActivity(intentMain);
            }
            else{
                Toast.makeText(c,"Cesta vazia",Toast.LENGTH_SHORT).show();
            }
        }  else if (id == R.id.nav_mypedidos) {
            Intent intentMain = new Intent(MainActivity_chat.this ,
                    MainActivity_pedidos.class);
            MainActivity_chat.this.startActivity(intentMain);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void barBottom(){


        View  vfav = (View) findViewById(R.id.incbar);

        Button btncasas= (Button) vfav.findViewById(R.id.btncasas);
        btncasas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentMain = new Intent(MainActivity_chat.this ,
                        MainActivity_lojas.class);
                ctrlservice=true;

                MainActivity_chat.this.startActivity(intentMain);
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


                    Intent intentMain = new Intent(MainActivity_chat.this ,
                            MainActivity_delivery.class);
                    MainActivity_chat.this.startActivity(intentMain);
                }
            }

        });

        Button btncesta= (Button) vfav.findViewById(R.id.btncestabar);
        btncesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.myPerfil == null) {
                    if (MainActivity.mycesta.size() > 0) {
                        Intent intentMain = new Intent(MainActivity_chat.this,
                                MainActivity_cesta.class);
                        ctrlservice = true;

                        MainActivity_chat.this.startActivity(intentMain);
                    } else {
                        Toast.makeText(c, "Sem itens na cesta", Toast.LENGTH_SHORT).show();
                    }
                }
            else
            {
                Intent intentMain = new Intent(MainActivity_chat.this,
                        MainActivity_dashempresa.class);
                MainActivity_chat.this.startActivity(intentMain);
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
                if (myUser!=null) {
                    if (MainActivity.listalojaschat.size() > 0 && !MainActivity.nomeuser.equals("LOJA")) {
                        Intent intentMain = new Intent(MainActivity_chat.this,
                                MainActivity_chat.class);
                        ctrlservice=true;

                        MainActivity_chat.this.startActivity(intentMain);
                    } else {
                        if (MainActivity.nomeuser.equals("LOJA")) {
                            ctrlservice=true;

                            Intent intentMain = new Intent(MainActivity_chat.this,
                                    MainActivity_chat.class);
                            MainActivity_chat.this.startActivity(intentMain);
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

    public void getClient(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            myUser = new User("", user.getUid(), user.getEmail(), null, null, "", "FREEN");
            ;
            getchat();
        }

    }

    public void getchat(){

        if (myUser!=null)
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("UsersClient")
                .child( myUser.Uid).child("chat").child("lojasOn").orderByKey().addValueEventListener(new ValueEventListener() {
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


}
