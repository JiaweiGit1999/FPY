package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Time_facility extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String duration;
    private Booking booking;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference docRef = db.collection("booking");
    Boolean valid_date = true;
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

        ImageView imageView = findViewById(R.id.back_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                booking.setTime(String.format(Locale.ENGLISH, "%02d", hour) + ":" + String.format(Locale.ENGLISH, "%02d", minute));
                booking.setDuration(duration);
                final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                valid_date = true;
                docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long slottaken = 0L;
                            Long takensoon = 5L;
                            String stringduration = document.getString("duration").replace(" hours","");
                            Long duration = Long.parseLong(stringduration);
                            if(document.getString("facility") == booking.getFacility()) {
                                try {

                                    String selectedtime = booking.getDate()+" "+booking.getTime();
                                    String bookedtime = document.get("date")+" "+document.get("time");

                                    Date selecteddate = format.parse(selectedtime);
                                    Date bookeddate = format.parse(bookedtime);

                                    slottaken = miliTohour(bookeddate,selecteddate);
                                    takensoon = miliTohour(selecteddate,bookeddate);

                                    Log.d("Date: ",bookedtime);
                                    Log.d("Date: ",duration.toString());
                                    Log.d("Date: ",slottaken.toString());
                                    Log.d("Date: ",takensoon.toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(slottaken < duration|| takensoon < duration){
                                    valid_date = false;
                                }
                            }
                        }
                        if(valid_date){
                            Log.d("Date: ", "Date is valid");
                            Intent intent = new Intent(Time_facility.this, booking_detail.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("booking", booking);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            Log.d("Date: ", "Date is not valid");
                            Toast.makeText(getApplicationContext(),"Time slot is not available for booking.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


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

    public long miliTohour(Date book,Date select){
        long diff = book.getTime() - select.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        return Math.abs(hours);
    }
}