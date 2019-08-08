package brejapp.com.brejapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import brejapp.com.brejapp.R;



public class MainActivity_barcode extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener
     {

    Context c;
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String BarcodeObject = "Barcode";
    private static final int RC_HANDLE_GMS = 9001;
         private CompoundButton autoFocus;
         private CompoundButton useFlash;
         private TextView statusMessage;
         private TextView barcodeValue;

         private static final int RC_BARCODE_CAPTURE = 9001;
         private static final String TAG = "BarcodeMain";

    Bitmap bitmap;
    private final static String DEBUG_TAG = "MakePhotoActivity";
    private Camera camera;
    private int cameraId = 0;
    boolean capture = false;
    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    Camera mCamera;
         private Point sizes;
         private FirebaseVisionImage image;
         private byte[] dataimg;
         boolean ctrl;

    private static final int RC_HANDLE_CAMERA_PERM = 2;
    String codebar;
        SurfaceView mSurfaceView;
        String mainActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_barcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        c = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainActivity = "MainDashboard";

        Button btntreturn = (Button) findViewById(R.id.btnreturncam);
        btntreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainActivity.equals("MainDashboard"))
                {
                    Intent intentMain = new Intent(MainActivity_barcode.this ,
                            MainActivity_dashempresa.class);
                    MainActivity_barcode.this.startActivity(intentMain);
                }
            }
        });



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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

            Intent intentMain = new Intent(MainActivity_barcode.this ,
                    MainActivity_lojas.class);
            MainActivity_barcode.this.startActivity(intentMain);

        } else if (id == R.id.nav_slideshow) {
            Intent intentMain = new Intent(MainActivity_barcode.this ,
                    MainActivity_categorias.class);
            MainActivity_barcode.this.startActivity(intentMain);
        } else if (id == R.id.nav_topicos) {
            Intent intentMain = new Intent(MainActivity_barcode.this ,
                    MainActivity_topicos.class);
            MainActivity_barcode.this.startActivity(intentMain);
        } else if (id == R.id.nav_share) {
            Intent intentMain = new Intent(MainActivity_barcode.this ,
                    MainActivity_contato.class);
            MainActivity_barcode.this.startActivity(intentMain);
        } else if (id == R.id.nav_empresa) {
            Intent intentMain = new Intent(MainActivity_barcode.this ,
                    MainActivity_empresa.class);
            MainActivity_barcode.this.startActivity(intentMain);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
