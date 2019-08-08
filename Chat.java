package brejapp.com.brejapp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Chat extends Application {

    static ArrayList<String> mynewmsg= new ArrayList<String>();
    static ArrayList<String> mynewmsguser= new ArrayList<String>();
    static ArrayList<String> mynewmsgcopy= new ArrayList<String>();
    static ArrayList<String> mychatuser= new ArrayList<String>();
    String local = "Brejapp";
    static   int cont=0;

    static boolean ctrlperst=false;
    @Override
    public void onCreate() {
        super.onCreate();

    }

    Context c;
    public String user;
    public String loja;



    public  Chat(){


    }

    public void setUID_LOJA(Itemloja Loja){
        String uid=FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                .child(Loja.Nome).child("UID").setValue(uid).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


    public void sendStartChat(final Itemloja Loja){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email =  user.getEmail();

        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }

        final String finalEmail = email;
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                .child(Loja.Nome).child(email).child("UIDUSER").setValue(user.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("UsersClient")
                        .child(user.getUid()).child("chat").child("lojasOn").child(Loja.Nome).setValue(Loja.Nome).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
    }


    public void sendmsg(final String msg, final Itemloja Loja, String usertype){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email =  user.getEmail();

        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }


        final String time = String.valueOf(new Date().getTime());
        Log.i("time","tinme "+time);

        final Chatmsgsend Chatmsgsend = new Chatmsgsend(msg, ServerValue.TIMESTAMP,usertype,"false");

        final String finalEmail = email;

        final String finalEmail1 = email;
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                .child(Loja.Nome).child(email).child("MSGS").child(time).setValue(Chatmsgsend).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                        .child(Loja.Nome).child("newmsg").child(finalEmail).child("email").setValue(user.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                                .child(Loja.Nome).child("Users").child(finalEmail).setValue(user.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                                .child(Loja.Nome).child("newmsg").child(finalEmail).child("msg").setValue(msg);
                        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                                .child(Loja.Nome).child("newmsgnotification").child(finalEmail).child("msg").setValue(msg);
                        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                                .child(Loja.Nome).child("newmsgnotification").child(finalEmail).child("email").setValue(user.getEmail());
                    }
                });
            }
        });

    }

    public void sendmsgLoja(String msg, final Itemloja Loja, final String user, String usertype){

        String email =  user;

        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }
        final String time = String.valueOf(new Date().getTime());
        Log.i("time","tinme "+email);

        final Chatmsgsend Chatmsgsend = new Chatmsgsend(msg, ServerValue.TIMESTAMP,"LOJA","false");

        final String finalEmail = email;
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                .child(Loja.Nome).child(email).child("MSGS").child(time).setValue(Chatmsgsend).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                        .child(Loja.Nome).child(finalEmail).child("msgNtf").child(time).setValue(Chatmsgsend);
            }
        });

    }


    public void checknewmsguser(final ImageView img){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email =  user.getEmail();
        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("Chat").
                keepSynced(true);

        local=MainActivity.listalojaschat.get(cont);
        cont++;

        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("Chat")
                .child(local).child(email).child("MSGS").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mynewmsg.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Chatmsg cm  = postSnapshot.getValue(Chatmsg.class);

                    if (cm.viewMsg.equals("false")&& cm.remetent.equals("LOJA")){
                        img.setBackgroundResource(R.drawable.commentnew);
                        Log.i("CHECKMSG","viewMsg "+local);

                        mynewmsg.add(local);
                    }
                }
                if (cont<MainActivity.listalojaschat.size()){
                    checknewmsguser(img);
                }else {

                    if (cont>=MainActivity.listalojaschat.size()) {
                        cont = 0;

                    }
                }
                if (mynewmsg.size()==0)
                    img.setBackgroundResource(R.drawable.comment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void checknewmsguseroutclass(final ImageView img, final ArrayList<String> listalojaschat){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email =  user.getEmail();
        for (int i = 0;i < email.length();i++){
            email=   email.replace(".","AAA");
            email=  email.replace("@","BBB");
        }
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("Chat").
                keepSynced(true);
        local=listalojaschat.get(cont);
        cont++;

        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com").child("Chat")
                .child(local).child(email).child("MSGS").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("CHECKMSG","CHECKMSG commentnew "+dataSnapshot.getValue());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Chatmsg cm  = postSnapshot.getValue(Chatmsg.class);
                    if (cm.viewMsg.equals("false")&& cm.remetent.equals("LOJA")){
                        img.setBackgroundResource(R.drawable.commentnew);

                        mynewmsg.add(local);

                    }
                }


                if (cont<listalojaschat.size()){
                    checknewmsguseroutclass(img,listalojaschat);
                }else {

                }
                if (mynewmsg.size()==0)
                    img.setBackgroundResource(R.drawable.comment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getMychatnewmsg(final String loja, final ImageView iconchat){
        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
                .child(loja).child("newmsg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("LOJA","viewMsg1 "+dataSnapshot.getValue());
                mynewmsguser.clear();
                iconchat.setBackgroundResource(R.drawable.commentnew);
                if (!dataSnapshot.exists()){
                    iconchat.setBackgroundResource(R.drawable.comment);

                }else {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        newmsg nm= postSnapshot.getValue(newmsg.class);

                        mynewmsguser.add(nm.email);

                    }

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        FirebaseDatabase.getInstance().getReferenceFromUrl("https://devscb-7afe7.firebaseio.com/").child("Chat")
//                    .child(loja).addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.i("LOJA","viewMsg1 "+dataSnapshot.getValue());
//                mychatuser.clear();
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    if (!postSnapshot.getKey().equals("UID")) {
//                        mychatuser.add(postSnapshot.getKey());
//                    }
//
//                        postSnapshot.getRef().child("MSGS").orderByChild("viewMsg").equalTo("false").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                                Chatmsg c = dataSnapshot.getValue(Chatmsg.class);
//
//                                    Log.i("LOJA","viewMsg3 "+postSnapshot.getKey());
//                                    iconchat.setBackgroundResource(R.drawable.star);
//
//                                }
//                                if (!dataSnapshot.exists())
//                                    iconchat.setBackgroundResource(android.R.drawable.stat_notify_chat);
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

}
