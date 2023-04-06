package com.example.myapplication;

import static model.GeneralListViews.lvGenericlist;


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

import java.util.ArrayList;

import generics.GenericAdapter;
import generics.GenericClass;
import model.DogWalker;
import util.Util;

public class SearchWalkersActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {


    GenericAdapter adapter;
    ArrayList<DogWalker> walkersList= new ArrayList<DogWalker>();
    Button btnBackDogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_walkers);
        initialize();
    }

    private void initialize() {


        btnBackDogList = findViewById(R.id.btnBackToDashboard);
        btnBackDogList.setOnClickListener(this);
        lvGenericlist = findViewById(R.id.dogWalkers_list_view);


        loadDBData();


    }

    private void loadDBData() {


            walkersList.clear();
            DatabaseReference dbRef = FirebaseDatabase
                    .getInstance()
                    .getReference(String.valueOf(Util.nodeValues.Users));

            dbRef.addListenerForSingleValueEvent(this);


    }


    private void setupDogWalkerListListener() {


            GenericClass<ArrayList<?>> genericObj = new GenericClass<ArrayList<?>>(walkersList);
            adapter= new GenericAdapter(this, genericObj,
                    R.layout.activity_search_walkers_element, Util.imageFolders.DogWalkersImages.toString(), "rate");
            lvGenericlist.setAdapter(adapter);
            lvGenericlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        DogWalker dogWalker = walkersList.get(position);
                        Intent intent = new Intent(SearchWalkersActivity.this,SelectWalkerForServiceActivity.class);
                        intent.putExtra("description", dogWalker.getDescription());
                        intent.putExtra("phoneNumber", dogWalker.getPhoneNumber());
                        intent.putExtra("dogWalkerId", dogWalker.getId());
                        intent.putExtra("dogOwnerId", getIntent().getStringExtra("userId"));
                        intent.putExtra("dogId", getIntent().getStringExtra("dogId"));
                        intent.putExtra("name", dogWalker.getFirstName() + " " + dogWalker.getLastName());
                        intent.putExtra("email", dogWalker.getEmail());
                        intent.putExtra("rate", String.valueOf(dogWalker.getRate()));
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
        if(snapshot.exists()){
            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                String walkerId = snapshot1.getKey();
                try{
                    if (snapshot1.child(Util.nodeValues.DogWalker.toString()).exists())
                        if ((Boolean) snapshot.child(walkerId).child(Util.nodeValues.DogWalker.toString()).child("active").getValue() == true) {

                            walkersList.add(new DogWalker(walkerId, 0.0, 0.0,
                                    (Boolean) snapshot.child(walkerId).child(Util.nodeValues.DogWalker.toString()).child("active").getValue(),
                                    Integer.parseInt(snapshot.child(walkerId).child(Util.nodeValues.DogWalker.toString()).child("ranking").getValue().toString()),
                                    snapshot.child(walkerId).child(Util.nodeValues.DogWalker.toString()).child("description").getValue().toString(),
                                    snapshot.child(walkerId).child(Util.nodeValues.DogWalker.toString()).child("phoneNumber").getValue().toString(),
                                    snapshot.child(walkerId).child("email").getValue().toString(),
                                    Double.parseDouble(snapshot.child(walkerId).child(Util.nodeValues.DogWalker.toString()).child("rate").getValue().toString()),
                                    snapshot.child(walkerId).child("firstName").getValue().toString(),
                                    snapshot.child(walkerId).child("lastName").getValue().toString()));
                            setupDogWalkerListListener();
                        }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_LONG)
                            .show();
                }

            }
            if(walkersList == null){
                Toast.makeText(getApplicationContext(),
                                "There are not dog walkers available",
                                Toast.LENGTH_LONG)
                        .show();
            }


        }


    }



    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackToDashboard:
                finish();
                break;

        }
    }
}