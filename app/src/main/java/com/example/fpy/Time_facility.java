package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Time_facility extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String duration;
    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_facility);

        Bundle getbundle = getIntent().getExtras();
        if (getbundle != null) {
            booking = getbundle.getParcelable("booking");
        }

        final Spinner spinner = findViewById(R.id.spinner);
        final TimePicker timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        spinner.setOnItemSelectedListener(this);

        List<String> category = new ArrayList<String>();
        category.add("1 hours");
        category.add("2 hours");
        category.add("3 hours");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button confirmbutton = findViewById(R.id.confirm_button);
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Time_facility.this, booking_detail.class);
                Bundle bundle = new Bundle();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                booking.setTime(String.format(Locale.ENGLISH, "%02d", hour) + ":" + String.format(Locale.ENGLISH, "%02d", minute));
                booking.setDuration(duration);
                bundle.putParcelable("booking", booking);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        duration = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}