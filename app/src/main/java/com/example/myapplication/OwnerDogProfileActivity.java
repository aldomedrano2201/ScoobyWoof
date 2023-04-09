package com.example.myapplication;

import static model.GeneralListViews.adapter;
import static model.GeneralListViews.dogsList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import generics.GenericAdapter;
import generics.GenericClass;
import model.Dog;
import model.GeneralListViews;
import util.Util;

public class OwnerDogProfileActivity extends AppCompatActivity implements View.OnClickListener {




    DatabaseReference databaseReference;
    EditText edDogName, edDogBreed, edDogBio;
    Button btnSaveDogProfile, btnBackToDogOwnerProfile;
    ImageView dogImage;
    String userIdValue,dogIdValue;
    Uri filePath;
    Dog dog;
    int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dog_profile);
        initialize();
    }

    private void initialize() {

        edDogName = findViewById(R.id.edDogName);
        edDogBreed = findViewById(R.id.edDogBreed);
        edDogBio = findViewById(R.id.edDogBio);
        dogImage = findViewById(R.id.imgPic);
        dogImage.setOnClickListener(this);
        btnSaveDogProfile = findViewById(R.id.btnSaveChangesDogProfile);
        btnSaveDogProfile.setOnClickListener(this);
        btnBackToDogOwnerProfile = findViewById(R.id.btnBackToDashboard);
        btnBackToDogOwnerProfile.setOnClickListener(this);
        userIdValue = getIntent().getStringExtra("userId");
        if(getIntent().getStringExtra("isDogDetailed").equals("true")){
            dogIdValue = getIntent().getStringExtra("id");
            loadDogProfile();
        }


    }

    private void loadDogProfile() {

        edDogBreed.setText(getIntent().getStringExtra("breed"));
        edDogBio.setText(getIntent().getStringExtra("description"));
        edDogName.setText(getIntent().getStringExtra("name"));
        try{
            dogImage.setImageBitmap(Util.getSavedImage(getIntent().getStringExtra("id")));
            Util.deleteFile(getIntent().getStringExtra("id"));
        }catch (Exception e){
            Toast.makeText(OwnerDogProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSaveChangesDogProfile:
                saveDogProfile();
                break;
            case R.id.imgPic:
                selectPhoto();
                break;
            case R.id.btnBackToDashboard:
                finish();
                break;
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                filePath = data.getData();
                if (null != filePath) {
                    // update the preview image in the layout
                    dogImage.setImageURI(filePath);
                }
            }
        }
    }

    private void saveDogProfile() {

        String breed = edDogBreed.getText().toString().trim();
        String name = edDogName.getText().toString().trim();
        String description = edDogBio.getText().toString().trim();
        //validate inputs
        if (breed.isEmpty()) {
            edDogBreed.setError("Enter your dog's breed");
            edDogBreed.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            edDogName.setError("Enter your dog's name");
            edDogName.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            edDogBio.setError("Enter dog's details");
            edDogBio.requestFocus();
            return;
        }


        saveProfile();

    }

    private void saveProfile() {

        if (dogIdValue == null)
            dogIdValue = UUID.randomUUID().toString();

        dog = new Dog(dogIdValue,edDogName.getText().toString().trim(),edDogBreed.getText().toString(),
                        edDogBio.getText().toString().trim());
        Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Dog.toString(),userIdValue,dogIdValue).setValue(dog)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            uploadPhoto();


                        } else {
                            Toast.makeText(OwnerDogProfileActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void uploadPhoto() {
        if(filePath != null)
        {
            try{
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                Util.setStorageReference(Util.imageFolders.DogImages.toString(),dogIdValue)
                        .putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(OwnerDogProfileActivity.this, "Registration completed", Toast.LENGTH_SHORT).show();
                                updateDogListView();

                            }


                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(OwnerDogProfileActivity.this, "Failed to load photograph"+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");

                            }
                        });
            }catch (Exception e){
                Toast.makeText(OwnerDogProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }else{
            updateDogListView();
            Toast.makeText(OwnerDogProfileActivity.this, "Registration completed", Toast.LENGTH_SHORT).show();

        }

    }

    private void updateDogListView() {

        Util.saveImage(BitmapFactory.decodeResource(getResources(), R.id.imgPicDog), dogIdValue);
        if(getIntent().getStringExtra("isDogDetailed").equals("true")) {

            dogsList.set(getIntent().getIntExtra("position", 0),dog);
        }else{
            dogsList.add(dog);

        }
        GenericClass<ArrayList<Dog>> genericObj = new GenericClass<ArrayList<Dog>>(dogsList);
        adapter = adapter= new GenericAdapter(this, genericObj,
                R.layout.activity_my_dog_list_element, Util.imageFolders.DogImages.toString(), "name","breed");
        GeneralListViews.lvGenericlist.setAdapter(adapter);


    }


}