package brejapp.com.brejapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class persistent extends Application {


    static boolean ctrlperst=false;
    @Override
    public void onCreate() {
        super.onCreate();
        try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                ctrlperst = true;
        }catch (Exception e){}
        }

}
