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

    /*
    @Author: Kenny Perroni
    This method launches the Activity to register users in the application
    The method is called when the Sign up button is clicked in the Login view
     */
    public void launchRegisterUser(View v) {
        startActivity(new Intent(this, RegisterUserActivity.class));
    }

    /*
    @Author: Kenny Perroni
    This method handles the authentication of the user via Firebase authentication service.
    The method is called when the Sign in button is clicked.
    It takes as input the email address and password entered by the user in the Login view
    The input is validated before it is processed. If it is valid, the firebase
    service is called to authenticate the user. If the credentials are correct, the user
    is taken to the application's home Activity, if it is not, the user will get a message that
    the authentication failed.
     */
    public void loginUser(View v) {
        // Instantiating and linking email and password values from the view
        EditText email = findViewById(R.id.editTextMainLoginEmail);
        EditText password = findViewById(R.id.editTextMainLoginPassword);
        // Calling function to validate email and password input
        if(isLoginValidated(email.getText().toString(), password.getText().toString())){
            // If the login input is correct, the firebase authentication service is instantiated
            FirebaseAuth firebase = FirebaseAuth.getInstance();
            // A dialog object is created to show the progress of the login process
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            // Setting a message to the progress dialog
            dialog.setMessage("Hold on...");
            // Showing the progress dialog
            dialog.show();
            // Calling firebase method to authenticate user using email address and password
            firebase.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        // This callback method is executed when the authentication process finishes
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // The dialog box is dismissed
                            dialog.dismiss();
                            // The task variable in the parameter is evaluated to check whether it was successful or not
                            if (!task.isSuccessful()) {
                                // If it was not successful, the credentials entered were not correct
                                // A message is shown to the user
                                Toast.makeText(MainActivity.this, "Authentication failed! \n Wrong username or password", Toast.LENGTH_LONG).show();
                            // If it was successful, the user is taken to the application's home Activity
                            } else {
                                // A message is shown to the user
                                Toast.makeText(MainActivity.this, "Authentication successful! \n Welcome to CrimeStalker!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }
                        }
                    });
        }
    }

    /*
    @Author: Kenny Perroni
    This method takes care of validating the email and password entered by the user
     */
    private boolean isLoginValidated(String email, String password){
        // Regex expression to validate email addresses
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // Checking if the email or password parameters are empty
        if(email.trim().equals("") || password.trim().equals("")){
            Toast.makeText(this, "Please enter both email and password to login", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Checking if the email entered matches the email regex expression
        if(!email.matches(emailPattern)){
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Returning true if none of the conditions above were met
        return true;
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
