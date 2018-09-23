package com.example.t410.firebaseapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.t410.firebaseapp.Classes.Crime;
import com.example.t410.firebaseapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    }

    public void launchRegisterUser(View v) {
        startActivity(new Intent(this, RegisterUserActivity.class));
    }

    public void loginUser(View v) {
        EditText email = findViewById(R.id.editTextMainLoginEmail);
        EditText password = findViewById(R.id.editTextMainLoginPassword);
        FirebaseAuth firebase = FirebaseAuth.getInstance();
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Hold on...");
        dialog.show();
        firebase.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        dialog.dismiss();
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(MainActivity.this, "Authentication failed!", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Authentication successful!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void firebaseTest() {
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
        FirebaseAuth firebase = FirebaseAuth.getInstance();


        firebase.createUserWithEmailAndPassword("k@e.com", "123456").
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // user registered successfully
                            // adding profile here
                            Toast.makeText(MainActivity.this, "User registered", Toast.LENGTH_LONG);
                        }
                    }
                });
    }
}
