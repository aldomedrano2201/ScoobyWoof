package com.example.myapplication;

import static model.GeneralListViews.lvGenericlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import generics.GenericAdapter;
import generics.GenericClass;
import model.Request;
import util.Util;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    GenericAdapter adapter;

    Button btnBackDogOwnerDashboard;
    String userIdValue, dogId, dogOwnerId;
    Request request;
    ArrayList<GenericClass> requestsList= new ArrayList<GenericClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initialize();
    }

    private void initialize() {


        btnBackDogOwnerDashboard = findViewById(R.id.btnBackToDashboard);
        btnBackDogOwnerDashboard.setOnClickListener(this);
        lvGenericlist = findViewById(R.id.notifications_list_view);
        loadDBData();


    }

    private void loadDBData() {
        requestsList.clear();
        DatabaseReference dbRef = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(Util.nodeValues.Requests));

        dbRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}