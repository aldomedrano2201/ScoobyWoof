package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.UUID;

import model.DogOwner;
import util.Util;

public class OwnerProfileActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference databaseReference;
    Button btnSaveProfile, btnBackDashboardOwner;
    EditText edAddress, edPhoneNumber, edDescription;
    TextView txtName, txtEmail;
    ImageView imgDogOwner;
    int SELECT_PICTURE = 200;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filePath;
    String userIdValue, name, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);
        initialize();
    }

    private void initialize() {

        btnSaveProfile = findViewById(R.id.btnSaveChangesOwnerProfile);
        btnSaveProfile.setOnClickListener(this);
        btnBackDashboardOwner = findViewById(R.id.btnBackToDashboard);
        btnBackDashboardOwner.setOnClickListener(this);
        edAddress = findViewById(R.id.edAddress);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edDescription = findViewById(R.id.edOwnerBio);
        txtEmail = findViewById(R.id.textHelloEmail);
        txtName = findViewById(R.id.textHelloName);
        imgDogOwner = findViewById(R.id.imgPic);
        imgDogOwner.setOnClickListener(this);
        userIdValue = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");



        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        txtName.setText(name);
        txtEmail.setText(email);

        checkProfile();
    }

    private void checkProfile() {

              Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Users.toString(),
                      userIdValue,
                      Util.nodeValues.DogOwner.toString())
                      .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount()!=0){

                        edPhoneNumber.setText(snapshot.child("phoneNumber").getValue().toString());
                        edAddress.setText(snapshot.child("address").getValue().toString());
                        edDescription.setText(snapshot.child("description").getValue().toString());


                        final long ONE_MEGABYTE = 1024 * 1024;
                        Util.setStorageReference(Util.imageFolders.DogOwnersImages.toString(),userIdValue)
                                .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imgDogOwner.setImageBitmap(bmp);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(OwnerProfileActivity.this, "Photograph not loaded", Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





    }

    private void saveDogOwnerProfile() {

        String address = edAddress.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();
        String description = edDescription.getText().toString().trim();
        //validate inputs
        if (address.isEmpty()) {
            edAddress.setError("Enter your address");
            edAddress.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            edPhoneNumber.setError("Enter your last name");
            edPhoneNumber.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            edDescription.setError("Enter your email");
            edDescription.requestFocus();
            return;
        }


        saveProfile();



    }

    private void saveProfile() {
        DogOwner dogOwner = new DogOwner(edAddress.getText().toString().trim(),
                edPhoneNumber.getText().toString().trim(),edDescription.getText().toString());

                Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Users.toString(),
                                userIdValue,
                                Util.nodeValues.DogOwner.toString())
                        .setValue(dogOwner)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            uploadPhoto();


                        } else {
                            Toast.makeText(OwnerProfileActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


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
                    imgDogOwner.setImageURI(filePath);
                }
            }
        }
    }

    private void uploadPhoto() {
        if(filePath != null)
        {
            try{
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                Util.setStorageReference(Util.imageFolders.DogOwnersImages.toString(),userIdValue)
                        .putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(OwnerProfileActivity.this, "Registration completed", Toast.LENGTH_SHORT).show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(OwnerProfileActivity.this, "Failed to load photograph"+e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(OwnerProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(OwnerProfileActivity.this, "Registration completed", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveChangesOwnerProfile:
                saveDogOwnerProfile();
                break;
            case R.id.imgPic:
                selectPhoto();
                break;
            case R.id.btnBackToDashboard:
                Intent i = new Intent(OwnerProfileActivity.this, OwnerDashboardActivity.class);
                i.putExtra("userId",userIdValue);
                startActivity(i);
                break;
        }
    }

}