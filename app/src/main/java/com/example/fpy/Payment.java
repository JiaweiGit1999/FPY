package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button savebutton = findViewById(R.id.Bsave);
        ImageView imageView = findViewById(R.id.back_button);
        TextView amount = findViewById(R.id.amount);
        TextView paydate = findViewById(R.id.Epayby);
        final TextView paydetails = findViewById(R.id.Edetail);
        TextView ordernumber = findViewById(R.id.Eordernumber);

        final double total_amount = 100;

        amount.setText("RM " + String.format(Locale.ENGLISH, "%.2f", total_amount));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        paydate.setText(simpleDateFormat.format(new Date()));

        paydetails.setText("Fee");

        ordernumber.setText("Unavailable");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Payment.this, payment_method.class);
                intent.putExtra("amount", total_amount);
                intent.putExtra("detail", paydetails.getText());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(new Intent(Payment.this, DashBoard.class));
            // using finish() is optional, use it if you do not want to keep currentActivity in stack
            finish();
        } else {
            super.onBackPressed();
        }
    }
}