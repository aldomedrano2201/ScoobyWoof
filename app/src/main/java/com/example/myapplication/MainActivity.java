package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin, btnWhyBecomeWoofWalker, btnWhyTrustScoobyWoof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        btnLogin = findViewById(R.id.btnLogin);
        btnWhyBecomeWoofWalker = findViewById(R.id.btnWhyBecomeWoofWalker);
        btnWhyTrustScoobyWoof = findViewById(R.id.btnWhyTrustScoobyWoof);
        btnWhyBecomeWoofWalker.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnWhyTrustScoobyWoof.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.btnLogin:
                showLoginPage();
                break;
            case R.id.btnWhyBecomeWoofWalker:
                showWhyBecomeWalker();
                break;
            case R.id.btnWhyTrustScoobyWoof:
                showWhyTrustScoobyWoof();
        }
    }

    private void showLoginPage() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
    private void  showWhyBecomeWalker(){
        Intent i = new Intent(MainActivity.this, MainExtraInfoWalkerActivity.class);
        startActivity(i);
    }
    private void showWhyTrustScoobyWoof()
    {
        Intent i = new Intent(MainActivity.this, MainExtraInfoOwnerActivity.class);
        startActivity(i);
    }


}
