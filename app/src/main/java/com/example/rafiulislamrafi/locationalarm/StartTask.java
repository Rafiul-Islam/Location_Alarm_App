package com.example.rafiulislamrafi.locationalarm;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Calendar;

public class StartTask extends Add_Task implements LocationListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    ImageView start_task_button;
    DataBase_Helper myDatabase;
    ArrayList<LatLng> locations;
    private GoogleMap mMap;
    private Geofence geofence;
    GoogleApiClient googleApiClient;
    private GeofencingRequest geofencingRequest;
    private PendingIntent geoPendingIntent;
    private static final String TAG = "MyTag";
    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;
    Location lastLocation;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    Marker geoFenceMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_task);

        //Hide all the titlebar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        locations = new ArrayList<>();
        myDatabase = new DataBase_Helper(this);

        start_task_button = (ImageView) findViewById(R.id.StartTaskButton);

        checkUserPermission();
        getGoogleApiClient();

        setAlert();


    }

    private boolean checkUserPermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            return true;
            // Toast.makeText(getApplicationContext(),"Need Location Permission",Toast.LENGTH_SHORT).show();
            // finishAffinity();
        }
        return false;
    }

    private void getGoogleApiClient() {
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//            Intent intent = new Intent(this,MapsActivity.class);
//            startActivity(intent);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private void start_services() {
        if(!Info.isStatus()){
            Info.setStatus(true);
            startAllservice();
        }
    }

    private ArrayList<LatLng> getPlace() {
        Cursor allData = myDatabase.getAllData();
        allData.moveToFirst();
        for(int i=0;i<allData.getCount();i++){
            Log.i("Location", "getPlace: " + allData.getString(1));
            String location_str = allData.getString(1);
            String regex = ",";
            String[] latlangs = location_str.split(regex);
            double lat = Double.parseDouble(latlangs[0]);
            double lon = Double.parseDouble(latlangs[1]);
            LatLng latLng = new LatLng(lat,lon);
            locations.add(latLng);
            allData.moveToNext();
        }
        return locations;
    }

    public void setAlert(){

        start_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor data = myDatabase.getAllData();

                Calendar calendar = Calendar.getInstance();

                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.set(Calendar.YEAR, mYear);
                calendar.set(Calendar.MONTH, mMonth);
                calendar.set(Calendar.DAY_OF_MONTH, mDay);

                calendar.set(Calendar.HOUR_OF_DAY, mHour);
                calendar.set(Calendar.MINUTE, mMinute);

                Toast.makeText(getApplicationContext(), "DATE : " + mDay + "-" + mMonth + "-" + mYear + "\nTIME - " + mHour + " : " + mMinute , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), myAlarmService.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        0, intent , PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                        .getSystemService(Context.ALARM_SERVICE);

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3000, pendingIntent);
              start_services();

            }

        });
    }

    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    public void getLastKnownLocation() {
        if(checkUserPermission()){
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(lastLocation!=null){
                double latitude = lastLocation.getLatitude();
                double longitude = lastLocation.getLongitude();
                Log.i(TAG, "onLocationChanged: "+latitude+"|"+longitude);
                startLocationUpdates();
            }
        }else{
            checkUserPermission();
        }
    }

    private void startLocationUpdates(){
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if ( checkUserPermission() )
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged ["+location+"]");
        lastLocation = location;
        //writeActualLocation(location);
    }


    // Create a Geofence
    private Geofence createGeofence( LatLng latLng, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    private GeofencingRequest createGeofenceRequest( Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }
    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkUserPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    public void onResult(@NonNull Status status) {
        if ( status.isSuccess() ) {
          //  drawGeofence();
        } else {
            // inform about fail
        }
    }


    private void startGeofence(LatLng latLng) {
        Log.i(TAG, "startGeofence()");
        if( geoFenceMarker != null ) {
            Geofence geofence = createGeofence( latLng, GEOFENCE_RADIUS );
            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    public void startAllservice() {
        getLastKnownLocation();
        ArrayList<LatLng> place = getPlace();
        for(LatLng latLng:place){
//            double latitude = latLng.latitude;
//            double longitude = latLng.longitude;
            startGeofence(latLng);
        }

    }


}
