package brejapp.com.brejapp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Home on 25/01/2018.
 */

public class locationService extends Service implements LocationListener {

    LocationManager service;
    static boolean enabled = false;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    public Thread mythread;
    Context c;
    static boolean loop = true;
    private String provider;
    // flag for GPS status
    // flag for GPS status
    boolean isGPSEnabled = false;

    static int time = 3000;
    static  int sleep = 2000;
    static   int distance =  0;
    // flag for network status
    boolean isNetworkEnabled = false;
    static LatLng point_ = new LatLng(0.0,0.0);;
    boolean networkEnabled=false;

    // flag for GPS status
    boolean canGetLocation = false;

    static Location location; // location
    static double latitude =0.0; // latitude
    static double longitude=0.0; // longitude
    static boolean ctrlTrecho=false;
    static boolean ctrllocaluser = false;
    // The minimum distance to change Updates in meters
     static long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // 10 meters
    static  ArrayList<LatLng> points = new ArrayList<>();

    // The minimum time between updates in milliseconds
     static  long MIN_TIME_BW_UPDATES = 1; // 1 s
    protected LocationManager locationManager;

    static String iditem="null";
    static String entidadeTrecho="null";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful


        c = getApplicationContext();
        enabled=true;
        getLocation();


        return Service.START_STICKY;
    }

    public  void upDatePoints(ArrayList<LatLng> pointsList){

    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

    }


    @Override
    public void onLocationChanged(Location locationChange) {
        Log.d("GPS ", "onLocationChanged ");
        location = locationChange;

        if (MainActivity_empresa.servgps==true){
            MainActivity_empresa me = new MainActivity_empresa();
            me.setLocal(new LatLng(locationChange.getLatitude(),locationChange.getLongitude()));
        }else{
            MainActivity me = new MainActivity();
            me.setLocal(new LatLng(locationChange.getLatitude(),locationChange.getLongitude()));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("GPS ", "onStatusChanged ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("GPS ", "onProviderEnabled ");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("GPS ", "onProviderEnabled ");
    }


    public Location getLocation() {


        try {
            locationManager = (LocationManager) c.getSystemService(LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                    // Permission already Granted
                    //Do your work here
                    //Perform operations here only which requires permission
                    Log.d("GPS ", "PERMISSION_GRANTED");

                }
                if (isNetworkEnabled) {

                    Log.d("GPS ", "isNetworkEnabled " );

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES ,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                    Log.d("GPS ", "locationManager "+MIN_DISTANCE_CHANGE_FOR_UPDATES );

                    if (locationManager != null) {

                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        Log.d("GPS ", "GPS location "+location);
                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("GPS ", "GPS isNetwork Lat: "+ latitude);

                        }
                    }
                }else

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    Log.d("GPS ", "isGPSEnabled: " + MIN_DISTANCE_CHANGE_FOR_UPDATES);
                    if (  ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {
                        // Permission already Granted
                        //Do your work here
                        //Perform operations here only which requires permission
                        Log.d("GPS ", "PERMISSION_GRANTED");

                    }
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES ,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d("GPS ", "GPS isGPS Lat: "+ latitude);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("GPS ", "GPS error "+  e.getMessage());
          //  Toast.makeText(c,"GPS NÃO PERMITIDO!",Toast.LENGTH_LONG).show();

        }


        return location;
    }


    /**
     * Function to get latitude
     * */

    static double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */

    static double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */

    public boolean canGetLocation() {
        return this.canGetLocation;
    }




}
