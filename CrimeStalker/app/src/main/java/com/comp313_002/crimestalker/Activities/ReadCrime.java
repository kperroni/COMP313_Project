package com.comp313_002.crimestalker.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.comp313_002.crimestalker.Classes.Crime;
import com.comp313_002.crimestalker.Classes.CrimeList;
import com.comp313_002.crimestalker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReadCrime extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("crimes/CrimeReports");

    private ListView listViewCrime;
    List<Crime> crimeList;
    String key;
    Geocoder geocoder;
    List<Address> address;
    Crime crime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_crime);
        Toolbar toolbar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            //setting the title
            toolbar.setTitle("Crime Reports");

        }


        geocoder = new Geocoder(this, Locale.getDefault());
        listViewCrime = (ListView) findViewById(R.id.listViewCrime);
        crimeList = new ArrayList<>();
    }

    //it will read all the changes done inside the database firebase
    @Override
    protected void onStart() {
        super.onStart();
        //it reads crimes/CrimeReports key-value
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                crimeList.clear();
                // loop in each value inside the database
                for (DataSnapshot crimes : dataSnapshot.getChildren()) {
                    crime = crimes.getValue(Crime.class);
                    crime.setKey(crimes.getKey()); //set key to pass as an internal parameter
                    try {
                        //getting the right location on the globe
                        address = geocoder.getFromLocation(crime.getLatitude(), crime.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        continue;
                    }
                    crime.setAddress(address.get(0).getAddressLine(0));
                    crimeList.add(crime);

                }
                //send the value get from database and pass to the list view
                CrimeList adapter = new CrimeList(ReadCrime.this, crimeList);
                listViewCrime.setAdapter(adapter);
                //listen the click on any item from the list view
                listViewCrime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                        //getting the right position from the listview and send to CommentCrimeActivity
                        TextView temp = (TextView) v.findViewById(R.id.textViewkey);
                        Intent i = new Intent(ReadCrime.this, CommentCrimeActivity.class);
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
