package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;

public class DateTimeActivity extends AppCompatActivity implements View.OnClickListener {

    DatePicker datePicker;
    TimePicker timePicker;
    Button btnSaveDateTime;
    Boolean isCalendarVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
        initialize();
    }

    private void initialize() {

        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        datePicker.setVisibility(View.INVISIBLE);
        timePicker.setVisibility(View.INVISIBLE);


        btnSaveDateTime = findViewById(R.id.btnSaveChangesDateTime);
        btnSaveDateTime.setOnClickListener(this);

        isCalendarVisible = getIntent().getBooleanExtra("isCalendarVisible", false);

        if (isCalendarVisible) {
            datePicker.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.INVISIBLE);
        }else{
            datePicker.setVisibility(View.INVISIBLE);
            timePicker.setVisibility(View.VISIBLE);
        }


    }



    private String getDateTime() {
        String dateTimeString = null;
        if(datePicker.getVisibility() == View.VISIBLE){
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int dayOfMonth = datePicker.getDayOfMonth();
            dateTimeString = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
            isCalendarVisible = false;
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                int hour = timePicker.getHour();
                dateTimeString = String.format("%02d", hour);
            }
            isCalendarVisible = true;
        }
        return dateTimeString;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveChangesDateTime:
                String dateTime = getDateTime();
                if (dateTime != null){
                    Intent intent = new Intent();
                    intent.putExtra("dateTime",dateTime);
                    intent.putExtra("isCalendarVisible", isCalendarVisible);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(DateTimeActivity.this, "Cannot retrieve the date/time value", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


}