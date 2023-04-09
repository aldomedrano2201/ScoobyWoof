package com.example.myapplication;

import static model.GeneralListViews.lvGenericlist;
import static model.GeneralListViews.notificationsList;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import generics.GenericAdapter;
import generics.GenericClass;
import model.Notification;
import util.Util;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    GenericAdapter adapter;

    Button btnBackDogOwnerDashboard;
    String userIdValue;



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
        userIdValue = getIntent().getStringExtra("userId");
        loadUserNotifications();


    }

    private void loadUserNotifications() {
        notificationsList.clear();
        Util.setNodeDatabaseReference(Util.nodeValues.Notifications.toString())
                .addListenerForSingleValueEvent(this);
    }

    private void setupRequestListListener() {


        GenericClass<ArrayList<?>> genericObj = new GenericClass<ArrayList<?>>(notificationsList);
        adapter= new GenericAdapter(this, genericObj,
                R.layout.activity_notifications_element, Util.imageFolders.DogWalkersImages.toString(),"dogName", "description");
        lvGenericlist.setAdapter(adapter);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBackToDashboard:
                Intent intent
                        = new Intent(getApplicationContext(),OwnerDashboardActivity.class);
                intent.putExtra("userId",userIdValue);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot snapshot1 : snapshot.getChildren()){
            if(snapshot1.child("dogOwnerId").getValue().equals(userIdValue)){
                Notification notification = new Notification(snapshot1.child("dogWalkerName").getValue().toString(),
                        snapshot1.child("dogName").getValue().toString(),
                        snapshot1.child("requestStatus").getValue().toString());
                notification.setDescription(notification.toString());
                notificationsList.add(notification);
            }
        }
        setupRequestListListener();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}