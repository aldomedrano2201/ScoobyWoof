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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import generics.GenericClass;
import model.Notification;
import model.Request;
import util.Util;

public class AcceptDeclineActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener{

    ImageView imgDogOwner, imgDog;
    Button btnAccept, btnDecline, btnGoBackDogWalkerProfile;
    TextView txtName, txtDogName, txtAddress, txtPhoneNumber,
            txtBreed, txtDogDescription, txtPickingTime;
    GenericClass genRequest;
    Request request;
    String dogWalkerName;
    int requestItemIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_decline);
        initialize();
    }

    private void initialize() {
        imgDogOwner = findViewById(R.id.imgPicDog);
        imgDog = findViewById(R.id.imgPicOwner);
        txtName = findViewById(R.id.textOwnerName);
        txtDogName = findViewById(R.id.textDogName);
        txtAddress = findViewById(R.id.textAddress);
        txtPhoneNumber = findViewById(R.id.textPhone);
        txtBreed = findViewById(R.id.textBreed);
        txtDogDescription = findViewById(R.id.textDescription);
        txtPickingTime = findViewById(R.id.textPickingTime);
        btnAccept = findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(this);
        btnDecline = findViewById(R.id.btnDecline);
        btnDecline.setOnClickListener(this);
        btnGoBackDogWalkerProfile = findViewById(R.id.btnBackToDashboard);
        btnGoBackDogWalkerProfile.setOnClickListener(this);
        dogWalkerName = getIntent().getStringExtra("dogWalkerName");
        requestItemIndex = getIntent().getIntExtra("requestItemIndex",-1);
        genRequest = (GenericClass) getIntent().getSerializableExtra("requestObj");
        request = (Request) genRequest.getValue();
        txtName.setText(genRequest.getName());
        txtDogName.setText(genRequest.getDogName());
        txtAddress.setText(genRequest.getAddress());
        txtBreed.setText(genRequest.getDogBreed());
        txtPickingTime.setText(request.getDateTime());
        txtPhoneNumber.setText(genRequest.getPhoneNumber());
        txtDogDescription.setText(genRequest.getDescription());
        loadDogOwnerImage();
    }



    private void loadDogOwnerImage() {


        if(Util.loadImageFromDB(Util.imageFolders.DogOwnersImages.toString(),request.getDogOwnerId(),imgDogOwner)){
            loadDogImage();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBackToDashboard:
                Intent intent = new Intent(AcceptDeclineActivity.this,ListServicesActivity.class);
                intent.putExtra("userIdValue", request.getDogWalkerId());
                startActivity(intent);
                break;

            case R.id.btnAccept:
                acknowledgeService(Util.requestStatus.Accepted.toString());
                break;

            case R.id.btnDecline:
                acknowledgeService(Util.requestStatus.Rejected.toString());
                break;

        }
    }

    private void acknowledgeService(String statusRequest) {
        if (requestItemIndex > - 1){
            Util.setNodeAndChildDatabaseReference(Util.nodeValues.Requests.toString(),request.getId())
                    .child("status").setValue(statusRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            request.setStatus(statusRequest);
                            requestList.remove(requestItemIndex);
                            notifyDogOwner(statusRequest);
                        }



                    });
        }else{
            Toast.makeText(getApplicationContext(),
                            "The request could not be accepted",
                            Toast.LENGTH_LONG)
                    .show();
        }


    }

    private void notifyDogOwner(String statusRequest) {
        try{
            Notification notification = new Notification(request.getDogWalkerId(),
                    dogWalkerName,
                    txtDogName.getText().toString(),
                    request.getDogOwnerId(),
                    Util.notificationStatus.Unread.toString(),
                    statusRequest);

            Util.setNodeAndChildDatabaseReference(Util.nodeValues.Notifications.toString(),UUID.randomUUID().toString())
                    .setValue(notification)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getApplicationContext(),
                                            "Thank you, we will notify the owner that you " + statusRequest.toLowerCase()+
                                                    " the service",
                                            Toast.LENGTH_LONG)
                                    .show();

                            if (requestList.size() == 0){
                                Intent intent = new Intent(AcceptDeclineActivity.this,WalkerDashboardActivity.class);
                                intent.putExtra("userId", request.getDogWalkerId());
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(AcceptDeclineActivity.this,ListServicesActivity.class);
                                intent.putExtra("dogWalkerId", request.getDogWalkerId());
                                intent.putExtra("dogWalkerName",dogWalkerName);
                                startActivity(intent);
                            }



                        }
                    });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG)
                    .show();
        }


    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }


    private void loadDogImage() {

        Util.setNodeDatabaseReference(Util.nodeValues.Dog.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!Util.loadImageFromDB(Util.imageFolders.DogImages.toString(),request.getDogId(),imgDog)){
                    Toast.makeText(getApplicationContext(), "Loading photograph", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



}