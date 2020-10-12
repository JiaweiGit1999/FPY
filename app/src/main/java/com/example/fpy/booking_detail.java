package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class booking_detail extends AppCompatActivity {
    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        Bundle getbundle = getIntent().getExtras();
        if (getbundle != null) {
            booking = getbundle.getParcelable("booking");
        }

        TextView facilityTextView = findViewById(R.id.FtextView);
        TextView dateTextView = findViewById(R.id.DtextView);
        TextView timeTextView = findViewById(R.id.TtextView);
        TextView durationTextView = findViewById(R.id.DutextView);

        facilityTextView.setText(booking.getFacility());
        dateTextView.setText(booking.getDate());
        timeTextView.setText(booking.getTime());
        durationTextView.setText(booking.getDuration());

        Button confirm = findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(booking_detail.this, success_booking.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("booking", booking);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}