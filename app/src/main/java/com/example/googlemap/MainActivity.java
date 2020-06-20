package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign variable
        supportMapFragment  =   (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        // Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        // Check permission
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // WHEN PERMISSION GRATED
            // CALL METHOD
            getCurrentLocation();
        }else {
            // WHEN PERMISSION DENIED
            // REQUEST PERMISSION
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getCurrentLocation() {
        // INITIALIZE TASK LOCATION

       Task<Location> task = client.getLastLocation();
       task.addOnSuccessListener(new OnSuccessListener<Location>() {
           @Override
           public void onSuccess(final Location location) {
               //   WHEN SUCCESS
               if (location != null) {
                   // SYNC MAP
                   supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                       @Override
                       public void onMapReady(GoogleMap googleMap) {
                           // INITIALIZE LAT LNG
                           LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                           // LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                           // CREATE MARKER OPTIONS
                           MarkerOptions options = new MarkerOptions().position(latLng).title("I am there");

                           // ZOOM MAP
                           googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                           // Add marker on map
                           googleMap.addMarker(options);
                       }
                   });
               }
           }
       });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
                //WHEN PERMISSION GRANTED
                // CALL METHOD

                getCurrentLocation();
            }
        }
    }
}
