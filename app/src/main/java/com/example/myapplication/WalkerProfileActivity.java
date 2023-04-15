package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import model.DogWalker;
import util.Util;

public class WalkerProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSaveProfile, btnBackDashboardWalker;
    EditText edRate, edPhoneNumber, edDescription;
    TextView txtName, txtEmail;
    ImageView imgDogWalker;
    int SELECT_PICTURE = 200;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filePath;
    String userIdValue, name, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_profile);
        initialize();
    }

    private void initialize() {

        btnSaveProfile = findViewById(R.id.btnSaveChangesWalkerProfile);
        btnSaveProfile.setOnClickListener(this);
        btnBackDashboardWalker = findViewById(R.id.btnBackToDashboard);
        btnBackDashboardWalker.setOnClickListener(this);
        edRate = findViewById(R.id.edRate);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edDescription = findViewById(R.id.edWalkerBio);
        txtEmail = findViewById(R.id.textHelloEmail);
        txtName = findViewById(R.id.textHelloName);
        imgDogWalker = findViewById(R.id.imgPic);
        imgDogWalker.setOnClickListener(this);
        userIdValue = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

        txtName.setText(name);
        txtEmail.setText(email);



        checkProfile();
    }

    private void checkProfile() {

        Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Users.toString(),userIdValue,Util.nodeValues.DogWalker.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    edPhoneNumber.setText(snapshot.child("phoneNumber").getValue().toString());
                    edRate.setText(snapshot.child("rate").getValue().toString());
                    edDescription.setText(snapshot.child("description").getValue().toString());

                    final long ONE_MEGABYTE = 1024 * 1024;
                    Util.setStorageReference(Util.imageFolders.DogWalkersImages.toString(),userIdValue)
                            .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imgDogWalker.setImageBitmap(bmp);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(WalkerProfileActivity.this, "Photograph not loaded", Toast.LENGTH_SHORT).show();
                        }
                    });





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void saveDogWalkerProfile() {

        String rate = edRate.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();
        String description = edDescription.getText().toString().trim();
        //validate inputs
        if (Util.isNullOrWhiteSpace(rate)) {
            edRate.setError("Enter your rate");
            edRate.requestFocus();
            return;
        }

        if (Util.isNullOrWhiteSpace(phoneNumber)) {
            edPhoneNumber.setError("Enter your last name");
            edPhoneNumber.requestFocus();
            return;
        }

        if (Util.isNullOrWhiteSpace(description)) {
            edDescription.setError("Enter your email");
            edDescription.requestFocus();
            return;
        }


        saveProfile();



    }

    private void saveProfile() {

        DogWalker dogWalker = new DogWalker( 0.0,0.0,false, 1,
                edPhoneNumber.getText().toString().trim(),edDescription.getText().toString(),
                Double.valueOf(edRate.getText().toString().trim()));
        Util.setNodeAndChildrenDatabaseReference(Util.nodeValues.Users.toString()
                        ,userIdValue
                        ,Util.nodeValues.DogWalker.toString()).setValue(dogWalker)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            uploadPhoto();


                        } else {
                            Toast.makeText(WalkerProfileActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void selectPhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_PICTURE);

    }

    // this function is triggered when user
    // selects the image from the imageChooser
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            imgDogWalker.setImageURI(filePath);
        }

    }

    private void uploadPhoto() {
        if(filePath != null)
        {
            try{
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                Util.setStorageReference(Util.imageFolders.DogWalkersImages.toString(),userIdValue)
                        .putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(WalkerProfileActivity.this, "Registration completed", Toast.LENGTH_SHORT).show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(WalkerProfileActivity.this, "Failed to load photograph"+e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(WalkerProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(WalkerProfileActivity.this, "Registration completed", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveChangesWalkerProfile:
                saveDogWalkerProfile();
                break;
            case R.id.imgPic:
                selectPhoto();
                break;
            case R.id.btnBackToDashboard:
                Intent i = new Intent(WalkerProfileActivity.this, WalkerDashboardActivity.class);
                i.putExtra("userId",userIdValue);
                startActivity(i);
                break;
        }
    }
}