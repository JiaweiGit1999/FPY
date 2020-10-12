package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class success_booking extends AppCompatActivity {

    private Booking booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_booking);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Bundle getbundle = getIntent().getExtras();
        if (getbundle != null) {
            booking = getbundle.getParcelable("booking");
        }

        Map<String, String> bookingdetails = new HashMap<>();
        bookingdetails.put("user_id", User.getInstance().getUid());
        bookingdetails.put("facility", booking.getFacility());
        bookingdetails.put("date", booking.getDate());
        bookingdetails.put("time", booking.getTime());
        bookingdetails.put("duration", booking.getDuration());

        DocumentReference ref = db.collection("booking").document();
        //String myId = ref.getId();
        ref.set(bookingdetails);

        //Toast.makeText(this,myId,Toast.LENGTH_LONG).show();

        Button button = findViewById(R.id.confirm_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(success_booking.this, DashBoard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}