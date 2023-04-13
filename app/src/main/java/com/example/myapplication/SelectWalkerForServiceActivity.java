package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import model.Request;
import util.Util;

public class SelectWalkerForServiceActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, OnCompleteListener<Void> {

    private static final int MY_REQUEST_CODE = 0;
    ImageView imgWalker;
    Button btnCreateDogWalkingRequest, btnSelectDatetime, btnBackDogList;
    TextView txtName, txtEmail, txtDescription, txtRate, txtPhoneNumber, textDateTime;
    boolean isCalendarObject = true;

    String dogWalkerId, dateTimeString;

    Request request = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_walker_for_service);
        initialize();
    }

    private void initialize() {

        imgWalker = findViewById(R.id.imgPic);
        btnCreateDogWalkingRequest = findViewById(R.id.btnSendRequest);
        btnCreateDogWalkingRequest.setOnClickListener(this);
        btnSelectDatetime = findViewById(R.id.btnDateAndTime);
        btnSelectDatetime.setOnClickListener(this);
        btnBackDogList = findViewById(R.id.btnBack);
        btnBackDogList.setOnClickListener(this);
        txtName = findViewById(R.id.textHelloName);
        txtEmail = findViewById(R.id.textHelloEmail);
        txtRate = findViewById(R.id.textRate);
        txtDescription = findViewById(R.id.textDescription);
        txtPhoneNumber = findViewById(R.id.textPhone);
        textDateTime = findViewById(R.id.textDateTime);
        txtName.setText(getIntent().getStringExtra("name"));
        txtEmail.setText(getIntent().getStringExtra("email"));
        txtDescription.setText(getIntent().getStringExtra("description"));
        txtRate.setText(getIntent().getStringExtra("rate"));
        txtPhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
        dogWalkerId = getIntent().getStringExtra("dogWalkerId");




        loadDogWalkerImage();

    }

    private void loadDogWalkerImage() {

        final long ONE_MEGABYTE = 1024 * 1024;
        Util.setStorageReference(Util.imageFolders.DogWalkersImages.toString(),dogWalkerId)
                .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgWalker.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SelectWalkerForServiceActivity.this, "Photograph not loaded", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void saveWalkingServiceRequest() {

        validateRequest();


    }

    private Request createRequestObject() {
        return request = new Request(UUID.randomUUID().toString(),getIntent().getStringExtra("dogId"),
                getIntent().getStringExtra("dogOwnerId"),getIntent().getStringExtra("dogWalkerId"),
                Util.requestStatus.Requested.toString(), textDateTime.getText().toString());
    }

    private void checkRequestCreated() {
        FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(Util.nodeValues.Requests))
                .addListenerForSingleValueEvent(this);
    }

    private void validateRequest() {
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}$";
        if (textDateTime.getText().toString().isEmpty()){
            textDateTime.setError("Please set the time and date of your request");
            textDateTime.requestFocus();
            textDateTime.setText("");
            isCalendarObject = true;
            dateTimeString = "";
        }else{
            if(textDateTime.getText().toString().matches(regex)){

                checkRequestCreated();


            }else{
                textDateTime.setError("Date and time format not properly set");
                textDateTime.requestFocus();
                textDateTime.setText("");
                isCalendarObject = true;
                dateTimeString = "";
            }

        }


    }

    private void createRequest(){

        Util.setNodeAndChildDatabaseReference(Util.nodeValues.Requests.toString(),
                        request.getId())
                .setValue(request)
                .addOnCompleteListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDateAndTime:
                Intent i = new Intent(SelectWalkerForServiceActivity.this, DateTimeActivity.class);
                i.putExtra("isCalendarVisible", isCalendarObject);
                startActivityForResult(i,MY_REQUEST_CODE);
                break;
            case R.id.btnSendRequest:
                saveWalkingServiceRequest();
                break;
            case R.id.btnBack:
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            dateTimeString = data.getStringExtra("dateTime");
            isCalendarObject = data.getBooleanExtra("isCalendarVisible",true);
            if (textDateTime.getText().toString().isEmpty())
                textDateTime.setText(dateTimeString);
            else
                textDateTime.setText(textDateTime.getText().toString() + " " + dateTimeString);

        }
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        boolean requestExists = false;
        if(snapshot.getChildrenCount() != 0){
            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                try
                {
                    String requestObjFromDBString = snapshot1.child("dateTime").getValue().toString() +
                            snapshot1.child("dogId").getValue().toString() + snapshot1.child("dogOwnerId").getValue().toString() +
                            snapshot1.child("dogWalkerId").getValue().toString();

                    if (compareRequests(requestObjFromDBString)){
                        requestExists = true;
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_LONG)
                            .show();
                }

            }

        }
        else{
            request = createRequestObject();
            createRequest();
        }

        if(requestExists == true){
            textDateTime.setText("");
            Toast.makeText(getApplicationContext(),
                            "There is a request already created for the same walker and dog at the same time",
                            Toast.LENGTH_LONG)
                    .show();
        }else{
            createRequest();
        }

    }

    //Compare the string content of two request objects to validate if the request was alerady created
    private boolean compareRequests(String requestObjFromDBString) {
        if (request == null)
            request = createRequestObject();
        String createdRequestString = request.getDateTime() + request.getDogId() +
                request.getDogOwnerId() + request.getDogWalkerId();

        if (requestObjFromDBString.equals(createdRequestString))
            return true;
        return false;
    }


    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Toast.makeText(getApplicationContext(),
                            "Request sent successfully, it will be notified to your dog walker",
                            Toast.LENGTH_LONG)
                    .show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(),
                            "Request not created",
                            Toast.LENGTH_LONG)
                    .show();
        }
    }
}