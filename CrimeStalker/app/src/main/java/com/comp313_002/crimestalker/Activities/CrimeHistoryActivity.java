package com.comp313_002.crimestalker.Activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.comp313_002.crimestalker.Classes.CrimeHistoryClient;
import com.comp313_002.crimestalker.R;
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
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * @author Kenneth Bato
 * CrimeHistoryActivity class is a map activity that handles displaying crime report history
 * retrieved from the CrimeHistoryClient Singleton
 */
public class CrimeHistoryActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private CrimeHistoryClient histClient;
    private LocationManager lm;
    //Current location coordinates in latitude and longtitude
    private double longitude;
    private double latitude;

    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 20;
    private static final String TAG = "CrimeHistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_history);
        //Get location services
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Call getLocation method to set current location
        getLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get singleton instance
        histClient = CrimeHistoryClient.getInstance();
        //Create JsonHttpResponseHandler
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    //Loop and parse through the response to get our data
                    JSONArray array = response.getJSONArray("features");
                    for(int i = 0; i < array.length(); i++)
                    {
                        JSONObject record = array.getJSONObject(i).getJSONObject("attributes");
                        //Create BitmapDescriptor that will be the icon for each record
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.crime_icon_orange);
                        //Create a marker on the map and set the title, icon and snippet
                        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(record.getDouble("Lat"), record.getDouble("Long"))));
                        marker.setTitle(record.getString("MCI"));
                        marker.setIcon(icon);
                        marker.setSnippet(String.format("Occurrence Date: %s %s, %s", record.getString("occurrencemonth"), record.getString("occurrenceday"), record.getString("occurrenceyear")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to retrieve response: ", e);
                }
            }
        };
        //Call the CrimeHistoryClient.getData() method from the singleton with the JsonHttpResponseHandler instance as the parameter
        histClient.getData(handler);
    }

    /*
     * Method to store device's current location
     * */
    private void getLocation() {
        // Check if permissions are granted
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        else {
            // Get the last known location given the NETWORK_PROVIDER
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            // If the location is not NULL
            if (location != null) {
                // Assign latitude and longitude values of the location to the variables
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        // Otherwise, if permissions are in place then...
        else {
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
                Toast.makeText(getApplicationContext(), "Marker text" + marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
