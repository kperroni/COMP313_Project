package com.example.t410.firebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference firedbCrimes = FirebaseDatabase.getInstance().getReference("crimes");
        DatabaseReference crimes;
        crimes = FirebaseDatabase.getInstance().getReference();
        // Creating a crime
        Crime crime = new Crime("Another crime", "This is another new crime", 2);
        crimes.child("crimes").push().setValue(crime);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot crimes : dataSnapshot.getChildren()) {
                    Crime aCrime = crimes.getValue(Crime.class);
                    Log.d("My app", "Getting data: "+aCrime.getDescription());
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        firedbCrimes.addValueEventListener(postListener);

        User newUser = new User("123", "k@e.com", "123", "123 ave.", "123-1234");
    }
}
