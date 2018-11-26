package com.comp313_002.crimestalker.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.comp313_002.crimestalker.Classes.User;
import com.comp313_002.crimestalker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity {

    // Instantiating firebase authentication service
    private FirebaseAuth firebase = FirebaseAuth.getInstance();
    // Instantiating reference to firebase's database. To-be used to store user's information
    private DatabaseReference users = FirebaseDatabase.getInstance().getReference();
    // Constant to check against phone number length
    private static final int PHONE_NUMBER_LENGTH = 10;
    // Constant to check against password length
    private static final int PASSWORD_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    /*
    @Author: Kenny Perroni
    This method takes care of registering the user in the application.
    It is called when the user clicks the submit button.
    The input is validated before it is processed. If it is valid, this function will
    take all the fields that the user entered in the view, so that a user profile
    is created in firebase's database.
     */
    public void registerUser(View v) {

        // Creating and linking edit text objects with the ones in the view for
        // user's first name, last name, address, phone number, email and password
        EditText firstName = findViewById(R.id.editTextRegisterUserFirstName);
        EditText lastName = findViewById(R.id.editTextRegisterUserLastName);
        EditText address = findViewById(R.id.editTextRegisterUserAddress);
        EditText phoneNumber = findViewById(R.id.editTextRegisterUserPhoneNumber);
        EditText email = findViewById(R.id.editTextRegisterUserEmail);
        EditText password = findViewById(R.id.editTextRegisterUserPassword);
        // Creating and linking checkbox object in the view for allowing police calls
        CheckBox allowPoliceCalls = findViewById(R.id.chkBoxRegisterUserAllowPoliceCalls);

        // Calling function to validate input entered
        if (isRegistrationValidated
                (firstName.getText().toString(), lastName.getText().toString(), address.getText().toString(),
                        phoneNumber.getText().toString(), email.getText().toString(), password.getText().toString())) {
            // A dialog object is created to show the progress of the create user process
            final ProgressDialog dialog = new ProgressDialog(RegisterUserActivity.this);
            // Setting a message to the progress dialog
            dialog.setMessage("Hold on...");
            // Showing the progress dialog
            dialog.show();

            // Instantiating a new User object with the input entered by the customer
            // This user will be stored in a user collection in firebase
            final User user = new User(firstName.getText().toString(), lastName.getText().toString(),
                    address.getText().toString(), phoneNumber.getText().toString(),
                    allowPoliceCalls.isChecked());

            // This user will be stored in the firebase's project authentication repository
            final User firebaseUser = new User(email.getText().toString(), password.getText().toString());

            // The firebase service to create user with email and password is created
            // Passing the parameters of the firebaseUser object
            firebase.createUserWithEmailAndPassword(firebaseUser.getEmail(), firebaseUser.getPassword()).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        // This callback method is executed when the authentication process finishes
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // The task variable in the parameter is evaluated to check whether it was successful or not
                            if (task.isSuccessful()) {
                                // If it was successful, the user was added successfully to the authentication repo in firebase
                                // Now, the Uid of the user just created is set to the user object that will be stored
                                // in the users collection in firebase
                                user.setUserId(task.getResult().getUser().getUid());
                                // Finally, using the users reference to firebase's database, the user object is pushed
                                // to the users collection
                                users.child("users").push().setValue(user);
                                // The dialog box is dismissed
                                dialog.dismiss();
                                // A message is shown to the user that the user was registered successfully
                                Toast.makeText(RegisterUserActivity.this, "User registered successfully!", Toast.LENGTH_LONG).show();
                                // Redirecting user to the Login Activity
                                startActivity(new Intent(RegisterUserActivity.this, MainActivity.class));
                            }
                            // If the task was not successful, a message is shown to the user that an error ocurred
                            else {
                                // The dialog box is dismissed
                                dialog.dismiss();
                                // Showing message
                                Toast.makeText(RegisterUserActivity.this, "Something went wrong when creating the user", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    /*
    @Author: Kenny Perroni
     This method is called when a user is being created.
     The method takes all the parameters in the view except for allow police calls since it is optional
     This method takes care of validating the input for a clean registration
     */
    private boolean isRegistrationValidated(String firstName, String lastName, String address, String phoneNumber, String email, String password) {
        // Regex expression to validate email addresses
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // Checking if any of the parameters entered are empty
        if (firstName.trim().equals("") || lastName.trim().equals("") || address.trim().equals("") ||
                phoneNumber.trim().equals("") || email.trim().equals("") || password.trim().equals("")) {
            // Showing a message to the user
            Toast.makeText(RegisterUserActivity.this, "Please enter all the mandatory fields", Toast.LENGTH_LONG).show();
            return false;
        }

        // Checking if the phone number is PHONE_NUMBER_LENGHT long
        if (phoneNumber.trim().length() < PHONE_NUMBER_LENGTH) {
            // Showing a message to the user
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Checking if the email entered matches the email regex expression
        if (!email.matches(emailPattern)) {
            // Showing a message to the user
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Checking if the password is PASSWORD_LENGTH long
        if (password.trim().length() < PASSWORD_LENGTH) {
            // Showing a message to the user
            Toast.makeText(this, "Please enter a password at least " + PASSWORD_LENGTH + " characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Returning true if none of the conditions above were met
        return true;
    }
}

