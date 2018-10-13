package com.comp313_002.crimestalker.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.comp313_002.crimestalker.Classes.Crime;
import com.comp313_002.crimestalker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends AppCompatActivity {
    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET};
    public static View.OnClickListener myOnClickListener;

    private TwitterLoginButton twitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpTwitterButton();
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(permissions, 1);
            }
        }
        myOnClickListener = new MyOnClickListener(this);
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
                    Log.d("My app", "Getting data: " + aCrime.getDescription());
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
                        if (task.isSuccessful()) {
                            // user registered successfully
                            // adding profile here
                            Toast.makeText(MainActivity.this, "User registered", Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

        }


    }

    //To Test ReportCrimeActivity
    public void testActivity_Clicked(View view) {
        Intent intent = new Intent(this, ReportCrimeActivity.class);
        startActivity(intent);


    }

    public void testReadCrime_Clicked(View view) {
        Intent intent = new Intent(this, ReadCrime.class);
        startActivity(intent);

    }

    private void setUpTwitterButton() {
        twitterButton = (TwitterLoginButton) findViewById(R.id.twitter_button);
        twitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.app_name),
                        Toast.LENGTH_SHORT).show();

                setUpViewsForTweetComposer();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.app_name),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setUpViewsForTweetComposer() {
        Intent readCrimeIntent = new Intent(MainActivity.this, ReadCrime.class);
        startActivity(readCrimeIntent);
    }
}
