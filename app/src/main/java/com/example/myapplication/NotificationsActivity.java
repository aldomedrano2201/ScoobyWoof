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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import generics.GenericAdapter;
import generics.GenericClass;
import model.Notification;
import util.Util;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, OnCompleteListener<Void> {

    GenericAdapter adapter;

    Button btnBackDogOwnerDashboard;
    String userIdValue;
    ArrayList<String> listOfNotificationIds = new ArrayList<String>();


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
        lvGenericlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Notification notificationItem = notificationsList.get(position);
                    if(notificationItem.getRequestStatus().equals(Util.requestStatus.Accepted.toString())){
                        Intent intent
                                = new Intent(getApplicationContext(),ReviewActivity.class);
                        intent.putExtra("dogWalkerId",notificationItem.getDogWalkerId());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),
                                        "You can't review dog walkers who were not able to walk your dog",
                                        Toast.LENGTH_LONG)
                                .show();
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBackToDashboard:
                updateNotificationsToReadStatus();
                Intent intent
                        = new Intent(getApplicationContext(),OwnerDashboardActivity.class);
                intent.putExtra("userId",userIdValue);
                startActivity(intent);
                break;

        }
    }

    private void updateNotificationsToReadStatus() {

        for (String notificationIdItem : listOfNotificationIds){
            Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Notifications.toString()
                            ,notificationIdItem
                            ,"status")
                    .setValue(Util.notificationStatus.Read)
                    .addOnCompleteListener(this);
        }

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot snapshot1 : snapshot.getChildren()){

            try{
                listOfNotificationIds.add(snapshot1.getKey());
                if(snapshot1.child("dogOwnerId").getValue().equals(userIdValue)){
                    Notification notification = new Notification(
                            snapshot1.child("dogWalkerId").getValue().toString(),
                            snapshot1.child("dogWalkerName").getValue().toString(),
                            snapshot1.child("dogName").getValue().toString(),
                            snapshot1.child("requestStatus").getValue().toString());
                    notification.setDescription(notification.toString());
                    notificationsList.add(notification);
                }
            }catch (Exception e){
                  e.printStackTrace();
            }

        }
        setupRequestListListener();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {

    }
}