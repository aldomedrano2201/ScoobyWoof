package com.example.myapplication;

import static model.GeneralListViews.lvGenericlist;
import static model.GeneralListViews.requestList;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;


import generics.GenericAdapter;
import generics.GenericClass;
import model.Dog;
import model.DogOwner;
import model.Request;
import util.Util;

public class ListServicesActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {


    GenericAdapter adapter;

    Button btnBackDogWalkerProfile;
    String userIdValue, dogWalkerName;



    ArrayList<GenericClass> genRequestsList= new ArrayList<GenericClass>();
    
    DogOwner dogOwner;
    Dog dog;
    Request request;
    int itemRequestIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_services);
        initialize();
    }

    private void initialize() {


        btnBackDogWalkerProfile = findViewById(R.id.btnBackToDashboard);
        btnBackDogWalkerProfile.setOnClickListener(this);
        lvGenericlist = findViewById(R.id.requests_list_view);
        userIdValue = getIntent().getStringExtra("dogWalkerId");
        dogWalkerName = getIntent().getStringExtra("dogWalkerName");
        loadDBData();


    }




    private void loadDBData() {

        request = requestList.get(itemRequestIndex);
        getUserAmdDogInfoFromDB();

    }





    private void getUserAmdDogInfoFromDB() {
        Util.setNodeAndChildDatabaseReference(Util.nodeValues.Users.toString(),request.getDogOwnerId())
                .addListenerForSingleValueEvent(this);
    }



    private void setupRequestListListener() {


        GenericClass<ArrayList<?>> genericObj = new GenericClass<ArrayList<?>>(genRequestsList);
        adapter= new GenericAdapter(this, genericObj,
                R.layout.activity_list_services_element, Util.imageFolders.DogImages.toString(), "name","dogName");
        lvGenericlist.setAdapter(adapter);
        lvGenericlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    GenericClass requestItem = genRequestsList.get(position);
                    Intent intent = new Intent(ListServicesActivity.this,AcceptDeclineActivity.class);
                    intent.putExtra("requestObj", (Serializable) requestItem);
                    intent.putExtra("dogWalkerName", dogWalkerName);
                    intent.putExtra("requestItemIndex",position);
                    startActivity(intent);
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
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        dogOwner = new DogOwner(snapshot.child("firstName").getValue().toString(),
                snapshot.child("lastName").getValue().toString(),
                snapshot.child("email").getValue().toString(),
                snapshot.child(Util.nodeValues.DogOwner.toString()).child("phoneNumber").getValue().toString());
        Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Dog.toString(),request.getDogOwnerId(),request.getDogId())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dog = new Dog(request.getDogId(),snapshot.child("name").getValue().toString(),
                            snapshot.child("breed").getValue().toString(),
                            snapshot.child("description").getValue().toString());
                    genRequestsList.add(new GenericClass(dogOwner.toString(), dogOwner.getPhoneNumber(),
                            dog.getId(), dog.getName(), dog.getDescription(), request));
                    try{
                        request = requestList.get(++itemRequestIndex);
                        getUserAmdDogInfoFromDB();
                    }catch (Exception e){
                        setupRequestListListener();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBackToDashboard:
                Intent intent
                        = new Intent(getApplicationContext(),WalkerDashboardActivity.class);
                intent.putExtra("userId",userIdValue);
                startActivity(intent);
                break;

        }
    }
}