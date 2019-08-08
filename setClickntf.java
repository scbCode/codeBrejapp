package brejapp.com.brejapp;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class setClickntf extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public setClickntf (){
        Log.i("click","Click log");
        FirebaseDatabase.getInstance().getReference().child("Central_Clicks").child("Notific")
                .child("teste").setValue("click");
    }
}
