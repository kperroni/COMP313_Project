package com.comp313_002.crimestalker.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.comp313_002.crimestalker.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Call911Activity extends AppCompatActivity {
    String[] value;
    Geocoder geocoder;
    List<Address> address;
    String completeAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call911);
        geocoder = new Geocoder(this, Locale.getDefault());
        //getting the value from ReportCrimeActivity
        value = getIntent().getExtras().getStringArray("coordinate");
        //getting the geo position
        try {
            address = geocoder.getFromLocation(Double.valueOf(value[0]), Double.valueOf(value[1]), 1);
            completeAddress = address.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //onClick that decides if was police call or email
    public void emergencyClick (View v){
        //button Call police
        if(v.getId()==R.id.imgbtnPhone)
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:111")); // number that will be called
            startActivity(intent);
        }
        //button send email
        else if(v.getId()==R.id.mgbtnEmail) {
            Intent intent = new Intent(Intent.ACTION_SENDTO)
                    .setData(new Uri.Builder().scheme("mailto").build())
                    .putExtra(Intent.EXTRA_EMAIL, new String[]{"Police <no@dd.com>"}) // not real email
                    .putExtra(Intent.EXTRA_SUBJECT, "Crime Stalk - Type: " + value[4]) // subject get from Type
                    .putExtra(Intent.EXTRA_TEXT, "Report Crime on "+ value[2] +" in this address: " + completeAddress); // body with date and complete address

            try {
                startActivity(Intent.createChooser(intent, "Send email using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(Call911Activity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId()==R.id.btnBack){
            startActivity(new Intent(Call911Activity.this,HomeActivity.class));
        }

    }
}
