package com.example.t410.firebaseapp.Activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.t410.firebaseapp.Classes.User;
import com.example.t410.firebaseapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity {

    private FirebaseAuth firebase = FirebaseAuth.getInstance();
    private DatabaseReference users = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    public void registerUser(View v) {
        EditText firstName = findViewById(R.id.editTextRegisterUserFirstName);
        EditText lastName = findViewById(R.id.editTextRegisterUserLastName);
        EditText address = findViewById(R.id.editTextRegisterUserAddress);
        EditText phoneNumber = findViewById(R.id.editTextRegisterUserPhoneNumber);
        EditText email = findViewById(R.id.editTextRegisterUserEmail);
        EditText password = findViewById(R.id.editTextRegisterUserPassword);
        CheckBox allowPoliceCalls = findViewById(R.id.chkBoxRegisterUserAllowPoliceCalls);

        final ProgressDialog dialog = new ProgressDialog(RegisterUserActivity.this);
        dialog.setMessage("Hold on...");
        dialog.show();

        final User user = new User(firstName.getText().toString(), lastName.getText().toString(),
                                   address.getText().toString(), phoneNumber.getText().toString(),
                                   allowPoliceCalls.isChecked());

        final User firebaseUser = new User(email.getText().toString(), password.getText().toString());

            firebase.createUserWithEmailAndPassword(firebaseUser.getEmail(), firebaseUser.getPassword()).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user.setUserId(task.getResult().getUser().getUid());
                                users.child("users").push().setValue(user);
                                dialog.dismiss();
                                Toast.makeText(RegisterUserActivity.this, "User registered successfully!", Toast.LENGTH_LONG).show();
                                // TODO: Login user once registration finishes
                            }

                            else{
                                dialog.dismiss();
                                Toast.makeText(RegisterUserActivity.this, "Something went wrong when creating the user", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    // TODO: Validate email and password values after they are entered
    private boolean validateEmail() {
        return false;
    }

    private boolean validatePassword() {
        return false;
    }
}
