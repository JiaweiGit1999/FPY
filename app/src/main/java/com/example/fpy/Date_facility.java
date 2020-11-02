package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Date_facility extends AppCompatActivity {
    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_facility);

        Bundle getbundle = getIntent().getExtras();
        if (getbundle != null) {
            booking = getbundle.getParcelable("booking");
        }

        ImageView imageView = findViewById(R.id.back_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        CalendarView calendar = findViewById(R.id.calendarView);
        final TextView dateview = findViewById(R.id.dateview);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        dateview.setText(sdf.format(new Date(calendar.getDate())));


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String Date = day + "-" + (month + 1) + "-" + year;
                dateview.setText(Date);
            }
        });
        Button confirmbutton = findViewById(R.id.confirm_button);
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Date_facility.this, Time_facility.class);
                Bundle bundle = new Bundle();
                booking.setDate((String) dateview.getText());
                bundle.putParcelable("booking", booking);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}