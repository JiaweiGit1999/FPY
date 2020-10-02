package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

public class Date_facility extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_facility);

        CalendarView calendar = findViewById(R.id.calendarView);
        final TextView dateview= findViewById(R.id.dateview);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String Date = day + "-" + (month+1)+"-" + year;
                dateview.setText(Date);
            }
        });
        Button confirmbutton = findViewById(R.id.confirm_button);
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Date_facility.this,Time_facility.class);
                startActivity(intent);
            }
        });
    }
}