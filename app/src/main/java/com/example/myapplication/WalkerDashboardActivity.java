package com.example.myapplication;

import static model.GeneralListViews.requestList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.DogWalker;
import model.Request;
import util.Util;

public class WalkerDashboardActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener{

    String userIdValue;

    Button btnCompleteWalkerProfile, btnNotifications,
            btnAcceptDeclineService, btnLogOut;
    TextView txtName, txtEmail;
    ImageView imgIcon;
    Switch swActivation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_dashboard);
        initialize();
    }

    private void initialize() {
        btnCompleteWalkerProfile = findViewById(R.id.btnCompleteProfileWalker);
        btnNotifications = findViewById(R.id.btnWalkerNotifications);
        btnAcceptDeclineService = findViewById(R.id.btnAcceptDecline);
        btnLogOut = findViewById(R.id.btnLogOut);

        btnNotifications.setOnClickListener(this);
        btnCompleteWalkerProfile.setOnClickListener(this);
        btnAcceptDeclineService.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        imgIcon = findViewById(R.id.imgPic);
        txtEmail = findViewById(R.id.textHelloEmail);
        txtName = findViewById(R.id.textHelloName);

        swActivation = findViewById(R.id.swWalkerActivation);
        swActivation.setOnClickListener(this);

        userIdValue = getIntent().getStringExtra("userId");

        loadUserInfo();



    }

    private void loadUserInfo() {

        Util.setNodeAndChildDatabaseReference(Util.nodeValues.Users.toString(),userIdValue)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
                        ,Util.nodeValues.DogWalker.toString())
                .addListenerForSingleValueEvent(this);

    }

    private void checkDogWalkingRequests() {
                Util.setNodeDatabaseReference(Util.nodeValues.Requests.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() != 0){
                            requestList.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                try{
                                    if (snapshot1.child("dogWalkerId").getValue().toString().equals(userIdValue) &&
                                            snapshot1.child("status").getValue().toString().equals(Util.requestStatus.Requested.toString())) {

                                        requestList.add(new Request(snapshot1.child("id").getValue().toString(),
                                                snapshot1.child("dogId").getValue().toString(),
                                                snapshot1.child("dogOwnerId").getValue().toString(),
                                                snapshot1.child("dogWalkerId").getValue().toString(),
                                                snapshot1.child("status").getValue().toString(),
                                                snapshot1.child("dateTime").getValue().toString()));
                                    }
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),
                                                    e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }

                            }
                            if (requestList.size() > 0)
                                Toast.makeText(getApplicationContext(),
                                                "You have new dog walking service(s) to be checked",
                                                Toast.LENGTH_LONG)
                                        .show();
                            else{
                                btnAcceptDeclineService.setEnabled(false);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void upDateActivation() {
                Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Users.toString(),
                                userIdValue,
                                Util.nodeValues.DogWalker.toString())
                        .child("active").setValue(swActivation.isChecked())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(WalkerDashboardActivity.this, "Activation updated", Toast.LENGTH_SHORT).show();



                        } else {
                            Toast.makeText(WalkerDashboardActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void userLogOut() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        Intent intent
                = new Intent(WalkerDashboardActivity.this,
                LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {


        if(!snapshot.exists()){


            Toast.makeText(getApplicationContext(),
                            "Please complete the profile",
                            Toast.LENGTH_LONG)
                    .show();
            btnNotifications.setEnabled(false);
            btnAcceptDeclineService.setEnabled(false);
            swActivation.setEnabled(false);




        }else{
            swActivation.setChecked((boolean)snapshot.child("active").getValue());
            final long ONE_MEGABYTE = 1024 * 1024;
            Util.setStorageReference(Util.imageFolders.DogWalkersImages.toString(),userIdValue)
                    .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imgIcon.setImageBitmap(bmp);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(WalkerDashboardActivity.this, "Photograph not loaded!", Toast.LENGTH_SHORT).show();
                }
            });
            checkDogWalkingRequests();

        }




    }



    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCompleteProfileWalker:
                Intent intent
                        = new Intent(WalkerDashboardActivity.this,WalkerProfileActivity.class);
                intent.putExtra("userId",userIdValue);
                intent.putExtra("email", txtEmail.getText().toString());
                intent.putExtra("name", txtName.getText().toString());
                startActivity(intent);
                break;
            case R.id.btnAcceptDecline:
                intent
                        = new Intent(WalkerDashboardActivity.this,
                        ListServicesActivity.class);
                intent.putExtra("dogWalkerId",userIdValue);
                intent.putExtra("dogWalkerName",txtName.getText());
                intent.putExtra("requestList", requestList);
                startActivity(intent);
                break;
            case R.id.btnLogOut:
                userLogOut();
                break;
            case R.id.swWalkerActivation:
                upDateActivation();
                break;



        }
    }


}