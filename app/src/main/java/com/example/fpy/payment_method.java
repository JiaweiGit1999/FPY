package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class payment_method extends AppCompatActivity {

    private Class intentclass = payment_method.class;
    private double total_amount;
    private String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button savebutton = findViewById(R.id.Bsave);
        ImageView imageView = findViewById(R.id.back_button);
        TextView amount = findViewById(R.id.Eamount);
        TextView detail = findViewById(R.id.Epayment);

        Intent dataintent = getIntent();
        if (dataintent != null) {
            total_amount = dataintent.getDoubleExtra("amount", 0);
            details = dataintent.getStringExtra("detail");
            amount.setText(String.format(Locale.ENGLISH, "%.2f", total_amount));
            detail.setText(details);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.Online:
                        intentclass = CheckoutActivityOnline.class;
                        break;
                    case R.id.creditcard:
                        intentclass = CheckoutActivityCredit.class;
                        break;
                    case R.id.Sarawakpay:
                        intentclass = CheckoutActivitySarawakPay.class;
                        break;
                    default:
                        intentclass = payment_method.class;
                        break;
                }
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intentclass == payment_method.class) {
                    Toast.makeText(payment_method.this, "Please choose a payment method", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(payment_method.this, intentclass);
                    intent.putExtra("amount", total_amount);
                    startActivity(intent);
                }
            }
        });
    }
}

