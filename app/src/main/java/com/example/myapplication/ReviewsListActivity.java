package com.example.myapplication;

import static model.GeneralListViews.lvGenericlist;
import static model.GeneralListViews.notificationsList;
import static model.GeneralListViews.reviewsList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import generics.GenericAdapter;
import generics.GenericClass;
import model.Notification;
import model.Review;
import util.Util;

public class ReviewsListActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    GenericAdapter adapter;

    Button btnBackDogWalkerDashboard;
    String dogWalkerId;
    TextView txtReviewTitle;

    float sumReview = 0;
    int reviewCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_list);
        initialize();
    }

    private void initialize() {
        txtReviewTitle = findViewById(R.id.textReviews);
        btnBackDogWalkerDashboard = findViewById(R.id.btnBackToDashboard);
        btnBackDogWalkerDashboard.setOnClickListener(this);
        dogWalkerId = getIntent().getStringExtra("dogWalkerId");
        lvGenericlist = findViewById(R.id.reviews_list_view);
        loadReviews();
    }

    private void loadReviews() {

        reviewsList.clear();
        Util.setNodeDatabaseReference(Util.nodeValues.Reviews.toString())
                .addListenerForSingleValueEvent(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackToDashboard:
                finish();
                break;

        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot snapshot1 : snapshot.getChildren()){

            try{
                if(snapshot1.child("dogWalkerId").getValue().equals(dogWalkerId)){
                    Review review = new Review(
                            snapshot1.child("message").getValue().toString(),
                            ((int)(Float.parseFloat(snapshot1.child("id").getValue().toString()))) + ".png"
                            );
                    reviewsList.add(review);
                    sumReview = sumReview + Float.parseFloat(snapshot1.child("id").getValue().toString());
                    reviewCounter++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        setupRequestListListener();
    }

    private void setupRequestListListener() {
        if (reviewCounter > 0){
            GenericClass<ArrayList<?>> genericObj = new GenericClass<ArrayList<?>>(reviewsList);
            adapter= new GenericAdapter(this, genericObj,
                    R.layout.activity_reviews_element, Util.imageFolders.StarsImages.toString(),"", "message");
            lvGenericlist.setAdapter(adapter);
            calculateReviewAverage();
        }else{
            Toast.makeText(getApplicationContext(),
                            "You have no reviews at the moment",
                            Toast.LENGTH_LONG)
                    .show();
        }


    }

    private void calculateReviewAverage() {

        txtReviewTitle.setText("Rating average: " + String.format(("%.1f"),(sumReview/reviewCounter)));
        sumReview = 0;
        reviewCounter = 0;
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}