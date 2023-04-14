package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import model.UserInfo;
import util.Util;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edFirstName, edLastName, edPassword, edEmail, edConfirmPassword;
    private RadioGroup rgSelectProfile;
    private RadioButton rbOwner, rbWalker;
    private Button btnConfirm;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        databaseReference =  FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(Util.nodeValues.Users));

        initialize();
    }

    private void initialize() {
        edFirstName = findViewById(R.id.edFirstName);
        edLastName = findViewById(R.id.edLastName);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        edEmail = findViewById(R.id.edEmail);
        rgSelectProfile = findViewById(R.id.rgSelectProfile);
        rbOwner = findViewById(R.id.rbOwner);
        rbWalker = findViewById(R.id.rbWalker);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        rbOwner.setOnClickListener(this);
        rbWalker.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnConfirm:
                registerNewUser(v);
                break;
        }
    }

    private void registerNewUser(View v) {

        String firstNameValue = edFirstName.getText().toString().trim();
        String lastNameValue = edLastName.getText().toString().trim();
        String emailValue = edEmail.getText().toString().trim();
        String passwordValue = edPassword.getText().toString().trim();
        String confirmPasswordVal = edConfirmPassword.getText().toString().trim();
        String userType;

        //validate inputs
        if (Util.isNullOrWhiteSpace(firstNameValue)) {
            edFirstName.setError("Enter your first name");
            edFirstName.requestFocus();
            return;
        }

        if (Util.isNullOrWhiteSpace(lastNameValue)) {
            edLastName.setError("Enter your last name");
            edLastName.requestFocus();
            return;
        }

        if (Util.isNullOrWhiteSpace(emailValue)) {
            edEmail.setError("Enter your email");
            edEmail.requestFocus();
            return;
        }

        if (Util.isNullOrWhiteSpace(passwordValue)) {
            edPassword.setError("Enter your password");
            edPassword.requestFocus();
            return;
        }
        if(passwordValue.equals(confirmPasswordVal)== false) {
            edConfirmPassword.setError("Passwords do not match!");
            edConfirmPassword.requestFocus();
            return;
        }

        if (rgSelectProfile.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select a user type", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (rbOwner.isChecked()) {
                userType = "DogOwner";
            } else {
                userType = "DogWalker";
            }
        }

        UserInfo userInfo = new UserInfo(firstNameValue, lastNameValue, emailValue, userType);
        createUser(userInfo);





    }

    private void createUser(UserInfo userInfo) {

        // create new user or register new user

            mAuth
                    .createUserWithEmailAndPassword(userInfo.getEmail(),
                            edPassword.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful()) {

                                addRegister(userInfo);

                            }
                            else {

                                // Registration failed
                                Toast.makeText(
                                                getApplicationContext(),
                                                "Registration failed!!"
                                                        + task.getException().getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();


                            }

                        }
                    });

    }

    private void addRegister(UserInfo userInfo) {
                Util.setNodeAndChildDatabaseReference(Util.nodeValues.Users.toString(),
                                mAuth.getCurrentUser().getUid())
                        .setValue(userInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "User registered successfully. A verification link will be sent to your email.", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                            clearInputs();
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }





    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();


                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

    private void clearInputs() {
        edFirstName.setText("");
        edLastName.setText("");
        edEmail.setText("");
        edPassword.setText("");
        edConfirmPassword.setText("");
        rgSelectProfile.clearCheck();
    }
}


