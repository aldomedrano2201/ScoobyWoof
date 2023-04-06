package com.example.myapplication;

import static model.GeneralListViews.dogsList;
import static model.GeneralListViews.lvGenericlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import generics.GenericAdapter;
import generics.GenericClass;
import model.Dog;
import util.Util;

public class MyDogListActivity extends AppCompatActivity implements View.OnClickListener {



    String userIdValue;
    Button btnAddDog, btnBack;

    GenericAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dog_list);
        initialize();
    }

    private void initialize() {

        userIdValue = getIntent().getStringExtra("userId");

        lvGenericlist = findViewById(R.id.dogs_list_view);

        btnAddDog = findViewById(R.id.btnAddDog);
        btnAddDog.setOnClickListener(this);
        btnBack = findViewById(R.id.btnBackToDashboard);
        btnBack.setOnClickListener(this);
        loadDBData();


    }

    private void loadDBData() {

        if (lvGenericlist.getCount() == 0){
            dogsList.clear();
            DatabaseReference dbRef = FirebaseDatabase
                    .getInstance()
                    .getReference(String.valueOf(Util.nodeValues.Dog))
                    .child(userIdValue);
            ValueEventListener listener= new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            String dogId = snapshot1.getKey();
                            dogsList.add(new Dog(snapshot.child(dogId).child("id").getValue().toString(),
                                    snapshot.child(dogId).child("name").getValue().toString(),
                                    snapshot.child(dogId).child("breed").getValue().toString(),
                                    snapshot.child(dogId).child("description").getValue().toString()));
                        }

                    }

                    setupDogListListener();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }



            };
            dbRef.addListenerForSingleValueEvent(listener);

        }

    }


    private void setupDogListListener() {

        GenericClass<ArrayList<Dog>> genericObj = new GenericClass<ArrayList<Dog>>(dogsList);
        adapter= new GenericAdapter(this, genericObj,
                R.layout.activity_my_dog_list_element, Util.imageFolders.DogImages.toString(), "breed");
        lvGenericlist.setAdapter(adapter);
        lvGenericlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getIntent().getBooleanExtra("isWalkingService",true)){
                    Dog dogObj = dogsList.get(position);
                    Intent intent = new Intent(MyDogListActivity.this,SearchWalkersActivity.class);
                    intent.putExtra("dogId", dogObj.getId());
                    intent.putExtra("userId", userIdValue);
                    startActivity(intent);
                }else{
                    Dog dogObj = dogsList.get(position);
                    Intent intent = new Intent(MyDogListActivity.this,OwnerDogProfileActivity.class);
                    intent.putExtra("description", dogObj.getDescription());
                    intent.putExtra("name", dogObj.getName());
                    intent.putExtra("breed", dogObj.getBreed());
                    intent.putExtra("id", dogObj.getId());
                    intent.putExtra("userId", userIdValue);
                    intent.putExtra("isDogDetailed", "true");
                    intent.putExtra("position",position);
                    startActivity(intent);

                }



            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddDog:
                Intent intent = new Intent(MyDogListActivity.this,OwnerDogProfileActivity.class);
                intent.putExtra("userId",userIdValue);
                intent.putExtra("isDogDetailed","false");
                startActivity(intent);
                break;
            case R.id.btnBackToDashboard:
                finish();
                break;

        }
    }


}