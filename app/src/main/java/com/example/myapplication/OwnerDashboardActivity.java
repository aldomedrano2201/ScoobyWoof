package com.example.myapplication;

import static model.GeneralListViews.notificationsList;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

import model.Request;
import util.Util;

public class OwnerDashboardActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

    String userIdValue;
    Button btnYourDogProfile, btnNotifications,
            btnRequestWalkingService, btnCompleteOwnerProfile, btnLogOut, btnFAQandSupport, btnViewInsights;
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
        btnFAQandSupport = findViewById(R.id.btnFAQandSupport);
        btnViewInsights = findViewById(R.id.btnViewInsights);
        btnViewInsights.setOnClickListener(this);
        btnFAQandSupport.setOnClickListener(this);
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

        Util.setNodeAndChildDatabaseReference(Util.nodeValues.Users.toString(),userIdValue)
                .addValueEventListener(new ValueEventListener() {
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

        Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Users.toString()
                        ,userIdValue
                        ,Util.nodeValues.DogOwner.toString())
                    .addListenerForSingleValueEvent(this);

    }

    private void checkNotifications() {
        Util.setNodeDatabaseReference(Util.nodeValues.Notifications.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() != 0){
                            notificationsList.clear();
                            int notificationsNumber = 0;
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                try{
                                    if (snapshot1.child("dogOwnerId").getValue().toString()
                                            .equals(userIdValue) &&
                                            snapshot1.child("status").getValue().toString()
                                                    .equals(Util.notificationStatus.Unread.toString())) {

                                        notificationsNumber++;
                                    }
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),
                                                    e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }

                            }
                            if (notificationsNumber > 0)
                                Toast.makeText(getApplicationContext(),
                                                "You have "+  notificationsNumber + " new notifications to be checked",
                                                Toast.LENGTH_LONG)
                                        .show();
                            }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
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


            if(!Util.loadImageFromDB(Util.imageFolders.DogOwnersImages.toString(),userIdValue,imgIcon)){
                Toast.makeText(getApplicationContext(), "Loading photograph", Toast.LENGTH_SHORT).show();
            }

            checkNotifications();
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
            case R.id.btnFAQandSupport:
                intent = new Intent(OwnerDashboardActivity.this, FaqActivity.class);
                startActivity(intent);
                break;
            case R.id.btnViewInsights:
                intent = new Intent(OwnerDashboardActivity.this, InsightsActivity.class);
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