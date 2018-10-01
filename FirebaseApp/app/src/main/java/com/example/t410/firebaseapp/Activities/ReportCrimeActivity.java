package com.example.t410.firebaseapp.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.t410.firebaseapp.Classes.Crime;
import com.example.t410.firebaseapp.R;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ReportCrimeActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapView mapView;
    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET};
    private static final String MAP_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_crime);
        verifyPermission();
        Bundle mapBundle = null;
        if (savedInstanceState != null) {
            mapBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(mapBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //Verify application permissions to access location services
    private void verifyPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ReportCrimeActivity.this, permissions, 1);
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        locationManager.requestLocationUpdates("gps", 10000, 2, locationListener);

    }

    //Input Validation
    private boolean hasValidInput(EditText inputText)
    {
        if(!Strings.isEmptyOrWhitespace(inputText.getText().toString())){
            return true;
        }
        else{
            return false;
        }
    }

    //Get the device's last known or current location
    private Location getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation("gps");
        }
        else{
            ActivityCompat.requestPermissions(ReportCrimeActivity.this, permissions, 1);
            return locationManager.getLastKnownLocation("gps");
        }
    }

    public void btnReportCrime_clicked(View view)
    {
        //TODO Push Reports to database
        EditText txtTitle = (EditText) findViewById(R.id.txtTitle);
        EditText txtDescription = (EditText) findViewById(R.id.txtDescription);
        EditText txtType = (EditText) findViewById(R.id.txtType);
        EditText txtComments = (EditText) findViewById(R.id.txtComments);
        Location currentLocation = getCurrentLocation();
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
        if(hasValidInput(txtTitle) && hasValidInput(txtType)){
            Crime crime = new Crime();
            crime.setTitle(txtTitle.getText().toString());
            crime.setDescription(txtDescription.getText().toString());
            crime.setType(txtType.getText().toString());
            if(!Strings.isEmptyOrWhitespace(txtComments.getText().toString()))
                crime.addComments(txtComments.getText().toString());
            crime.setLatitude((float)currentLocation.getLatitude());
            crime.setLongitude((float)currentLocation.getLongitude());
            crime.setTimeStamp(date);

            Toast.makeText( this.getApplicationContext(),"Crime Reported",Toast.LENGTH_LONG ).show();
            clearInputFields();
        }
        else{
            Toast.makeText( this.getApplicationContext(),"Title and Type is required",Toast.LENGTH_LONG ).show();
            txtTitle.setError(txtTitle.getHint() + " is required!");
            txtType.setError(txtType.getHint() + " is required!");
        }

    }
    private void clearInputFields()
    {
        ((EditText) findViewById(R.id.txtTitle)).getText().clear();
        ((EditText) findViewById(R.id.txtDescription)).getText().clear();
        ((EditText) findViewById(R.id.txtType)).getText().clear();
        ((EditText) findViewById(R.id.txtComments)).getText();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location currentLocation = getCurrentLocation();
        LatLng coordinates = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(coordinates).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.addMarker(new MarkerOptions().position(coordinates).title("Marker"));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }
}
