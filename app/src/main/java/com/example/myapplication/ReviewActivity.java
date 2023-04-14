package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.UUID;

import model.Review;
import util.Util;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener, OnCompleteListener<Void> {

    RatingBar ratingBar;
    EditText edDescriptionReview;
    Button btnSaveReview, btnBackToNotifications;
    ImageView imgDogWalker;
    String reviewDetail,dogWalkerId;
    float userRating = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initialize();
    }

    private void initialize() {
        imgDogWalker = findViewById(R.id.dogWalkerPic);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
        edDescriptionReview = findViewById(R.id.edReview);
        btnSaveReview = findViewById(R.id.btnSaveReview);
        btnSaveReview.setOnClickListener(this);
        btnBackToNotifications = findViewById(R.id.btnBackToNotifications);
        btnBackToNotifications.setOnClickListener(this);
        dogWalkerId = getIntent().getStringExtra("dogWalkerId");
        loadDogWalkerImage();
    }

    private void loadDogWalkerImage() {

        if(!Util.loadImageFromDB(Util.imageFolders.DogWalkersImages.toString(),dogWalkerId,imgDogWalker)){
            Toast.makeText(getApplicationContext(), "Loading photograph", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBackToNotifications:
                finish();
                break;
            case R.id.btnSaveReview:
                validateReview();
                finish();
                break;

        }
    }

    private void validateReview() {
        reviewDetail = edDescriptionReview.getText().toString();

        if(Util.isNullOrWhiteSpace(reviewDetail)){
            edDescriptionReview.setError("Please type a review!");
            edDescriptionReview.requestFocus();
            return;
        }

        if(userRating == 0){
            Toast.makeText(getApplicationContext(), "Please rate the walker",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        saveReview();



    }

    private void saveReview() {

        Review review = new Review(reviewDetail,dogWalkerId, userRating);

        Util.setNodeAndChildDatabaseReference(Util.nodeValues.Reviews.toString(), UUID.randomUUID().toString())
                .setValue(review)
                .addOnCompleteListener(this);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        userRating = ratingBar.getRating();
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(getApplicationContext(), "Thank you, your reviews are very important for our walkers",
                Toast.LENGTH_SHORT).show();
    }


}