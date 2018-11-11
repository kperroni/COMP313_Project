package com.comp313_002.crimestalker.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp313_002.crimestalker.Classes.Crime;
import com.comp313_002.crimestalker.Classes.CrimeList;
import com.comp313_002.crimestalker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReadCrime extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("crimes/CrimeReports");

    private ListView listViewCrime ;
    List<Crime> crimeList;
    String key;
    Geocoder geocoder;
    List<Address> address;
    Crime crime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_crime);
        geocoder = new Geocoder(this, Locale.getDefault());
        listViewCrime = (ListView)findViewById(R.id.listViewCrime);
        crimeList = new ArrayList<>();

       // readData();
    }


    @Override
    protected void onStart() {
        super.onStart();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                crimeList.clear();

                for (DataSnapshot crimes: dataSnapshot.getChildren()){
                    crime = crimes.getValue(Crime.class);
                    crime.setKey(crimes.getKey()); //set key to pass as an internal parameter
                    try {
                        address = geocoder.getFromLocation(crime.getLatitude(), crime.getLatitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e){
                        continue;
                    }
                    crime.setAddress(address.get(0).getAddressLine(0));
                    crimeList.add(crime);

                }
                CrimeList adapter = new CrimeList(ReadCrime.this,crimeList);
                listViewCrime.setAdapter(adapter);

                listViewCrime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                        TextView temp = (TextView) v.findViewById(R.id.textViewkey);

                        //Toast.makeText(ReadCrime.this, temp.getText().toString(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ReadCrime.this,CommentCrimeActivity.class);
                        i.putExtra("key", temp.getText().toString());
                        startActivity(i);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
