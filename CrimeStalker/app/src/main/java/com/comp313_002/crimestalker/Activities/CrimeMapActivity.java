package com.comp313_002.crimestalker.Activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.comp313_002.crimestalker.Classes.Crime;
import com.comp313_002.crimestalker.Classes.CrimeList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.comp313_002.crimestalker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CrimeMapActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private LocationManager lm;
    private static final int DEFAULT_ZOOM = 20;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference crimesRef = database.getReference("crimes/CrimeReports");
    List<Crime> crimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_map);

        crimeList = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }

    private void getLocation(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        else{
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        else{
            mMap.setMyLocationEnabled(true);
            }
        LatLng position = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(position).title("You are here!"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM));
        mMap.setOnMarkerClickListener(this);

        // This is to execute a block of code when the text of the marker is clicked
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // TODO
                Toast.makeText(getApplicationContext(), "Marker text" +marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        this.loadMapWithCrimes();
        }

        private void loadMapWithCrimes(){
            crimesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    crimeList.clear();
                    for (DataSnapshot crimes: dataSnapshot.getChildren()){
                        Crime crime = crimes.getValue(Crime.class);
                        crimeList.add(crime);
                    }
                    for (Crime aCrime : crimeList){
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.crime_icon);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(aCrime.getLatitude(), aCrime.getLongitude())).title(aCrime.getTitle()).icon(icon));
                        // Add id to marker's tag, so that it can be queried when the marker is clicked
                        // Marker can be switched depending on the type of the crime
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked ",
                    Toast.LENGTH_SHORT).show();
            // Here another activity can be launched to show the information about the crime clicked


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
    }
