package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Facility extends AppCompatActivity {
    private Booking booking = new Booking();

    private void intent(String facility) {
        Intent intent = new Intent(Facility.this, Date_facility.class);
        Bundle bundle = new Bundle();
        booking.setFacility(facility);
        bundle.putParcelable("booking", booking);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        CardView cardView1 = findViewById(R.id.bbq);
        CardView cardView2 = findViewById(R.id.Sky);
        CardView cardView3 = findViewById(R.id.ping);
        CardView cardView4 = findViewById(R.id.room);

        ImageView imageView = findViewById(R.id.back_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent("BBQ Pit");
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent("Sky Lounge");
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent("Ping-Pong Table");
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent("AV Room-+");
            }
        });
    }
}