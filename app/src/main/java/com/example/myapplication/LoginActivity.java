package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import util.Util;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    private EditText edPassword, edEmail;
    private Button btnLogIn, btnRegisterNow;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize() {
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnLogIn = findViewById(R.id.btnLogin);
        btnRegisterNow = findViewById(R.id.btnRegisterNow);
        btnRegisterNow.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }


    private void logIn() {

        String emailValue = edEmail.getText().toString().trim();
        String passwordValue = edPassword.getText().toString().trim();


        if (emailValue.isEmpty()) {
            edEmail.setError("Enter your email");
            edEmail.requestFocus();
            return;
        }

        if (passwordValue.isEmpty()) {
            edPassword.setError("Enter your password");
            edPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(edEmail.getText().toString().trim(), edPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email or password incorrect", Toast.LENGTH_SHORT).show();

                        } else {
                            checkIfEmailVerified();
                        }
                        // ...
                    }
                });

    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.

            Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
            goToActivityProfile();

        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();

            //restart this activity
        }


    }

    private void goToActivityProfile() {



        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(Util.nodeValues.Users));
        DatabaseReference userChild = databaseReference.child(mAuth.getCurrentUser().getUid());
        userChild.addValueEventListener(this);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
            if (snapshot.child("userType").getValue().toString().equals("DogWalker")){
                Intent intent
                        = new Intent(LoginActivity.this,
                        WalkerDashboardActivity.class);
                intent.putExtra("userId",mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }else{
                Intent intent
                        = new Intent(LoginActivity.this,
                        OwnerDashboardActivity.class);
                intent.putExtra("userId",mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                logIn();
                break;
            case R.id.btnRegisterNow:
                showRegisterPage();
                break;
        }
    }


    private void showRegisterPage(){
        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }



}