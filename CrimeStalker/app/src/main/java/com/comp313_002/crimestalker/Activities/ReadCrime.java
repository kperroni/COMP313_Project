package com.comp313_002.crimestalker.Activities;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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


import java.util.ArrayList;
import java.util.List;

public class ReadCrime extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("crimes/CrimeReports");

    private ListView listViewCrime ;
    List<Crime> crimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_crime);

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
                    Crime crime = crimes.getValue(Crime.class);
                    crimeList.add(crime);
                }
                CrimeList adapter = new CrimeList(ReadCrime.this,crimeList);
                listViewCrime.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
