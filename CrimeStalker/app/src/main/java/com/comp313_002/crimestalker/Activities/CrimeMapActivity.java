package com.comp313_002.crimestalker.Activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.google.android.gms.maps.model.MapStyleOptions;
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

    // GoogleMap variable to manipulate the map
    private GoogleMap mMap;
    // Double variables to control longitude and latitude values
    // Used to pinpoint locations in the map
    private double longitude;
    private double latitude;
    // Location manager variable to access current location of the phone where the app is being run
    private LocationManager lm;
    // Constant to set the default zoom value when the map is launched
    private static final int DEFAULT_ZOOM = 20;

    // Variable to get reference to firebase's database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    // Variable to create reference to the crimes collection in firebase's database
    private DatabaseReference crimesRef = database.getReference("crimes/CrimeReports");
    private static final String TAG = "CrimeHistoryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Assigning the lm variable to the system service LOCATION_SERVICE
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // Calling getLocation method
        getLocation();
    }

    /*
    @Author: Kenny Perroni
    This method requests any permissions necessary to allow the app to use location services.
    Also, it populates the longitude and latitude variables with the current location values of the phone
     */
    private void getLocation(){
        // Checking if the permissions are defined in the Android manifest file
        // Otherwise, request the permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        // If permissions are defined then...
        else{
            // Get the last known location given the NETWORK_PROVIDER
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            // If the location is not NULL
            if(location != null){
                // Assign latitude and longitude values of the location to the variables
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    /*
     * @Author: Kenny Perroni
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
        // Assignning googleMap parameter to our class variable mMap to manipulate map
        mMap = googleMap;

        //Set map styling
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // Checking if permissions are enabled for ACCESS_FINE_LOCATION permission in the Android manifest file
        // If there are not defined, request them
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        // Otherwise, if permissions are in place then...
        else{
            // Setting MyLocationEnable option in the map
            mMap.setMyLocationEnabled(true);
            }
        // Creating a LatLng object with the latitude and longitude values of te current location of the user
        LatLng position = new LatLng(latitude, longitude);
        // Adding the *You are here!* marker for the current location
        mMap.addMarker(new MarkerOptions().position(position).title("You are here!"));
        // Moving camera to current location using the DEFAULT_ZOOM value as the zoom
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM));
        // Setting onClickListener when a marker is clicked
        mMap.setOnMarkerClickListener(this);

        // This is to execute a block of code when the text of the marker is clicked
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // TODO: Here an action could be program when the marker's info window is clicked
                Toast.makeText(getApplicationContext(), "Marker text" +marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        // Calling method loadMapWithCrimes to show the crimes in the database on the map
        this.loadMapWithCrimes();
        }

        /*
        @Author: Kenny Perroni
        This method is called once the map is ready and the current location marker has been added.
        The method reads from the crimes collection in firebase to show the crimes on the map with
        an icon.
         */
        private void loadMapWithCrimes(){
            // Creating a listener for the crimesRef reference to the crimes collection in firebase
            crimesRef.addValueEventListener(new ValueEventListener() {
                // When the data changes on the collection, this code will be executed
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // This foreach loop iterates through the dataSnapshot parameter that holds all the crime values
                    for (DataSnapshot crimes: dataSnapshot.getChildren()){
                        // Instantiating a crime object and casting the crimes snapshot to Crime.class
                        Crime crime = crimes.getValue(Crime.class);
                        // Creating an icon variable to set it to the crime value on the map
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.crime_icon_orange);
                        // Adding the marker with the latitude and longitude values from the crime in the current iteration
                        // Also, adding the icon created above to it
                        mMap.addMarker(new MarkerOptions().position(new LatLng(crime.getLatitude(), crime.getLongitude())).title(crime.getTitle()).icon(icon));
                    }
                }

                // This method is called if an error occurs within the database
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Error catching here
                }
            });
        }

     /*
     @Author: Kenny Perroni
     This method will be executed when the marker is clicked
     At this point, there is no functionality attached to this but to show only a message that
     that particular marker has been clicked
      */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        //Showing a message that the marker's title passed by parameter has been clicked
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked ",
                    Toast.LENGTH_SHORT).show();
            // TODO: Here another activity can be launched to show the details about the crime marker clicked

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}
