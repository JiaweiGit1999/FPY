package com.example.fpy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Tenant extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_details);
        TextView name = findViewById(R.id.fname);
        TextView ic = findViewById(R.id.icNumber);
        TextView contact = findViewById(R.id.contactNo);
        TextView email = findViewById(R.id.Eemail);
        ImageView button = findViewById(R.id.back_button);

        Intent intent = getIntent();
        if(intent!=null)
        {
            try {
                name.setText(intent.getStringExtra("name"));
                ic.setText(intent.getStringExtra("ic"));
                contact.setText(intent.getStringExtra("contact"));
                email.setText(intent.getStringExtra("email"));

            } catch (Exception e) {

            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(new Intent(Tenant.this, UnitsOwned.class));
            // using finish() is optional, use it if you do not want to keep currentActivity in stack
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
