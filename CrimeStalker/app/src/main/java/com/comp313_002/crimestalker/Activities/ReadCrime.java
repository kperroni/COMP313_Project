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
import com.comp313_002.crimestalker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadCrime extends AppCompatActivity {

    private FirebaseDatabase myData;
    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener myAuthListener;
    private DatabaseReference myRef;
    private String userID;

    private ListView myLitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_crime);

        myLitView = (ListView)findViewById(R.id.id_dynamic);
        readData();
    }

    protected void readData(){
        // Read from the database
        myAuth = FirebaseAuth.getInstance();
        myData = FirebaseDatabase.getInstance();
        myRef = myData.getReference();

        FirebaseUser user = myAuth.getCurrentUser();
        userID = user.getUid();

        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d("userId",user.getUid());
                    Toast.makeText(ReadCrime.this, "signed with" + user.getEmail(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("signed out",user.getUid());
                    Toast.makeText(ReadCrime.this, "signed out" + user.getEmail(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            Crime crime = new Crime();
            crime.setTitle(ds.child(userID).getValue(Crime.class).getTitle());

            ArrayList<String> array = new ArrayList<>();
            array.add(crime.getTitle());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            myLitView.setAdapter(adapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        myAuth.addAuthStateListener(myAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Check if user is signed in (non-null) and update UI accordingly.
        myAuth.removeAuthStateListener(myAuthListener);
    }
}
