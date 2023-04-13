package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FaqActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnQuestion1, btnQuestion2, btnQuestion3, btnQuestion4 , btnQuestion5,btnQuestion6,btnQuestion7,btnBackToDashboard;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        btnQuestion1 = findViewById(R.id.btnQuestion1);
        btnQuestion2 = findViewById(R.id.btnQuestion2);
        btnQuestion3 = findViewById(R.id.btnQuestion3);
        btnQuestion4 = findViewById(R.id.btnQuestion4);
        btnQuestion5 = findViewById(R.id.btnQuestion5);
        btnQuestion6 = findViewById(R.id.btnQuestion6);
        btnQuestion7 = findViewById(R.id.btnQuestion7);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);

        btnQuestion1.setOnClickListener(this);
        btnQuestion2.setOnClickListener(this);
        btnQuestion3.setOnClickListener(this);
        btnQuestion4.setOnClickListener(this);
        btnQuestion5.setOnClickListener(this);
        btnQuestion6.setOnClickListener(this);
        btnQuestion7.setOnClickListener(this);
        btnBackToDashboard.setOnClickListener(this);



        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("faq");


    }

    private void loadQuestionDetails(String faq) {
        databaseReference.child(faq).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String question = snapshot.child("question").getValue().toString();
                String answer = snapshot.child("answer").getValue().toString();

                Intent intent = new Intent(FaqActivity.this, Faq1Activity.class);
                intent.putExtra("question", question);
                intent.putExtra("answer", answer);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnQuestion1:
                loadQuestionDetails("faq1");

                break;
            case R.id.btnQuestion2:
                loadQuestionDetails("faq2");
                break;
            case R.id.btnQuestion3:
                loadQuestionDetails("faq3");
                break;
            case R.id.btnQuestion4:
                loadQuestionDetails("faq4");
                break;
            case R.id.btnQuestion5:
                loadQuestionDetails("faq5");
                break;
            case R.id.btnQuestion6:
                loadQuestionDetails("faq6");
                break;
            case R.id.btnQuestion7:
                loadQuestionDetails("faq7");
                break;
            case R.id.btnBackToDashboard:
                finish();
                break;
        }
    }
}