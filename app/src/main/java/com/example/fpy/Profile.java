package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //user details
    String username,ic,contact,email,gender;
    EditText ename,eemail,ephone,egender;
    User user;
    Button bsave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        user = User.getInstance();
        //initialize views
        ename = findViewById(R.id.Ename);
        eemail = findViewById(R.id.Eemail);
        ephone = findViewById(R.id.Ephone);
        egender = findViewById(R.id.Egender);
        bsave = findViewById(R.id.Bsave);

        //setup info
        ename.setText(user.getUsername());
        ephone.setText(user.getContact());
        egender.setText(user.getGender());
        eemail.setText(user.getEmail());

        //on click save
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> update = new HashMap<>();
                update.put("name", ename.getText().toString());
                update.put("contact", ephone.getText().toString());
                update.put("gender", egender.getText().toString());
                update.put("email", eemail.getText().toString());

                db.collection("landlord").document(user.getUid()).set(update, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ename.setText(ename.getText().toString());
                        ephone.setText(ephone.getText().toString());
                        egender.setText(egender.getText().toString());
                        eemail.setText(eemail.getText().toString());
                    }
                });
            }
        });
    }
}