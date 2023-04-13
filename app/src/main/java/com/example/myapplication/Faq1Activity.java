package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Faq1Activity extends AppCompatActivity implements View.OnClickListener{

    private TextView textQuestion, textAnswer;
    private ImageView imgFaq1;
    private DatabaseReference databaseReference;
    Button btnBackToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq1);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);
        textQuestion = findViewById(R.id.textQuestion);
        imgFaq1 = findViewById(R.id.imgFaq1);
        textAnswer = findViewById(R.id.textAnswer);

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        String answer = intent.getStringExtra("answer");
        textQuestion.setText(question);
        textAnswer.setText(answer);

        btnBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        finish();
    }
}