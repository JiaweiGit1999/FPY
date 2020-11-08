package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

public class Payment extends AppCompatActivity {

    private double total_amount = 0;
    private TextView amount;
    private TextView paydate;
    private TextView paydetails;
    private TextView ordernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button savebutton = findViewById(R.id.Bsave);
        ImageView imageView = findViewById(R.id.back_button);
        amount = findViewById(R.id.amount);
        paydate = findViewById(R.id.Epayby);
        paydetails = findViewById(R.id.Edetail);
        ordernumber = findViewById(R.id.Eordernumber);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        String uid = User.getInstance().getUid();

        Query query = FirebaseFirestore.getInstance().collection("billing")
                .whereEqualTo("user_id", uid).limit(1);
        query.orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() != 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.get("status").toString().equals("unpaid")) {
                                try {
                                    total_amount = document.getDouble("amount");
                                    amount.setText("RM " + String.format(Locale.ENGLISH, "%.2f", total_amount / 100));
                                    paydate.setText(simpleDateFormat.format(new Date(document.getDate("due_date").getTime())));
                                    paydetails.setText(document.get("description").toString());
                                    ordernumber.setText(document.getId());
                                } catch (Exception e) {
                                    setData();
                                }
                            } else {
                                setData();
                            }
                        }
                    } else {
                        setData();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_amount == 0)
                    Toast.makeText(Payment.this, "You have paid the latest bill!", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(Payment.this, payment_method.class);
                    intent.putExtra("amount", total_amount);
                    intent.putExtra("detail", paydetails.getText());
                    intent.putExtra("order_id", ordernumber.getText());
                    startActivity(intent);
                }
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

    private void setData() {
        amount.setText("RM " + String.format(Locale.ENGLISH, "%.2f", total_amount));
        paydate.setText("-");
        paydetails.setText("-");
        ordernumber.setText("-");
    }
}