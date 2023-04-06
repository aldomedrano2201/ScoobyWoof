package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import util.Util;

public class SelectWalkerActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgWalker;
    Button btnCreateDogWalkingRequest, btnSelectDatetime;
    TextView txtName, txtEmail, txtDescription, txtRate, txtPhoneNumber, textDateTime;

    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    boolean isCalendarObject = true;

    String dogWalkerId, dateTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_walker);
        initialize();
    }

    private void initialize() {

        imgWalker = findViewById(R.id.imgPic);
        btnCreateDogWalkingRequest = findViewById(R.id.btnRequestWalkingService);
        btnCreateDogWalkingRequest.setOnClickListener(this);
        btnSelectDatetime = findViewById(R.id.btnDateAndTime);
        btnSelectDatetime.setOnClickListener(this);
        txtName = findViewById(R.id.textHelloName);
        txtEmail = findViewById(R.id.textHelloEmail);
        txtRate = findViewById(R.id.textRate);
        txtDescription = findViewById(R.id.textDescription);
        txtPhoneNumber = findViewById(R.id.textPhone);
        textDateTime = findViewById(R.id.textDateTime);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        txtName.setText(getIntent().getStringExtra("name"));
        txtEmail.setText(getIntent().getStringExtra("email"));
        txtDescription.setText(getIntent().getStringExtra("description"));
        txtRate.setText(getIntent().getStringExtra("rate"));
        txtPhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
        dogWalkerId = getIntent().getStringExtra("userId");

        if(getIntent().getStringExtra("dateTime") != null){
            dateTimeString = getIntent().getStringExtra("dateTime");
            textDateTime.setText(dateTimeString);
            isCalendarObject = getIntent().getBooleanExtra("MY_BOOLEAN_KEY", false);
        }


        loadDogWalkerImage();

    }

    private void loadDogWalkerImage() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child(Util.imageFolders.DogWalkersImages  + "/" +
                dogWalkerId);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgWalker.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SelectWalkerActivity.this, "Photograph not loaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDateAndTime:
                Intent i = new Intent(SelectWalkerActivity.this, DateTimeActivity.class);
                i.putExtra("isCalendarVisible", isCalendarObject);
                startActivity(i);
                break;

        }
    }
}