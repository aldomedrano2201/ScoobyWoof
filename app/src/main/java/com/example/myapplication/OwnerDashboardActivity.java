package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import util.Util;

public class OwnerDashboardActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

    String userIdValue;
    DatabaseReference databaseReference;
    Button btnYourDogProfile, btnNotifications,
            btnRequestWalkingService, btnCompleteOwnerProfile, btnLogOut;
    TextView txtName, txtEmail;
    ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);
        initialize();
    }

    private void initialize() {
        btnCompleteOwnerProfile = findViewById(R.id.btnCompleteProfileOwner);
        btnNotifications = findViewById(R.id.btnOwnerNotifications);
        btnRequestWalkingService = findViewById(R.id.btnRequestWalkingService);
        btnYourDogProfile = findViewById(R.id.btnYourDogProfile);
        btnNotifications = findViewById(R.id.btnOwnerNotifications);
        btnNotifications.setOnClickListener(this);
        btnYourDogProfile.setOnClickListener(this);
        btnRequestWalkingService.setOnClickListener(this);
        btnCompleteOwnerProfile.setOnClickListener(this);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(this);
        imgIcon = findViewById(R.id.imgPic);
        txtEmail = findViewById(R.id.textHelloEmail);
        txtName = findViewById(R.id.textHelloName);

        userIdValue = getIntent().getStringExtra("userId");

        loadUserInfo();



    }

    private void loadUserInfo() {

        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(Util.nodeValues.Users));
        DatabaseReference userInfo = databaseReference.child(userIdValue);
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtEmail.setText(snapshot.child("email").getValue().toString());
                String firstLastName = snapshot.child("firstName").getValue().toString() + " " +
                        snapshot.child("lastName").getValue().toString();
                txtName.setText(firstLastName);
                checkProfileCompleted();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkProfileCompleted() {
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(Util.nodeValues.Users));


            DatabaseReference dogOwnerInfo = databaseReference.child(userIdValue).child(Util.nodeValues.DogOwner.toString());
            dogOwnerInfo.addListenerForSingleValueEvent(this);


    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (!snapshot.exists()){
            Toast.makeText(getApplicationContext(),
                            "Please complete the profile",
                            Toast.LENGTH_LONG)
                    .show();
            btnNotifications.setEnabled(false);
            btnRequestWalkingService.setEnabled(false);
        }else{
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference photoReference= storageReference.child(Util.imageFolders.DogOwnersImages  + "/" +
                    userIdValue);

            final long ONE_MEGABYTE = 1024 * 1024;
            photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imgIcon.setImageBitmap(bmp);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(OwnerDashboardActivity.this, "Photograph not loaded!", Toast.LENGTH_SHORT).show();
                }
            });

        }





    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCompleteProfileOwner:
                Intent intent
                        = new Intent(OwnerDashboardActivity.this,
                        OwnerProfileActivity.class);
                intent.putExtra("userId",userIdValue);
                intent.putExtra("email", txtEmail.getText().toString());
                intent.putExtra("name", txtName.getText().toString());
                startActivity(intent);
                break;
            case R.id.btnYourDogProfile:
                intent
                        = new Intent(OwnerDashboardActivity.this,
                        MyDogListActivity.class);
                intent.putExtra("userId",userIdValue);
                intent.putExtra("isWalkingService",false);
                startActivity(intent);
                break;
            case R.id.btnRequestWalkingService:
                intent
                        = new Intent(OwnerDashboardActivity.this,
                        MyDogListActivity.class);
                intent.putExtra("userId",userIdValue);
                intent.putExtra("isWalkingService",true);
                startActivity(intent);
                break;

            case R.id.btnOwnerNotifications:
                intent
                        = new Intent(OwnerDashboardActivity.this,
                        NotificationsActivity.class);
                intent.putExtra("userId",userIdValue);
                startActivity(intent);
                break;


            case R.id.btnLogOut:
                userLogOut();
                break;

        }
    }

    private void userLogOut() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        Intent intent
                = new Intent(OwnerDashboardActivity.this,
                LoginActivity.class);
        startActivity(intent);
    }
}