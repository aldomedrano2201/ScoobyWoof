package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InsightsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnInsights1, btnInsights2, btnInsights3, btnInsights4, btnBackToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);
        initialize();
    }

    private void initialize() {
        btnInsights1 = findViewById(R.id.btnInsights1);
        btnInsights2 = findViewById(R.id.btnInsights2);
        btnInsights3 = findViewById(R.id.btnInsights3);
        btnInsights4 = findViewById(R.id.btnInsights4);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);

        btnInsights1.setOnClickListener(this);
        btnInsights2.setOnClickListener(this);
        btnInsights3.setOnClickListener(this);
        btnInsights4.setOnClickListener(this);
        btnBackToDashboard.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (v.getId()) {
            case R.id.btnInsights1:
                String url = "https://www.canadianpetconnection.ca/top-twenty-two-dog-foods-canada-2023/";
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.btnInsights2:
                String url2 = "https://www.petmd.com/dog/care/responsible-pet-owners-checklist-taking-care-pet";
                intent.setData(Uri.parse(url2));
                startActivity(intent);
                break;
            case R.id.btnInsights3:
                String url3 = "https://www.akc.org/expert-advice/health/how-often-should-you-walk-your-dog/";
                intent.setData(Uri.parse(url3));
                startActivity(intent);
                break;
            case R.id.btnInsights4:
                String url4 = "https://blog.homesalive.ca/dog-blog/indoor-games-for-dogs";
                intent.setData(Uri.parse(url4));
                startActivity(intent);
                break;
            case R.id.btnBackToDashboard:
                finish();
                break;
        }
    }
}